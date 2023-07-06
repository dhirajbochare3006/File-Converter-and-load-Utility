package com.kmbl.tax.converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kmbl.tax.camel.config.Configrations;
import com.kmbl.tax.camel.config.NckResponseConfigDetails;
import com.kmbl.tax.camel.entity.Audit;
import com.kmbl.tax.camel.model.ConfigModel;
import com.kmbl.tax.camel.model.NckResponseConfig;
import com.kmbl.tax.camel.repository.UtilityAudit;
import com.kmbl.tax.utils.CommonConstants;
import com.kmbl.tax.utils.CommonUtils;
import com.opencsv.CSVWriter;

public class ExcelConverter {

	private static final Logger logger = LoggerFactory.getLogger(ExcelConverter.class);

	private int totalRecords = 0;
	private long totalAmount = 0;
	private String cifId = null;
	private String accountNumber = null;
	CommonUtils utils = new CommonUtils();
	Configrations configrations = new Configrations();
	NckResponseConfigDetails nckDetails = new NckResponseConfigDetails();

	public String convert(String excelFilePath, int auditId) {

		String crnNo = configrations.getCrnNo();
		ConfigModel configModel = configrations.getFinalFileMovementSFTPConfigDetails(crnNo);
		try {
			cifId = configModel.getCifId();
			if (validateFileName(excelFilePath, configModel.getInputFilePattern())) {
				if (CommonConstants.FILE_NOT_PROCESSED.equalsIgnoreCase(checkingFileAlreadyProcessedOrNot(excelFilePath,
						configModel.getSuccessFileMovementOnServerPath()))) {
					if (CommonConstants.CORRECT_DATE.equalsIgnoreCase(getDateFromFile(excelFilePath))) {
						if (null != configModel && !configModel.toString().isEmpty()
								&& !configModel.getBankName().isEmpty()) {

							String seqNo = getSequenceNumber(excelFilePath);
							logger.info(
									" Inside ExcelConverter :: convert() method :: Collecting the sequence number from the file name :: the sequence no is :: "
											+ seqNo);
							String csvFilePath = configModel.getCsvFilePath().replace(CommonConstants.CRN_NO, crnNo)
									.replace(CommonConstants.DATE, getTodaysDate())
									.replace(CommonConstants.SEQUENCENO, seqNo);
							List<String> columnSequence = setColumnSequence(configModel.getCellDetails());
							logger.info(" Inside ExcelConverter :: convert() method :: Added the coloumn sequence.");

							int headerRowNumber = configModel.getHeaderRowNo(); // Header row number (1-based index)

							if (containsScriptingTag(excelFilePath)) {
								NckResponseConfig nckResponseModel = nckDetails.getNckResponseDetails();
								generateNckFileAndUpload(crnNo, seqNo, configModel, auditId,
										nckResponseModel.getHeader(),
										nckResponseModel.getScriptingTagErrorAndDescription(),
										CommonConstants.FAILED_PROCESS_DETAILS
												+ CommonConstants.SCRIPTING_TAG_PRESENT);
								logger.info(" Inside ExcelConverter :: convert() method ::The file has been rejected due to the presence of scripting tags.");
							} else {
								if (isDecimalValuePresent(excelFilePath, configModel.getDecimalFieldName())) {
									NckResponseConfig nckResponseModel = nckDetails.getNckResponseDetails();
									generateNckFileAndUpload(crnNo, seqNo, configModel, auditId,
											nckResponseModel.getHeader(),
											nckResponseModel.getInvalidAmountDetailsErrorAndDescription(),
											CommonConstants.FAILED_PROCESS_DETAILS
													+ CommonConstants.INCORRECT_TOTAL_AMOUNT);
									logger.error(
											"Inside ExcelConverter :: convert() method ::Conversion failed, becaus of Incorrect total amount");
									return CommonConstants.FAIL;
								} else {
									File file = new File(csvFilePath.toString());
									String fileCreateStatus = convertExcelToCsv(excelFilePath, file.toString(),
											columnSequence, headerRowNumber, configModel.getTotalAmount(),
											configModel.getDebitAccountNumber(), configModel.getDebitDate(),
											configModel.getDateFormat(), configModel.getBankName(),
											configModel.getInputDateFormat());

									if (CommonConstants.SUCCESS.equalsIgnoreCase(fileCreateStatus)) {
										insertHeaderAndFileMovement(file, csvFilePath, configModel, auditId);
									} else {
										NckResponseConfig nckResponseModel = nckDetails.getNckResponseDetails();
										String nckFileDescription = null;
										String error = null;
										if (CommonConstants.INCORRECT_HEADERS.equalsIgnoreCase(fileCreateStatus)) {
											nckFileDescription = nckResponseModel
													.getInvalidFileHeadersErrorAndDescription();
											error = CommonConstants.INCORRECT_FILE_HEADER;
										} else {
											nckFileDescription = nckResponseModel
													.getInvalidFileDetailsErrorAndDescription();
											error = CommonConstants.INCORRECT_FILE_DETAILS;
										}
										generateNckFileAndUpload(crnNo, seqNo, configModel, auditId,
												nckResponseModel.getHeader(), nckFileDescription,
												CommonConstants.FAILED_PROCESS_DETAILS + error);
										utils.deletefileFromLocal(true, file);
										logger.error(
												"Inside ExcelConverter :: convert() method ::Conversion failed, becaus of Incorrect file details..");
										return CommonConstants.FAIL;
									}
								}
							}
						} else {
							logger.error(
									"Inside ExcelConverter :: convert() method :: Properties not found for provided CRN Number, please check");
							NckResponseConfig nckResponseModel = nckDetails.getNckResponseDetails();
							String seqNo = getSequenceNumber(excelFilePath);
							generateNckFileAndUpload(crnNo, seqNo, configModel, auditId, nckResponseModel.getHeader(),
									nckResponseModel.getInvalidFileCorporateErrorAndDescription(),
									CommonConstants.FAILED_PROCESS_DETAILS + CommonConstants.INCORRECT_FILENAME);
							logger.error("Inside ExcelConverter :: convert() method :: Incorrect file name..");
							return CommonConstants.FAIL;
						}
					} else {
						NckResponseConfig nckResponseModel = nckDetails.getNckResponseDetails();
						String seqNo = getSequenceNumber(excelFilePath);
						generateNckFileAndUpload(crnNo, seqNo, configModel, auditId, nckResponseModel.getHeader(),
								nckResponseModel.getInvalidInputDateFileErrorAndDescription(),
								CommonConstants.FAILED_PROCESS_DETAILS + CommonConstants.INCORRECT_FILE_NAME_DATE);
						logger.error(
								"Inside ExcelConverter :: convert() method :: Conversion failed, becaus of File date cannot be greater than system date.");
						return CommonConstants.FAIL;
					}
				} else {
					NckResponseConfig nckResponseModel = nckDetails.getNckResponseDetails();
					String seqNo = getSequenceNumber(excelFilePath);
					generateNckFileAndUpload(crnNo, seqNo, configModel, auditId, nckResponseModel.getHeader(),
							nckResponseModel.getDuplicateFileNameErrorAndDescription(),
							CommonConstants.FAILED_PROCESS_DETAILS + CommonConstants.FILE_ALREADY_PROCESSED1);
					logger.error(
							"Inside ExcelConverter :: convert() method :: Conversion failed, becaus of Duplicate File name.");
					return CommonConstants.FAIL;
				}
			} else {
				NckResponseConfig nckResponseModel = nckDetails.getNckResponseDetails();
				String seqNo = getSequenceNumber(excelFilePath);
				generateNckFileAndUpload(crnNo, seqNo, configModel, auditId, nckResponseModel.getHeader(),
						nckResponseModel.getInvalidFileNameErrorAndDescription(),
						CommonConstants.FAILED_PROCESS_DETAILS + CommonConstants.INCORRECT_FILENAME);
				logger.error("Inside ExcelConverter :: convert() method :: Incorrect file name..");
				return CommonConstants.FAIL;
			}
		} catch (Exception e) {
			logger.error("Inside ExcelConverter :: convert() method :: Exception occured while converting file :: "
					+ e.getMessage());
			return CommonConstants.FAIL;
		}
		return CommonConstants.SUCCESS;
	}

	private String checkingFileAlreadyProcessedOrNot(String excelFilePath, String folderPath) {

		File filePath = new File(excelFilePath);
		File file = new File(folderPath, filePath.getName());
		if (file.exists()) {
			return CommonConstants.FILE_ALREADY_PROCESSED;
		} else {
			return CommonConstants.FILE_NOT_PROCESSED;
		}
	}

	private void insertHeaderAndFileMovement(File file, String csvFilePath, ConfigModel configModel, int auditId) {
		String status = insertHeaderToCsv(file.toString());
		totalRecords = 0;
		totalAmount = 0;
		cifId = null;
		accountNumber = null;
		logger.info(
				" Inside ExcelConverter :: convert() method :: .csv file created from the received file, and the path is :: "
						+ csvFilePath);

		try (CSVParser csvParser = new CSVParser(new FileReader(csvFilePath), CSVFormat.DEFAULT)) {
			int rowCount = 0;
			for (CSVRecord csvRecord : csvParser) {
				rowCount++;
			}
			logger.info(" Inside ExcelConverter :: convert() method :: Number of rows of csv is :: " + (rowCount - 2));
			Audit audit = new Audit();
			audit.setAuditLogId(auditId);
			audit.setInputRecordCount(rowCount - 2);
			audit.setProcesedRecordCount(rowCount - 2);
			audit.setOutputFileName(csvFilePath);
			audit.setProcessEndTime(utils.dateGenerate());
			audit.setProcessDetails(CommonConstants.SUCCESS_PROCESS_DETAILS);
			audit.setStatus(CommonConstants.COMPLETED);
			UtilityAudit.updateAuditDetails(audit);
		} catch (IOException e) {
			logger.error(
					" Inside ExcelConverter :: convert() method :: Exception occured while collecting the rowCount");
		}

		logger.info(" Inside convert :: convert() method :: created .csv file movement to the SFTP started... ");
		if (CommonConstants.SUCCESS.equalsIgnoreCase(status)) {
			utils.csvFileMoventToSFTP(configModel.getUsername(), configModel.getPassword(), configModel.getHost(),
					extractFileName(csvFilePath), configModel.getFinalFolder(), csvFilePath);

			logger.info(
					" Inside ExcelConverter :: convert() method :: created .csv file movement to the SFTP ended... ");

			utils.serverFileMovement(csvFilePath.toString(), configModel.getProcessedCsvFileMovementServerPath(),
					csvFilePath);
		}

	}

	public static String getDateFromFile(String fileName) {
		File inputFile = new File(fileName);
		Pattern pattern = Pattern.compile("\\d{6}"); // Matches exactly six digits
		Matcher matcher = pattern.matcher(inputFile.getName());

		if (matcher.find()) {
			String dateStr = matcher.group();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
			LocalDate date = LocalDate.parse(dateStr, formatter);

			LocalDate currentDate = LocalDate.now();
			if (date.isAfter(currentDate)) {
				return CommonConstants.INCORRECT_DATE;
			} else {
				return CommonConstants.CORRECT_DATE;
			}
		}
		return CommonConstants.CORRECT_DATE;
	}

	@SuppressWarnings("static-access")
	public void generateNckFileAndUpload(String crnNo, String seqNo, ConfigModel configModel, int auditId,
			String fileHeader, String fileErrorCodeAndDesc, String auditDescription) throws Exception {

		String nckCsvFilePath = configModel.getNckCsvFileName().replace(CommonConstants.CRN_NO, crnNo)
				.replace(CommonConstants.DATE, getTodaysDate()).replace(CommonConstants.SEQUENCENO, seqNo);
		String status = generateNCKSampleCsv(nckCsvFilePath, fileHeader, fileErrorCodeAndDesc);

		Audit audit = new Audit();
		audit.setAuditLogId(auditId);
		audit.setStatus(CommonConstants.FAILED);
		audit.setProcessDetails(auditDescription);
		audit.setProcessEndTime(utils.dateGenerate());
		audit.setProcesedRecordCount(0);
		audit.setOutputFileName(nckCsvFilePath);
		UtilityAudit.updateAuditDetails(audit);

		logger.info(" Inside convert :: convert() method :: created .csv file movement to the SFTP started... ");

		if (CommonConstants.SUCCESS.equalsIgnoreCase(status)) {
			utils.csvFileMoventToSFTP(configModel.getUsername(), configModel.getPassword(), configModel.getHost(),
					extractFileName(nckCsvFilePath), configModel.getFailedFolder(), nckCsvFilePath);

			logger.info(
					" Inside ExcelConverter :: convert() method :: created .csv file movement to the SFTP ended... ");

			utils.serverFileMovement(nckCsvFilePath.toString(), configModel.getProcessedNckFileMovementServerPath(),
					nckCsvFilePath);
		}

	}

	public String getSequenceNumber(String excelFilePath) {
		if (null != excelFilePath) {
			File inputFile = new File(excelFilePath);
			String regex = CommonConstants.FINDING_SEQUENCE_NUMBER_REGEX;
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(inputFile.getName());
			if (matcher.find()) {
				return matcher.group(1);
			} else {
				logger.info(
						" Inside ExcelConverter :: convert() method :: No three-digit sequence number found before the file extension. ");
				return "";
			}
		}
		return "";
	}

	private boolean validateFileName(String excelFilePath, String pattern) {
		try {
			File inputFile = new File(excelFilePath);
			String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
			Pattern regex = Pattern.compile(pattern);
			Matcher matcher = regex.matcher(fileName);
			if (matcher.matches()) {
				String dd = fileName.substring(9, 11);
				String mm = fileName.substring(11, 13);
				String yy = fileName.substring(13, 15);

				// Additional logic to validate specific ranges or valid values for DDMMYY
				int day = Integer.parseInt(dd);
				int month = Integer.parseInt(mm);
				int year = Integer.parseInt(yy);

				// Example validation for DDMMYY values within specific ranges
				if (day >= 1 && day <= 31 && month >= 1 && month <= 12 && year >= 0 && year <= 99) {
					logger.info(
							"Inside ExcelConverter :: validateFileName() method :: Valid received file name format. ");
					return true;
				} else {
					logger.info(
							"Inside ExcelConverter :: validateFileName() method :: Invalid received file name format. ");
					return false;
				}
			} else {
				logger.info(
						"Inside ExcelConverter :: validateFileName() method :: Invalid received file name format. ");
				return false;
			}

		} catch (Exception e) {
			logger.error(
					" Inside ExcelConverter :: validateFileName() method ::  Exception occured while validating the file"
							+ e.getMessage());
			return false;
		}
	}

	private String generateNCKSampleCsv(String nckCsvFilePath, String fileHeader, String fileErrorCodeAndDesc) {

		List<String> headers = setColumnSequence(fileHeader);
		List<String> values = setColumnSequence(fileErrorCodeAndDesc);

		try (CSVWriter writer = new CSVWriter(new FileWriter(nckCsvFilePath))) {
			writer.writeNext(headers.toArray(new String[0]));
			writer.writeNext(values.toArray(new String[0]));
		} catch (IOException e) {
			logger.error(
					" Inside ExcelConverter :: getCrnNo() method ::  An error occurred while generating the CSV file: "
							+ e.getMessage());
		}
		return CommonConstants.SUCCESS;
	}

	private String getTodaysDate() {
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		return today.format(formatter);
	}

	private List<String> setColumnSequence(String cellDetails) {
		List<String> columnSequence = new ArrayList<>();
		if (null != cellDetails && !cellDetails.isEmpty()) {
			String[] headerParts = cellDetails.split(",");
			for (String header : headerParts) {
				columnSequence.add(header.trim());
			}
		}
		return columnSequence;
	}

	private String convertExcelToCsv(String excelFilePath, String csvFilePath, List<String> columnSequence,
			int headerRowNumber, String amountColumnName, String accountNumberColumnName, String debitDateColumnName,
			String dateFormat, String bankName, String inputDateFormat) {
		try (Workbook workbook = getWorkbook(excelFilePath);
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(csvFilePath), StandardCharsets.UTF_8))) {

			int sheetCount = workbook.getNumberOfSheets();
			ExecutorService executorService = Executors.newFixedThreadPool(sheetCount);
			List<Callable<Void>> tasks = new ArrayList<>();
			logger.info(" Inside ExcelConverter :: convertExcelToCsv() method :: cvs writing started >>>>>>",
					CommonConstants.LOG_TYPE_INFO);

			for (int i = 0; i < sheetCount; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				Row headerRow = sheet.getRow(headerRowNumber - 1);

				Iterator<Cell> cellIterator = headerRow.cellIterator();
				List<String> headerColumns = new ArrayList<>();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (isInputSafe(cell.getStringCellValue().trim())) {
						String sanitizedInput = sanitizeInput(cell.getStringCellValue().trim());
						headerColumns.add(sanitizedInput.trim().toUpperCase());
					} else {
						logger.error(
								" Inside ExcelConverter :: convertExcelToCsv() method :: Script tag present in the header",
								CommonConstants.LOG_TYPE_INFO);
						return CommonConstants.INCORRECT_HEADERS;
					}
				}

				for (String var : columnSequence) {
					if (!headerColumns.contains(var.trim().toUpperCase())) {
						logger.error(
								" Inside ExcelConverter :: convertExcelToCsv() method :: Received file headers are incorrect, please check the file headers",
								CommonConstants.LOG_TYPE_INFO);
						return CommonConstants.INCORRECT_HEADERS;
					}
				}

				Map<String, Integer> headerToIndexMap = createHeaderToIndexMap(headerRow);
				Row rowAcc = sheet.getRow(headerRowNumber);
				int columnIndex = headerToIndexMap.get(accountNumberColumnName);
				Cell cell = rowAcc.getCell(columnIndex);
				accountNumber = extractCellValueAsString(cell);

				tasks.add(() -> {
					// int rowCount = sheet.getLastRowNum();

					int rowCount1 = 0;
					for (Row row : sheet) {
						boolean rowIsEmpty = true;
						for (Cell cell1 : row) {
							if (cell1.getCellType() != CellType.BLANK) {
								rowIsEmpty = false;
								break;
							}
						}
						if (!rowIsEmpty) {
							rowCount1++;
						}
					}
					if (0 != rowCount1)
						rowCount1 = rowCount1 - 1;

					for (int rowIndex = headerRowNumber; rowIndex <= rowCount1; rowIndex++) {
						Row row = sheet.getRow(rowIndex);
						if (row != null) {
							writeCsvRow(writer, row, headerToIndexMap, columnSequence, amountColumnName,
									debitDateColumnName, dateFormat, bankName, inputDateFormat);
						}
					}
					return null;
				});
			}

			executorService.invokeAll(tasks);
			executorService.shutdown();

			writer.write("T");
			writer.newLine();
			writer.flush();
			writer.close();
			workbook.close();
			logger.info(" Inside ExcelConverter :: convertExcelToCsv() method :: cvs writing endded >>>>");
		} catch (Exception e) {
			logger.error(
					" Inside ExcelConverter :: convertExcelToCsv() method ::  Exception occured while writing the csv :: "
							+ e.getMessage());
			return CommonConstants.INCORRECT_DATA;
		}
		return CommonConstants.SUCCESS;
	}

	private Workbook getWorkbook(String excelFilePath) throws IOException {
		try (InputStream inputStream = new FileInputStream(excelFilePath)) {
			if (excelFilePath.toLowerCase().endsWith(".xls")) {
				return new HSSFWorkbook(inputStream); // For .xls files
			} else if (excelFilePath.toLowerCase().endsWith(".xlsx")) {
				return new XSSFWorkbook(inputStream); // For .xlsx files
			} else {
				throw new IllegalArgumentException("Unsupported file format. Only .xls and .xlsx files are supported.");
			}
		}
	}

	private void writeCsvRow(BufferedWriter writer, Row row, Map<String, Integer> headerToIndexMap,
			List<String> columnSequence, String amountColumnName, String debitDateColumnName, String dateFormat,
			String bankName, String inputDateFormat) throws IOException {
		StringBuilder csvRow = new StringBuilder();

		csvRow.append("D,");
		for (String columnName : columnSequence) {

			int columnIndex = headerToIndexMap.get(columnName);
			Cell cell = row.getCell(columnIndex);
			String cellValue = extractCellValueAsString(cell);

			if (columnName.equalsIgnoreCase(CommonConstants.TAN) && (null == cellValue || cellValue.isEmpty())) {
				columnIndex = headerToIndexMap.get(CommonConstants.PAN);
				cell = row.getCell(columnIndex);
				cellValue = extractCellValueAsString(cell);
			}

			if (columnName.equals(debitDateColumnName)) {
				if (cellValue != null && !cellValue.equals("")) {
					csvRow.append(dateConverter(cellValue, dateFormat, inputDateFormat));
				} else
					csvRow.append("");
			} else if (columnName.equalsIgnoreCase(CommonConstants.TAN)) {
				if (cellValue == null || cellValue.isEmpty())
					csvRow.append("");
				else
					csvRow.append(cellValue);
				csvRow.append(",");
				csvRow.append(bankName);
			} else if (cellValue == null)
				csvRow.append("");
			else
				csvRow.append(cellValue);
			if (!columnName.equals(columnSequence.get(columnSequence.size() - 1))) {
				csvRow.append(",");
			}

			if (columnName.equals(amountColumnName)) {
				if (null == cellValue || cellValue.isEmpty()) {
					totalAmount = totalAmount;
				} else {
					long newValue = Long.parseLong(cellValue);
					totalAmount = totalAmount + newValue;
				}
			}
		}
		writer.write(csvRow.toString());
		writer.newLine();
		totalRecords++;
	}

	private Map<String, Integer> createHeaderToIndexMap(Row headerRow) {
		Map<String, Integer> headerToIndexMap = new HashMap<>();
		for (Cell cell : headerRow) {
			String cellValue = extractCellValueAsString(cell);
			headerToIndexMap.put(cellValue, cell.getColumnIndex());
		}
		return headerToIndexMap;
	}

	private String extractCellValueAsString(Cell cell) {
		if (cell == null) {
			return null;
		}
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			// Double d = cell.getNumericCellValue();
			// int i = d.intValue();
			// return Integer.toString(i);
			long longValue = (long) cell.getNumericCellValue();
			return String.valueOf(longValue);
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		default:
			return "";
		}
	}

	private String insertHeaderToCsv(String csvFilePath) {
		StringBuilder firstLine = new StringBuilder();
		firstLine.append("H,").append(cifId).append(",").append(totalRecords).append(",").append(totalAmount)
				.append(",").append(accountNumber);

		String updatedFirstLine = firstLine.toString();

		try {
			// Open the CSV file in read-write mode
			RandomAccessFile file = new RandomAccessFile(csvFilePath, "rw");

			// Calculate the length of the new row in bytes
			byte[] newRowBytes = updatedFirstLine.getBytes();

			// Create a temporary file
			String tempFilePath = csvFilePath + ".tmp";
			RandomAccessFile tempRAF = new RandomAccessFile(tempFilePath, "rw");

			// Write the new row to the temporary file
			tempRAF.write(newRowBytes);
			tempRAF.writeBytes(System.lineSeparator());

			// Copy the existing content from the original file to the temporary file
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = file.read(buffer)) != -1) {
				tempRAF.write(buffer, 0, bytesRead);
			}

			// Close the files
			file.close();
			tempRAF.close();

			// Replace the original file with the temporary file
			String originalFilePath = csvFilePath;
			if (new java.io.File(originalFilePath).delete()) {
				java.io.File tempFile = new java.io.File(tempFilePath);
				if (!tempFile.renameTo(new java.io.File(originalFilePath))) {
					throw new IOException("Failed to replace the original file.");
				}
			} else {
				throw new IOException("Failed to replace the original file.");
			}

			file.close();
			tempRAF.close();

			// Delete the temporary file
			java.io.File tempFile = new java.io.File(tempFilePath);
			if (tempFile.exists()) {
				tempFile.delete();
			}
		} catch (IOException e) {
			e.getMessage();
		}
		return CommonConstants.SUCCESS;
	}

	public String dateConverter(String dateString, String dateFormat, String inputDateFormat) {
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputDateFormat);
		String formattedDate = null;
		try {
			Date date = inputFormat.parse(dateString);
			SimpleDateFormat outputFormat = new SimpleDateFormat(dateFormat);
			formattedDate = "" + outputFormat.format(date);
		} catch (ParseException e) {
			logger.error(
					" Inside ExcelConverter :: dateConverter() method ::  Exception occured while parsing the date, please check the date format : "
							+ e.getMessage());
		}
		return formattedDate;
	}

	private String extractFileName(String filePath) {
		File file = new File(filePath);
		String fileName = file.getName();
		return fileName;
	}

	public static boolean isInputSafe(String input) {
		return !input.contains("<script>");
	}

	public static String sanitizeInput(String input) {
		return Encode.forHtml(input);
	}

	public static boolean isDecimalValuePresent(String filePath, String columnName) throws IOException {
		// Load the Excel file based on the file extension
		Workbook workbook;
		if (filePath.toLowerCase().endsWith(".xlsx")) {
			workbook = new XSSFWorkbook(new FileInputStream(filePath));
		} else if (filePath.toLowerCase().endsWith(".xls")) {
			workbook = new HSSFWorkbook(new FileInputStream(filePath));
		} else {
			throw new IllegalArgumentException("Invalid file format. Supported formats are .xls and .xlsx.");
		}

		// Get the sheet by name
		Sheet sheet = workbook.getSheetAt(0);
		if (sheet == null) {
			throw new IllegalArgumentException("Sheet not found: ");
		}

		// Find the column index based on the column name
		int columnIndex = -1;
		Row headerRow = sheet.getRow(0); // Assuming the header row is at index 0
		for (Cell cell : headerRow) {
			if (cell.getStringCellValue().equalsIgnoreCase(columnName)) {
				columnIndex = cell.getColumnIndex();
				break;
			}
		}

		if (columnIndex == -1) {
			throw new IllegalArgumentException("Column not found: " + columnName);
		}

		// Iterate through the rows and check for decimal values in the specified column
		for (Row row : sheet) {
			Cell cell = row.getCell(columnIndex);
			if (cell != null && cell.getCellType() == CellType.NUMERIC) {
				double numericValue = cell.getNumericCellValue();
				if (numericValue % 1 != 0) {
					// Decimal value found
					workbook.close(); // Close the workbook
					return true;
				}
			}
		}

		workbook.close();
		return false;
	}

	public static boolean containsScriptingTag(String filePath) throws IOException {
		Workbook workbook;
		if (filePath.toLowerCase().endsWith(".xlsx")) {
			workbook = new XSSFWorkbook(new FileInputStream(filePath));
		} else if (filePath.toLowerCase().endsWith(".xls")) {
			workbook = new HSSFWorkbook(new FileInputStream(filePath));
		} else {
			throw new IllegalArgumentException("Invalid file format. Supported formats are .xls and .xlsx.");
		}

		boolean containsScriptingTag = false;

		for (Sheet sheet : workbook) {
			for (Row row : sheet) {
				for (Cell cell : row) {
					if (cell.getCellType() == CellType.STRING) {
						String cellValue = cell.getStringCellValue();
						// Check for potential scripting tags (you may customize this condition based on
						// your needs)
						if (cellValue.toLowerCase().contains("<script")
								|| cellValue.toLowerCase().contains("javascript:")) {
							containsScriptingTag = true;
							break;
						}
					}
				}
				if (containsScriptingTag) {
					break;
				}
			}
			if (containsScriptingTag) {
				break;
			}
		}

		workbook.close();
		return containsScriptingTag;
	}

}
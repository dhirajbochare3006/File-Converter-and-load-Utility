package com.kmbl.tax.camel.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kmbl.tax.camel.model.ConfigModel;
import com.kmbl.tax.camel.model.DbConfig;
import com.kmbl.tax.utils.CommonConstants;

public class Configrations {

	private static final Logger logger = LoggerFactory.getLogger(Configrations.class);

	public ConfigModel getConfigrationDetails() {

		logger.info(" Inside Configrations :: getConfigrationDetails() method ::  to collect the Sftp Configrations ");
		ConfigModel model = new ConfigModel();
		Properties prop = new Properties();
		try (InputStream inputStream = getClass().getResourceAsStream(CommonConstants.PROPERTY_FILE_PATH)) {
			prop.load(inputStream);
		} catch (IOException e) {
			// Handle the exception
		}

		model.setConfigType(prop.getProperty(CommonConstants.CONFIG_TYPE));
		model.setFailedFolder(prop.getProperty(CommonConstants.SFTP_FAILED_FOLDER_NAME));
		model.setHost(prop.getProperty(CommonConstants.SFTP_HOST));
		model.setInboxFolder(prop.getProperty(CommonConstants.SFTP_INBOX_FOLDER_NAME));
		model.setPassword(prop.getProperty(CommonConstants.SFTP_PASSWORD));
		model.setPort(prop.getProperty(CommonConstants.SFTP_PORT));
		model.setSuccessFolder(prop.getProperty(CommonConstants.SFTP_INBOX_SUCCESS_NAME));
		model.setUsername(prop.getProperty(CommonConstants.SFTP_USERNAME));
		model.setDelay(prop.getProperty(CommonConstants.SFTP_DELAY));
		model.setOptions(prop.getProperty(CommonConstants.SFTP_OPTIONS));
		model.setFileDownloadOnServerPath(prop.getProperty(CommonConstants.FILE_DOWNLOAD_ON_SERVER_PATH));
		model.setFailedFileMovementOnServerPath(prop.getProperty(CommonConstants.FAILED_FILE_MOVEMENT_ON_SERVER_PATH));
		model.setSuccessFileMovementOnServerPath(
				prop.getProperty(CommonConstants.SUCCESS_FILE_MOVEMENT_ON_SERVER_PATH));
		/*
		 * model.setProcessedCsvFileMovementServerPath(prop.getProperty(CommonConstants.
		 * PROCESSED_NCK_FILE_MOVEMENT_ON_SERVER_PATH));
		 * model.setProcessedNckFileMovementServerPath(prop.getProperty(CommonConstants.
		 * PROCESSED_NCK_CSV_FILE_MOVEMENT_ON_SERVER_PATH));
		 */

		return model;
	}

	public String getLogFileConfigDetails() {
		String logFilePath = null;
		Properties prop = new Properties();
		try (InputStream inputStream = getClass().getResourceAsStream(CommonConstants.PROPERTY_FILE_PATH)) {
			prop.load(inputStream);
		} catch (IOException e) {
			// Handle the exception
		}
		logFilePath = prop.getProperty("log.file.path");

		return logFilePath;
	}

	public ConfigModel getFinalFileMovementSFTPConfigDetails(String crnNo) {
		Properties prop = new Properties();
		try (InputStream inputStream = getClass().getResourceAsStream(CommonConstants.PROPERTY_FILE_PATH)) {
			prop.load(inputStream);
		} catch (IOException e) {
			return null;
		}
		ConfigModel model = new ConfigModel();
		model.setHost(prop.getProperty(CommonConstants.FINAL_SFTP_HOST));
		model.setUsername(prop.getProperty(CommonConstants.FINAL_SFTP_USERNAME));
		model.setPassword(prop.getProperty(CommonConstants.FINAL_SFTP_PASSWORD));
		model.setPort(prop.getProperty(CommonConstants.FINAL_SFTP_PORT));
		model.setFinalFolder(prop.getProperty(CommonConstants.FINAL_SFTP_FOLDER_NAME));
		model.setFailedFolder(prop.getProperty(CommonConstants.SFTP_FAILED_FOLDER_NAME));
		model.setCsvFilePath(prop.getProperty(CommonConstants.CSV_FILE_FORMAT));
		model.setNckCsvFileName(prop.getProperty(CommonConstants.NCK_CSV_FILE_FORMAT));
		model.setProcessedCsvFileMovementServerPath(
				prop.getProperty(CommonConstants.PROCESSED_CSV_FILE_MOVEMENT_ON_SERVER_PATH));
		model.setProcessedNckFileMovementServerPath(
				prop.getProperty(CommonConstants.PROCESSED_NCK_CSV_FILE_MOVEMENT_ON_SERVER_PATH));
		model.setSuccessFileMovementOnServerPath(prop.getProperty(CommonConstants.SUCCESS_FILE_MOVEMENT_ON_SERVER_PATH));
		if (null != crnNo && !crnNo.isEmpty()) {
			model.setInputDateFormat(prop.getProperty(crnNo + CommonConstants.INPUT_DATE_FORMAT));
			model.setHeaderRowNo(Integer.parseInt(prop.getProperty(crnNo + CommonConstants.HEADER_ROW_NUMBER)));
			model.setCellDetails(prop.getProperty(crnNo));
			model.setBankName(prop.getProperty(crnNo + CommonConstants.BANK_NAME));
			model.setNckCsvFileHeaders(prop.getProperty(crnNo + CommonConstants.NCK_FILE_HEADERS));
			model.setNckCsvFileValues(prop.getProperty(crnNo + CommonConstants.NCK_FILE_VALUES));
			model.setTotalAmount(prop.getProperty(crnNo+CommonConstants.TOTAL_AMOUNT));
			model.setDebitAccountNumber(prop.getProperty(crnNo+CommonConstants.DEBIT_ACCOUNT_NUMBER));
			model.setDebitDate(prop.getProperty(crnNo+CommonConstants.DEBIT_DATE));
			model.setDateFormat(prop.getProperty(crnNo+CommonConstants.DATE_FORMAT));
			model.setCifId(prop.getProperty(crnNo + CommonConstants.CIF_ID));
			model.setInputFilePattern(prop.getProperty(crnNo + CommonConstants.INPUT_FILE_PATTERN));
			model.setDecimalFieldName(prop.getProperty(crnNo + CommonConstants.DECIMAL_FIELD_NAME));
		}
		return model;
	}

	public static Properties readPropertiesFile(String fileName) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(fileName);
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}
		return prop;
	}

	public String getCrnNo() {
		Properties prop = new Properties();
		try (InputStream inputStream = getClass().getResourceAsStream(CommonConstants.PROPERTY_FILE_PATH)) {
			prop.load(inputStream);
		} catch (IOException e) {
			return null;
		}
		return prop.getProperty(CommonConstants.CITY_BANK_CRN_NUMBER);
	}
	
	public DbConfig getDbConfig() {
        Properties prop = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream(CommonConstants.PROPERTY_FILE_PATH)) {
            prop.load(inputStream);
        } catch (IOException e) {
            // Handle the exception
        }
        DbConfig model = new DbConfig();
        model.setJdbcUrl(prop.getProperty(CommonConstants.ORACLE_DB_URL));
        model.setOracleDbUsername(prop.getProperty(CommonConstants.ORACLE_DB_USERNAME));
        model.setOracleDbPassword(prop.getProperty(CommonConstants.ORACLE_DB_PASSWORD));
        return model;
    }

	public ConfigModel getNckFileName() {
		Properties prop = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream(CommonConstants.PROPERTY_FILE_PATH)) {
            prop.load(inputStream);
        } catch (IOException e) {
            // Handle the exception
        }
        ConfigModel model = new ConfigModel();
		model.setNckCsvFileName(prop.getProperty(CommonConstants.NCK_CSV_FILE_FORMAT));
		model.setHost(prop.getProperty(CommonConstants.FINAL_SFTP_HOST));
		model.setUsername(prop.getProperty(CommonConstants.FINAL_SFTP_USERNAME));
		model.setPassword(prop.getProperty(CommonConstants.FINAL_SFTP_PASSWORD));
		model.setPort(prop.getProperty(CommonConstants.FINAL_SFTP_PORT));
		model.setFinalFolder(prop.getProperty(CommonConstants.FINAL_SFTP_FOLDER_NAME));
		model.setFailedFolder(prop.getProperty(CommonConstants.SFTP_FAILED_FOLDER_NAME));
		model.setCsvFilePath(prop.getProperty(CommonConstants.CSV_FILE_FORMAT));
		model.setNckCsvFileName(prop.getProperty(CommonConstants.NCK_CSV_FILE_FORMAT));
		model.setProcessedCsvFileMovementServerPath(
				prop.getProperty(CommonConstants.PROCESSED_CSV_FILE_MOVEMENT_ON_SERVER_PATH));
		model.setProcessedNckFileMovementServerPath(
				prop.getProperty(CommonConstants.PROCESSED_NCK_CSV_FILE_MOVEMENT_ON_SERVER_PATH));
		return model;
	}

}

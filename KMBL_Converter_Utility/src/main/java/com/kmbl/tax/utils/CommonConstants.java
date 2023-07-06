package com.kmbl.tax.utils;

public class CommonConstants {

	public static final String FAIL = "Fail";

	public static final String FTP_FILE_LOCATION = "download.file.path";
	public static final String CSV_FILE_LOCATION = "generated.csv.path";

	public static final String FILE_JPEG = "jpeg";
	public static final String FILE_PDF = "pdf";
	public static final String FILE_PNG = "png";
	public static final String FILE_DOC = "doc";
	public static final String FILE_GIF = "gif";
	public static final String FILE_SVG = "svg";
	public static final String FILE_XLS = "xls";

	public static final String IMAGE = "Image";
	public static final String PDF = "Pdf";
	public static final String DOC = "Documents";
	public static final String GIF = "Gif";
	public static final String SVG = "Svg";
	public static final String XLS = "Xls";

	public static final String FRWD_SLASH = "/";
	public static final String WIN_DAT_FILES = "WIN_DAT_FILES";
	public static final String DAT_EXT = ".dat";
	public static final String WIN_DAT_PDF_FILES = "WIN_DAT_PDF_FILES";
	public static final String PDF_EXT = ".pdf";
	public static final String FTP = "FTP";
	public static final String SFTP = "SFTP";
	public static final String SUCCESS = "Success";
	public static final String DOUBLE_FRWD_SLASH = "//";
	public static final String AT_SIGN = "@";
	public static final String COLON = ":";
	public static final String QUESION_MARK = "?";
	public static final String PASSWORD = "password";
	public static final String EQUAL = "=";
	public static final String AND = "&";

	public static final String PROPERTY_FILE_PATH = "/application.properties";
	public static final String NCK_RESPONSE_FILE_PATH = "/nckresponse.properties";
	public static final String CONFIG_TYPE = "config.type";
	public static final String SFTP_FAILED_FOLDER_NAME = "sftp.failed.file.movement.folderName";
	public static final String SFTP_HOST = "sftp.host";
	public static final String SFTP_INBOX_FOLDER_NAME = "sftp.file.collect.folderName";
	public static final String SFTP_PASSWORD = "sftp.password";
	public static final String SFTP_PORT = "sftp.port";
	public static final String SFTP_INBOX_SUCCESS_NAME = "sftp.success.file.movement.folderName";
	public static final String SFTP_USERNAME = "sftp.username";
	public static final String SFTP_DELAY = "sftp.delay";
	public static final String SFTP_OPTIONS = "sftp.options";
	public static final String FILE_DOWNLOAD_ON_SERVER_PATH = "file_download_on_server_path";
	public static final String FAILED_FILE_MOVEMENT_ON_SERVER_PATH = "failed_file_movement_folder_path";
	public static final String SUCCESS_FILE_MOVEMENT_ON_SERVER_PATH = "success_file_movement_folder_path";
	public static final String REJECTED_FILE_MOVEMENT_ON_SERVER_PATH = "success_file_movement_folder_path";
	public static final String PROCESSED_NCK_CSV_FILE_MOVEMENT_ON_SERVER_PATH = "processed_nck_file_movement_folder_path";
	public static final String PROCESSED_CSV_FILE_MOVEMENT_ON_SERVER_PATH = "processed_csv_file_movement_folder_path";

	public static final String MODULE_TYPE = "MODULE_TYPE";
	public static final String INBOX_FOLDER = "INBOX_FOLDER";
	public static final String SUCCESS_FOLDER = "SUCCESS_FOLDER";
	public static final String FAILED_FOLDER = "FAILED_FOLDER";
	public static final String NCK_CSV_FILE_MOVEMNT_FOLDER_PATH = "NCK.CSV_PROCESSED_FOLDER_NAME";
	public static final String CSV_FILE_MOVEMNT_FOLDER_PATH = "CSV_PROCESSED_FOLDER_NAME";
	public static final String FILE_DOWNLOAD_PATH = "FILE_DOWNLOAD_PATH";
	public static final String LOG_TYPE_INFO = "info";
	public static final String LOG_TYPE_WARNING = "warning";

	public static final String FINAL_SFTP_HOST = "sftp.host.final.file.movement";
	public static final String FINAL_SFTP_PASSWORD = "sftp.password.final.file.movement";
	public static final String FINAL_SFTP_PORT = "sftp.port.final.file.movement";
	public static final String FINAL_SFTP_USERNAME = "sftp.username.final.file.movement";
	public static final String FINAL_SFTP_FOLDER_NAME = "sftp.folder.name.final.file.movement";

	public static final String TOTAL_AMOUNT = ".total.amount";
	public static final String DEBIT_ACCOUNT_NUMBER = ".debit.account.number";
	public static final String DEBIT_DATE = ".debit.date";
	public static final String DATE_FORMAT = ".date.format";

	public static final String BANK_NAME = ".name";
	public static final String CSV_FILE_FORMAT = "csv.file.name";
	public static final String NCK_CSV_FILE_FORMAT = "nck.csv.file.name";
	public static final String CRN_NO = "crnNo";
	public static final String DATE = "DATE";
	public static final String SEQUENCENO = "SEQUENCENO";
	public static final String BANK_NAME_HEADER = "Bank Name";
	public static final String TAN = "TAN";
	public static final String PAN = "PAN";

	public static final String HEADER_ROW_NUMBER = ".header-row-no";

	public static final String INPUT_DATE_FORMAT = ".input-date-format";

	public static final String NCK_FILE_ERROR_CODE_HEADER = "ERROR_CODE";
	public static final String NCK_FILE_ERROR_ERROR_DESCRIPTION_HEADER = "ERROR_DESCRIPTION";

	public static final String NCK_FILE_ERROR_CODE = "TVE007";
	public static final String NCK_FILE_ERROR_ERROR_DESCRIPTION = "Invalid Corporate Id";

	public static final String FILE_VALIDATING_REGEX = "^BDT_DTAX_(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[012])\\d{2}_\\d{3}$";
	public static final String FINDING_SEQUENCE_NUMBER_REGEX = "_(\\d{3})\\.[^.]+$";

	public static final String CITY_BANK_CRN_NUMBER = "city_bank_crn_number";

	public static final String NCK_FILE_HEADERS = ".NCK.file.headers";
	public static final String NCK_FILE_VALUES = ".NCK.file.values";

	public static final String CIF_ID = ".cifId";
	public static final String INPUT_FILE_PATTERN = ".input.file.pattern";
	public static final String DECIMAL_FIELD_NAME = ".decimal.Field.Name";
	
	
	public static final String ORACLE_DB_URL = "oracle.db.jdbcString";
    public static final String ORACLE_DB_USERNAME = "oracle.db.username";
    public static final String ORACLE_DB_PASSWORD= "oracle.db.password";
    
    public static final String COMPLETED = "Completed";
    public static final String FAILED = "Failed";
    public static final String INPROCESS = "In-Process";
    public static final String METHOD_NAME = "Post";
    public static final String CLASS_NAME = "SFTPFileReadProcessor";
    public static final String UTILITY_NAME = "Converter Utility";
    public static final String PROCESS_USER = "System user";
    
    public static final String SUCCESS_PROCESS_DETAILS = "File converted .xls/.xlsx to .csv successfully !!!";
    public static final String FAILED_PROCESS_DETAILS = "File conversion failed ";
    public static final String INCORRECT_FILENAME = " because of Incorrect Filename";
    public static final String INCORRECT_FILE_HEADER = " because of Incorrect file Headers";
    public static final String INCORRECT_FILE_DETAILS = " because of Incorrect file details";
    public static final String FILE_ALREADY_PROCESSED1 = " because of file already processed, Duplicate File name";
    public static final String INCORRECT_FILE_NAME_DATE = " because of File date greater than system date";
    public static final String INCORRECT_FILE_EXTENSION = " because of File Extension does not match with the converter.";
    public static final String INCORRECT_TOTAL_AMOUNT = " because of incorrect total amount.";
    public static final String SCRIPTING_TAG_PRESENT = " because of The file has been rejected due to the presence of scripting tags.";
    
    public static final String NCK_FILE_HEADER=	"NCK.file.headers";
    public static final String NCK_CURRUPTED_FILE_ERROR_CODE_AND_DESC="NCK.corrupted.file.values";
    public static final String NCK_UNSUPPORTED_FILE_FORMAT_ERROR_CODE_AND_DESC="NCK.unsupported.file.format.values";
    public static final String NCK_UNSUPPORTED_FILE_SIZE_ERROR_CODE_AND_DESC="NCK.unsupported.file.size.values";
    public static final String NCK_DUPLICATE_FILE_NAME_ERROR_CODE_AND_DESC="NCK.duplicate.file.name.values";
    public static final String NCK_INVALID_FILE_NAME_NAME_ERROR_CODE_AND_DESC="NCK.invalid.file.name.values";
    public static final String NCK_DUPLICATE_FILE_ERROR_CODE_AND_DESC="NCK.duplicate.file.values";
    public static final String NCK_INVALID_FILE_CORPORATE_ERROR_CODE_AND_DESC="NCK.invalid.file.corporate.values";
    public static final String NCK_DATE_FILE_ERROR_CODE_AND_DESC="NCK.date.issue.file.values";
    public static final String NCK_FILE_HEADER_ERROR_CODE_AND_DESC="NCK.invalid.header.file.values";
    public static final String NCK_FILE_DETAILS_ERROR_CODE_AND_DESC="NCK.invalid.details.file.values";
    public static final String NCK_INVALID_AMOUNT_ERROR_CODE_AND_DESC="NCK.decimal.amount.file.values";
    public static final String NCK_SCRIPTING_TAG_ERROR_CODE_AND_DESC="NCK.scripting.tag.present.values";
    
    public static final String INCORRECT_HEADERS = "INCORRECT HEADERS";
    public static final String INCORRECT_DATA = "INCORRECT DATA";
    public static final String INCORRECT_DATE = "INCORRECT DATE";
    public static final String CORRECT_DATE = "CORRECT DATE";
    public static final String FILE_ALREADY_PROCESSED = "file Already processed";
    public static final String FILE_NOT_PROCESSED = "file not processed";
    

}

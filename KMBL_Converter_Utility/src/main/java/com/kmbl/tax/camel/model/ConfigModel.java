package com.kmbl.tax.camel.model;

public class ConfigModel {

	private String username;
	private String password;
	private String configType;
	private String host;
	private String port;
	private String inboxFolder;
	private String successFolder;
	private String failedFolder;
	private String processedNckFileMovementServerPath;
	private String processedCsvFileMovementServerPath;
	private String delay;
	private String options;
	private String finalFolder;
	private String cellDetails;
	private String debitAccountNumber;
	private String totalAmount;
	private String debitDate;
	private String dateFormat;
	private String bankName;
	private String csvFilePath;
	private Integer headerRowNo;
	private String cifId;
	private String inputDateFormat;
	private String inputFilePattern;
	private String fileDownloadOnServerPath;

	private String successFileMovementOnServerPath;
	private String failedFileMovementOnServerPath;
	private String rejectedFileMovementOnServerPath;

	private String nckCsvFileName;
	private String nckCsvFileHeaders;
	private String nckCsvFileValues;

	private String decimalFieldName;

	public String getNckCsvFileHeaders() {
		return nckCsvFileHeaders;
	}

	public void setNckCsvFileHeaders(String nckCsvFileHeaders) {
		this.nckCsvFileHeaders = nckCsvFileHeaders;
	}

	public String getNckCsvFileValues() {
		return nckCsvFileValues;
	}

	public void setNckCsvFileValues(String nckCsvFileValues) {
		this.nckCsvFileValues = nckCsvFileValues;
	}

	public String getNckCsvFileName() {
		return nckCsvFileName;
	}

	public void setNckCsvFileName(String nckCsvFileName) {
		this.nckCsvFileName = nckCsvFileName;
	}

	public String getRejectedFileMovementOnServerPath() {
		return rejectedFileMovementOnServerPath;
	}

	public void setRejectedFileMovementOnServerPath(String rejectedFileMovementOnServerPath) {
		this.rejectedFileMovementOnServerPath = rejectedFileMovementOnServerPath;
	}

	public String getSuccessFileMovementOnServerPath() {
		return successFileMovementOnServerPath;
	}

	public void setSuccessFileMovementOnServerPath(String successFileMovementOnServerPath) {
		this.successFileMovementOnServerPath = successFileMovementOnServerPath;
	}

	public String getFailedFileMovementOnServerPath() {
		return failedFileMovementOnServerPath;
	}

	public void setFailedFileMovementOnServerPath(String failedFileMovementOnServerPath) {
		this.failedFileMovementOnServerPath = failedFileMovementOnServerPath;
	}

	public String getCsvFilePath() {
		return csvFilePath;
	}

	public void setCsvFilePath(String csvFilePath) {
		this.csvFilePath = csvFilePath;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getDebitAccountNumber() {
		return debitAccountNumber;
	}

	public void setDebitAccountNumber(String debitAccountNumber) {
		this.debitAccountNumber = debitAccountNumber;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDebitDate() {
		return debitDate;
	}

	public void setDebitDate(String debitDate) {
		this.debitDate = debitDate;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getCellDetails() {
		return cellDetails;
	}

	public void setCellDetails(String cellDetails) {
		this.cellDetails = cellDetails;
	}

	public String getFinalFolder() {
		return finalFolder;
	}

	public void setFinalFolder(String finalFolder) {
		this.finalFolder = finalFolder;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getInboxFolder() {
		return inboxFolder;
	}

	public void setInboxFolder(String inboxFolder) {
		this.inboxFolder = inboxFolder;
	}

	public String getSuccessFolder() {
		return successFolder;
	}

	public void setSuccessFolder(String successFolder) {
		this.successFolder = successFolder;
	}

	public String getFailedFolder() {
		return failedFolder;
	}

	public void setFailedFolder(String failedFolder) {
		this.failedFolder = failedFolder;
	}

	public Integer getHeaderRowNo() {
		return headerRowNo;
	}

	public void setHeaderRowNo(Integer headerRowNo) {
		this.headerRowNo = headerRowNo;
	}

	public String getInputDateFormat() {
		return inputDateFormat;
	}

	public void setInputDateFormat(String inputDateFormat) {
		this.inputDateFormat = inputDateFormat;
	}

	public String getFileDownloadOnServerPath() {
		return fileDownloadOnServerPath;
	}

	public void setFileDownloadOnServerPath(String fileDownloadOnServerPath) {
		this.fileDownloadOnServerPath = fileDownloadOnServerPath;
	}

	public String getProcessedNckFileMovementServerPath() {
		return processedNckFileMovementServerPath;
	}

	public void setProcessedNckFileMovementServerPath(String processedNckFileMovementServerPath) {
		this.processedNckFileMovementServerPath = processedNckFileMovementServerPath;
	}

	public String getProcessedCsvFileMovementServerPath() {
		return processedCsvFileMovementServerPath;
	}

	public void setProcessedCsvFileMovementServerPath(String processedCsvFileMovementServerPath) {
		this.processedCsvFileMovementServerPath = processedCsvFileMovementServerPath;
	}

	public String getCifId() {
		return cifId;
	}

	public void setCifId(String cifId) {
		this.cifId = cifId;
	}

	public String getInputFilePattern() {
		return inputFilePattern;
	}

	public void setInputFilePattern(String inputFilePattern) {
		this.inputFilePattern = inputFilePattern;
	}

	public String getDecimalFieldName() {
		return decimalFieldName;
	}

	public void setDecimalFieldName(String decimalFieldName) {
		this.decimalFieldName = decimalFieldName;
	}

	@Override
	public String toString() {
		return "ConfigModel [username=" + username + ", password=" + password + ", configType=" + configType + ", host="
				+ host + ", port=" + port + ", inboxFolder=" + inboxFolder + ", successFolder=" + successFolder
				+ ", failedFolder=" + failedFolder + ", delay=" + delay + ", fileDownloadOnServerPath="
				+ fileDownloadOnServerPath + ", successFileMovementOnServerPath=" + successFileMovementOnServerPath
				+ ", failedFileMovementOnServerPath=" + failedFileMovementOnServerPath + ", options=" + options + "]";
	}
}

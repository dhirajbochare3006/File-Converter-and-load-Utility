package com.kmbl.tax.camel.entity;

import java.util.Date;

public class Audit {

	int auditLogId;
	String utilityName;
	String className;
	String methodName;
	String processUser;
	String processStartTime;
	String processEndTime;
	String processDetails;
	String inputFileName;
	String outputFileName;
	int inputRecordCount;
	int procesedRecordCount;
	String status;
	String processId;
	
	public int getAuditLogId() {
		return auditLogId;
	}
	public void setAuditLogId(int auditLogId) {
		this.auditLogId = auditLogId;
	}
	public String getUtilityName() {
		return utilityName;
	}
	public void setUtilityName(String utilityName) {
		this.utilityName = utilityName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getProcessUser() {
		return processUser;
	}
	public void setProcessUser(String processUser) {
		this.processUser = processUser;
	}
	public String getProcessStartTime() {
		return processStartTime;
	}
	public void setProcessStartTime(String processStartTime) {
		this.processStartTime = processStartTime;
	}
	public String getProcessEndTime() {
		return processEndTime;
	}
	public void setProcessEndTime(String processEndTime) {
		this.processEndTime = processEndTime;
	}
	public String getProcessDetails() {
		return processDetails;
	}
	public void setProcessDetails(String processDetails) {
		this.processDetails = processDetails;
	}
	public int getInputRecordCount() {
		return inputRecordCount;
	}
	public void setInputRecordCount(int inputRecordCount) {
		this.inputRecordCount = inputRecordCount;
	}
	public int getProcesedRecordCount() {
		return procesedRecordCount;
	}
	public void setProcesedRecordCount(int procesedRecordCount) {
		this.procesedRecordCount = procesedRecordCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInputFileName() {
		return inputFileName;
	}
	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}
	public String getOutputFileName() {
		return outputFileName;
	}
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
}

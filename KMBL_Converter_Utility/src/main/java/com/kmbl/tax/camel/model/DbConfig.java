package com.kmbl.tax.camel.model;

public class DbConfig {

	private String jdbcUrl;
	private String oracleDbUsername;
	private String oracleDbPassword;
	
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getOracleDbUsername() {
		return oracleDbUsername;
	}
	public void setOracleDbUsername(String oracleDbUsername) {
		this.oracleDbUsername = oracleDbUsername;
	}
	public String getOracleDbPassword() {
		return oracleDbPassword;
	}
	public void setOracleDbPassword(String oracleDbPassword) {
		this.oracleDbPassword = oracleDbPassword;
	}
}

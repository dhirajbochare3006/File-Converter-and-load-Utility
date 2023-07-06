package com.kmbl.tax.camel.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kmbl.tax.camel.config.Configrations;
import com.kmbl.tax.camel.entity.Audit;
import com.kmbl.tax.camel.model.DbConfig;
import com.kmbl.tax.utils.CommonConstants;

@Component
public class UtilityAudit {

	private static final Logger logger = LoggerFactory.getLogger(UtilityAudit.class);
	static Configrations configrations = new Configrations();

	public static int saveAuditDetails(Audit audit) {
		int sequenceId = 0;
		DbConfig configModel = configrations.getDbConfig();
		String selectQuery = "SELECT seq_audit_log_id.CURRVAL FROM kotax_audit_log"; // Retrieve the current sequence ID
		String query = "INSERT INTO kotax_audit_log (audit_log_id,utility_name,class_name,method_name,process_user,process_starttime,process_details,input_file_name,input_record_count,processed_record_count,status,process_id) VALUES (seq_audit_log_id.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection connection = DriverManager.getConnection(configModel.getJdbcUrl(),
				configModel.getOracleDbUsername(), configModel.getOracleDbPassword());
				PreparedStatement statement = connection.prepareStatement(query);
				PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {

			// Set the values for the parameters

			statement.setString(1, audit.getUtilityName());
			statement.setString(2, audit.getClassName());
			statement.setString(3, audit.getMethodName());
			statement.setString(4, CommonConstants.PROCESS_USER);
			statement.setTimestamp(5, Timestamp.valueOf(audit.getProcessStartTime()));
			statement.setString(6, audit.getProcessDetails());
			statement.setString(7, audit.getInputFileName());
			statement.setInt(8, audit.getInputRecordCount());
			statement.setInt(9, audit.getProcesedRecordCount());
			statement.setString(10, audit.getStatus());
			statement.setString(11, audit.getProcessId());

			// Execute the insert statement
			int rowsInserted = statement.executeUpdate();

			if (rowsInserted > 0) {
				logger.info(
						" Inside UtilityAudit :: saveAuditDetails() method :: The audit data has been successfully inserted into the database.");
				ResultSet resultSet = selectStatement.executeQuery();
				if (resultSet.next()) {
					sequenceId = resultSet.getInt(1);
					logger.info(
							" Inside UtilityAudit :: saveAuditDetails() method :: Current sequence ID: " + sequenceId);
				}
				resultSet.close();
			} else {
				logger.error(
						" Inside UtilityAudit :: saveAuditDetails() method :: Failed to insert the audit data into the database.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(
					" Inside UtilityAudit :: saveAuditDetails() method :: Exception occured while inserting the audit details :: "
							+ e.getMessage());
		}
		return sequenceId;

	}

	public static void updateAuditDetails(Audit audit) {

		DbConfig configModel = configrations.getDbConfig();
		String updateQuery = null;
		if(0 != audit.getInputRecordCount() && audit.getInputRecordCount()>0) {
			updateQuery = "UPDATE kotax_audit_log SET input_record_count = "
					+ audit.getInputRecordCount() + ", processed_record_count=" + audit.getProcesedRecordCount() + ", output_file_name= '" +audit.getOutputFileName()
					+"', process_endtime = TIMESTAMP '"+Timestamp.valueOf(audit.getProcessEndTime()) +"', status='" +audit.getStatus()+"', process_details= '" + audit.getProcessDetails()
					+ "' WHERE audit_log_id = " + audit.getAuditLogId();
		}else {
			updateQuery = "UPDATE kotax_audit_log SET process_endtime = TIMESTAMP '"
					+ Timestamp.valueOf(audit.getProcessEndTime()) + "', status='" + audit.getStatus() + "', process_details= '" + audit.getProcessDetails()
					+ "', output_file_name= '" +audit.getOutputFileName() + "', processed_record_count=" + audit.getProcesedRecordCount()
				+ " WHERE audit_log_id = " + audit.getAuditLogId();
		}

		try (Connection connection = DriverManager.getConnection(configModel.getJdbcUrl(),
				configModel.getOracleDbUsername(), configModel.getOracleDbPassword());
				Statement statement = connection.createStatement()) {

			// Execute the update statement
			int rowsUpdated = statement.executeUpdate(updateQuery);

			if (rowsUpdated > 0) {
				logger.info(" Inside UtilityAudit :: saveAuditDetails() method :: Audit data updated successfully.");
			} else {
				logger.error(" Inside UtilityAudit :: saveAuditDetails() method :: No Audit data was updated.");
			}

		} catch (SQLException e) {
			logger.error(
					" Inside UtilityAudit :: saveAuditDetails() method :: Exception occured while updating the audit details :: "
							+ e.getMessage());
		}
	}

}

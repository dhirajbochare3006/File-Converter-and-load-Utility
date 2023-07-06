package com.kmbl.tax.camel.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.kmbl.tax.camel.model.DbConfig;

public class DBConnection {

    public Connection getDbConnection() {
        Connection dbConnection = null;
		Configrations configrations = new Configrations();
		DbConfig configModel = configrations.getDbConfig();
        try {
            dbConnection = DriverManager.getConnection(configModel.getJdbcUrl(), configModel.getOracleDbUsername(), configModel.getOracleDbPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
        return dbConnection;
    }
}


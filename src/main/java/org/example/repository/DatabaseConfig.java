package org.example.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.example.logger.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseConfig {
    private static HikariDataSource dataSource;
    protected PropertiesConfiguration queries;

    static {
        initializeDataSource();
    }

    private static synchronized void initializeDataSource() {
        if (dataSource != null) return;

        try {
            PropertiesConfiguration config = new PropertiesConfiguration();
            config.load("application.properties");

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(config.getString("db.url"));
            hikariConfig.setUsername(config.getString("db.username"));
            hikariConfig.setPassword(config.getString("db.password"));
            hikariConfig.setMaximumPoolSize(config.getInt("db.pool.size", 10));

            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            hikariConfig.addDataSourceProperty("allowPublicKeyRetrieval", "true"); // Allow public key retrieval
            hikariConfig.addDataSourceProperty("useSSL", "false"); // Disable SSL if not needed

            dataSource = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database connection pool", e);
        }
    }

    protected DatabaseConfig() throws ConfigurationException {
        this.queries = new PropertiesConfiguration();
        this.queries.load("application.properties");
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource.getConnection();
    }

    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }

    public void handleException(String methodName, String message, Exception e) {
        if (e instanceof SQLException) {
            SQLException sqlEx = (SQLException) e;
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,message +
                    " - SQL State: " + sqlEx.getSQLState() +
                    ", Error Code: " + sqlEx.getErrorCode() +
                    ", Message: " + sqlEx.getMessage());
        } else {
            Logger.getInstance().writeLog(Logger.Log.ERROR,Logger.Queue.DB,
                    "Error appeared in method: " + methodName + " with Message: " + message + " Exception Message: " + e.getMessage());
        }
    }
}
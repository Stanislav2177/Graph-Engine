package org.example.repository;

import org.apache.commons.configuration.ConfigurationException;
import org.example.logger.Logger;
import org.example.models.TransportType;
import org.example.repository.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TransportTypesRepo extends DatabaseConfig {
    private final String SQL_INSERT_TYPE = "INSERT INTO transport_types(name, description) VALUES(?, ?)";
    private final String SQL_SELECT_TYPE_BY_NAME = "SELECT * FROM transport_types WHERE type_id = ?";
    private final String SQL_DELETE_TYPE = "DELETE FROM transport_types WHERE type_id = ?";

    public TransportTypesRepo() throws ConfigurationException, org.apache.commons.configuration.ConfigurationException {
        super();
    }

    public void addTransportType(String name, String description) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_TYPE)) {

            statement.setString(1, name);
            statement.setString(2, description);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"Creating transport type failed, no rows affected.");
                throw new SQLException("Creating transport type failed, no rows affected.");
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB,"Created transport type: " + name +
                    " with description: " + description);
        } catch (SQLException e) {
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"SQL State: " + e.getSQLState());
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"Error Code: " + e.getErrorCode());
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"Message: " + e.getMessage());
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public TransportType getTransportType(int typeId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_TYPE_BY_NAME)) {

            statement.setInt(1, typeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    TransportType transportType = new TransportType();
                    transportType.setTypeId(resultSet.getInt(1));
                    transportType.setName(resultSet.getString(2));
                    transportType.setDescription(resultSet.getString(3));
                    return transportType;
                } else {
                    throw new SQLException("Transport type was not found with id: " + typeId);
                }
            }
        } catch (SQLException e) {
            handleException("Error retrieving transport data", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }


    public void deleteTransportType(byte typeId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_TYPE)) {

            statement.setByte(1, typeId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"Deleting transport type failed, no rows affected.");
                throw new SQLException("Deleting transport type failed, no rows affected.");
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB,"Deleted transport type with ID: " + typeId);
        } catch (SQLException e) {
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"SQL State: " + e.getSQLState());
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"Error Code: " + e.getErrorCode());
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"Message: " + e.getMessage());
            throw new RuntimeException("Database operation failed", e);
        }
    }
}
package org.example.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.configuration.ConfigurationException;
import org.example.logger.Logger;

import java.sql.*;
import java.util.List;

public class TransportRoutesRepo extends DatabaseConfig {
    private final String SQL_INSERT_ROUTE = "INSERT INTO transport_routes(source_node_id, target_node_id, route_path) VALUES(?, ?, ?)";

    public TransportRoutesRepo() throws ConfigurationException, org.apache.commons.configuration.ConfigurationException {
        super();
    }

    public int addTransportRoute(List<Integer> routes, int source, int target) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_ROUTE, Statement.RETURN_GENERATED_KEYS)) {

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(routes);
            statement.setInt(1, source);
            statement.setInt(2, target);
            statement.setString(3, json);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"Creating edge failed, no rows affected.");
                throw new SQLException("Creating edge failed, no rows affected.");
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB,"Created route between " + source +
                    " and " + target + " with path : " + json);

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB,"Created node with ID: " + id);
                    return id;
                }
            } catch (SQLException e) {
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"No generated key received with message : " + e.getMessage());
            }
        } catch (SQLException e) {
            handleException("addTransportRoute", "Problem in creating transport route", e);
            throw new RuntimeException("Database operation failed", e);
        } catch (JsonProcessingException e) {
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"Message: " + e.getMessage());
            throw new RuntimeException("DataBase Operation failed", e);
        }

        return -1;
    }
}

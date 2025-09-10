package org.example.repository;

import org.apache.commons.configuration.ConfigurationException;
import org.example.logger.Logger;
import org.example.models.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EdgesRepo extends DatabaseConfig {
    private final String SQL_INSERT_EDGE = "INSERT INTO edges(source_node_id, target_node_id, weight) VALUES(?, ?, ?)";
    private final String SQL_SELECT_ALL_EDGES_IDS_BASED_ON_SOURCE_NODE_ID = "SELECT target_node_id, weight  FROM edges WHERE source_node_id = ?";
    private final String SQL_UPDATE_EDGE = "UPDATE edges SET weight = ? WHERE source_node_id = ? AND target_node_id = ?";

    public EdgesRepo() throws ConfigurationException, org.apache.commons.configuration.ConfigurationException {
        super();
    }

    public void addConnectionBetweenTwoNodes(int sourceID, int targetID, double weight) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_EDGE)) {

            statement.setInt(1, sourceID);
            statement.setInt(2, targetID);
            statement.setDouble(3, weight);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"Creating edge failed, no rows affected.");
                throw new SQLException("Creating edge failed, no rows affected.");
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB,"Created connection between " + sourceID +
                    " and " + targetID + " with weight " + weight + " total rows affected: " + affectedRows);
        } catch (SQLException e) {
            handleException("addConnectionBetweenTwoNodes", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }
    public boolean updateEdgeWeight(int sourceId, int targetId, double newWeight) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_EDGE)) {

             statement.setDouble(1, newWeight);
            statement.setInt(2, sourceId);
            statement.setInt(3, targetId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                Logger.getInstance().writeLog(Logger.Log.WARNING, Logger.Queue.DB,
                        "No edge found to update between source: " + sourceId + " and target: " + targetId);
                return false;
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS,Logger.Queue.DB,
                    "Updated edge weight between " + sourceId + " and " + targetId + " to " + newWeight);
            return true;

        } catch (SQLException e) {
            handleException("updateEdgeWeight", e.getMessage(), e);
            throw new RuntimeException("Database operation (updateEdgeWeight) failed", e);
        }
    }

    public List<ConnectionDB> getAllEdgesIDsBasedOnSourceNodeID(int id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_EDGES_IDS_BASED_ON_SOURCE_NODE_ID)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();


            List<ConnectionDB> connectionDBList = new ArrayList<>();
            while (rs.next()) {
                int target_node_id = rs.getInt("target_node_id");
                int weight = rs.getInt("weight");
                connectionDBList.add(new ConnectionDB(target_node_id, weight));
            }

            if(connectionDBList.size() == 0){
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,"No data was received for node " + id);
                return connectionDBList;
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB,"Total edges loaded for node " + id + ": " + connectionDBList.size());

            return connectionDBList;
        } catch (SQLException e) {
            handleException("getAllEdgesIDsBasedOnSourceNodeID", e.getMessage(), e);
            throw new RuntimeException("Database operation (getAllEdgesIDsBasedOnSourceNodeID) failed", e);
        }
    }
}

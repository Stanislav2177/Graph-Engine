package org.example.repository;

import org.example.graph.Node;
import org.apache.commons.configuration.ConfigurationException;
import org.example.logger.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NodesRepo extends DatabaseConfig {
    private final String SQL_INSERT_INTO_NODES = "INSERT INTO nodes(label, is_station, lat, lng) VALUES(?, ?, ?, ?)";
    private final String SQL_SEARCH_BY_LABEL = "SELECT * FROM nodes WHERE label = ?";
    private final String SQL_DELETE_BY_ID = "DELETE  FROM nodes WHERE id  = ?";
    private final String SQL_SELECT_ALL = "SELECT * FROM nodes";
    private final String SQL_UPDATE_NODE = "UPDATE nodes SET label = ?, is_station = ?, lat = ?, lng = ? WHERE id = ?";

    public NodesRepo() throws ConfigurationException, org.apache.commons.configuration.ConfigurationException {
        super();
    }

    public int addNodeToDatabase(Node node) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_NODES, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, node.getLabel());
            statement.setBoolean(2, node.isStation());
            statement.setDouble(3, node.getLat());
            statement.setDouble(4, node.getLng());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    node.setId(id);
                    Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB,"Created node with ID: " + id);
                    return id;
                }
            } catch (SQLException e) {
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB, "No generated key received with message : " + e.getMessage());
            }
            return 0;
        } catch (SQLException e) {
            handleException("addNodeToDatabase", e.getMessage(), e);
            return 0;
        }
    }

    //Not fully implemented
    public Node getNodeByLabel(String label) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SEARCH_BY_LABEL)) {

            statement.setString(1, label);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Node node = new Node(
                            resultSet.getString("label"),
                            resultSet.getDouble("lat"),
                            resultSet.getDouble("lng")
                    );
                    node.setId(resultSet.getInt("id"));
                    node.setStation(resultSet.getBoolean("is_station"));
                    return node;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving node", e);
        }
        return null;
    }

    public boolean updateNode(Node node) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_NODE)) {

            statement.setString(1, node.getLabel());
            statement.setBoolean(2, node.isStation());
            statement.setDouble(3, node.getLat());
            statement.setDouble(4, node.getLng());
            statement.setInt(5, node.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                Logger.getInstance().writeLog(Logger.Log.WARNING, Logger.Queue.DB,"No rows affected when attempting to update node with ID: " + node.getId());
                return false;
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB,"Successfully updated node with ID: " + node.getId());
            return true;

        } catch (SQLException e) {
            handleException("updateNode", e.getMessage(), e);
            throw new RuntimeException("Database operation (updateNode) failed", e);
        }
    }

    public int getNodeIdByLabel(String label) {
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_SEARCH_BY_LABEL)) {
            statement.setString(1, label);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    int id = resultSet.getInt("id");
                    return id;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving node", e);
        }
        return -1;
    }
    public boolean deleteNodeById(int id) {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            // 1. First delete transport_vehicles that reference this node
            String deleteVehiclesSQL = "DELETE FROM transport_vehicles " +
                    "WHERE start_node_id = ? OR end_node_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteVehiclesSQL)) {
                stmt.setInt(1, id);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }

            // 2. Handle transport_data (via routes)
            String updateTransportDataSQL = "UPDATE transport_data td " +
                    "JOIN transport_routes tr ON td.route_id = tr.route_id " +
                    "SET td.route_id = NULL " +
                    "WHERE tr.source_node_id = ? OR tr.target_node_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateTransportDataSQL)) {
                stmt.setInt(1, id);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }

            // 3. Delete transport_routes
            String deleteRoutesSQL = "DELETE FROM transport_routes " +
                    "WHERE source_node_id = ? OR target_node_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteRoutesSQL)) {
                stmt.setInt(1, id);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }

            // 4. Delete schedule_transports
            String deleteSchedulesSQL = "DELETE FROM schedule_transports " +
                    "WHERE source_node_id = ? OR target_node_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteSchedulesSQL)) {
                stmt.setInt(1, id);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }

            // 5. Delete edges
            String deleteEdgesSQL = "DELETE FROM edges " +
                    "WHERE source_node_id = ? OR target_node_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteEdgesSQL)) {
                stmt.setInt(1, id);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }

            // 6. Finally delete the node
            try (PreparedStatement deleteNodeStmt = connection.prepareStatement(SQL_DELETE_BY_ID)) {
                deleteNodeStmt.setInt(1, id);
                int affectedRows = deleteNodeStmt.executeUpdate();

                if (affectedRows == 0) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,
                        "Rollback failed: " + ex.getMessage());
            }
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,
                    "Delete node failed: " + e.getMessage());
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB,
                        "Connection close failed: " + e.getMessage());
            }
        }
    }

    public List<Node> getAllNodes() {
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {
            List<Node> nodes = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String label = rs.getString("label");
                boolean is_station = rs.getBoolean("is_station");
                double lat = rs.getDouble("lat");
                double lng = rs.getDouble("lng");

                nodes.add(new Node(id, label, is_station, lat, lng));
            }
            if (nodes.size() == 0) {
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB, "No data was received, when attempt was made for receiving all nodes");
                throw new SQLException("Receiving data from table nodes failed.");
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB,"Total nodes loaded: " + nodes.size());

            return nodes;
        } catch (SQLException e) {
            handleException("getAllNodes", e.getMessage(), e);
            throw new RuntimeException("Database operation (getAllNodes) failed", e);
        }
    }
}
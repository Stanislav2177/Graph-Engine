package org.example.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.configuration.ConfigurationException;
import org.example.logger.Logger;
import org.example.models.TransportType;
import org.example.models.TransportVehicle;
import org.example.models.TransportVehicleDetailedDTO;
import org.example.transport.DetailedTransportData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransportVehiclesRepo extends DatabaseConfig {
    private final String SQL_INSERT = "INSERT INTO transport_vehicles(registration_number, transport_type_id, total_seats, engine_info, serviceability, additional_info, start_node_id, end_node_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQL_SELECT_BY_ID = "SELECT * FROM transport_vehicles WHERE vehicle_id = ?";
    private final String SQL_UPDATE = "UPDATE transport_vehicles SET registration_number = ?, transport_type_id = ?, total_seats = ?, engine_info = ?, serviceability = ?, additional_info = ? WHERE vehicle_id = ?";
    private final String SQL_DELETE = "DELETE FROM transport_vehicles WHERE vehicle_id = ?";
    private final String SQL_SELECT_BY_NODE_ID = "SELECT * FROM transport_vehicles WHERE start_node_id = ?";
    private final String SQL_SELECT_ALL_VEHICLES = "SELECT \n" +
            "    tv.vehicle_id,\n" +
            "    tv.registration_number,\n" +
            "    tv.transport_type_id,\n" +
            "    tt.name AS transport_type_name,\n" +
            "    tv.total_seats,\n" +
            "    tv.engine_info,\n" +
            "    tv.serviceability,\n" +
            "    tv.additional_info,\n" +
            "    tv.start_node_id,\n" +
            "    start_node.label AS start_node_label,\n" +
            "    tv.end_node_id,\n" +
            "    end_node.label AS end_node_label\n" +
            "FROM \n" +
            "    transport_vehicles tv\n" +
            "LEFT JOIN \n" +
            "    transport_types tt ON tv.transport_type_id = tt.type_id\n" +
            "LEFT JOIN \n" +
            "    nodes start_node ON tv.start_node_id = start_node.id\n" +
            "LEFT JOIN \n" +
            "    nodes end_node ON tv.end_node_id = end_node.id\n" +
            "ORDER BY \n" +
            "    tv.vehicle_id;";
    private final String SQL_SELECT_BY_START_AND_END_NODE_ID = "SELECT * FROM transport_vehicles WHERE start_node_id = ? AND end_node_id = ?";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TransportVehiclesRepo() throws ConfigurationException, org.apache.commons.configuration.ConfigurationException {
        super();
    }

    // CREATE
    public boolean addVehicle(TransportVehicle transportVehicle, byte transportTypeId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, transportVehicle.getRegistrationNumber());
            statement.setByte(2, (byte) transportVehicle.getTransportType().getTypeId());
            statement.setInt(3, transportVehicle.getTotalSeats());
            statement.setString(4, transportVehicle.getEngineInfo());
            statement.setBoolean(5, transportVehicle.isServiceability());

            if (transportVehicle.getAdditionalInfo() != null) {
                statement.setString(6, objectMapper.writeValueAsString(transportVehicle.getAdditionalInfo()));
            } else {
                statement.setNull(6, java.sql.Types.JAVA_OBJECT);
            }

            statement.setInt(7, transportVehicle.getSourceId());
            statement.setInt(8, transportVehicle.getTargetId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int vehicleId = generatedKeys.getInt(1);
                    Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB,
                            "Created vehicle with ID: " + vehicleId);
                    return true;
                } else {
                    throw new SQLException("Creating vehicle failed, no ID obtained.");
                }
            }
        } catch (SQLException | JsonProcessingException e) {
            handleException("Error creating vehicle", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public List<TransportVehicleDetailedDTO> getAllVehiclesDetailed(){
            List<TransportVehicleDetailedDTO> result = new ArrayList<>();

            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL_VEHICLES);
                 ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    TransportVehicleDetailedDTO data = new TransportVehicleDetailedDTO();

                    data.setVehicleId(rs.getInt("vehicle_id"));
                    data.setRegistrationNumber(rs.getString("registration_number"));
                    data.setTotalSeats(rs.getInt("total_seats"));
                    data.setEngineInfo(rs.getString("transport_type_name"));
                    data.setServiceability(rs.getBoolean("total_seats"));
                    data.setAdditionalInfo(rs.getString("engine_info"));
                    data.setServiceability(rs.getBoolean("serviceability"));
                    data.setAdditionalInfo(rs.getString("additional_info"));
                    data.setSourceNodeId(rs.getInt("start_node_id"));
                    data.setSourceLabel(rs.getString("start_node_label"));
                    data.setTargetNodeId(rs.getInt("end_node_id"));
                    data.setTargetLabel(rs.getString("end_node_label"));

                    result.add(data);
                }


                if (result.isEmpty()) {
                    Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB, "No transport data found in detailed query.");
                }

                return result;
            } catch (SQLException e) {
                handleException("Error fetching detailed transport data", e.getMessage(), e);
            }

            return result;
    }

    /**
     * Retrieves all vehicles associated with a specific node
     * @param nodeId The ID of the node to get vehicles for
     * @return List of TransportVehicle objects associated with the node
     */
    public List<TransportVehicle> getVehiclesByNodeId(int nodeId) {
        List<TransportVehicle> vehicles = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_NODE_ID)) {

            statement.setInt(1, nodeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                TransportTypesRepo transportTypesRepo = new TransportTypesRepo();

                while (resultSet.next()) {
                    TransportVehicle transportVehicle = readResultAndCreateObj(resultSet, transportTypesRepo);
                    vehicles.add(transportVehicle);
                }
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB,
                    "Retrieved " + vehicles.size() + " vehicles for node ID: " + nodeId);

            return vehicles;
        } catch (SQLException | ConfigurationException e) {
            handleException("Error retrieving vehicles for node ID: " + nodeId, e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    /**
     * Retrieves all vehicles associated with a specific nodes
     * @param sourceId The start node for vehicle
     * @param targetId The end node for vehicle
     * @return List of TransportVehicle objects associated with the node
     */
    public List<TransportVehicle> getVehiclesByStartAndEndNodeId(int sourceId, int targetId) {
        List<TransportVehicle> vehicles = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_START_AND_END_NODE_ID)) {

            statement.setInt(1, sourceId);
            statement.setInt(2, targetId);
            try (ResultSet resultSet = statement.executeQuery()) {
                TransportTypesRepo transportTypesRepo = new TransportTypesRepo();

                while (resultSet.next()) {
                    TransportVehicle transportVehicle = readResultAndCreateObj(resultSet, transportTypesRepo);
                    vehicles.add(transportVehicle);
                }
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS,
                    Logger.Queue.DB,"Retrieved " + vehicles.size() + " vehicles for start node ID: " + sourceId + " end node ID : " + targetId);

            return vehicles;
        } catch (SQLException | ConfigurationException e) {
            handleException("Error retrieving vehicles for node ID: " + sourceId, e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    // READ
    public TransportVehicle getVehicle(int vehicleId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {

            statement.setInt(1, vehicleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    TransportTypesRepo transportTypesRepo = new TransportTypesRepo();
                    TransportVehicle transportVehicle = readResultAndCreateObj(resultSet, transportTypesRepo);

                    return transportVehicle;
                } else {
                    throw new SQLException("Vehicle not found with ID: " + vehicleId);
                }
            } catch (ConfigurationException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            handleException("Error retrieving vehicle", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    // UPDATE
    public void updateVehicle(int vehicleId, String registrationNumber, Byte transportTypeId,
                              int totalSeats, String engineInfo, boolean serviceability,
                              Map<String, Object> additionalInfo) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {

            statement.setString(1, registrationNumber);
            if (transportTypeId != null) {
                statement.setByte(2, transportTypeId);
            } else {
                statement.setNull(2, java.sql.Types.TINYINT);
            }
            statement.setInt(3, totalSeats);
            statement.setString(4, engineInfo);
            statement.setBoolean(5, serviceability);

            if (additionalInfo != null) {
                statement.setString(6, objectMapper.writeValueAsString(additionalInfo));
            } else {
                statement.setNull(6, java.sql.Types.JAVA_OBJECT);
            }

            statement.setInt(7, vehicleId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating vehicle failed, no rows affected.");
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS,
                    Logger.Queue.DB,"Updated vehicle with ID: " + vehicleId);
        } catch (SQLException | JsonProcessingException e) {
            handleException("Error updating vehicle", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    // DELETE
    public void deleteVehicle(int vehicleId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {

            statement.setInt(1, vehicleId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting vehicle failed, no rows affected.");
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS,
                    Logger.Queue.DB,"Deleted vehicle with ID: " + vehicleId);
        } catch (SQLException e) {
            handleException("Error deleting vehicle", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    /**
     * Method to create base transportVehicle object, which to be used for further processing
     * it return base object, only with this static fields, not included start_node_id and end_node_id
     * they are added further in the other states
     *
     * @param resultSet          Set of what is returned for the DB
     * @param transportTypesRepo instance of the repo for accessing the tables
     * @return object which can be processed alone or added to a DS
     */
    private TransportVehicle readResultAndCreateObj(ResultSet resultSet, TransportTypesRepo transportTypesRepo) {
        try {
            TransportVehicle vehicle = new TransportVehicle();
            // Set basic vehicle properties
            vehicle.setVehicleId(resultSet.getInt("vehicle_id"));
            vehicle.setRegistrationNumber(resultSet.getString("registration_number"));
            vehicle.setTotalSeats(resultSet.getInt("total_seats"));
            vehicle.setEngineInfo(resultSet.getString("engine_info"));
            vehicle.setServiceability(resultSet.getBoolean("serviceability"));
            vehicle.setSourceId(resultSet.getInt("start_node_id"));

            // Set transport type
            int transportTypeId = resultSet.getInt("transport_type_id");
            TransportType transportType = transportTypesRepo.getTransportType(transportTypeId);
            vehicle.setTransportType(transportType);

            // Set additional info if present
            String additionalInfoJson = resultSet.getString("additional_info");
            vehicle.setAdditionalInfo(additionalInfoJson);

            return vehicle;
        } catch (Exception ex) {
            Logger.getInstance().writeLog(Logger.Log.SUCCESS,
                    Logger.Queue.DB,"Error reading the result set for retrivieng a vehicle, with message: " + ex.getMessage());
            return null;
        }
    }
}

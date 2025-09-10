package org.example.repository;

import org.apache.commons.configuration.ConfigurationException;
import org.example.graph.Node;
import org.example.logger.Logger;
import org.example.models.TransportDataStatuses;
import org.example.transport.DetailedTransportData;
import org.example.transport.Trip;
import org.example.transport.TripStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class TransportDataRepo extends DatabaseConfig {
    private final String SQL_INSERT = "INSERT INTO transport_data(vehicle_id, route_id, schedule_time, actual_departure, actual_arrival, current_status) VALUES(?, ?, ?, ?, ?, ?)";
    private final String SQL_SELECT_BY_ID = "SELECT * FROM transport_data WHERE transport_data_id = ?";
    private final String SQL_UPDATE = "UPDATE transport_data SET vehicle_id = ?, route_id = ?, schedule_time = ?, actual_departure = ?, actual_arrival = ?, current_status = ? WHERE transport_data_id = ?";
    private final String SQL_DELETE = "DELETE FROM transport_data WHERE transport_data_id = ?";
    private final String SQL_SELECT_ALL = "SELECT * FROM transport_data";
    private final String SQL_UPDATE_STATUS = "UPDATE transport_data SET current_status = ? WHERE transport_data_id = ?";
    private final String SQL_UPDATE_DEPARTURE = "UPDATE transport_data SET actual_departure = ?, current_status = 'departed' WHERE transport_data_id = ?";
    private final String SQL_UPDATE_ARRIVAL = "UPDATE transport_data SET actual_arrival = ?, current_status = 'arrived' WHERE transport_data_id = ?";
    private final String SQL_DETAILED_TRANSPORT_DATA = """
                SELECT 
                    td.transport_data_id,
                    td.schedule_time,
                    td.actual_departure,
                    td.actual_arrival,
                    td.current_status,

                    tv.vehicle_id,
                    tv.registration_number,
                    tv.total_seats,
                    tv.engine_info,
                    tv.serviceability,
                    tv.additional_info,

                    tt.name AS transport_type_name,
                    tt.description AS transport_type_description,

                    tr.route_id,
                    tr.route_path,
                    tr.is_started,
                    tr.problem,

                    srcNode.id AS source_node_id,
                    srcNode.label AS source_label,
                    srcNode.lat AS source_lat,
                    srcNode.lng AS source_lng,

                    tgtNode.id AS target_node_id,
                    tgtNode.label AS target_label,
                    tgtNode.lat AS target_lat,
                    tgtNode.lng AS target_lng

                FROM transport_data td
                JOIN transport_vehicles tv ON td.vehicle_id = tv.vehicle_id
                LEFT JOIN transport_types tt ON tv.transport_type_id = tt.type_id
                JOIN transport_routes tr ON td.route_id = tr.route_id
                JOIN nodes srcNode ON tr.source_node_id = srcNode.id
                JOIN nodes tgtNode ON tr.target_node_id = tgtNode.id
            """;

    public TransportDataRepo() throws ConfigurationException {
        super();
    }

    // CREATE
    public int addTransportData(int vehicleId, int routeId, LocalDateTime scheduleTime,
                                LocalDateTime actualDeparture, LocalDateTime actualArrival,
                                TransportDataStatuses status) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, vehicleId);
            statement.setInt(2, routeId);
            setTimestampOrNull(statement, 3, scheduleTime);
            setTimestampOrNull(statement, 4, actualDeparture);
            setTimestampOrNull(statement, 5, actualArrival);
            statement.setString(6, status.toString());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating transport data failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int transportDataId = generatedKeys.getInt(1);
                    logSuccess("Created transport data with ID: " + transportDataId);
                    return transportDataId;
                } else {
                    throw new SQLException("Creating transport data failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            handleException("Error creating transport data", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public List<DetailedTransportData> getDetailedTransportDataList() {
        List<DetailedTransportData> result = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DETAILED_TRANSPORT_DATA);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                DetailedTransportData detailedTransportData = createDetailedTransportData(rs);
                result.add(detailedTransportData);
            }

            if (result.isEmpty()) {
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB, "No transport data found in detailed query.");
            }

        } catch (SQLException e) {
            handleException("Error fetching detailed transport data", e.getMessage(), e);
        }

        return result;
    }

    // READ
    public DetailedTransportData getDetailedTransportData(int transportDataId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID + " WHERE td.route_id = ?")) {

            statement.setInt(1, transportDataId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    DetailedTransportData detailedTransportData = createDetailedTransportData(resultSet);
                    return detailedTransportData;
                } else {
                    throw new SQLException("Transport data not found with ID: " + transportDataId);
                }
            }
        } catch (SQLException e) {
            handleException("Error retrieving transport data", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }


    // READ
    public Map<String, Object> getTransportData(int transportDataId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {

            statement.setInt(1, transportDataId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Map<String, Object> transportData = new HashMap<>();
                    transportData.put("transport_data_id", resultSet.getInt("transport_data_id"));
                    transportData.put("vehicle_id", resultSet.getInt("vehicle_id"));
                    transportData.put("route_id", resultSet.getInt("route_id"));
                    transportData.put("schedule_time", convertToLocalDateTime(resultSet.getTimestamp("schedule_time")));
                    transportData.put("actual_departure", convertToLocalDateTime(resultSet.getTimestamp("actual_departure")));
                    transportData.put("actual_arrival", convertToLocalDateTime(resultSet.getTimestamp("actual_arrival")));
                    transportData.put("current_status", resultSet.getString("current_status"));
                    return transportData;
                } else {
                    throw new SQLException("Transport data not found with ID: " + transportDataId);
                }
            }
        } catch (SQLException e) {
            handleException("Error retrieving transport data", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public List<Trip> getAllTransportData() {
        try (Statement stmt = getConnection().createStatement();
             ResultSet resultSet = stmt.executeQuery(SQL_SELECT_ALL)) {

            List<Trip> trips = new ArrayList<>();

            while (resultSet.next()) {
                TransportDataStatuses transportDataStatus = null;

                for (TransportDataStatuses value : TransportDataStatuses.values()) {
                    if (Objects.equals(value.toString(), resultSet.getString("current_status"))) {
                        transportDataStatus = value;
                    }
                }
                if (transportDataStatus == null) {
                    transportDataStatus = TransportDataStatuses.cancelled;
                }

                trips.add(new Trip(resultSet.getInt("transport_data_id"),
                        resultSet.getInt("vehicle_id"),
                        resultSet.getInt("route_id"),
                        convertToLocalDateTime(resultSet.getTimestamp("schedule_time")),
                        convertToLocalDateTime(resultSet.getTimestamp("actual_departure")),
                        convertToLocalDateTime(resultSet.getTimestamp("actual_arrival")),
                        transportDataStatus,
                        0,
                        TripStatus.TRAVELING
                ));
            }
            if (trips.size() == 0) {
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB, "No data was received, when attempt was made for receiving all nodes");
                throw new SQLException("Receiving data from table nodes failed.");
            }

            return trips;
        } catch (Exception ex) {
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.DB, "No data was received, when attempt was made for receiving all transportData");
        }

        return null;
    }

    // UPDATE
    public void updateTransportData(Trip trip) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {

            statement.setInt(1, trip.getVehicleId());
            statement.setInt(2, trip.getRoutePathId());
            setTimestampOrNull(statement, 3, trip.getScheduleTime());
            setTimestampOrNull(statement, 4, trip.getActualDeparture());
            setTimestampOrNull(statement, 5, trip.getActualArrival());
            statement.setString(6, trip.getTransportDataStatuses().toString());
            statement.setInt(7, trip.getTripId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating transport data failed, no rows affected.");
            }

            logSuccess("Updated transport data with ID: " + trip.getTripId());
        } catch (SQLException e) {
            handleException("Error updating transport data", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    // DELETE
    public void deleteTransportData(int transportDataId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {

            statement.setInt(1, transportDataId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting transport data failed, no rows affected.");
            }

            logSuccess("Deleted transport data with ID: " + transportDataId);
        } catch (SQLException e) {
            handleException("Error deleting transport data", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

// SPECIALIZED METHODS FOR TRANSPORT DATA

    public void updateStatus(int transportDataId, String newStatus) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_STATUS)) {

            statement.setString(1, newStatus);
            statement.setInt(2, transportDataId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating status failed, no rows affected.");
            }

            logSuccess("Updated status to " + newStatus + " for transport data ID: " + transportDataId);
        } catch (SQLException e) {
            handleException("Error updating status", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public void recordDeparture(int transportDataId, LocalDateTime departureTime) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_DEPARTURE)) {

            setTimestampOrNull(statement, 1, departureTime);
            statement.setInt(2, transportDataId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Recording departure failed, no rows affected.");
            }

            logSuccess("Recorded departure for transport data ID: " + transportDataId);
        } catch (SQLException e) {
            handleException("Error recording departure", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public void recordArrival(int transportDataId, LocalDateTime arrivalTime) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ARRIVAL)) {

            setTimestampOrNull(statement, 1, arrivalTime);
            statement.setInt(2, transportDataId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Recording arrival failed, no rows affected.");
            }

            logSuccess("Recorded arrival for transport data ID: " + transportDataId);
        } catch (SQLException e) {
            handleException("Error recording arrival", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        }
    }

// HELPER METHODS

    private void setTimestampOrNull(PreparedStatement statement, int parameterIndex, LocalDateTime dateTime) throws SQLException {
        if (dateTime != null) {
            statement.setTimestamp(parameterIndex, Timestamp.valueOf(dateTime));
        } else {
            statement.setNull(parameterIndex, Types.TIMESTAMP);
        }
    }

    private LocalDateTime convertToLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    private void logSuccess(String message) {
        Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB, message);
    }

    private DetailedTransportData createDetailedTransportData(ResultSet rs) throws SQLException {
        DetailedTransportData data = new DetailedTransportData();
        data.setTransportDataId(rs.getInt("transport_data_id"));
        data.setScheduleTime(convertToLocalDateTime(rs.getTimestamp("schedule_time")));
        data.setActualDeparture(convertToLocalDateTime(rs.getTimestamp("actual_departure")));
        data.setActualArrival(convertToLocalDateTime(rs.getTimestamp("actual_arrival")));
        data.setCurrentStatus(rs.getString("current_status"));

        data.setVehicleId(rs.getInt("vehicle_id"));
        data.setRegistrationNumber(rs.getString("registration_number"));
        data.setTotalSeats(rs.getInt("total_seats"));
        data.setEngineInfo(rs.getString("engine_info"));
        data.setServiceability(rs.getBoolean("serviceability"));
        data.setAdditionalInfo(rs.getString("additional_info"));

        data.setTransportTypeName(rs.getString("transport_type_name"));
        data.setTransportTypeDescription(rs.getString("transport_type_description"));

        data.setRouteId(rs.getInt("route_id"));
        data.setRoutePath(rs.getString("route_path"));
        data.setStarted(rs.getBoolean("is_started"));
        data.setProblem(rs.getString("problem"));

        data.setSourceNodeId(rs.getInt("source_node_id"));
        data.setSourceLabel(rs.getString("source_label"));
        data.setSourceLat(rs.getDouble("source_lat"));
        data.setSourceLng(rs.getDouble("source_lng"));

        data.setTargetNodeId(rs.getInt("target_node_id"));
        data.setTargetLabel(rs.getString("target_label"));
        data.setTargetLat(rs.getDouble("target_lat"));
        data.setTargetLng(rs.getDouble("target_lng"));

        return data;
    }
}

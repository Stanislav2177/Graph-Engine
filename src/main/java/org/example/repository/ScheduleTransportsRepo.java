package org.example.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.configuration.ConfigurationException;
import org.example.logger.Logger;
import org.example.models.ScheduleTransport;
import org.example.models.ScheduleTransportDTO;
import org.example.models.TransportVehicle;
import org.example.models.TransportVehicleDetailedDTO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleTransportsRepo extends DatabaseConfig{

    private final String SQL_INSERT = "INSERT INTO schedule_transports(source_node_id, target_node_id, daily_start_time, expected_end_time, days) VALUES(?, ?, ?, ?, ?)";
    private final String SQL_SELECT_ALL = "SELECT * FROM schedule_transports";


    public ScheduleTransportsRepo() throws ConfigurationException, org.apache.commons.configuration.ConfigurationException {
        super();
    }

    public List<ScheduleTransportDTO> getAllSchedules(){
        List<ScheduleTransportDTO> result = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                ScheduleTransportDTO data = new ScheduleTransportDTO();

                data.setSourceNodeId(rs.getInt("source_node_id"));
                data.setTargetNodeId(rs.getInt("target_node_id"));
                data.setDailyStartHour(rs.getTimestamp("daily_start_time"));
                data.setDailyEndHour(rs.getTimestamp("expected_end_time"));
                var string = rs.getString("days").split(",");
                data.setAvailableDays(List.of(string));
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

    public boolean addScheduleTransport(ScheduleTransport scheduleTransport, int sourceNodeId, int targetNodeId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(scheduleTransport.getAvailableDays());
            statement.setInt(1, sourceNodeId);
            statement.setInt(2, targetNodeId);
            LocalTime startTime = scheduleTransport.getDailyStartHour();
            LocalTime endTime = scheduleTransport.getDailyEndHour();

// Combine with today's date (adjust timezone if needed)
            LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), startTime);
            LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), endTime);

// Convert to Timestamp
            statement.setTimestamp(3, Timestamp.valueOf(startDateTime));
            statement.setTimestamp(4, Timestamp.valueOf(endDateTime));
            statement.setString(5, json);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int scheduleId = generatedKeys.getInt(1);
                    Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.DB,
                            "Created schedule with ID: " + scheduleId);
                    return true;
                } else {
                    throw new SQLException("Creating vehicle failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            handleException("Error creating vehicle", e.getMessage(), e);
            throw new RuntimeException("Database operation failed", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

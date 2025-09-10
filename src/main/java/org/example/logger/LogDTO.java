package org.example.logger;

import org.eclipse.jetty.util.log.Log;

import java.sql.Timestamp;

public class LogDTO {
    private String timestamp;
    private String type;
    private String queue;
    private String message;

    public LogDTO(String timestamp, String type, String queue, String message) {
        this.timestamp = timestamp;
        this.type = type;
        this.queue = queue;
        this.message = message;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

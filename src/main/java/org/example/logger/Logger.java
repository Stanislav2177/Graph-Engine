package org.example.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Logger {

    private static final String LOG_FILE = "C:\\Users\\Stanislav\\Desktop\\graph engine model making for trans net test\\log\\";
    private static final BlockingQueue<String> logQueueBase = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> logDatabase = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> logQueueGraph = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> logQueueSystem = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> logQueueFlow = new LinkedBlockingQueue<>();
    private static final Logger instance = new Logger();
    private List<LogDTO> latestLogsBase = new ArrayList<>();
    private List<LogDTO> latestLogsGraph = new ArrayList<>();
    private List<LogDTO> latestLogsDatabase = new ArrayList<>();
    private List<LogDTO> latestLogsSystem= new ArrayList<>();
    private List<LogDTO> latestLogsFlow= new ArrayList<>();

    public enum Log {
        WARNING,
        SUCCESS,
        ERROR
    }

    public enum Queue {
        Base,
        Graph,
        DB,
        System,
        Flow
    }

    private Logger() {
        Thread logWriterThread = new Thread(this::processLogQueue);
        logWriterThread.setDaemon(true);
        logWriterThread.start();
    }

    public static Logger getInstance() {
        return instance;
    }

    public void writeLog(Log type, Queue queue, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = "[" + timestamp + "] " + "[" + queue + "]" + " [" + type + "] " + message;
        try {
            switch (queue){
                case Graph:
                    latestLogsGraph.add(new LogDTO(timestamp, queue.toString(), type.toString(), message));
                    logQueueGraph.put(logEntry);
                    break;
                case DB:
                    latestLogsDatabase.add(new LogDTO(timestamp, queue.toString(), type.toString(), message));
                    logDatabase.put(logEntry);
                    break;
                case Base:
                    latestLogsBase.add(new LogDTO(timestamp, queue.toString(), type.toString(), message));
                    logQueueBase.put(logEntry);
                    break;
                case System:
                    latestLogsSystem.add(new LogDTO(timestamp, queue.toString(), type.toString(), message));
                    logQueueSystem.put(logEntry);
                    break;
                case Flow:
                    latestLogsFlow.add(new LogDTO(timestamp, queue.toString(), type.toString(), message));
                    logQueueFlow.put(logEntry);
                    break;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processLogQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String graphLog = logQueueGraph.poll(100, TimeUnit.MILLISECONDS);
                if (graphLog != null) {
                    writeToFile(graphLog, Queue.Graph);
                }

                String dbLog = logDatabase.poll(100, TimeUnit.MILLISECONDS);
                if (dbLog != null) {
                    writeToFile( dbLog, Queue.DB);
                }

                String baseLog = logQueueBase.poll(100, TimeUnit.MILLISECONDS);
                if (baseLog != null) {
                    writeToFile(baseLog, Queue.Base);
                }

                String systemLog = logQueueSystem.poll(100, TimeUnit.MILLISECONDS);
                if (systemLog != null) {
                    writeToFile(systemLog, Queue.System);
                    //Process system log internally, for debugging purpose
                    System.out.println(systemLog);
                }

                String flowLog = logQueueFlow.poll(100, TimeUnit.MILLISECONDS);
                if (flowLog != null) {
                    writeToFile(flowLog, Queue.System);
                }

                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public String getLatestLogs(Queue queue) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        switch (queue){
            case Graph:
                json = mapper.writeValueAsString(latestLogsGraph);
                latestLogsGraph.clear();
                break;
            case DB:
                json = mapper.writeValueAsString(latestLogsDatabase);
                latestLogsDatabase.clear();
                break;
            case Base:
                json = mapper.writeValueAsString(latestLogsBase);
                latestLogsBase.clear();
                break;
            case System:
                json = mapper.writeValueAsString(latestLogsSystem);
                latestLogsSystem.clear();
                break;
            case Flow:
                json = mapper.writeValueAsString(latestLogsFlow);
                latestLogsFlow.clear();
                break;
        }

        return json;
    }

    private void writeToFile(String logEntry, Queue queue) {
        LocalDate currentDate = LocalDate.now();
        String filepath = LOG_FILE +"_" + queue+ "_" + currentDate + ".log";
        Path path = Paths.get(filepath);
        File file = new File(filepath);

        try {
            if (!file.exists()) {
                Files.createFile(path);
                System.out.println("✅ File created: " + filepath);
            }
        } catch (IOException e) {
            System.out.println("❌ Error creating log file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("❌ Error writing to log file: " + e.getMessage());
        }
    }
}

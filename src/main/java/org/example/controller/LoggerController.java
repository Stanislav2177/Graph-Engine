package org.example.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.graph.Node;
import org.example.logger.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

public class LoggerController implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        // Handle CORS preflight request
        if (method.equalsIgnoreCase("OPTIONS")) {
            handleOptionsRequest(exchange);
            return;
        }

        //TODO: IMPLEMENT FILTER CLASS LOGIC WITH HIERARCHY OF THE APIs
        //TODO: WHICH TO CONTAINS AND SECURITY

        switch (method) {
            case "GET":
                handleGetRequest(exchange, path);
                break;
            default:
                sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET");

        String response = null;
        String latestLogs = "";
        if (path.equals("/get-latestLogsBase")) {
            latestLogs = Logger.getInstance().getLatestLogs(Logger.Queue.Base);
            response = latestLogs;
        }
        else if (path.equals("/get-latestLogsGraph")) {
            latestLogs = Logger.getInstance().getLatestLogs(Logger.Queue.Graph);
            response = latestLogs;
        }
        else if (path.equals("/get-latestLogsSystem")) {
            latestLogs = Logger.getInstance().getLatestLogs(Logger.Queue.System);
            response = latestLogs;
        }
        else if (path.equals("/get-latestLogsDB")) {
            latestLogs = Logger.getInstance().getLatestLogs(Logger.Queue.DB);
            response = latestLogs;
        }

        else if (path.equals("/get-latestLogsFlow")) {
            latestLogs = Logger.getInstance().getLatestLogs(Logger.Queue.Flow);
            response = latestLogs;
        }
        else{
            sendResponse(exchange, 404, "Endpoint not found");
        }

        if(response.length() == 2){
            exchange.sendResponseHeaders(202, response.getBytes().length);
        }else{
            exchange.sendResponseHeaders(200, response.getBytes().length);
        }
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void handleOptionsRequest(HttpExchange exchange) throws IOException {
        // Set CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().add("Access-Control-Max-Age", "3600");

        exchange.sendResponseHeaders(204, -1);
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}

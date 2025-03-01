package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.graph.Node;
import org.example.service.GraphEngineService;
import org.example.service.GraphService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

public class GraphController implements HttpHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public GraphService graphEngineService;

    public GraphController() {
    }

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
            case "POST":
                handlePostRequest(exchange, path);
                break;
            case "DELETE":
                handleDeleteRequest(exchange, path);
                break;
            default:
                sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    // Handle OPTIONS request for CORS preflight
    private void handleOptionsRequest(HttpExchange exchange) throws IOException {
        // Set CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().add("Access-Control-Max-Age", "3600");

        // Send empty response for OPTIONS request
        exchange.sendResponseHeaders(204, -1);
    }


    private void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET");

        String method = exchange.getRequestMethod();
        System.out.println("Received a " + method + " request at " + path);
        String response = null;
        if (path.equals("/graph")) {
            response = graphEngineService.createJsonForFullGraph();
        } else if (path.equals("/graph/search")) {
            URI requestURI = exchange.getRequestURI();
            Map<String, String> queryParams = graphEngineService.getQueryParams(requestURI);
            String from = queryParams.get("from");
            String to = queryParams.get("to");
            String shortestPath = graphEngineService.findShortestPath(new Node(from), new Node(to));
            response = shortestPath;
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
        //TODO : MAKE A CUSTOME RESPONSE
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
        //System.out.println("Endpoint created: http://localhost:" + getPort() + path);

    }

    private void handlePostRequest(HttpExchange exchange, String path) throws IOException {
        try {
            // Set CORS headers
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, GET");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            // Read the request body
            InputStream requestBody = exchange.getRequestBody();
            byte[] data = requestBody.readAllBytes();
            String requestBodyString = new String(data);
            System.out.println("Received POST data: " + requestBodyString);

            String serverResponse;
            int statusCode;

            // Handle different paths
            if (path.equals("/graph/add-node")) {
                graphEngineService.addNodeAndTargetsToGraph(requestBodyString);
                serverResponse = "Node added successfully: " + requestBodyString;
                statusCode = 200;
            } else if (path.equals("/graph/add-connections")) {
                graphEngineService.connectSourceWithEdges(requestBodyString);
                serverResponse = "Connections added successfully: " + requestBodyString;
                statusCode = 200;
            } else {
                serverResponse = "Not Found";
                statusCode = 404;
            }

            // Convert the response to bytes
            byte[] responseBytes = objectMapper.writeValueAsBytes(serverResponse);

            // Send response headers and body
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream responseBody = exchange.getResponseBody()) {
                responseBody.write(responseBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Send an error response
            exchange.sendResponseHeaders(500, 0);
            try (OutputStream responseBody = exchange.getResponseBody()) {
                responseBody.write("Internal Server Error".getBytes());
            }
        }
    }


    private void handleDeleteRequest(HttpExchange exchange, String path) throws IOException {
//        if (path.equals("/full-graph")) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "DELETE");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        // Read the request body
        InputStream requestBody = exchange.getRequestBody();
        byte[] data = requestBody.readAllBytes();
        String requestBodyString = new String(data);
        System.out.println("Received DELETE data: " + requestBodyString);
        if (path.equals("/graph/specificNodes")) {
            graphEngineService.deleteNode(requestBodyString);
        } else if (path.equals("/graph/deleteConnections")) {
            graphEngineService.deleteEdges(requestBodyString);
        } else {
            sendResponse(exchange, 404, "Not Found");
        }

        String method = exchange.getRequestMethod();
        System.out.println("Received a " + method + " request at " + path);
        // Set response headers and body
        //TODO : MAKE A CUSTOME RESPONSE
        String response = "THANK YOU";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
        //System.out.println("Endpoint created: http://localhost:" + getPort() + path);
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    public GraphEngineService getGraphEngineService() {
//        return graphEngineService;
        return null;
    }
}
package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.service.GraphEngineService;
import org.example.service.GraphService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GraphController implements HttpHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public  GraphService graphEngineService;

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
                if (path.equals("/graph")) {
                    handleGetRequest(exchange, path);
                }
            case "POST":
                if(path.equals("/graph/add-node")) {
                    handlePostRequest(exchange, path);
                }
            case "DELETE":
                if(path.equals("/graph/specificNodes")){
                    handleDeleteRequest(exchange, path);
                }
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
        if (path.equals("/graph")) {
//        if (path.equals("/full-graph")) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET");

            String method = exchange.getRequestMethod();
            System.out.println("Received a " + method + " request at " + path);
            // Set response headers and body
            //TODO : MAKE A CUSTOME RESPONSE
            String response = graphEngineService.createJsonForFullGraph();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            //System.out.println("Endpoint created: http://localhost:" + getPort() + path);
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
    }

    private void handlePostRequest(HttpExchange exchange, String path) throws IOException {
//        if (path.equals("/v1/full-graph")) {
        if (path.equals("/graph/add-node")) {
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

                // Prepare the server response
                String serverResponse = "Data received: " + requestBodyString;
                byte[] responseBytes = objectMapper.writeValueAsBytes(serverResponse);

                graphEngineService.addNodeAndTargetsToGraph(requestBodyString);
                // Send response headers with the correct Content-Length
                exchange.sendResponseHeaders(200, responseBytes.length);

                // Write the response body
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(responseBytes);
                responseBody.close();
            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0); // Send an error response
                exchange.getResponseBody().close();
            }
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
    }

    private void handleDeleteRequest(HttpExchange exchange, String path) throws IOException {
        if (path.equals("/graph/specificNodes")) {
//        if (path.equals("/full-graph")) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "DELETE");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            // Read the request body
            InputStream requestBody = exchange.getRequestBody();
            byte[] data = requestBody.readAllBytes();
            String requestBodyString = new String(data);
            System.out.println("Received DELETE data: " + requestBodyString);

            graphEngineService.deleteNode(requestBodyString);


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
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
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
package org.example.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class HTTPServerController {

    private final HttpServer httpServer;
    private int port;

    // Simple in-memory user store (replace with database in production)
    private static final Map<String, String> users = new HashMap<>();
    static {
        users.put("admin", "password"); // In real app, store hashed passwords
    }

    public HTTPServerController(int port) throws IOException {
        this.port = port;
        this.httpServer = HttpServer.create(new InetSocketAddress(getPort()), 0);
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    // Add a new method for creating authenticated contexts
    public void createAuthenticatedContext(String endPoint, HttpHandler httpHandler) {
        if (endPoint == null || !endPoint.startsWith("/")) {
            throw new IllegalArgumentException("Invalid path: " + endPoint);
        }

        httpServer.createContext(endPoint, exchange -> {
            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                sendResponse(exchange, 401, "Unauthorized: No token provided");
                return;
            }
            String token = authHeader.substring(7);
            try {
                JwtUtil.validateToken(token);
                httpHandler.handle(exchange);
            } catch (Exception e) {
                sendResponse(exchange, 401, "Unauthorized: Invalid token");
            }
        });
    }

    // Add login endpoint
    public void addLoginEndpoint() {
        createContext("/login", exchange -> {
            // Add CORS headers to ALL responses
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

            String method = exchange.getRequestMethod();

            if (method.equalsIgnoreCase("OPTIONS")) {
                handleOptionsRequest(exchange);
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String username = "admin";
                String password = "password";

                if (users.containsKey(username) && users.get(username).equals(password)) {
                    String token = JwtUtil.generateToken(username);
                    sendResponse(exchange, 200, token);
                } else {
                    sendResponse(exchange, 401, "Invalid credentials");
                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        });
    }

    private void handleOptionsRequest(HttpExchange exchange) throws IOException {
        // These headers are already set in the main handler
        exchange.sendResponseHeaders(204, -1);
    }

    // Helper method to send responses
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    // Your existing methods remain the same...
    public void createContext(String endPoint, HttpHandler httpHandler) {
        if (endPoint == null || !endPoint.startsWith("/")) {
            throw new IllegalArgumentException("Invalid path: " + endPoint);
        }
        httpServer.createContext(endPoint, httpHandler);
        System.out.println("Endpoint created: http://localhost:" + getPort() + endPoint);
    }


    public void startHttpServer() {
        addLoginEndpoint();
        httpServer.start();
        System.out.println("Server started at http://localhost:" + getPort());
    }

    // ... rest of your existing methods
}
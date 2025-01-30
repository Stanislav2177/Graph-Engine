package org.example.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HTTPServerController {

    private final HttpServer httpServer;
    private int port;
    public HTTPServerController(int port) throws IOException {
        this.port = port;
        this.httpServer = HttpServer.create(new InetSocketAddress(getPort()), 0);
    }

    public void createContext(String endPoint, HttpHandler httpHandler){
        if (endPoint == null || !endPoint.startsWith("/")) {
            throw new IllegalArgumentException("Invalid path: " + endPoint);
        }
        httpServer.createContext(endPoint, httpHandler);
        System.out.println("Endpoint created: http://localhost:" + getPort() + endPoint);
    }

    public void stopHttpServerHandler(String endPoint, String response) {
        httpServer.createContext(endPoint, new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String method = exchange.getRequestMethod();
                System.out.println("Received a " + method + " request at " + endPoint);
                stopHttpServer();
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        });
        System.out.println("Endpoint created: http://localhost:" +getPort() + endPoint);
    }

    public void startHttpServer() {
        httpServer.start();
        System.out.println("Server started at http://localhost:8082");
    }

    public void stopHttpServer() {
        httpServer.stop(5); // Stop server gracefully with a 5-second delay
        System.out.println("Server stopped.");
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

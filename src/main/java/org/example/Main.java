package org.example;

import org.example.controller.GraphController;
import org.example.controller.HTTPServerController;

public class Main {
    public static void main(String[] args) {
        try {
            HTTPServerController httpServer = new HTTPServerController(8082);
            httpServer.createContext("/graph", new GraphController());
            httpServer.stopHttpServerHandler("/stop", "Server is Stopped");
            httpServer.startHttpServer();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}


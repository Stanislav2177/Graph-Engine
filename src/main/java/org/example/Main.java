package org.example;

import org.example.controller.GraphController;
import org.example.controller.HTTPServerController;
import org.example.graph.GraphAdjList;
import org.example.graph.Node;
import org.example.service.GraphService;

public class Main {
    public static void main(String[] args) {
        try {
            GraphController graphController = new GraphController();
            graphController.graphEngineService = new GraphService();
            graphController.graphEngineService.findShortestPath(new Node("Svilengrad"), new Node("Plovdiv"));


            HTTPServerController httpServer = new HTTPServerController(8082);
            httpServer.createContext("/graph", graphController);
            httpServer.createContext("/graph/search", graphController);
            httpServer.createContext("/graph/add-node", graphController);
            httpServer.createContext("/graph/specificNodes", graphController);
            httpServer.createContext("/graph/add-connections", graphController);
            httpServer.createContext("/graph/deleteConnections", graphController);
            httpServer.createContext("/graph/saveMap", graphController);

            httpServer.stopHttpServerHandler("/stop", "Server is Stopped");
            httpServer.startHttpServer();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}


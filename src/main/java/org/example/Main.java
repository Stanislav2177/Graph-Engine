package org.example;

import org.example.controller.GraphController;
import org.example.controller.HTTPServerController;
import org.example.controller.LoggerController;
import org.example.graph.Node;
import org.example.logger.Logger;
import org.example.service.GraphService;
import org.example.workflow.WorkflowMechanism;

public class Main {
    public static void main(String[] args) {
        try {
            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System,"Graph Server is started");
            GraphController graphController = new GraphController();
            LoggerController loggerController = new LoggerController();
            HTTPServerController httpServer = new HTTPServerController(8082);
            graphController.graphEngineService = new GraphService();
//            httpServer.createAuthenticatedContext("/graph", graphController);
//            httpServer.createAuthenticatedContext("/graph/search", graphController);
//            httpServer.createAuthenticatedContext("/graph/add-node", graphController);
//            httpServer.createAuthenticatedContext("/graph/specificNodes", graphController);
//            httpServer.createAuthenticatedContext("/graph/add-connections", graphController);
//            httpServer.createAuthenticatedContext("/graph/deleteConnections", graphController);
//            httpServer.createAuthenticatedContext("/graph/new-trip", graphController);
//            httpServer.createAuthenticatedContext("/graph/all-trips", graphController);
//            httpServer.createAuthenticatedContext("/graph/add-vehicle", graphController);
//            httpServer.createAuthenticatedContext("/graph/all-vehicles", graphController);
//            httpServer.createAuthenticatedContext("/graph/add-schedule", graphController);
//
//            httpServer.createAuthenticatedContext("/get-latestLogsBase", loggerController);
//            httpServer.createAuthenticatedContext("/get-latestLogsGraph", loggerController);
//            httpServer.createAuthenticatedContext("/get-latestLogsSystem", loggerController);
//            httpServer.createAuthenticatedContext("/get-latestLogsDB", loggerController);
//            httpServer.createAuthenticatedContext("/get-latestLogsFlow", loggerController);


            httpServer.createContext("/graph", graphController);
            httpServer.createContext("/graph/search", graphController);
            httpServer.createContext("/graph/add-node", graphController);
            httpServer.createContext("/graph/specificNodes", graphController);
            httpServer.createContext("/graph/add-connections", graphController);
            httpServer.createContext("/graph/deleteConnections", graphController);
            httpServer.createContext("/graph/new-trip", graphController);
            httpServer.createContext("/graph/all-trips", graphController);
            httpServer.createContext("/graph/add-vehicle", graphController);
            httpServer.createContext("/graph/all-vehicles", graphController);
            httpServer.createContext("/graph/add-schedule", graphController);

            httpServer.createContext("/get-latestLogsBase", loggerController);
            httpServer.createContext("/get-latestLogsGraph", loggerController);
            httpServer.createContext("/get-latestLogsSystem", loggerController);
            httpServer.createContext("/get-latestLogsDB", loggerController);
            httpServer.createContext("/get-latestLogsFlow", loggerController);

            //httpServer.stopHttpServerHandler("/stop", "Server is Stopped");
            httpServer.startHttpServer();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}


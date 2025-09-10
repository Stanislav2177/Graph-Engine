package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.configuration.ConfigurationException;
import org.example.graph.*;
import org.example.logger.Logger;
import org.example.models.*;
import org.example.repository.*;
import org.example.transport.DetailedTransportData;
import org.example.workflow.StatesTransferModel;
import org.example.workflow.WorkflowMechanism;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;



//GraphService is communicating directly with DB through NodesRepo and EdgesRepo
public class GraphService implements GraphEngineService {
    private record NodesConnections(String id, String source, String target, int weight) {
    }

    private GraphAdjList graph = new GraphAdjList();
    private final JsonConstructor jsonConstructor = new JsonConstructor();
    private ObjectMapper mapper;
    private NodesRepo nodesRepo;
    private EdgesRepo edgesRepo;
   private TransportDataRepo transportDataRepo ;
    private TransportVehiclesRepo transportVehiclesRepo;
    private ScheduleTransportsRepo scheduleTransportsRepo;
    public GraphService() throws ConfigurationException {
        mapper = new ObjectMapper();
        nodesRepo = new NodesRepo();
        edgesRepo = new EdgesRepo();
        transportDataRepo = new TransportDataRepo();
        transportVehiclesRepo = new TransportVehiclesRepo();
        scheduleTransportsRepo = new ScheduleTransportsRepo();

        //Loading the whole database into memory for faster Interactions
        loadDataFromDB();
    }

    public GraphAdjList getGraph() {
        return graph;
    }

    public void setGraph(GraphAdjList graph) {
        this.graph = graph;
    }

    public JsonConstructor getJsonConstructor() {
        return jsonConstructor;
    }

    @Override
    public void addNodeAndTargetsToGraph(String json) {
        try {
            GraphData graphData = mapper.readValue(json, GraphData.class);
            System.out.println("Source: " + graphData.getSource());
            Node sourceNode = new Node(graphData.getSource());
            getGraph().addNode(sourceNode);

            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System, "Adding nodes to the graph; source: " + graphData.getSource());

            if (graphData.getTarget().size() > 0) {
                for (Edge edge : graphData.getTarget()) {
                    Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System,"Destination: " + edge.getNode().getLabel() + ", Weight: " + edge.getWeight());
                    getGraph().addEdge(sourceNode, new Node(edge.getNode().getLabel()), edge.getWeight());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addVehicleDB(String json) throws JsonProcessingException {
        try{
            TransportVehicle transportVehicle = mapper.readValue(json, TransportVehicle.class);

            Node nodeFromGraphSource = getGraph().getNodeFromGraph(transportVehicle.getSourceName());

            Node nodeFromGraphTarget = getGraph().getNodeFromGraph(transportVehicle.getTargetName());

            transportVehicle.setSourceId(nodeFromGraphSource.getId());
            transportVehicle.setTargetId(nodeFromGraphTarget.getId());
            boolean i = transportVehiclesRepo.addVehicle(transportVehicle, transportVehicle.getTransportTypeId());
            if(i){
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Added Successfully");
                return mapper.writeValueAsString(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Problem with adding vehicle");
            return mapper.writeValueAsString(response);
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System,"Method addVehicleDB failed: " + ex.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Вече съществува превозно средство с този номер");
            return mapper.writeValueAsString(response);        }
    }

    public String addNewSchedule(String json){
        try {
            mapper.registerModule(new JavaTimeModule()); // Critical for LocalTime
            ScheduleTransport scheduleTransport = mapper.readValue(json, ScheduleTransport.class);
            Node nodeFromGraphSource = graph.getNodeFromGraph(scheduleTransport.getSourceNode());
            Node nodeFromGraphTarget = graph.getNodeFromGraph(scheduleTransport.getTargetNode());

            boolean b = scheduleTransportsRepo.addScheduleTransport(scheduleTransport, nodeFromGraphSource.getId(), nodeFromGraphTarget.getId());

            if(b){
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Added Successfully");
                return mapper.writeValueAsString(response);
            }else{
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Failed");
            }

            return mapper.writeValueAsString(new ApiResponse(false, "Problem with adding schedule"));
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System,"Method startNewTrip failed: " + ex.getMessage());
            return null;
        }
    }

    public String getAllSchedules(){
        try {
            List<ScheduleTransportDTO> allSchedules = scheduleTransportsRepo.getAllSchedules();

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String jsonResponse = mapper.writeValueAsString(allSchedules);
            return jsonResponse;
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System,"Method startNewTrip failed: " + ex.getMessage());
            return null;
        }
    }

    public List<ScheduleTransportDTO> getAllSchedulesList(){
        try {
            List<ScheduleTransportDTO> allSchedules = scheduleTransportsRepo.getAllSchedules();

            return allSchedules;
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System,"Method startNewTrip failed: " + ex.getMessage());
            return null;
        }
    }

    public String  startNewTrip(String json) throws JsonProcessingException {
        try {
            TripDataDTO tripDataDTO = mapper.readValue(json, TripDataDTO.class);

            Node sourceNode = graph.getNodeFromGraph(tripDataDTO.getSource());
            Node targetNode = graph.getNodeFromGraph(tripDataDTO.getTarget());
            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System, "Making new trip: Source node: " + sourceNode.getLabel());
            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System, "Making new trip: Target node: " + targetNode.getLabel());

            WorkflowMechanism workflowMechanism = new WorkflowMechanism();
            workflowMechanism.startFlow(sourceNode,targetNode, tripDataDTO);

            StatesTransferModel transferModel = workflowMechanism.getTransferModel();
            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(transferModel);

            return jsonResponse;
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System,"Method startNewTrip failed: " + ex.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Problem append: " + ex.getMessage() );
            String json1 = mapper.writeValueAsString(response);

            return json1;
        }
    }


    public String addNodeInMemory(String json){
        try{
            GraphData graphData = mapper.readValue(json, GraphData.class);
            Node sourceNode = new Node(graphData.getSource(), Double.parseDouble(graphData.getLatitude()),Double.parseDouble(graphData.getLongitude()));
            getGraph().addNode(sourceNode);
            nodesRepo.addNodeToDatabase(sourceNode);

            return "Node Added successfully";
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System,"Method addConnectionToDB failed: " + ex.getMessage());
            return "Problem in adding";
        }
    }

    public String getAllVehiclesDetailed() throws ConfigurationException, JsonProcessingException {
        List<TransportVehicleDetailedDTO> detailedTransportDataList = transportVehiclesRepo.getAllVehiclesDetailed();

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(detailedTransportDataList);
        return jsonResponse;
    }
    public String getAllTransportData() throws ConfigurationException, JsonProcessingException {
        List<DetailedTransportData> detailedTransportDataList = transportDataRepo.getDetailedTransportDataList();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String jsonResponse = mapper.writeValueAsString(detailedTransportDataList);
        return jsonResponse;
    }

    public void addConnectionToDB(Node source, Node target, double weight) {
        try {
            int sourceNodeIdByLabel = nodesRepo.getNodeIdByLabel(source.getLabel());
            int targetNodeIdByLabel = nodesRepo.getNodeIdByLabel(target.getLabel());
            edgesRepo.addConnectionBetweenTwoNodes(sourceNodeIdByLabel, targetNodeIdByLabel, weight);
        } catch (Exception ex) {
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System,"Method addConnectionToDB failed: " + ex.getMessage());
        }
    }

    //todo: For bigger database, to separate the cities on regions, for  better performance
    //DB -> IN_MEMORY
    public void loadDataFromDB() {
        List<Node> allNodes = nodesRepo.getAllNodes();
        if (allNodes.size() == 0) {
            return;
        }

        //Traverse all nodes
        for (Node node : allNodes) {
            //Add the node to the graph
            getGraph().addNode(node);

            List<ConnectionDB> allEdgesIDsBasedOnSourceNodeID = edgesRepo.getAllEdgesIDsBasedOnSourceNodeID(node.getId());

            //Traverse all connections
            for (ConnectionDB connectionDB : allEdgesIDsBasedOnSourceNodeID) {

                //Search the node which is expected to be target
                allNodes.forEach(target -> {
                    //When a node is found, its added to the graph
                    if (target.getId() == connectionDB.getTargetNode_id()) {
                        getGraph().addEdge(node, target, connectionDB.getWeight());
                    }
                });
            }
        }

        Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System,"Data was loaded from DB: all nodes" + allNodes.size());
        getGraph().printGraph();
    }


    // Save the graph configuration to the DB tables
    public void saveDataToDB() {
        Map<Node, List<Edge>> adjList = getGraph().getAdjList();

        // Iterate through all nodes in the adjacency list
        for (Node node : adjList.keySet()) {
            // Update the node in the DB
            nodesRepo.updateNode(node);

            // Get all edges for this node
            List<Edge> edges = adjList.get(node);

            for (Edge edge : edges) {
                // Update or create each edge
                try {
                    // First try to update existing edge
                    boolean updateSuccess = edgesRepo.updateEdgeWeight(
                            node.getId(),
                            edge.getNode().getId(),
                            edge.getWeight()
                    );

                    // If edge doesn't exist, create it
                    if (!updateSuccess) {
                        edgesRepo.addConnectionBetweenTwoNodes(
                                node.getId(),
                                edge.getNode().getId(),
                                edge.getWeight()
                        );
                    }
                } catch (Exception e) {
                    Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System,
                            "Failed to save edge between " + node.getId() +
                                    " and " + edge.getNode().getId() +
                                    ": " + e.getMessage());
                }
            }
        }

        Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System,"Graph data successfully saved to database");
    }

    public String deleteNode(String json) throws JsonProcessingException {
        try {
            if (json.startsWith("\"") && json.endsWith("\"")) {
                json = mapper.readValue(json, String.class);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Deleted Successfully");

            GraphData graphData = mapper.readValue(json, GraphData.class);
            String source = graphData.getSource();
            Node nodeFromGraph = graph.getNodeFromGraph(source);
            List<Edge> edges = graph.getAdjList().get(nodeFromGraph);
            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System,"Deleting Node: " + source);
            graph.deleteSource(source);

            for (var e  : edges) {
                graph.deleteEdges(e.getNode().toString());
                Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System,"Deleting Edge Node: " + e.getNode().getLabel());
            }
            String json1 = mapper.writeValueAsString(response);
            System.out.println(json);

            nodesRepo.deleteNodeById(nodeFromGraph.getId());
            return json1;
        } catch (Exception e) {
            return mapper.writeValueAsString(new ApiResponse(true, "Problem with deleting: " + e.getMessage()));
        }
    }

    public String createJsonForFullGraph() {
        try {
            return getJsonConstructor().constructJson(getGraph().getAdjList());
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void deleteEdges(String json) {
        try {
            List<NodesConnections> data = mapper.readValue(json, new TypeReference<List<NodesConnections>>() {
            });

            for (NodesConnections k : data) {
                graph.deleteEdge(new Node(k.source), new Node(k.target));
            }
            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System,"Total " + data.size() + " connections will be deleted");
        } catch (Exception e) {
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System,"Problem in deleting edges with message " + e.getMessage());
        }
    }

    public void connectSourceWithEdges(String json) {
        try {
            EdgeData edge = mapper.readValue(json, new TypeReference<EdgeData>() {
            });
            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System,"Source: " + edge.getSource()+
                    "; Targets: " + edge.getTarget() +
                    "; Weight: " + edge.getWeight());

            graph.addEdge(new Node(edge.getSource()), new Node(edge.getTarget()), Double.parseDouble(edge.getWeight()));
            Node source = graph.getNodeFromGraph(edge.getSource());
            Node target = graph.getNodeFromGraph(edge.getTarget());

            if(source == null || target == null){
                Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System,"No node was found in the graph: Source: " + edge.getSource() + " Target: " + edge.getTarget());
            }else{
                double v = Double.parseDouble(edge.getWeight());
                edgesRepo.addConnectionBetweenTwoNodes(source.getId(), target.getId(), v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getQueryParams(URI uri) {
        Map<String, String> queryParams = new HashMap<>();
        String query = uri.getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8) : pair;
                String value = idx > 0 && pair.length() > idx + 1
                        ? URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8)
                        : null;
                queryParams.put(key, value);
            }
        }
        return queryParams;
    }

    public String findShortestPathJSON(Node start, Node target) throws JsonProcessingException {
        try {

            ShortestPathDto shortestPathDijkstra = graph.findShortestPathDijkstra(start, target);
            String json = mapper.writeValueAsString(shortestPathDijkstra);

            System.out.println("JSON : " + json);
            return json;
        } catch (Exception ex) {
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System , "Problem findShortestPath with message " + ex.getMessage());
            return null;
        }
    }

    public ShortestPathDto findShortestPath(Node start, Node target) throws JsonProcessingException {
        try {

            ShortestPathDto shortestPathDijkstra = graph.findShortestPathDijkstra(start, target);
            return shortestPathDijkstra;
        } catch (Exception ex) {
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System, "Problem findShortestPath with message " + ex.getMessage());
            return null;
        }
    }

    public double getDistanceBetweenTwoNodes(String start, String target) {
        try {
            return graph.getWeightBetween(new Node(start), new Node(target));
        } catch (Exception ex) {
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System, "Problem ingetDistanceBetweenTwoNodes with message " + ex.getMessage());
            return 0;
        }
    }

}

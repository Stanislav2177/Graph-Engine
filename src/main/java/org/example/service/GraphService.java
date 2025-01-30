package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.GraphController;
import org.example.graph.*;

import java.util.List;
import java.util.Map;

public class GraphService implements GraphEngineService{
    private GraphAdjList graph = new GraphAdjList();
    private final JsonConstructor jsonConstructor = new JsonConstructor();

    public GraphService() {
        initGraph();
    }

    @Override
    public void addNodeAndTargetsToGraph(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            GraphData graphData = mapper.readValue(json, GraphData.class);
            System.out.println("Source: " + graphData.getSource().getLabel());
            for (Edge edge : graphData.getDestinations()) {
                System.out.println("Destination: " + edge.getNode().getLabel() + ", Weight: " + edge.getWeight());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<Node, List<Edge>> getAdjList(){
        return getGraph().getAdjList();
    }

    public String createJsonForFullGraph(){
        try{
            return getJsonConstructor().constructJson(getAdjList());
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void deleteNode(String source) {

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

    private void initGraph(){
        Node nodeA = new Node("Sofia");
        Node nodeB = new Node("Varna");
        Node nodeC = new Node("Plovdiv");
        Node nodeD = new Node("Smolyan");
        Node nodeE = new Node("Burgas");
        Node nodeF = new Node("Svilengrad");
        Node nodeG = new Node("Pernik");

        graph.addEdge(nodeA, nodeG, 30.0);
        graph.addEdge(nodeA, nodeB, 400.0);
        graph.addEdge(nodeA, nodeC, 119.5);

        graph.addEdge(nodeB, nodeA, 130);
        graph.addEdge(nodeB, nodeC, 300);
        graph.addEdge(nodeB, nodeE, 110);
        graph.addEdge(nodeB, nodeD, 530);

        graph.addEdge(nodeC, nodeD, 120);
        graph.addEdge(nodeE, nodeB, 140);
        graph.addEdge(nodeF, nodeB, 240);
        graph.addEdge(nodeG, nodeB, 240);

        System.out.println("Adjacency List:");
        graph.printGraph();
    }
}

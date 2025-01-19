package org.example;


import org.example.graph.Edge;
import org.example.graph.GraphAdjList;
import org.example.graph.JsonConstructor;
import org.example.graph.Node;

import java.util.List;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        GraphAdjList graph = new GraphAdjList();

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


        Map<Node, List<Edge>> adjList = graph.getAdjList();
        JsonConstructor jsonConstructor = new JsonConstructor();
        try{

            jsonConstructor.constructJson(adjList);
        }catch (Exception e){
            System.out.println(e);
        }
      //  graph.findShortestPathDijkstra(nodeB);
    }
}


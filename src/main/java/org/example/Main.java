package org.example;


import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class Main {
    public static void main(String[] args) {
        // define edges of the graph
        List<Edge> edges = Arrays.asList(
                new Edge(0, 1, 2),
                new Edge(0, 2, 4),
                new Edge(1, 2, 4),
                new Edge(2, 0, 5),
                new Edge(2, 1, 4),
                new Edge(3, 2, 3),
                new Edge(4, 5, 1),
                new Edge(5, 4, 3));

        // call graph class Constructor to construct a graph
        Graph graph = new Graph(edges);

        // print the graph as an adjacency list
        Graph.printGraph(graph);

        Graph g = new Graph(5);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 3);
        g.addEdge(1, 2);
        g.addEdge(2, 4);
        //print the DFS Traversal sequence
        System.out.println("Depth First Traversal for given graph"+
                "(with 0 as starting vertex)");
        g.DFS(0);
        System.out.println();

        g.BFS(0);
    }
}


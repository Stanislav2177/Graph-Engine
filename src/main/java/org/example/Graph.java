package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// Graph class
public class Graph {

    List<List<Node>> adjList = new ArrayList<>();
    private int Vertices;   // No. of vertices
    private LinkedList<Integer> adjListVisited[];

    // graph Constructor: to initialize adjacency lists as per no of vertices
    public Graph(int v) {
        Vertices = v;
        adjListVisited = new LinkedList[v];
        for (int i = 0; i < v; ++i)
            adjListVisited[i] = new LinkedList();
    }

    //add an edge to the graph
    public void addEdge(int v, int w) {
        adjListVisited[v].add(w);  // Add w to v's list.
    }

    // helper function for DFS technique
    private void DFS_helper(int v, boolean[] visited) {
        // current node is visited
        visited[v] = true;
        System.out.print(v + " ");

        // process all adjacent vertices
        for (int n : adjListVisited[v]) {
            if (!visited[n])
                DFS_helper(n, visited);
        }
    }

    public void DFS(int v) {
        //initially none of the vertices are visited
        boolean[] visited = new boolean[Vertices];

        // call recursive DFS_helper function for DFS technique
        DFS_helper(v, visited);
    }

    // BFS traversal from the root_node
    public void BFS(int root_node) {
        // initially all vertices are not visited
        boolean[] visited = new boolean[Vertices];

        // BFS queue
        LinkedList<Integer> queue = new LinkedList<>();

        // current node = visited, insert into queue
        visited[root_node] = true;
        queue.add(root_node);

        while (queue.size() != 0) {
            // deque an entry from queue and process it
            root_node = queue.poll();
            System.out.print(root_node + " ");

            // get all adjacent nodes of current node and process
            for (int n : adjListVisited[root_node]) {
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
    }

    //Graph Constructor
    public Graph(List<Edge> edges) {
        // adjacency list memory allocation
        for (int i = 0; i < edges.size(); i++)
            adjList.add(i, new ArrayList<>());

        // add edges to the graph
        for (Edge e : edges) {
            // allocate new node in adjacency List from src to dest
            adjList.get(e.src).add(new Node(e.dest, e.weight));
        }
    }

    // print adjacency list for the graph
    public static void printGraph(Graph graph) {
        int src_vertex = 0;
        int list_size = graph.adjList.size();

        System.out.println("The contents of the graph:");
        while (src_vertex < list_size) {
            //traverse through the adjacency list and print the edges
            for (Node edge : graph.adjList.get(src_vertex)) {
                System.out.print("Vertex:" + src_vertex + " ==> " + edge.value +
                        " (" + edge.weight + ")\t");
            }

            System.out.println();
            src_vertex++;
        }
    }
}

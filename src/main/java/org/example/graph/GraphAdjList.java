package org.example.graph;

import java.util.*;

public class GraphAdjList {
    private Map<Node, List<Edge>> adjList;

    public GraphAdjList() {
        adjList = new HashMap<>();
    }

    public void addNode(Node node) {
        adjList.putIfAbsent(node, new ArrayList<>());
    }

    public Map<Node, List<Edge>> getAdjList() {
        return adjList;
    }

    public void addEdge(Node src, Node dest, double weight) {
        addNode(src);
        addNode(dest);

        adjList.get(src).add(new Edge(dest, weight));
    }

    public void printGraph() {
        for (Map.Entry<Node, List<Edge>> entry : adjList.entrySet()) {
            Node node = entry.getKey();
            List<Edge> edges = entry.getValue();
            System.out.print(node.getLabel() + " -> ");
            for (Edge edge : edges) {
                System.out.print(edge.node.getLabel() + "(" + edge.weight + ") ");
            }
            System.out.println();
        }
    }

    public void findShortestPathDijkstra(Node start) {
        Map<Node, Double> dist = new HashMap<>();
        Map<Node, Node> prev = new HashMap<>(); // Tracks predecessors

        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));
        Set<Node> visited = new HashSet<>();

        // Initialize distances
        for (Node node : adjList.keySet()) {
            dist.put(node, Double.POSITIVE_INFINITY);
        }
        dist.put(start, 0.0);

        // Start from the source node
        pq.add(new Edge(start, 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            Node currentNode = current.node;

            if (visited.contains(currentNode)) continue;
            visited.add(currentNode);

            for (Edge edge : adjList.get(currentNode)) {
                if (!visited.contains(edge.node)) {
                    double newDist = dist.get(currentNode) + edge.weight;
                    if (newDist < dist.get(edge.node)) {
                        dist.put(edge.node, newDist);
                        prev.put(edge.node, currentNode); // Update predecessor
                        pq.add(new Edge(edge.node, newDist));
                    }
                }
            }
        }

        System.out.println("Shortest distances from node " + start.getLabel() + ":");
        for (Map.Entry<Node, Double> entry : dist.entrySet()) {
            System.out.println("To " + entry.getKey().getLabel() + " -> " + entry.getValue());
            System.out.println("Path: " + reconstructPath(entry.getKey(), prev));
        }
    }

    private List<Node> reconstructPath(Node target, Map<Node, Node> prev) {
        List<Node> path = new LinkedList<>();
        for (Node at = target; at != null; at = prev.get(at)) {
            path.add(0, at);
            System.out.println("path: " + at.getLabel());
        }
        return path;
    }


}

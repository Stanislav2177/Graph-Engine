package org.example.graph;

import org.example.models.ShortestPathDto;

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

    public void deleteEdge(Node src, Node dest){
        adjList.get(src).removeIf((d) -> Objects.equals (d.getNode().getLabel(), dest.getLabel()));
    }

    public boolean deleteEdges(String target) {
        if (target == null || target.isEmpty()) {
            return false;
        }

        Set<Node> nodes = adjList.keySet();
        for (Node node : nodes) {
            adjList.get(node).removeIf((n) -> n.getNode().getLabel().equals(target));
        }
        return false;
    }

    public boolean deleteSource(String label) {
        if (label == null || label.isEmpty()) {
            return false;
        }

        Node node = new Node(label);

        if (adjList.containsKey(node)) {
            // Remove the node and all its edges
            adjList.remove(node);
            return true;
        }

        return false;
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

    // Dijkstra's algorithm to find the shortest path from start to target
    public ShortestPathDto findShortestPathDijkstra(Node start, Node target) {
        Map<Node, Double> dist = new HashMap<>();
        Map<Node, Node> prev = new HashMap<>();
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

            // Stop if target is reached
            if (currentNode.equals(target)) break;

            for (Edge edge : adjList.getOrDefault(currentNode, new ArrayList<>())) {
                if (!visited.contains(edge.node)) {
                    double newDist = dist.get(currentNode) + edge.weight;
                    if (newDist < dist.get(edge.node)) {
                        dist.put(edge.node, newDist);
                        prev.put(edge.node, currentNode);
                        pq.add(new Edge(edge.node, newDist));
                    }
                }
            }
        }

        // If no path found
        if (dist.get(target) == Double.POSITIVE_INFINITY) {
            System.out.println("No path found from " + start.getLabel() + " to " + target.getLabel());
            return new ShortestPathDto(start.getLabel(), target.getLabel(), List.of(), new HashMap<>());
        }

        // Reconstruct path
        List<Node> path = reconstructPath(target, prev);
        List<String> pathLabels = path.stream().map(Node::getLabel).toList();

        // Calculate weights between vertices in the path
        Map<String, Double> weightsBetweenVertices = new HashMap<>();
        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next = path.get(i + 1);
            double weight = getWeightBetween(current, next);
            weightsBetweenVertices.put(current.getLabel() + " -> " + next.getLabel(), weight);
        }

        System.out.println("Shortest path from " + start.getLabel() + " to " + target.getLabel() + ": " + pathLabels);
        System.out.println("Weights between vertices: " + weightsBetweenVertices);

        return new ShortestPathDto(start.getLabel(), target.getLabel(), pathLabels, weightsBetweenVertices);
    }

    private double getWeightBetween(Node from, Node to) {
        for (Edge edge : adjList.getOrDefault(from, new ArrayList<>())) {
            if (edge.node.equals(to)) {
                return edge.weight;
            }
        }
        return Double.POSITIVE_INFINITY; // If no edge exists
    }


    private List<Node> reconstructPath(Node target, Map<Node, Node> prev) {
        List<Node> path = new LinkedList<>();
        for (Node at = target; at != null; at = prev.get(at)) {
            path.add(0, at);
        }
        return path;
    }




}

package org.example.graph;

import org.example.logger.Logger;
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

    public boolean addEdge(Node src, Node dest, double weight) {
        try {
            addNode(src);
            addNode(dest);

            adjList.get(src).add(new Edge(dest, weight));
            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System, "Connection is added between: " + src.getLabel() + " and " + dest.getLabel() + " with weight: " + weight);
            return true;
        } catch (Exception ex) {
            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System, "Problem with addEdge method: " + ex.getMessage());
            return false;
        }
    }


    public void deleteEdge(Node src, Node dest) {
        try{
            adjList.get(src).removeIf((d) -> Objects.equals(d.getNode().getLabel(), dest.getLabel()));
            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System, "Delete Edge successfully finished: SRC " + src.getLabel() + " DEST: " + dest.getLabel());
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.SUCCESS,Logger.Queue.System, "Problem with addEdge method: " + ex.getMessage());
        }
    }

    public boolean deleteEdges(String target) {
        try {
            if (target == null || target.isEmpty()) {
                return false;
            }

            Set<Node> nodes = adjList.keySet();
            for (Node node : nodes) {
                adjList.get(node).removeIf((n) -> n.getNode().getLabel().equals(target));
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System,"Delete Edges successfully finished: TARGET " + target);
            return true;
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System, "Problem with deleteEdges method: " + ex.getMessage());
            return false;
        }
    }

    public Node getNodeFromGraph(String nodeName){
        for (Node node : getAdjList().keySet()) {
            if(node.getLabel().equals(nodeName)){
                return node;
            }
        }

        return null;
    }

    public boolean deleteSource(String target) {
        try {
            if (target == null || target.isEmpty()) {
                return false;
            }

            Node node = new Node(target);
            if (adjList.containsKey(node)) {
                // Remove the node and all its edges
                adjList.remove(node);
            }

            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System, "Delete Source successfully finished: TARGET " + target);
            return true;
        }
        catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.System, "Problem with deleteSource method: " + ex.getMessage());
            return false;
        }
    }


    //Replace node
    public void changeNodeData(Node node) {
        for (Node n : getAdjList().keySet()) {
            if (n.getLabel().equals(node.getLabel())) {
                List<Edge> edges = getAdjList().get(n);
                getAdjList().remove(n);
                getAdjList().put(node, edges);
                break;
            }
        }
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

        for (Node node : adjList.keySet()) {
            dist.put(node, Double.POSITIVE_INFINITY);
        }
        dist.put(start, 0.0);

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
            return new ShortestPathDto(start.getLabel(), target.getLabel(), List.of(), List.of() , new HashMap<>());
        }

        // Reconstruct path
        List<Node> path = reconstructPath(target, prev);
        List<String> pathLabels = path.stream().map(Node::getLabel).toList();
        List<Integer> pathIds = path.stream().map(Node::getId).toList();

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

        return new ShortestPathDto(start.getLabel(), target.getLabel(), pathLabels, pathIds, weightsBetweenVertices);
    }

    public double getWeightBetween(Node from, Node to) {
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

package org.example.graph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.GraphDTO;
import org.example.models.NodeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonConstructor {

    public String constructJson(Map<Node, List<Edge>> adjList) throws JsonProcessingException {
        List<NodeDTO> nodes = new ArrayList<>();
        List<GraphDTO> edges = new ArrayList<>();
        int edgeCounter = 1;

        // Add all nodes to the node list
        Set<Node> nodeSet = adjList.keySet();
        for (Node node : nodeSet) {
            nodes.add(new NodeDTO(node.getLabel(), node.getLabel(), node.getLat(), node.getLng()));
        }

        // Add all edges
        for (Node node : nodeSet) {
            System.out.println("Start: " + node.getLabel());
            List<Edge> edgeList = adjList.get(node);
            for (Edge edge : edgeList) {
                System.out.println("Target: " + edge.getNode().getLabel());
                edges.add(new GraphDTO(
                        String.valueOf(edgeCounter),
                        node.getLabel(),
                        edge.node.getLabel(),
                        edge.weight
                ));
                edgeCounter++;
            }
        }

        // Combine nodes and edges into a single JSON structure
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Map.of("elements",
                Map.of("nodes", nodes, "edges", edges)));

        System.out.println(json);
        return json;
    }
}

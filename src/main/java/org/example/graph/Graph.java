//package org.example.graph;
//
//import java.util.HashMap;
//
//public class Graph {
//    private double[][] matrix;
//    private HashMap<Integer, Node> hashMap;
//
//    private int availableSrc;
//    private int availableDest;
//
//    public Graph(int totalNodes){
//        this.matrix = new double[totalNodes][totalNodes];
//        fillMatrix(totalNodes);
//        hashMap = new HashMap<>();
//        availableSrc = 0;
//        availableDest = 0;
//    }
//
//    private void fillMatrix(int totalNodes){
//        for(int x = 0; x < totalNodes; x++){
//            for(int y = 0; y < totalNodes; y++){
//                this.matrix[x][y] = 0;
//            }
//        }
//    }
//
//    public void addEdge(Node src, Node dest,  double weight){
//        if(src.getmatrixID() == -1){
//            src.setID(availableSrc);
//            availableSrc++;
//        }
//        if(dest.getmatrixID() == -1){
//            dest.setID(availableDest);
//            availableDest++;
//        }
//
//        if(this.matrix.length == availableDest || this.matrix.length == availableSrc){
//            allocateMoreMemory();
//        }
//        this.matrix[src.getmatrixID()][dest.getmatrixID()] = weight;
//    }
//    private void allocateMoreMemory(){
//        double[][] allocatedMatrix = new double[this.matrix.length * 2][this.matrix.length * 2];
//        for(int x = 0; x < this.matrix.length; x++){
//            for(int y = 0; y < this.matrix.length; y++){
//                allocatedMatrix[x][y] = this.matrix[x][y];
//            }
//        }
//
//        for (int i = this.matrix.length-1; i <allocatedMatrix.length ; i++) {
//            for (int j = this.matrix.length - 1; j < allocatedMatrix.length ; j++) {
//                allocatedMatrix[i][j] = 0;
//            }
//        }
//        this.matrix = allocatedMatrix;
//    }
//
//    private int getMatrixSize(){
//        return this.matrix.length;
//    }
//    public double[][] getMatrix(){
//        return this.matrix;
//    }
//    public void printMatrix(){
//        for(int z = 0; z < this.matrix.length; z++ ){
//            System.out.print("\t" + z);
//        }
//        System.out.println();
//
//        for(int x = 0; x < this.matrix.length; x++){
//            System.out.print(x + " ");
//            for(int y = 0; y < this.matrix.length; y++){
//                System.out.print( this.matrix[x][y]  +  " ");
//            }
//            System.out.println();
//        }
//    }
//
//   private double minDistance(double[] dist, Boolean[] sptSet)
//    {
//        // Initialize min value
//        double min = Integer.MAX_VALUE, min_index = -1;
//
//        for (int v = 0; v < getMatrixSize(); v++)
//            if (sptSet[v] == false && dist[v] <= min) {
//                min = dist[v];
//                min_index = v;
//            }
//
//        return min_index;
//    }
//    private void printSolutionShortestPath(double[] dist)
//    {
//        System.out.println(
//                "Vertex \t\t Distance from Source");
//        for (int i = 0; i < getMatrixSize(); i++)
//            System.out.println(i + " \t\t " + dist[i]);
//    }
//
//
//    // Function that implements Dijkstra's single source
//    // shortest path algorithm for a graph represented using
//    // adjacency matrix representation
//    public void findShortestPathDijkstra(double graph[][], int src)
//    {
//        int V = getMatrixSize();
//        double dist[] = new double[V]; // The output array.
//        // dist[i] will hold
//        // the shortest distance from src to i
//
//        // sptSet[i] will true if vertex i is included in
//        // shortest path tree or shortest distance from src
//        // to i is finalized
//        Boolean sptSet[] = new Boolean[V];
//
//        // Initialize all distances as INFINITE and stpSet[]
//        // as false
//        for (int i = 0; i < V; i++) {
//            dist[i] = Integer.MAX_VALUE;
//            sptSet[i] = false;
//        }
//
//        // Distance of source vertex from itself is always 0
//        dist[src] = 0;
//
//        // Find shortest path for all vertices
//        for (int count = 0; count < V - 1; count++) {
//            // Pick the minimum distance vertex from the set
//            // of vertices not yet processed. u is always
//            // equal to src in first iteration.
//            int u = (int)minDistance(dist, sptSet);
//
//            // Mark the picked vertex as processed
//            sptSet[u] = true;
//
//            // Update dist value of the adjacent vertices of
//            // the picked vertex.
//            for (int v = 0; v < V; v++)
//
//                // Update dist[v] only if is not in sptSet,
//                // there is an edge from u to v, and total
//                // weight of path from src to v through u is
//                // smaller than current value of dist[v]
//                if (!sptSet[v] && graph[u][v] != 0
//                        && dist[u] != Integer.MAX_VALUE
//                        && dist[u] + graph[u][v] < dist[v])
//                    dist[v] = dist[u] + graph[u][v];
//        }
//
//        // print the constructed distance array
//        printSolutionShortestPath(dist);
//    }
//
//
//}

package cpp;

import java.util.HashMap;
import java.util.LinkedList;

public class Algorithm {

    public LinkedList<Vertex> countOddDegVerticesAndCheckCohesion(Graph g) {
        LinkedList<Vertex> oddVertices = new LinkedList<>();

        for (Vertex vertex : g.getAdjList().keySet()) {
            if (g.getAdjList().get(vertex).size() % 2 != 0) {
                oddVertices.add(vertex);
            }
            if (g.getAdjList().get(vertex).size() < 1) {
                return null;
            }
        }

        return oddVertices;
    }

    public void showCPPRoute(Graph g) {
        LinkedList<Vertex> oddVertices = countOddDegVerticesAndCheckCohesion(g);

        // Only consistent graph can be solved
        if (oddVertices == null) {
            return;
        }
        // 2 vertices with odd degree
        else if (oddVertices.size() == 2) {
            // Find dijkstra's shortest paths starting from first odd degree vertex
            DijkstrasResultHolder dijkstrasResult = dijkstrasAlgorithm(g, oddVertices.get(0));

            // Double the edges on the shortest path between two odd degree vertices
            while (!oddVertices.get(1).equals(oddVertices.get(0))) {
                // Find in Dijkstra's result current vertex equal to oddVertices.get(1) (one of odd degree vertices)
                Vertex prevVertex = dijkstrasResult.getPrevVertex().get(oddVertices.get(1));

                // Double the edge
                g.addEdge(prevVertex.getX(), prevVertex.getY(), oddVertices.get(1).getX(), oddVertices.get(1).getY());

                // Set prevVertex as new end vertex
                oddVertices.set(1, prevVertex);
            }
        }
        // More than 2 vertices with odd degree (always even number of these vertices)
        else {
            // TODO
        }


        System.out.println("Chinese postman problem route:");
        System.out.print("(0,0)");

        findEulerianCycle(g, new Vertex(0, 0));

        System.out.println();
    }

    private void findEulerianCycle(Graph g, Vertex v) {
        // Recur over all vertices adjacent to this vertex
        for (int i = 0; i < g.getAdjList().get(v).size(); ++i) {
            Edge edge = g.getAdjList().get(v).get(i);

            // Check if edge v - edge.getEndVertex is valid
            if (isNextEdgeValid(g, v, edge.getEndVertex())) {
                System.out.print(" -> " + edge.getEndVertex());

                g.deleteEdge(v.getX(), v.getY(), edge.getEndVertex().getX(), edge.getEndVertex().getY());
                findEulerianCycle(g, edge.getEndVertex());
            }
        }
    }

    private boolean isNextEdgeValid(Graph g, Vertex v1, Vertex v2) {
        // Edge v1 - v2 is a bridge -> valid edge
        if (g.getAdjList().get(v1).size() == 1) {
            return true;
        }

        // If there are multiple adjacent vertices. We have to check whether v1 - v2 is a bridge
        g.deleteEdge(v1.getX(), v1.getY(), v2.getX(), v2.getY());

        // BFS to check if we can go from v1 to v2 not using direct edge
        boolean available = isNextVertexAvailable(g, v1, v2);

        g.addEdge(v1.getX(), v1.getY(), v2.getX(), v2.getY());

        return available;
    }

    private boolean isNextVertexAvailable(Graph g, Vertex v1, Vertex v2) {
        HashMap<Vertex, Boolean> isVisited = new HashMap<>();
        LinkedList<Vertex> queue = new LinkedList<>();

        for (Vertex vertex : g.getAdjList().keySet()) {
            isVisited.put(vertex, false);
        }

        isVisited.put(v1, true);
        queue.add(v1);

        while (queue.size() != 0) {
            v1 = queue.poll();

            for (Edge edge : g.getAdjList().get(v1)) {
                if (!isVisited.get(edge.getEndVertex())) {
                    // Reached v2 from v1 not using direct edge
                    if (edge.getEndVertex().equals(v2)) {
                        return true;
                    }

                    isVisited.put(edge.getEndVertex(), true);
                    queue.add(edge.getEndVertex());
                }
            }
        }

        return false;
    }

    private static class DijkstrasResultHolder {
        private HashMap<Vertex, Vertex> prevVertex = new HashMap<>();
        private HashMap<Vertex, Double> totalCosts = new HashMap<>();

        public HashMap<Vertex, Vertex> getPrevVertex() {
            return prevVertex;
        }

        public HashMap<Vertex, Double> getTotalCosts() {
            return totalCosts;
        }

        public void setPrevVertex(HashMap<Vertex, Vertex> prevVertex) {
            this.prevVertex = prevVertex;
        }

        public void setTotalCosts(HashMap<Vertex, Double> totalCosts) {
            this.totalCosts = totalCosts;
        }
    }

    private DijkstrasResultHolder dijkstrasAlgorithm(Graph g, Vertex start) {
        // Previous vertex, i.e. when moving from (0,0) to (1,1), prevVertex stores it <curr (1,1), prev (0,0)>
        HashMap<Vertex, Vertex> prevVertex = new HashMap<>();

        // Total costs to get to given vertex
        HashMap<Vertex, Double> totalCosts = new HashMap<>();

        // Actual best cost queue to get to given vertex
        HashMap<Vertex, Double> bestCostsQueue = new HashMap<>();

        // Visited vertices
        HashMap<Vertex, Boolean> isVisited = new HashMap<>();

        for (Vertex vertex : g.getAdjList().keySet()) {
            bestCostsQueue.put(vertex, Double.MAX_VALUE);
            totalCosts.put(vertex, Double.MAX_VALUE);
            isVisited.put(vertex, false);
        }

        // Save information about starting vertex
        totalCosts.put(start, 0.0);
        bestCostsQueue.put(start, 0.0);
        prevVertex.put(start, start);

        while (!bestCostsQueue.isEmpty()) {
            Vertex smallestCostVertex = minVertexCurrCost(bestCostsQueue);

            isVisited.put(smallestCostVertex, true);

            // Iterate over all adjacent vertices to smallestCostVertex
            for (Edge edge : g.getAdjList().get(smallestCostVertex)) {
                // If neighbour is not on the visited list, check the new (alternative) path leading to it
                if (!isVisited.get(edge.getEndVertex())) {
                    double altPath = bestCostsQueue.get(smallestCostVertex) + edge.getWeight();

                    if (altPath < totalCosts.get(edge.getEndVertex())) {
                        totalCosts.put(edge.getEndVertex(), altPath);
                        bestCostsQueue.put(edge.getEndVertex(), altPath);
                        prevVertex.put(edge.getEndVertex(), smallestCostVertex);
                    }
                }
            }

            // Mark smallestCostVertex as visited (checked all his neighbours)
            bestCostsQueue.remove(smallestCostVertex);
        }

        // Store results in DijkstraResultHolder
        Algorithm.DijkstrasResultHolder holder = new Algorithm.DijkstrasResultHolder();
        holder.setPrevVertex(prevVertex);
        holder.setTotalCosts(totalCosts);

        return holder;
    }

    private Vertex minVertexCurrCost(HashMap<Vertex, Double> vertices) {
        Vertex minVertex = null;

        for (Vertex vertex : vertices.keySet()) {
            if (minVertex == null) {
                minVertex = vertex;
            } else if (vertices.get(vertex) < vertices.get(minVertex)) {
                minVertex = vertex;
            }
        }

        return minVertex;
    }

}

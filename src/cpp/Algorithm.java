package cpp;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Algorithm {

    private static DecimalFormat df;

    public Algorithm() {
        df = new DecimalFormat("0.0");
        df.setRoundingMode(RoundingMode.DOWN);
    }

    public void showCPPRoute(LinkedList<Object> cppRoute, int mode) {
        if (mode ==0 ) {
            System.out.println("Chinese postman problem (heuristic) route:");
        } else {
            System.out.println("Chinese postman problem (accurate) route:");
        }

        for (int j = 0; j < cppRoute.size(); ++j) {
            if (j == cppRoute.size() - 2) {
                System.out.println(cppRoute.get(j));
            } else if (j == cppRoute.size() - 1) {
                System.out.println("Total cost: " + df.format(cppRoute.get(j)));
            } else {
                System.out.print(cppRoute.get(j) + " -> ");
            }
        }
    }

    public LinkedList<Object> countCPPRoute(Graph g, int mode) {
        LinkedList<Object> cppRoute = new LinkedList<>();

        // Start in (0, 0)
        if (!g.getAdjList().containsKey(new Vertex(0, 0))) {
            return cppRoute;
        }

        // Only consistent graph can be solved, check mode, check if there are at least one vertex
        if (!g.isConsistent() || !(mode >= 0 && mode <= 1) || g.getAdjList().size() == 0) {
            System.out.println("Wrong graph");
            return cppRoute;
        }

        LinkedList<Vertex> oddVertices = g.getOddDegVertices();

        // 2 vertices with odd degree
        if (oddVertices.size() == 2) {
            // Find Dijkstra's shortest paths starting from first odd degree vertex
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
            // Create new complete graph on odd degree vertices
            Graph completeGraph = new Graph();
            completeGraph.addVertices(oddVertices);

            // Secondary adjacent list of vertices used to prevent adding same edge twice
            HashMap<Vertex, LinkedList<Vertex>> secondaryAdjList = new HashMap<>();

            for (Vertex vertex : completeGraph.getAdjList().keySet()) {
                secondaryAdjList.put(vertex, new LinkedList<>());
            }

            HashMap<Vertex, HashMap<Vertex, Vertex>> prevVertexList = new HashMap<>();
            HashMap<Vertex, HashMap<Vertex, Double>> totalCostsList = new HashMap<>();

            // Weight of edges - queue
            PriorityQueue<Double> weights = new PriorityQueue<>();

            // Find Dijkstra's shortest paths among all odd degree vertices
            for (Vertex oddVertex : oddVertices) {
                DijkstrasResultHolder dijkstrasResult = dijkstrasAlgorithm(g, oddVertex);

                // Store results of Dijkstra's algorithm
                prevVertexList.put(oddVertex, dijkstrasResult.getPrevVertex());
                totalCostsList.put(oddVertex, dijkstrasResult.getTotalCosts());

                // Add edges between current odd degree vertex and other vertices (create complete graph)
                for (Vertex vertex : dijkstrasResult.getTotalCosts().keySet()) {
                    // Do not add edges between the same vertex, even degree vertices or ones that are already added
                    if (dijkstrasResult.getTotalCosts().get(vertex) == 0
                            || !secondaryAdjList.containsKey(vertex)
                            || secondaryAdjList.get(vertex).contains(oddVertex)) {
                        continue;
                    }

                    completeGraph.addEdge(oddVertex.getX(), oddVertex.getY(), vertex.getX(), vertex.getY(), dijkstrasResult.getTotalCosts().get(vertex));
                    weights.add(dijkstrasResult.getTotalCosts().get(vertex));

                    secondaryAdjList.get(oddVertex).add(vertex);
                }
            }

            // Minimum weight perfect matching list of vertices
            HashMap<Vertex, Vertex> perfMatch;

            // Heuristic
            if (mode == 0) {
                try {
                    // Find minimum weight perfect matching (heuristic approach)
                    perfMatch = findHeuristicMatch(completeGraph, weights);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return cppRoute;
                }
            }
            // Accurate
            else {
                LinkedList<LinkedList<Vertex>> matches = new LinkedList<>();

                // Store all possible matches
                findAllMatches(matches, oddVertices, new LinkedList<>());

                // Find minimum weight perfect matching (accurate approach)
                perfMatch = findPerfectMatch(matches, totalCostsList);
            }

            // Double the edges in original graph
            for (Vertex vertex : perfMatch.keySet()) {
                while (!perfMatch.get(vertex).equals(vertex)) {
                    // Find previous vertex for end vertex of pair of vertices from connections (min weight perfect matching)
                    Vertex prevVertex = prevVertexList.get(vertex).get(perfMatch.get(vertex));

                    // Double the edge
                    g.addEdge(prevVertex.getX(), prevVertex.getY(), perfMatch.get(vertex).getX(), perfMatch.get(vertex).getY());

                    // Update end vertex of pair of vertices from connections (min. weight perfect matching)
                    perfMatch.put(vertex, prevVertex);
                }
            }
        }

        Vertex startVertex = new Vertex(0, 0);
        cppRoute.add(startVertex);
        double totalCost = 0.0;

        totalCost = findEulerianCycle(g, cppRoute, startVertex, totalCost);

        // Add route cost as last object in list
        cppRoute.add(totalCost);

        return cppRoute;
    }

    private HashMap<Vertex, Vertex> findHeuristicMatch(Graph completeGraph, PriorityQueue<Double> weights) throws Exception {
        // There will be number of vertices / 2 new edges
        int loops = completeGraph.getAdjList().size() / 2;

        HashMap<Vertex, Vertex> perfMatch = new HashMap<>();

        while (loops > 0) {
            if (weights.peek() == null) {
                throw new Exception("Priority queue returns null");
            }

            double currMinWeight = weights.poll();
            Vertex begin = null;
            Vertex end = null;

            outerloop:
            for (Vertex vertex : completeGraph.getAdjList().keySet()) {
                for (Edge edge : completeGraph.getAdjList().get(vertex)) {
                    // Find first edge which weight is equal to minimum weight from weights priority queue
                    if (edge.getWeight() == currMinWeight) {
                        perfMatch.put(vertex, edge.getEndVertex());
                        begin = vertex;
                        end = edge.getEndVertex();
                        break outerloop;
                    }
                }
            }

            if (begin == null || end == null) {
                throw new Exception("Begin or end vertex is null");
            }

            // Remove weight of edge from queue that will be deleted
            for (Edge edge : completeGraph.getAdjList().get(begin)) {
                weights.remove(edge.getWeight());
            }

            for (Edge edge : completeGraph.getAdjList().get(end)) {
                weights.remove(edge.getWeight());
            }

            // Delete vertices connected to minimum weight edge
            completeGraph.deleteVertex(begin.getX(), begin.getY());
            completeGraph.deleteVertex(end.getX(), end.getY());

            --loops;
        }

        return perfMatch;
    }

    private void findAllMatches(LinkedList<LinkedList<Vertex>> matches, LinkedList<Vertex> vertices, LinkedList<Vertex> visited) {
        // First of n vertices can associated with any of other n-1 vertices. After choosing first pair, there are n-2 vertices to consider and so on.
        if (vertices.isEmpty()) {
            matches.add(new LinkedList<>(visited));
            return;
        }

        if (vertices.size() % 2 == 0) {
            Vertex firstVertex = vertices.getFirst();
            visited.add(firstVertex);
            LinkedList<Vertex> remainingVertices = new LinkedList<>(vertices);
            remainingVertices.remove(firstVertex);
            findAllMatches(matches, remainingVertices, visited);
            visited.removeLast();
        } else {
            for (Vertex vertex : vertices) {
                visited.add(vertex);
                LinkedList<Vertex> remainingVertices = new LinkedList<>(vertices);
                remainingVertices.remove(vertex);
                findAllMatches(matches, remainingVertices, visited);
                visited.removeLast();
            }
        }
    }

    private HashMap<Vertex, Vertex> findPerfectMatch(LinkedList<LinkedList<Vertex>> matches, HashMap<Vertex, HashMap<Vertex, Double>> totalCostsList) {
        int bestMatchId = 0;
        int loopCntr = 0;
        double bestCost = Double.MAX_VALUE;

        for (LinkedList<Vertex> match : matches) {
            double cost = 0;

            // Count sum of weights
            for (int i = 0; i < match.size() - 1; i += 2) {
                cost += totalCostsList.get(match.get(i)).get(match.get(i + 1));
            }

            // Update id when smaller sum of costs
            if (cost < bestCost) {
                bestCost = cost;
                bestMatchId = loopCntr;
            }

            ++loopCntr;
        }

        HashMap<Vertex, Vertex> bestMatching = new HashMap<>();

        // Fill best connections
        for (int i = 0; i < matches.get(bestMatchId).size(); i += 2) {
            bestMatching.put(matches.get(bestMatchId).get(i), matches.get(bestMatchId).get(i + 1));
        }

        return bestMatching;
    }

    private double findEulerianCycle(Graph g, LinkedList<Object> cppRoute, Vertex v, double totalCost) {

        // Recur over all vertices adjacent to this vertex
        for (int i = 0; i < g.getAdjList().get(v).size(); ++i) {
            Edge edge = g.getAdjList().get(v).get(i);

            // Check if edge v - edge.getEndVertex is valid
            if (isNextEdgeValid(g, v, edge.getEndVertex())) {
                cppRoute.add(edge.getEndVertex());

                totalCost += edge.getWeight();

                g.deleteEdge(v.getX(), v.getY(), edge.getEndVertex().getX(), edge.getEndVertex().getY());
                totalCost = findEulerianCycle(g, cppRoute, edge.getEndVertex(), totalCost);
            }
        }

        return totalCost;
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

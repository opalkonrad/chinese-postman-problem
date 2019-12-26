package cpp;

import java.util.HashMap;
import java.util.LinkedList;

public class Algorithm {

    public boolean checkCohesion(Graph g) {
        for (Vertex vertex : g.getAdjList().keySet()) {
            if (g.getAdjList().get(vertex).size() < 1) {
                return false;
            }
        }

        return true;
    }

    public void showCPPRoute(Graph g) {
        // Only consistent graph can be solved
        if (!checkCohesion(g)) {
            return;
        }

        System.out.println("Chinese postman problem route:");
        System.out.print("(0,0)");

        findEulerianTour(g, new Vertex(0, 0));

        System.out.println();
    }

    private void findEulerianTour(Graph g, Vertex v) {
        // Recur over all vertices adjacent to this vertex
        for (int i = 0; i < g.getAdjList().get(v).size(); ++i) {
            Edge edge = g.getAdjList().get(v).get(i);

            // Check if edge v - edge.getEndVertex is valid
            if (isNextEdgeValid(g, v, edge.getEndVertex())) {
                System.out.print(" -> " + edge.getEndVertex());

                g.deleteEdge(v.getX(), v.getY(), edge.getEndVertex().getX(), edge.getEndVertex().getY());
                findEulerianTour(g, edge.getEndVertex());
            }
        }
    }

    private boolean isNextEdgeValid(Graph g, Vertex v1, Vertex v2) {
        // Edge v1 - v2 is a bridge -> valid edge
        if (g.getAdjList().get(v1).size() == 1) {
            return true;
        }

        // If there are multiple adjacents. We have to check whether v1 - v2 is a bridge
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

            // Reached v2 from v1 not using direct edge
            if (v1.equals(v2)) {
                return true;
            }

            for (Edge edge : g.getAdjList().get(v1)) {
                if (!isVisited.get(edge.getEndVertex())) {
                    isVisited.put(edge.getEndVertex(), true);
                    queue.add(edge.getEndVertex());
                }
            }
        }

        return false;
    }

}

package cpp;

import java.util.HashMap;
import java.util.LinkedList;

public class Graph {

    private HashMap<Vertex, LinkedList<Edge>> adjList;


    public Graph() {
        this.adjList = new HashMap<>();
    }

    public HashMap<Vertex, LinkedList<Edge>> getAdjList() {
        return adjList;
    }

    public void addVertex(int x, int y) {
        Vertex v = new Vertex(x, y);

        if (adjList.containsKey(v)) {
            return;
        }

        adjList.put(v, new LinkedList<>());
    }

    public void addVertices(LinkedList<Vertex> vertices) {
        for (Vertex vertex : vertices) {
            if (adjList.containsKey(vertex)) {
                continue;
            }

            adjList.put(vertex, new LinkedList<>());
        }
    }

    public void deleteVertex(int x, int y) {
        Vertex v = new Vertex(x, y);

        adjList.remove(v);

        for (Vertex vertex : adjList.keySet()) {
            adjList.get(vertex).removeIf(val -> val.getEndVertex().getX() == v.getX() && val.getEndVertex().getY() == v.getY());
        }
    }

    public void addEdge(int x1, int y1, int x2, int y2) {
        Vertex vFirst = new Vertex(x1, y1);
        Vertex vLast = new Vertex(x2, y2);

        double weight = Math.sqrt(Math.pow(vLast.getX() - vFirst.getX(), 2) + Math.pow(vLast.getY() - vFirst.getY(), 2));

        if (!(adjList.containsKey(vFirst) && adjList.containsKey(vLast))) {
            return;
        }

        adjList.get(vFirst).add(new Edge(vLast, weight));
        adjList.get(vLast).add(new Edge(vFirst, weight));
    }

    public void addEdge(int x1, int y1, int x2, int y2, double weight) {
        Vertex vFirst = new Vertex(x1, y1);
        Vertex vLast = new Vertex(x2, y2);

        if (!(adjList.containsKey(vFirst) && adjList.containsKey(vLast))) {
            return;
        }

        adjList.get(vFirst).add(new Edge(vLast, weight));
        adjList.get(vLast).add(new Edge(vFirst, weight));
    }

    public void deleteEdge(int x1, int y1, int x2, int y2) {
        Vertex vFirst = new Vertex(x1, y1);
        Vertex vLast = new Vertex(x2, y2);

        if (!(adjList.containsKey(vFirst) && adjList.containsKey(vLast))) {
            return;
        }

        // Delete first occurrence of edge between vFirst and vLast
        for (Edge edge : adjList.get(vFirst)) {
            if (vLast.getX() == edge.getEndVertex().getX() && vLast.getY() == edge.getEndVertex().getY()) {
                adjList.get(vFirst).remove(edge);
                break;
            }
        }

        // Delete first occurrence of edge between vLast and vFirst
        for (Edge edge : adjList.get(vLast)) {
            if (vFirst.getX() == edge.getEndVertex().getX() && vFirst.getY() == edge.getEndVertex().getY()) {
                adjList.get(vLast).remove(edge);
                break;
            }
        }
    }

    public void showGraph() {
        int edges = 0;

        for (Vertex vertex : adjList.keySet()) {
            edges += adjList.get(vertex).size();
        }

        System.out.println("Vertices: " + adjList.size() + ", Edges: " + edges / 2);
        System.out.println("Begin vertex [End vertex/weight, End vertex/weight...]");

        adjList.forEach((key, value) -> System.out.println(key + " " + value));
    }

}

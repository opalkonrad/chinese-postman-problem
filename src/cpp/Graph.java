package cpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {

    private HashMap<Vertex, LinkedList<Edge>> adjList;
    Random rand = new Random();


    public Graph() {
        this.adjList = new HashMap<>();
    }

    public Graph(Graph original) {
        this.adjList = new HashMap<>(original.getAdjList());

        // Copy adjList
        for (Vertex vertex : original.getAdjList().keySet()) {
            Vertex newVertex = new Vertex(vertex.getX(), vertex.getY());
            this.adjList.put(newVertex, new LinkedList<>());

            for (Edge edge : original.getAdjList().get(vertex)) {
                Edge newEdge = new Edge(edge.getEndVertex(), edge.getWeight());
                this.adjList.get(newVertex).add(newEdge);
            }
        }

        this.rand = new Random();
    }

    public HashMap<Vertex, LinkedList<Edge>> getAdjList() {
        return adjList;
    }

    public LinkedList<Vertex> getOddDegVertices() {
        LinkedList<Vertex> oddVertices = new LinkedList<>();

        for (Vertex vertex : adjList.keySet()) {
            if (adjList.get(vertex).size() % 2 != 0) {
                oddVertices.add(vertex);
            }
        }

        return oddVertices;
    }

    public boolean isConsistent() {
        HashMap<Vertex, Boolean> isVisited = new HashMap<>();
        LinkedList<Vertex> queue = new LinkedList<>();

        for (Vertex vertex : adjList.keySet()) {
            isVisited.put(vertex, false);
        }

        Vertex currVertex = new Vertex(0, 0);

        isVisited.put(currVertex, true);
        queue.add(currVertex);
        int vertices = 1;

        while (queue.size() != 0) {
            currVertex = queue.poll();

            for (Edge edge : adjList.get(currVertex)) {
                if (!isVisited.get(edge.getEndVertex())) {
                    isVisited.put(edge.getEndVertex(), true);
                    queue.add(edge.getEndVertex());
                    ++vertices;
                }
            }
        }

        return adjList.size() == vertices;
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

        System.out.println("Graph (vertices: " + adjList.size() + ", edges: " + edges / 2 + "). Representation: begin_vertex [end_vertex/weight, end_vertex/weight...]");

        adjList.forEach((key, value) -> System.out.println(key + " " + value));
    }

    public void generateGraphFromFile(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner sc = new Scanner(file);

        while (sc.hasNext()) {
            int x1, y1, x2, y2;
            x1 = sc.nextInt();
            y1 = sc.nextInt();
            x2 = sc.nextInt();
            y2 = sc.nextInt();
            addVertex(x1, y1);
            addVertex(x2, y2);
            addEdge(x1, y1, x2, y2);
        }
    }

    public void generateGraph(int vertices, int oddDegVertices) {
        if (oddDegVertices > vertices || oddDegVertices % 2 != 0 || oddDegVertices < 0 || vertices == 0) {
            System.out.println("Wrong requirements");
            return;
        }

        int min = -100;
        int max = 100;
        int x;
        int y;
        LinkedList<Vertex> verticesList = new LinkedList<>();

        // Add starting vertex
        addVertex(0, 0);
        verticesList.add(new Vertex(0, 0));

        // Add vertices
        while (adjList.size() != vertices) {
            x = rand.nextInt((max - min) + 1) + min;
            y = rand.nextInt((max - min) + 1) + min;

            Vertex vertex = new Vertex(x, y);

            if (!verticesList.contains(vertex)) {
                addVertex(x, y);
                verticesList.add(vertex);
            }
        }

        while (!isConsistent()) {
            Collections.shuffle(verticesList);

            for (int j = 0; j < verticesList.size() - 1; j += 2) {
                addEdge(verticesList.get(j).getX(), verticesList.get(j).getY(), verticesList.get(j + 1).getX(), verticesList.get(j + 1).getY());
            }
        }

        Vertex firstVertex = null;
        int currOddDegVerticesCnt = getOddDegVertices().size();

        for (Vertex vertex : adjList.keySet()) {
            if (currOddDegVerticesCnt == oddDegVertices) {
                return;
            }
            // There are more odd degree vertices that we want
            else if (currOddDegVerticesCnt > oddDegVertices && adjList.get(vertex).size() % 2 != 0) {
                if (firstVertex == null) {
                    firstVertex = vertex;
                    continue;
                }

                addEdge(firstVertex.getX(), firstVertex.getY(), vertex.getX(), vertex.getY());
                firstVertex = null;
                currOddDegVerticesCnt -= 2;
            }
            // There are less odd degree vertices that we want
            else if (currOddDegVerticesCnt < oddDegVertices && adjList.get(vertex).size() % 2 == 0) {
                if (firstVertex == null) {
                    firstVertex = vertex;
                    continue;
                }

                addEdge(firstVertex.getX(), firstVertex.getY(), vertex.getX(), vertex.getY());
                firstVertex = null;
                currOddDegVerticesCnt += 2;
            }
        }
    }

}

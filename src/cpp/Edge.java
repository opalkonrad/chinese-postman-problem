package cpp;

public class Edge {
    private Vertex endVertex;
    private double weight;

    public Edge(Vertex endVertex, double weight) {
        this.endVertex = endVertex;
        this.weight = weight;
    }

    public Vertex getEndVertex() {
        return endVertex;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "end: " + endVertex + ", weight: " + weight;
    }
}

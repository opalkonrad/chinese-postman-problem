package cpp;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Edge {
    private Vertex endVertex;
    private double weight;

    private static DecimalFormat df;

    public Edge(Vertex endVertex, double weight) {
        this.endVertex = endVertex;
        this.weight = weight;
        df = new DecimalFormat("0.0");
        df.setRoundingMode(RoundingMode.DOWN);
    }

    public Vertex getEndVertex() {
        return endVertex;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return endVertex + "/" + df.format(weight);
    }
}

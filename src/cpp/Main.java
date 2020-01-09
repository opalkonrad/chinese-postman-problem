package cpp;

public class Main {

    public static void main(String[] args) {

//        Test test = new Test();
//        test.performCorrectnessTests();

        Graph g = new Graph();
        g.generateGraph(30, 6);
        g.showGraph();
        Algorithm a = new Algorithm();
        a.showCPPRoute(g, 0);

    }
}

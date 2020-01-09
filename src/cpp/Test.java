package cpp;

public class Test {

    public void performCorrectnessTests() {
        Algorithm a = new Algorithm();

        System.out.println("#################################");
        System.out.println("###     Correctness Tests     ###");
        System.out.println("#################################\n");

        System.out.println("Test 1 (Eulerian Graph)");
        System.out.println("------------------------------");

        Graph g1 = new Graph();

        g1.addVertex(0, 0);
        g1.addVertex(0, 2);
        g1.addVertex(2, 0);
        g1.addVertex(2, 2);

        g1.addEdge(0, 0, 0, 2);
        g1.addEdge(0, 0, 2, 0);
        g1.addEdge(2, 0, 2, 2);
        g1.addEdge(0, 2, 2, 2);

        g1.showGraph();
        System.out.println();
        a.showCPPRoute(g1);

        System.out.println("\nTest 2 (2 odd vertices)");
        System.out.println("------------------------------");

        Graph g2 = new Graph();

        g2.addVertex(0, 0);
        g2.addVertex(0, 2);
        g2.addVertex(2, 0);
        g2.addVertex(2, 2);

        g2.addEdge(0, 0, 0, 2);
        g2.addEdge(0, 0, 2, 0);
        g2.addEdge(0, 2, 2, 0);
        g2.addEdge(2, 0, 2, 2);
        g2.addEdge(0, 2, 2, 2);

        g2.showGraph();
        System.out.println();
        a.showCPPRoute(g2);

        System.out.println("\nTest 3 (2 odd vertices)");
        System.out.println("------------------------------");

        Graph g3 = new Graph();

        g3.addVertex(0, 0);
        g3.addVertex(0, 2);
        g3.addVertex(2, 0);
        g3.addVertex(2, 2);
        g3.addVertex(2, 4);

        g3.addEdge(0, 0, 0, 2);
        g3.addEdge(0, 0, 2, 0);
        g3.addEdge(0, 0, 2, 2);
        g3.addEdge(2, 0, 2, 2);
        g3.addEdge(0, 2, 2, 2);
        g3.addEdge(0, 2, 2, 4);
        g3.addEdge(2, 2, 2, 4);

        g3.showGraph();
        System.out.println();
        a.showCPPRoute(g3);

        System.out.println("\nTest 4 (2 odd vertices)");
        System.out.println("------------------------------");

        Graph g4 = new Graph();

        g4.addVertex(0, 0);
        g4.addVertex(2, 2);
        g4.addVertex(4, 2);
        g4.addVertex(2, 4);
        g4.addVertex(4, 4);
        g4.addVertex(6, 4);

        g4.addEdge(0, 0, 2, 2);
        g4.addEdge(2, 2, 4, 2);
        g4.addEdge(2, 2, 2, 4);
        g4.addEdge(2, 2, 4, 4);
        g4.addEdge(2, 4, 4, 4);
        g4.addEdge(4, 2, 4, 4);
        g4.addEdge(4, 4, 6, 4);

        g4.showGraph();
        System.out.println();
        a.showCPPRoute(g4);
    }

}

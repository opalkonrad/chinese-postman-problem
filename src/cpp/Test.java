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

        Graph g2 = new Graph(g1);

        g1.showGraph();

        System.out.println();

        a.showCPPRoute(g1, 0);
        a.showCPPRoute(g2, 1);


        System.out.println("\nTest 2 (2 odd vertices)");
        System.out.println("------------------------------");

        Graph g3 = new Graph();

        g3.addVertex(0, 0);
        g3.addVertex(0, 2);
        g3.addVertex(2, 0);
        g3.addVertex(2, 2);

        g3.addEdge(0, 0, 0, 2);
        g3.addEdge(0, 0, 2, 0);
        g3.addEdge(2, 0, 2, 2);
        g3.addEdge(0, 2, 2, 2);
        g3.addEdge(0, 2, 2, 0);

        Graph g4 = new Graph(g3);

        g3.showGraph();

        System.out.println();

        a.showCPPRoute(g3, 0);
        a.showCPPRoute(g4, 1);


        System.out.println("\nTest 3 (4 odd vertices)");
        System.out.println("------------------------------");

        Graph g5 = new Graph();

        g5.addVertex(0, 0);
        g5.addVertex(2, 2);
        g5.addVertex(4, 2);
        g5.addVertex(4, 4);
        g5.addVertex(6, 4);

        g5.addEdge(0, 0, 2, 2);
        g5.addEdge(2, 2, 4, 2);
        g5.addEdge(2, 2, 4, 4);
        g5.addEdge(2, 4, 4, 4);
        g5.addEdge(4, 2, 6, 4);
        g5.addEdge(4, 4, 4, 2);

        Graph g6 = new Graph(g5);

        g5.showGraph();

        System.out.println();

        a.showCPPRoute(g5, 0);
        a.showCPPRoute(g6, 1);
    }

}

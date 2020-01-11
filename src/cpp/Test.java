package cpp;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

public class Test {

    public void performCorrectnessTests() {
        Algorithm a = new Algorithm();

        System.out.println("# CORRECTNESS TESTS\n");

        System.out.println("# Test 1 (Eulerian Graph)");
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


        System.out.println("\n# Test 2 (2 odd vertices)");
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


        System.out.println("\n# Test 3 (4 odd vertices)");
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

    public void performPerformanceTests(int iterations, int vertices, int oddDegVertices) {
        Algorithm a = new Algorithm();

        System.out.println("# PERFORMANCE TESTS\n");

        long totalTimeHeuristic = 0;
        long totalTimeAccurate = 0;

        for (int i = 0; i < iterations; ++i) {
            System.out.println("\n# Loop: " + (i + 1) + "/" + iterations);
            Graph g1 = new Graph();
            g1.generateGraph(vertices, oddDegVertices);
            Graph g2 = new Graph(g1);

            g1.showGraph();
            System.out.println();
            long startHeuristic = System.nanoTime();
            a.showCPPRoute(g1, 0);
            long elapsedTimeHeuristic = System.nanoTime() - startHeuristic;
            System.out.println("-> elapsed time: " + elapsedTimeHeuristic + "ns");
            totalTimeHeuristic += elapsedTimeHeuristic;

            System.out.println();

            long startAccurate = System.nanoTime();
            a.showCPPRoute(g2, 1);
            long elapsedTimeAccurate = System.nanoTime() - startAccurate;
            System.out.println("-> elapsed time: " + elapsedTimeAccurate + "ns");
            totalTimeAccurate += elapsedTimeAccurate;
        }

        long avgTimeHeuristic = totalTimeHeuristic / iterations;
        long avgTimeAccurate = totalTimeAccurate / iterations;

        System.out.println("\n# Summary");
        System.out.println("Average time (heuristic): " + avgTimeHeuristic + "ns");
        System.out.println("Average time (accurate):  " + avgTimeAccurate + "ns");
    }

    public void warmUp() {
        Algorithm a = new Algorithm();

        System.out.println("# Warming up JVM...");

        PrintStream originalPrintStream = System.out;

        PrintStream nowhere = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                // Do nothing
            }
        });

        System.setOut(nowhere);

        // Warm up
        for (int i = 0; i < 1000; ++i) {
            Graph gWarmUp1 = new Graph();
            gWarmUp1.generateGraph(50, 10);
            Graph gWarmUp2 = new Graph(gWarmUp1);
            a.showCPPRoute(gWarmUp1, 0);
            a.showCPPRoute(gWarmUp2, 1);
        }

        System.setOut(originalPrintStream);
    }

}

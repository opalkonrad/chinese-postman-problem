package cpp;

import java.io.OutputStream;
import java.io.PrintStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Scanner;

public class Test {

    static Scanner sc = new Scanner(System.in);
    private static DecimalFormat df;

    public Test() {
        df = new DecimalFormat("0.0");
        df.setRoundingMode(RoundingMode.DOWN);
    }

    public void performCorrectnessTests() {
        Algorithm a = new Algorithm();

        System.out.println("Correctness tests\n");

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

        LinkedList<Object> cpp1 = a.countCPPRoute(g1, 0);
        a.showCPPRoute(cpp1, 0);
        System.out.println();
        LinkedList<Object> cpp2 = a.countCPPRoute(g2, 1);
        a.showCPPRoute(cpp2, 1);


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

        LinkedList<Object> cpp3 = a.countCPPRoute(g3, 0);
        a.showCPPRoute(cpp3, 0);
        System.out.println();
        LinkedList<Object> cpp4 = a.countCPPRoute(g4, 1);
        a.showCPPRoute(cpp4, 1);


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

        LinkedList<Object> cpp5 = a.countCPPRoute(g5, 0);
        a.showCPPRoute(cpp5, 0);
        System.out.println();
        LinkedList<Object> cpp6 = a.countCPPRoute(g6, 1);
        a.showCPPRoute(cpp6, 1);
    }

    public void performPerformanceTests(int iterations, int vertices, int oddDegVertices) {
        if (iterations < 1) {
            System.out.println("Must be at least one iteration!");
            return;
        }

        Algorithm a = new Algorithm();

        System.out.println("Performance tests");

        long totalTimeHeuristic = 0;
        long totalTimeAccurate = 0;
        double totalCostHeuristic = 0.0;
        double totalCostAccurate = 0.0;
        LinkedList<Object> cppHeuristic;
        LinkedList<Object> cppAccurate;

        for (int i = 0; i < iterations; ++i) {
            System.out.println("\nLoop: " + (i + 1) + "/" + iterations);
            Graph g1 = new Graph();
            g1.generateGraph(vertices, oddDegVertices);
            Graph g2 = new Graph(g1);

            g1.showGraph();
            System.out.println();
            long startHeuristic = System.nanoTime();
            cppHeuristic = a.countCPPRoute(g1, 0);
            long elapsedTimeHeuristic = System.nanoTime() - startHeuristic;

            a.showCPPRoute(cppHeuristic, 0);
            totalCostHeuristic += (double) cppHeuristic.get(cppHeuristic.size() - 1);

            System.out.println("Elapsed time: " + elapsedTimeHeuristic + "ns");
            totalTimeHeuristic += elapsedTimeHeuristic;

            System.out.println();

            long startAccurate = System.nanoTime();
            cppAccurate = a.countCPPRoute(g2, 1);
            long elapsedTimeAccurate = System.nanoTime() - startAccurate;

            a.showCPPRoute(cppAccurate, 1);
            totalCostAccurate += (double) cppAccurate.get(cppAccurate.size() - 1);

            System.out.println("Elapsed time: " + elapsedTimeAccurate + "ns");
            totalTimeAccurate += elapsedTimeAccurate;
        }

        long avgTimeHeuristic = totalTimeHeuristic / iterations;
        long avgTimeAccurate = totalTimeAccurate / iterations;
        double avgCostHeuristic = totalCostHeuristic / iterations;
        double avgCostAccurate = totalCostAccurate / iterations;

        System.out.println("\n\nSummary");
        System.out.println("Average time (heuristic): " + avgTimeHeuristic + "ns");
        System.out.println("Average time (accurate):  " + avgTimeAccurate + "ns");
        System.out.println("Average cost (heuristic): " + df.format(avgCostHeuristic));
        System.out.println("Average cost (accurate):  " + df.format(avgCostAccurate));
    }

    public void warmUp() {
        Algorithm a = new Algorithm();

        System.out.println("Warming up JVM...");

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
            gWarmUp1.generateGraph(100, 12);
            Graph gWarmUp2 = new Graph(gWarmUp1);
            a.countCPPRoute(gWarmUp1, 0);
            a.countCPPRoute(gWarmUp2, 1);
        }

        System.setOut(originalPrintStream);
    }

}

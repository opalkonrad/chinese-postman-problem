package cpp;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Menu {

    static Scanner sc = new Scanner(System.in);

    public static void startMenu() {
        Test test = new Test();

        System.out.println("Welcome to Chinese Postman Problem solver!");

        while (true) {
            System.out.println("\n# Menu");
            System.out.println("Select what would you like to do:");
            System.out.println("1. Perform correctness tests");
            System.out.println("2. Perform performance tests");
            System.out.println("3. Solve my graph (from file)");
            System.out.println("4. Exit program");

            System.out.print("I choose: ");
            int mode = 0;
            if (sc.hasNextInt()) {
                mode = sc.nextInt();

                if (!(mode > 0 && mode < 5)) {
                    System.out.println("Something went wrong, try one more time!");
                } else {
                    System.out.println();
                }
            } else {
                System.out.println("Something went wrong, try one more time!");
                sc.next();
            }

            switch (mode) {
                case 1:
                    test.performCorrectnessTests();
                    break;

                case 2:
                    int iterations, vertices, oddDegVertices;

                    while (true) {
                        System.out.print("Enter number of iterations (i > 0): ");

                        if (sc.hasNextInt()) {
                            iterations = sc.nextInt();

                            if (iterations < 1) {
                                continue;
                            }

                            break;
                        } else {
                            sc.next();
                        }
                    }

                    while (true) {
                        System.out.print("Enter number of vertices (v > 0): ");

                        if (sc.hasNextInt()) {
                            vertices = sc.nextInt();

                            if (vertices <= 0) {
                                continue;
                            }

                            break;
                        } else {
                            sc.next();
                        }
                    }

                    while (true) {
                        System.out.print("Enter number of odd degree vertices (ov >= 0 && ov % 2 == 0 && ov < v): ");

                        if (sc.hasNextInt()) {
                            oddDegVertices = sc.nextInt();

                            if (oddDegVertices < 0 || oddDegVertices % 2 != 0 || oddDegVertices >= vertices) {
                                continue;
                            }

                            break;
                        } else {
                            sc.next();
                        }
                    }

                    System.out.println();

                    test.warmUp();

                    System.out.println();

                    test.performPerformanceTests(iterations, vertices, oddDegVertices);
                    break;

                case 3:
                    System.out.println("Graph should look like:");
                    System.out.println("v1.x v1.y v2.x v2.y, where v1/v2 - vertex 1/vertex 2 and .x/.y - x and y coordinates, i.e.");
                    System.out.println("\n0 0 2 2");
                    System.out.println("2 2 4 2\n");
                    System.out.print("Insert path to file (with .txt extension): ");

                    String path = sc.next();

                    Graph userGraph = new Graph();
                    try {
                        userGraph.generateGraphFromFile(path);
                    } catch (FileNotFoundException e) {
                        System.out.println("File not found");
                        break;
                    }

                    System.out.println("\nSelect version of algorithm:");
                    System.out.println("1. Heuristic");
                    System.out.println("2. Accurate");

                    int algMode;
                    while (true) {
                        System.out.print("I choose: ");

                        if (sc.hasNextInt()) {
                            algMode = sc.nextInt();

                            if (algMode < 1 || algMode > 2) {
                                continue;
                            }

                            break;
                        } else {
                            sc.next();
                        }
                    }

                    Algorithm a = new Algorithm();

                    LinkedList<Object> result = a.countCPPRoute(userGraph, (algMode - 1));

                    a.showCPPRoute(result, (algMode - 1));

                    break;

                case 4:
                    System.out.println("Goodbye");
                    return;
            }
        }
    }

}

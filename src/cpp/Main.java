package cpp;

public class Main {

    public static void main(String[] args) {

        Graph g = new Graph();
        g.addVertex(0, 0);
        g.addVertex(0, 1);
        g.addVertex(1, 1);
        g.addVertex(1, 0);
        g.addEdge(0, 0, 0, 1);
        g.addEdge(0, 0, 1, 0);
        g.addEdge(1, 0, 1, 1);
        g.addEdge(1, 1, 0, 1);




        g.showGraph();

//        g.deleteVertex(0, 0);
//        boolean a = g.deleteEdge(1,0,1,1);
//        System.out.println(a);
////        g.addEdge(1, 0, 1, 1);

        g.showGraph();



    }
}

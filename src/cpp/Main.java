package cpp;

public class Main {

    public static void main(String[] args) {

        Test test = new Test();

        test.performCorrectnessTests();

        test.warmUp();
        test.performPerformanceTests(100, 50, 12);
    }

}

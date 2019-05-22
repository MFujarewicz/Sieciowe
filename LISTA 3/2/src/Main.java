import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        CsmaSim simulation = new CsmaSim(100);

        simulation.addNode(0, 2);
        simulation.addNode(10, 2);
        simulation.addNode(20, 2);
        simulation.addNode(30, 2);
        simulation.addNode(40, 2);
        simulation.addNode(50, 2);
        simulation.addNode(60, 2);
        simulation.addNode(70, 2);
        simulation.addNode(80, 2);
        simulation.addNode(90, 2);

        simulation = new CsmaSim(100);
        simulation.addNode(10, 1);
        simulation.addNode(20, 1);

        simulation.run();
    }


}

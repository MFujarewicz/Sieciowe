import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;



public class CsmaSim {


    private static final int STEP_TIME_MILIS = 0;
    Cable cable;
    ArrayList<Node> nodes;


    public CsmaSim(int length){
        cable = new Cable(length);
        nodes = new ArrayList<>();
    }
    public void addNode(int position, int messageCount){
        nodes.add(new Node(position, messageCount, cable.cable.length));
    }
    public void run() throws InterruptedException {
        //System.out.print(cable+"\n");
        while (messagesLeft()>0  || !cable.isIdle || cable.isCollision){
            nodesStep();
            cable.step();
            sleep(STEP_TIME_MILIS);
            System.out.print(cable+"\n");
        }

        //System.out.print(cable+"\n");
    }

    private void nodesStep() {

        for(Node n : nodes){
            if(n.sending && cable.isIdle && !cable.isCollision){
                n.sending = false;
                n.selectTarget(cable.cable.length);
                n.power = 1;
                n.timeout = 0;
            }

            if(cable.isCollision && n.sending){
                n.messageCount++;
                n.timeout = n.random.nextInt( (int) Math.pow(2, n.power)+1);
                n.power ++;
                n.sending = false;

            }

            if(cable.isIdle && !cable.isCollision && n.messageCount>0 && n.timeout == 0){
                cable.cable[n.position] = n.target;
                n.sending = true;
                n.messageCount--;
            }

            if(n.timeout>0) n.timeout--;
        }
    }

    int messagesLeft(){
        int msgCount = 0;

        for (Node n: nodes){
            msgCount+=n.messageCount;
        }
        return msgCount;
    }
}

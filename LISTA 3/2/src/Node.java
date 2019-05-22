import java.util.Random;

public class Node {
    Random random;
    int position;
    int messageCount;
    int timeout;
    int power;
    boolean sending;
    int target;
    int cableSize;

    public Node(int position, int messageCount, int cableSize){
        this.position = position;
        this.messageCount = messageCount;
        this.cableSize = cableSize;
        timeout = 0;
        power = 1;
        sending = false;
        random = new Random();
        selectTarget(cableSize);
    }

    public void selectTarget(int cableSize){
        do {
            target = random.nextInt(cableSize);
        }while (target == position);

    }

}

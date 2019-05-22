import java.util.Arrays;
import java.util.Collections;

public class Cable {


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public int[] cable;
    public boolean isIdle = true;
    public boolean isCollision = false;


    public boolean checkIdle(){
        int counter = 0;
        for(int i : cable){
            if(i != -1) counter++;
        }
        if(counter == 0) return true;
        else return false;
    };
    public boolean checkCollision(){
        int counter = 0;
        for(int i : cable){
            if(i != -1) counter++;
        }
        if(counter > 1) return true;
        else return false;
    };

    public void step(){
        if (isCollision) Arrays.fill(cable, -1);
        isIdle = checkIdle();
        isCollision = checkCollision();

        for(int i=0; i<cable.length; i++){
            if(cable[i] == -1) continue;

            if(cable[i] == i){
                cable[i] = -1;
                isIdle = true;
                continue;
            }
            if(cable[i] > i){
                cable[i+1] = cable[i];
                cable[i] = -1;
                i++;
            }else {
                cable[i-1] = cable[i];
                cable[i] = -1;
                i--;
            }
        }
    }


    public Cable(int length){
        cable = new int[length];
        Arrays.fill(cable, -1);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i : cable){
            if(i == -1) stringBuilder.append(0);
            else stringBuilder.append(ANSI_RED + i + ANSI_RESET);
        }
        return stringBuilder.toString();// + isIdle + isCollision;
    }
}

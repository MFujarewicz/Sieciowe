import java.io.*;
import java.util.Scanner;
import java.util.zip.CRC32;



public class Encoder {

    static String prefix = "01111110";
    static String suffix = "01111110";
    static int maxBytesInFrame = 32;




    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(new FileReader(args[0]));
        StringBuilder sb = new StringBuilder();
        while (in.hasNext()){
            sb.append(in.next());
        }
        in.close();

        String sourceString = sb.toString();
        String outputStr = new String();
        FileWriter out = new FileWriter("encoded.txt");


        while (!sourceString.isEmpty()){
            if(sourceString.length()>=maxBytesInFrame){
                outputStr = sourceString.substring(0, maxBytesInFrame);
                sourceString = sourceString.substring(maxBytesInFrame, sourceString.length());
            }
            else {
                outputStr = sourceString;
                sourceString = "";
            }

            outputStr = addCRC(outputStr);
            outputStr = stuffBytes(outputStr);
            outputStr = encapsulate(outputStr);
            out.append(outputStr);


        }


        out.close();
    }

    private static String addCRC(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);

        CRC32 crc = new CRC32();
        crc.update(s.getBytes(), 0, s.length());

        for(int i = 0; i<32-(Long.toString(crc.getValue(), 2).length()); i++){
            sb.append("0");
        }

        sb.append(Long.toString(crc.getValue(), 2));

        return sb.toString();

    }

    static String encapsulate(String s){
            return prefix + s + suffix;
        }

        static String stuffBytes(String s){
            return s.replaceAll("11111", "111110");
        }

}

import sun.net.ftp.FtpClient;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.spec.DSAGenParameterSpec;
import java.util.Scanner;
import java.util.zip.CRC32;

public class Decoder {

    static String prefix = "01111110";
    static String suffix = "01111110";

    public static void main(String[] args) throws IOException {


        @SuppressWarnings("duplicates")
        StringBuilder sb = new StringBuilder();
        Scanner in = new Scanner(new FileReader(args[0]));
        while (in.hasNext()){
            sb.append(in.next());
        }
        in.close();

        String source = sb.toString();
        CRC32 crc32 = new CRC32();
        String crcChecksumOld;
        String crcChecksumNew;


        FileWriter out = new FileWriter("decoded.txt");

        String frame;
        while (source.contains(prefix) && source.substring(source.indexOf(prefix)+prefix.length()).contains(suffix)){
            source = source.substring(source.indexOf(prefix)+prefix.length());
            if (source.indexOf(prefix) == 0) {
                continue;
            }
            frame = source.substring(0, source.indexOf(suffix));
            source = source.substring(source.indexOf(suffix)+suffix.length());

            frame = destuff(frame);

            crcChecksumOld = frame.substring(frame.length()-32);
            frame = frame.substring(0, frame.length()-32);

            crc32.reset();
            crc32.update(frame.getBytes(), 0, frame.length());
            crcChecksumNew = Long.toString(crc32.getValue(), 2);
            for(int i=crcChecksumNew.length(); i<32; i++){
                crcChecksumNew = "0" + crcChecksumNew;
            }

            if(!crcChecksumNew.equals(crcChecksumOld)){
                System.err.println("Data Corrupted");
                continue;
            }

            out.append(frame);
        }





        //out.write(out.toString());
        out.close();

    }

    private static String destuff(String s){
        return s.replaceAll("111110", "11111");

    }
}

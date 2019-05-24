import java.net.*;
import java.util.ArrayList;

public class Z2Receiver {
    static final int datagramSize = 50;
    InetAddress localHost;
    int destinationPort;
    DatagramSocket socket;
    ReceiverThread receiver;
    StringBuilder stringBuilder;

    public Z2Receiver(int myPort, int destPort)
            throws Exception {
        localHost = InetAddress.getByName("127.0.0.1");
        destinationPort = destPort;
        socket = new DatagramSocket(myPort);
        receiver = new ReceiverThread();
        stringBuilder = new StringBuilder();
    }

    public static void main(String[] args)
            throws Exception {
        Z2Receiver receiver = new Z2Receiver(Integer.parseInt(args[0]),
                Integer.parseInt(args[1]));
        receiver.receiver.start();
    }

    class ReceiverThread extends Thread {

        public void run() {
            int counter = 0;
            ArrayList<Z2Packet> packetsReceived = new ArrayList<>();

            try {
                while (true) {

                    for (int i=0; i<packetsReceived.size(); i++){
                        if(packetsReceived.get(i).getIntAt(0) == counter){
                            Z2Packet p = packetsReceived.get(i);
                            System.out.println("R:" + p.getIntAt(0) + ": " + (char) p.data[4]);
                            stringBuilder.append((char) p.data[4]);
                            System.out.println(stringBuilder.toString());
                            counter++;
                            packetsReceived.remove(packetsReceived.get(i));
                            i=0;
                        }
                    }

                    @SuppressWarnings("duplicates")
                    byte[] data = new byte[datagramSize];
                    DatagramPacket packet =
                            new DatagramPacket(data, datagramSize);
                    socket.receive(packet);
                    Z2Packet p = new Z2Packet(packet.getData());
                    packetsReceived.add(p);


                  /*  System.out.println("R:" + p.getIntAt(0)
                            + ": " + (char) p.data[4]);*/
                    // WYSLANIE POTWIERDZENIA
                    packet.setPort(destinationPort);
                    socket.send(packet);
                }
            } catch (Exception e) {
                System.out.println("Z2Receiver.ReceiverThread.run: " + e);
            }
        }

    }


}

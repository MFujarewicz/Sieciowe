import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

class Z2Sender {
    static final int datagramSize = 50;
    static final int sleepTime = 500;
    static final int maxPacket = 50;
    InetAddress localHost;
    int destinationPort;
    DatagramSocket socket;
    SenderThread sender;
    ReceiverThread receiver;
    volatile ArrayList<DatagramPacket> datagramPackets;


    public Z2Sender(int myPort, int destPort)
            throws Exception {
        localHost = InetAddress.getByName("127.0.0.1");
        destinationPort = destPort;
        socket = new DatagramSocket(myPort);
        sender = new SenderThread();
        receiver = new ReceiverThread();
        datagramPackets = new ArrayList<>();
    }

    public static void main(String[] args)
            throws Exception {
        Z2Sender sender = new Z2Sender(Integer.parseInt(args[0]),
                Integer.parseInt(args[1]));
        sender.sender.start();
        sender.receiver.start();
    }

    class SenderThread extends Thread {
        public void run() {
            int i, x;
            try {
                for (i = 0; (x = System.in.read()) >= 0; i++) {
                    Z2Packet p = new Z2Packet(4 + 1);
                    p.setIntAt(i, 0);
                    p.data[4] = (byte) x;
                    DatagramPacket packet =
                            new DatagramPacket(p.data, p.data.length,
                                    localHost, destinationPort);
                    datagramPackets.add(packet);
                }
            } catch (Exception e) {
                System.out.println("Z2Sender.SenderThread.run: " + e);
            }


            synchronized (datagramPackets) {
                while (!datagramPackets.isEmpty()) {

                    for (DatagramPacket datagramPacket : datagramPackets) {
                        try {
                            socket.send(datagramPacket);
                            sleep(sleepTime);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        datagramPackets.wait(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("transmission confirmed");
            }
        }
    }

    class ReceiverThread extends Thread {

        public void run() {
            try {
                while (true) {
                    byte[] data = new byte[datagramSize];
                    DatagramPacket packet =
                            new DatagramPacket(data, datagramSize);
                    socket.receive(packet);
                    Z2Packet p = new Z2Packet(packet.getData());
                    Z2Packet pSent;

                    synchronized (datagramPackets) {
                        Iterator<DatagramPacket> datagramIterator = datagramPackets.iterator();
                        while (datagramIterator.hasNext()) {
                            DatagramPacket datagramPacket = datagramIterator.next();
                            pSent = new Z2Packet(datagramPacket.getData());
                            if (pSent.getIntAt(0) == p.getIntAt(0)) {
                                datagramIterator.remove();
                            }

                        }
                    }

                    System.out.println("S:" + p.getIntAt(0) +
                            ": " + (char) p.data[4]);
                    //System.out.println("left to send: " + datagramPackets.size());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print(datagramPackets.size());
            }
        }

    }


}

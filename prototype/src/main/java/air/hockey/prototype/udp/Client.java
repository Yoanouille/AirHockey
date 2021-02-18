package air.hockey.prototype.udp;

import air.hockey.prototype.javafx.ProtoPhysicJavaFx;
import air.hockey.prototype.model.Model;
import air.hockey.prototype.model.Pusher;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {
    private String hostname = "localhost";
    public final int SERVER_PORT = 6666;
    private DatagramSocket socket;
    private Model model;

    public Client(Model m) throws IOException {
        socket = new DatagramSocket();
        model = m;

        String connect = "connexion";
        byte[] msgco = connect.getBytes();
        DatagramPacket msg = new DatagramPacket(msgco,msgco.length, InetAddress.getByName(hostname),SERVER_PORT);
        socket.send(msg);

        byte[] buf = new byte[1];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        System.out.println("ATTEND REPONSE DU SERVEUR");
        socket.receive(packet);
        System.out.println("J'AI RECU");
        String res = new String(buf);
        if(res.equals("1")){
            model.swapPushers();
        }
        System.out.println(res);
        new Sender().start();
        new Receiver().start();
    }

    public void sendPusher() throws IOException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(model.getPushers()[0]);
        oo.close();
        byte[] pusherSerialized = bStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(pusherSerialized, pusherSerialized.length, InetAddress.getByName(hostname), SERVER_PORT);
        socket.send(packet);
    }

    public void receivePusher() throws IOException, ClassNotFoundException {
        byte[]buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
        Pusher p = (Pusher)ois.readObject();
        ois.close();
        model.getPushers()[1] = p;
    }

    public static void main(String[] args) throws IOException {
        ProtoPhysicJavaFx.launch(ProtoPhysicJavaFx.class, args);
    }

    public class Sender extends Thread {

        @Override
        public void run() {
            while (true) {
                System.out.print("");//Très louche!!!...
                if(model.hasPusherMoved()) {
                    System.out.println("J'ENVOIE LA POSITION DU PUSHER");
                    try {
                        sendPusher();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public class Receiver extends Thread {

        @Override
        public void run() {
            while(true) {
                //TODO RECOIT LA VRAIE POSITION DU PALET ET L'ACTUALISE
                try {
                    System.out.println("J'ATTEND DE RECEVOIR LE PUSHER");
                    receivePusher();
                    System.out.println("J'AI RECU LE PUSHER");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

package airhockey.network;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import airhockey.model.Model;
import airhockey.model.Palet;
import airhockey.model.Pusher;

public class Client{
    private String id;
    private DatagramSocket socket;
    private Model model;
    private int numPlayer;
    //TODO ADD JAVAFX ATTRIBUTE

    public Client(Model m) throws SocketException {
        socket = new DatagramSocket();
        model = m;
    }

    public void createRoom() throws IOException {
        numPlayer = 0;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeUTF("creer");
        oo.close();
        byte[] message = bStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(message, message.length, InetAddress.getByName(Server.HOSTNAME), Server.PORT);
        socket.send(packet);

        byte[] idBuff = new byte[Server.ID_LENGTH];
        packet = new DatagramPacket(idBuff, idBuff.length);
        socket.receive(packet);
        id = new String(idBuff);

        byte[] buff = new byte[5];
        packet = new DatagramPacket(idBuff, buff.length);
        socket.receive(packet);
        String msg = new String(buff);
        if(msg.equals("start")){
            startGame();
        }
    }

    public void joinRoom(String id) throws IOException {
        numPlayer = 1;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeUTF("rejoindre");
        oo.writeUTF(id);
        oo.close();
        byte[] message = bStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(message, message.length, InetAddress.getByName(Server.HOSTNAME), Server.PORT);
        socket.send(packet);

        byte[] buff = new byte[8];
        packet = new DatagramPacket(buff, buff.length);
        socket.receive(packet);
        String msg = new String(buff);
        switch(msg){
            case "yesRoom ":
                startGame();
                break;

            //TODO HANDLE noRoom AND fullRoom
        }
    }

    public void startGame(){
        //TODO CHANGE SCENE TO GAME SCENE
        new Sender().start();
        new Receiver().start();
    }

    public void sendPusher() throws IOException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(model.getPushers()[numPlayer]);
        oo.close();
        byte[] pusherSerialized = bStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(pusherSerialized, pusherSerialized.length, InetAddress.getByName(Server.HOSTNAME), Server.PORT);
        socket.send(packet);
    }

    public void receiveModel() throws IOException, ClassNotFoundException {
        //System.out.println("J'AI RECU MODELE");
        byte[]buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
        Pusher p = ((Pusher[])ois.readObject())[1-numPlayer];
        Palet pa = (Palet)ois.readObject();
        ois.close();
        model.getPushers()[1-numPlayer] = p;
        model.setPalet(pa);
    }

    public class Sender extends Thread{
        @Override
        public void run() {
            while(!socket.isClosed()){
                try {
                    Thread.sleep(1000/40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    sendPusher();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class Receiver extends Thread{
        @Override
        public void run() {
            while(!socket.isClosed()){
                try {
                    receiveModel();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
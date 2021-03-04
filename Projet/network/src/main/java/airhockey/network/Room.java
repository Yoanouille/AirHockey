package airhockey.network;

import airhockey.model.Model;
import airhockey.model.Pusher;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Room {
    private String id;
    private DatagramSocket serverSocket;
    private ArrayList<Integer> clientPorts;
    private ArrayList<InetAddress> clientAddresses;
    private Model model;

    public Room(DatagramSocket serverSocket, String id) throws SocketException {
        this.serverSocket = serverSocket;
        this.id = id;
        this.clientPorts = new ArrayList<Integer>();
        this.clientAddresses = new ArrayList<InetAddress>();
        model = new Model();
    }

    public String getId(){
        return id;
    }

    public void join(int port, InetAddress address) throws IOException {
        if(clientPorts.size() == 2){
            byte[] buf = "fullRoom".getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            serverSocket.send(packet);
        }
        clientPorts.add(port);
        clientAddresses.add(address);
        if(clientPorts.size() == 2){
            byte[] buf = "start".getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, clientAddresses.get(0), clientPorts.get(0));
            serverSocket.send(packet);
            new Sender().start();
        }
    }

    public void receive(ObjectInputStream ois, int port, InetAddress address) throws IOException, ClassNotFoundException {
        Pusher p = (Pusher)ois.readObject();
        ois.close();
        int iClient = (port == clientPorts.get(0) && address.equals(clientAddresses.get(0))) ? 1 : 0;
        model.getPushers()[1-iClient] = p;
    }

    public void sendPaletAndPushers() throws IOException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(model.getPushers());
        oo.writeObject(model.getPalet());
        oo.close();
        byte[] objectSerialized = bStream.toByteArray();
        int port = clientPorts.get(0);
        InetAddress address = clientAddresses.get(0);
        DatagramPacket packet = new DatagramPacket(objectSerialized, objectSerialized.length, address, port);
        serverSocket.send(packet);

        int port2 = clientPorts.get(1);
        InetAddress address2 = clientAddresses.get(1);
        DatagramPacket packet2 = new DatagramPacket(objectSerialized, objectSerialized.length, address2, port2);
        serverSocket.send(packet2);
    }

    public class Sender extends Thread {
        @Override
        public void run() {
            double lastT = System.nanoTime();
            double t;
            while(true) {
                t = System.nanoTime();
                double dt = (t-lastT)/(1e9*1.0);
                model.update(dt);
                lastT = t;

                try {
                    Thread.sleep(1000/40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    sendPaletAndPushers();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
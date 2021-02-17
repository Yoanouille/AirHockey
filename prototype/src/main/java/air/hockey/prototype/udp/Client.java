package air.hockey.prototype.udp;

import air.hockey.prototype.javafx.ProtoPhysicJavaFx;
import air.hockey.prototype.model.Model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Client {
    public final int SERVER_PORT = 6666;
    private ProtoPhysicJavaFx game;
    public Client(String[]args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        game = new ProtoPhysicJavaFx();
        //TODO ENVOYER AU SERVEUR QUE TU VEUX TE CONNECTER ET ATTENTRE SA REPONSE POUR LANCER LES 3 THREAD DESSOUS
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String res = new String(buf);
        System.out.println(buf);
        //new Sender(socket).start();
        //new Receiver(socket).start();
        //ProtoPhysicJavaFx.launch(ProtoPhysicJavaFx.class, args);
    }

    public static void main(String[] args) throws IOException {
        new Client(args);
    }

    public class Sender extends Thread {
        private DatagramSocket socket;
        public Sender(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while (true) {
                Model model = game.getModel();
                if(model.hasPusherMoved()) {
                    //TODO ENVOYER AU SERVEUR LA NOUVELLE POSITION DU PUSHER
                }
            }
        }
    }

    public class Receiver extends Thread {
        private DatagramSocket socket;
        public Receiver(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while(true) {
                //TODO RECOIT LA VRAIE POSITION DU PALET ET L'ACTUALISE, PEUT RECEVOIR LA POSITION DU PUSHER
            }
        }
    }

}
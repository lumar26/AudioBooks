package rs.ac.bg.fon.mmklab.peer.service.server_communication;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class ServerCommunicator {

    private static ServerCommunicator instance;

    private final Socket communicationSocket;
    private final PrintStream streamToServer;
    private final BufferedReader streamFromServer;

    //  ----------------------------------------------------------------------------------------------------------------
//  posto za jednog peer-a hocemo da postoji samo jedan jedini ServerCommunicator iskoristicemo Singleton pattern
//    da bismo obezbedili da se radi sa jednom istom instancom
    private ServerCommunicator(InetAddress serverAddress, int serverPort) throws IOException {

        this.communicationSocket = new Socket(serverAddress, serverPort); // u ovom trenutku je odradjena accept metoda na serverskoj strani i pokrenuo se hendler, ako ne baci izuzetak
        this.streamToServer = new PrintStream(communicationSocket.getOutputStream());
        this.streamFromServer = new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));
    }

    public static ServerCommunicator getInstance(InetAddress serverAddress, int serverPort) throws IOException {
        if (instance == null) return new ServerCommunicator(serverAddress, serverPort);
        else return instance;
    }

    public Socket getCommunicationSocket() {
        return communicationSocket;
    }

    public PrintStream getStreamToServer() {
        return streamToServer;
    }

    public BufferedReader getStreamFromServer() {
        return streamFromServer;
    }
}

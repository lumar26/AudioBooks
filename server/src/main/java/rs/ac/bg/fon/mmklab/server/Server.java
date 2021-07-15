package rs.ac.bg.fon.mmklab.server;

import rs.ac.bg.fon.mmklab.book.AudioBook;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    /*Server od polja treba da ima listu dostupnih knjiga*/
    private static List<AudioBook> availableBooks = new ArrayList<>();
    /*treba nam port za rs.ac.bg.fon.mmklab.app.server da osluskuje*/

    static final int port = 8000;
    /*treba nam lista aktivnih niti koje hendluju klijente*/
    private static List<ClientHandler> handlers = new ArrayList<>();

    public static void main(String[] args) {
        //    port = Integer.parseInt(args[0]); // ako resimo da pokrecemo rs.ac.bg.fon.mmklab.app.server iz komandne linije

        ServerSocket receiveSocket;
        Socket communicationSocket;

        try {
            receiveSocket = new ServerSocket(port);
            System.out.println("SERVER: STARTED");

            while (true) {
                System.out.println("SERVER: awaiting connection");

                communicationSocket = receiveSocket.accept();
                System.out.println("SERVER: connection established");
                ClientHandler handler = new ClientHandler(communicationSocket);
                handlers.add(handler);
                handler.start();
                System.out.println("SERVER: connection activated, number of connections: " + handlers.size());

            }
        } catch (IOException e) {
            System.err.println("ERROR: Could not open receive socket on given port");
        }
    }
}

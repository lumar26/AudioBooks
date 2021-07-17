package rs.ac.bg.fon.mmklab.peer.service.server_communication;

import rs.ac.bg.fon.mmklab.book.AudioBook;
import rs.ac.bg.fon.mmklab.peer.service.util.ServerFinder;
import rs.ac.bg.fon.mmklab.util.JsonConverter;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerCommunicator {

    private static ServerCommunicator instance;

    private final Socket communicationSocket;
    private final ObjectOutputStream streamToServer;
    private final ObjectInputStream streamFromServer;

    private ServerCommunicator() throws IOException {
        this.communicationSocket = new Socket(ServerFinder.getServerAddress(), ServerFinder.getServerPort()); // ne znam sto ne javlja gresku kad getServerAddres baca izuzetak...
        this.streamToServer = new ObjectOutputStream(communicationSocket.getOutputStream());
        this.streamFromServer = new ObjectInputStream(communicationSocket.getInputStream());
    }

    public static ServerCommunicator getInstance() throws IOException {
        if (instance == null) return new ServerCommunicator();
        else return instance;
    }

    public void sendAvailableBooks(List<AudioBook> listOfBooks) throws IOException {

//        System.out.println("(initialConnectToServer): Lista knjiga koja se salje serveru ima sledeci oblik: " + JsonConverter.bookListToJSON(listOfBooks));

        streamToServer.writeObject(listOfBooks); // da li moze cela lista odjednom da se pise u tok pitanje je....
        streamToServer.write(0); // ovde neka se pise neki signal da se doslo do kraja liste
    }


    public List<AudioBook> getAvailableBooks() {
        List<AudioBook> result = null;
        while(true) {
            try {
                if (streamFromServer.readInt() == 0) break;
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println("Nije procitan int signal");
            }
            try {
               result = (List<AudioBook>) streamFromServer.readObject();
            } catch (IOException e) {
                System.err.println("Greska (getAvailableBooks): nije uspelo citanje iz toka od servera");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}

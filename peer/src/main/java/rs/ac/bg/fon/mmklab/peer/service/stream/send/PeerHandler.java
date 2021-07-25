package rs.ac.bg.fon.mmklab.peer.service.stream.send;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import rs.ac.bg.fon.mmklab.book.BookInfo;
import rs.ac.bg.fon.mmklab.util.JsonConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class PeerHandler extends Service {

    private Socket socket;
    private PrintStream toReceiver;
    private BufferedReader fromReceiver;

    public PeerHandler(Socket socket) throws IOException {
        this.socket = socket;
        toReceiver = new PrintStream(socket.getOutputStream());
        fromReceiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                establishConnection();
                return null;
            }
        };
    }

    public void establishConnection() {
        try {
            String req = fromReceiver.readLine();
            if (req.equals("Available for streaming?")){
                toReceiver.println("Yes, which book?");
                String jsonBookInfo = fromReceiver.readLine();
                BookInfo bookForStreaming = JsonConverter.jsonToBookInfo(jsonBookInfo);
                System.out.println("Uspesan prijem informacije o knjizi koju slusalac zeli: " + bookForStreaming.toString());
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Greska (PeerHandler -> establishConnection): problemi sa ulaznim tokom, nisu primljene ocekivane poruke");
        }
    }

    //    ili je mozda bolje da ne implementira interfejs kako bi bila privatna

}

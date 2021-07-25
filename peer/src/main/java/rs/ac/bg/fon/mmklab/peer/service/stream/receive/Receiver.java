package rs.ac.bg.fon.mmklab.peer.service.stream.receive;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import rs.ac.bg.fon.mmklab.book.AudioBook;
import rs.ac.bg.fon.mmklab.book.BookInfo;
import rs.ac.bg.fon.mmklab.book.BookOwner;
import rs.ac.bg.fon.mmklab.util.JsonConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Receiver extends Service<AudioBook> {
//    nit slusaoca krece sa radom onog trenutka kad korisnik odabere knjigu koju hoce da slusa

    private BookOwner bookOwner;
    private BookInfo bookInfo;

    /*deklaracija soketa i tokova koji sluze za uspostavljanje veze sa posiljaocem*/
    private Socket socket;
    private PrintStream toSender;
    private BufferedReader fromSender;

    /*deklaracija soketa i tokova koji sluze za prijem strimovanog audio sadrzaja*/
// ...

    public Receiver(AudioBook audioBook) throws IOException {
        this.bookOwner = audioBook.getBookOwner();
        this.bookInfo = audioBook.getBookInfo();
    }



    private void establishConnection() {
        System.out.println("(establisConnection)");
        try {
            this.socket = new Socket(bookOwner.getIpAddress(), bookOwner.getPort()); // u ovom trenutku je uspostavljena veza
            toSender = new PrintStream(socket.getOutputStream());
            fromSender = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("(establisConnection): Odradjena inicijalizacija tokova");
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Greska (Receiver -> establishConnection): nije doslo do otvaranja soketa ka posiljaocu");
        }

        toSender.println("Available for streaming?");
        System.out.println("1.---------- Poslali smo poruku posiljaocu");
        try {
            String res = fromSender.readLine();
            if (res.equals("Yes, which book?")){
                System.out.println("Receiver: Sender confirmed");
                toSender.println(JsonConverter.bookInfoToJson(bookInfo));
                System.out.println("Poslata knjiga posiljaocu: " + JsonConverter.bookInfoToJson(bookInfo));
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Greska (Receiver -> establishConnection): sender nije poslao poruku na nas odgovor");

        }
    }


    @Override
    protected Task<AudioBook> createTask() {
        return new Task<>() {
            @Override
            protected AudioBook call() throws Exception {
                establishConnection();
                return null;
            }
        };
    }
}

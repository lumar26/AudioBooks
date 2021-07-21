package rs.ac.bg.fon.mmklab.peer.ui.components.request_books;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import rs.ac.bg.fon.mmklab.book.AudioBook;
import rs.ac.bg.fon.mmklab.communication.peer_to_server.ListExchanger;
import rs.ac.bg.fon.mmklab.peer.domain.Configuration;
import rs.ac.bg.fon.mmklab.peer.service.server_communication.ServerCommunicator;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class RequestBooksTab {
    private static Configuration configuration;

    public static void updaTeConfiguration(Configuration newConfiguration) {
        configuration = newConfiguration;
    }

    public static void display(TabPane root) {
        BorderPane requestBooksTabContent = new BorderPane();
        Button sendRequestBtn = new Button("Get available books");
        VBox availableBooks = new VBox(5);
        sendRequestBtn.setOnAction(action -> showAvailableBooks(availableBooks));
        requestBooksTabContent.setTop(sendRequestBtn);
        requestBooksTabContent.setCenter(availableBooks);


        Tab requestBooksTab = new Tab();
        requestBooksTab.setText("Available books");
        requestBooksTab.setContent(requestBooksTabContent);
        if (requestBooksTab.isSelected()) {
//            ako je fajl selektovan da odradimo da se automatski azurira lista knjiga
        }

        root.getTabs().addAll(requestBooksTab);

    }

    private static void showAvailableBooks(VBox availableBooks) {
        List<AudioBook> list = null;
        if (configuration == null) {
            System.err.println("Korisnik jos uvek nije odradio nikakvu konfiguraciju pa je nemoguce povuci listu knjiga sa servera");
            return;
        }
        try {
            ServerCommunicator communicator = ServerCommunicator
                    .getInstance(InetAddress.getByName(configuration.getServerName()), configuration.getServerPort());
            list = ListExchanger.getAvailableBooks(communicator.getStreamFromServer(), communicator.getStreamToServer());
            System.out.println();
            System.out.println(">>>> Dobili smo listu knjiga <<<<<<<<");
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Greska: nepoznat server");
        }
        if (list != null) {
            System.out.println();
            System.out.println(">>>> Lista nije null <<<<<<<<");
            list.forEach(book -> {
                Button bookBtn = new Button(book.getBookInfo().getAuthor() + " - " + book.getBookInfo().getTitle());
                availableBooks.getChildren().add(bookBtn);
                bookBtn.setPrefWidth(500);
                bookBtn.setOnAction(e -> System.out.println("Ovde dodajemo metodu iz klase Receiver koja ostvaruje dalje komunikaciju sa peer-om koji poseduje tu knjigu"));
            });
        } else
            System.err.println("Greska (RequestBooksTab -> showAvailableBooks): Lista nije popunjena, ostala je null");
    }


}
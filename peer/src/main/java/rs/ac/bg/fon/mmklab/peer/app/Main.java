package rs.ac.bg.fon.mmklab.peer.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rs.ac.bg.fon.mmklab.peer.service.server_communication.ServerCommunicator;
import rs.ac.bg.fon.mmklab.peer.service.util.BooksFinder;
import rs.ac.bg.fon.mmklab.book.AudioBook;
import rs.ac.bg.fon.mmklab.util.JsonConverter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    String pathToFolder = "/home/lumar26/Public/AudioBooks", audioExtension = ".wav";

    InetAddress serverAddress;

    {
        try {
            serverAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
//            e.printStackTrace();
            System.err.println("Greska (MAIN): ne postoji localhost" );
        }
    }
    int serverPort = 5005;

    @Override
    public void init() throws Exception {
//        slanje liste knjiga serveru

        List<AudioBook> listOfBooks = BooksFinder.fetchBooks(pathToFolder, audioExtension);
        String booksArray = JsonConverter.bookListToJSON(listOfBooks);
        System.out.println(booksArray);
        System.out.println("Da li je Json validan: " + JsonConverter.isValidListOfBooks(booksArray));
        List<AudioBook> newList = JsonConverter.jsonToBookList(booksArray);
        newList.forEach(book -> book.toString());
//        ServerCommunicator.getInstance().sendAvailableBooks(listOfBooks);
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane requestBooksTabContent = new BorderPane();
        Button sendRequestBtn = new Button("Get available books");
        TextArea listOfBooks = new TextArea();
//        event handler nam treba za ovo dugme
        sendRequestBtn.setOnAction(action -> showAvailableBooks(listOfBooks));
        requestBooksTabContent.setTop(sendRequestBtn);
        requestBooksTabContent.setCenter(listOfBooks);


        TabPane root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); //korisnik nema mogucnost da zatvori tab

        Tab requestBooksTab = new Tab();
        requestBooksTab.setText("Get available books list");
        requestBooksTab.setContent(requestBooksTabContent);
        if (requestBooksTab.isSelected()){
//            ako je fajl selektovan da odradimo da se automatski azurira lista knjiga
        }

        Tab audioPlayerTab = new Tab();
        audioPlayerTab.setText("Audio player");

//        dodavanje tabova na TabPane
        root.getTabs().addAll(requestBooksTab, audioPlayerTab);



        primaryStage.setTitle("Audio Books");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();


    }

    private void showAvailableBooks(TextArea listOfBooks) {
        try {
            List<AudioBook>  list = ServerCommunicator.getInstance().getAvailableBooks();
            list.forEach(book -> listOfBooks.appendText(book.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*List<AudioBook>  list = BooksFinder.fetchBooks(pathToFolder, audioExtension);
        list.forEach(book -> listOfBooks.appendText(book.toString()));*/  // radi sve ok, lepo se prikazuje, samo treba da se zameni nacin na koji se prikazuje
    }

    @Override
    public void stop() throws Exception {
//        Ovde cemo da implementiramo komunikaciju sa najpre peer-om ukoliko je prestanak rada aplikacije nasilan, tj pre završetka strimovanja
//        Nakon toga komunikacija sa serverom u smislu odjavljivanja sa servera
    }
}

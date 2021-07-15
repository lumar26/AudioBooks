package rs.ac.bg.fon.mmklab.peer.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rs.ac.bg.fon.mmklab.peer.service.server_communication.ServerCommunicator;
import rs.ac.bg.fon.mmklab.peer.service.service_utils.BooksFinder;
import rs.ac.bg.fon.mmklab.book.AudioBook;

import java.net.InetAddress;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
//        slanje liste knjiga serveru
        String pathToFolder = "/home/lumar26/Public/AudioBooks", audioExtension = ".wav";

        InetAddress serverAddress = InetAddress.getByName("localhost");
        int serverPort = 5005;


        List<AudioBook> listOfBooks = BooksFinder.fetchBooks(pathToFolder, audioExtension);
//        System.out.println(listOfBooks);
//        listOfBooks.forEach(book -> book.toString());
        ServerCommunicator.initialConnectToServer(serverAddress, serverPort, listOfBooks);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Audio Books");
        BorderPane root = new BorderPane();



        Button btnPlay = new Button("Play");


        Button btnStop = new Button("Stop reproduction");
        btnStop.setOnAction(e -> {
//            stopAudio();
        });
        root.setCenter(btnPlay);
        root.setBottom(btnStop);


        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();


    }
}

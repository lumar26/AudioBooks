package rs.ac.bg.fon.mmklab.peer.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rs.ac.bg.fon.mmklab.communication.peer_to_server.ListExchanger;
import rs.ac.bg.fon.mmklab.peer.domain.Configuration;
import rs.ac.bg.fon.mmklab.peer.service.server_communication.ServerCommunicator;
import rs.ac.bg.fon.mmklab.peer.service.util.BooksFinder;
import rs.ac.bg.fon.mmklab.book.AudioBook;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
/*
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
    int serverPort = 5005;*/

    private Configuration configuration;

    @Override
    public void start(Stage primaryStage) {

        BorderPane requestBooksTabContent = new BorderPane();
        Button sendRequestBtn = new Button("Get available books");
        TextArea listOfBooksArea = new TextArea();
        sendRequestBtn.setOnAction(action -> showAvailableBooks(listOfBooksArea));
        requestBooksTabContent.setTop(sendRequestBtn);
        requestBooksTabContent.setCenter(listOfBooksArea);


        TabPane root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); //korisnik nema mogucnost da zatvori tab

        Tab requestBooksTab = new Tab();
        requestBooksTab.setText("Get available books list");
        requestBooksTab.setContent(requestBooksTabContent);
        if (requestBooksTab.isSelected()) {
//            ako je fajl selektovan da odradimo da se automatski azurira lista knjiga
        }

        Tab audioPlayerTab = new Tab();
        audioPlayerTab.setText("Audio player");

//        tab za unos putanje ka folderu knjiga

        Tab configTab = new Tab();
        configTab.setText("Enter configurations");

        Label serverName = new Label("Unesite ime domena servera: ");
        TextField serverNameTxt = new TextField();
        Label serverPort = new Label("Unesite broj porta servera: ");
        TextField serverPortTxt = new TextField();
        Label audioExtension = new Label("Unesite ekstenziju audio fajla (default = \".wav\"): ");
        TextField audioExtensionTxt = new TextField();
        Label pathToFolder = new Label("Unesite Putanju do fascikle gde se nalaze audio knjige: ");
        TextField pathToFolderTxt = new TextField();

//svaka stavka za unos ce biti poseban horizontal box koji sadrzi po labelu i poklje za unos

        VBox labels = new VBox(25);
        labels.getChildren().addAll(serverName, serverPort, audioExtension, pathToFolder);
        VBox textFields = new VBox(15);
        textFields.getChildren().addAll(serverNameTxt, serverPortTxt, audioExtensionTxt, pathToFolderTxt);

//        Submit dugme
        Button submitBtn = new Button("Potvrdi");
        submitBtn.setOnAction(a -> {
            configuration = configurationFactory(serverNameTxt, serverPortTxt, audioExtensionTxt, pathToFolderTxt);
//            onog trenutka kad popunimo konfiguracije svakako cemo da saljemo serveru sve
            sendListOfBooks(configuration);
            clearInputContent(textFields);
        });



        HBox configLayout = new HBox();
        configLayout.getChildren().addAll(labels, textFields, submitBtn);
        configTab.setContent(configLayout);

//        dodavanje tabova na TabPane
        root.getTabs().addAll(configTab, requestBooksTab, audioPlayerTab);

        primaryStage.setTitle("Audio Books");
        primaryStage.setWidth(700);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();


    }

    private void sendListOfBooks(Configuration configuration){
        //            onog trenutka kad popunimo konfiguracije svakako cemo da saljemo serveru sve
        try {
            if(configuration == null || !configuration.isComplete()){
//                    korisnik nije lepo uneo konfiguraciju
                System.err.println("Vrati korisnika na prvi tab da lepo unese konfiguraciju i naznaci mu sta je zeznuo");
            }

            ServerCommunicator communicator = ServerCommunicator.getInstance(InetAddress.getByName(configuration.getServerName()), configuration.getServerPort());

            ListExchanger.sendAvailableBooks(
                    BooksFinder.fetchBooks(configuration.getPathToBookFolder(), configuration.getAudioExtension()),
                    communicator.getStreamToServer(),
                    communicator.getStreamFromServer());
        } catch (IOException e) {
//                e.printStackTrace();
            System.err.println("Greska(): ili je nepostojeca adresa servera prosledjena, ili los port, ili ");
        }

    }

    private void clearInputContent(VBox textFields){
        textFields.getChildren().forEach(field -> ((TextField) field).setText("")); // ovo je mozda rizicno ali ovde znamo da su tu sigurno samo ta polja za unos teksta
    }

    private void showAvailableBooks(TextArea listOfBooks) {
        if(configuration == null){
            System.err.println("Korisnik jos uvek nije odradio nikakvu konfiguraciju pa je nemoguce povuci listu knjiga sa servera");
            return;
        }
        try {
            ServerCommunicator communicator = ServerCommunicator
                    .getInstance(InetAddress.getByName(configuration.getServerName()), configuration.getServerPort());
            List<AudioBook> list = ListExchanger.getAvailableBooks(communicator.getStreamFromServer(), communicator.getStreamToServer());
            list.forEach(book -> listOfBooks.appendText(book.getBookInfo().toString()));
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Greska: nepoznat server");
        }
//        ovo sluzi za probu dok se ne implementira server
        /*List<AudioBook>  list = BooksFinder.fetchBooks(pathToFolder, audioExtension);
        list.forEach(book -> listOfBooks.appendText(book.getBookInfo().toString()));*/  // radi sve ok, lepo se prikazuje, samo treba da se zameni nacin na koji se prikazuje
    }

    private static Configuration configurationFactory(TextField serverNameTxt, TextField serverPortTxt, TextField audioExtensionTxt, TextField pathToFolderTxt) {
        String rawServerName = serverNameTxt.getText();
        String rawServerPort = serverPortTxt.getText();
        String rawAudioExtension = audioExtensionTxt.getText();
        String rawPathToFolder = pathToFolderTxt.getText();

//        ovde bi trebali da rade neki regularni izrazi malo da se proveri da li se unose okej stvari

        return new Configuration(rawServerName.trim(), Integer.parseInt(rawServerPort.trim()), rawAudioExtension.trim(), rawPathToFolder.trim());
    }

    @Override
    public void stop() throws Exception {
//        Ovde cemo da implementiramo komunikaciju sa najpre peer-om ukoliko je prestanak rada aplikacije nasilan, tj pre završetka strimovanja
//        Nakon toga komunikacija sa serverom u smislu odjavljivanja sa servera
    }
}

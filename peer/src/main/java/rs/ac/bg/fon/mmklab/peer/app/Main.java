package rs.ac.bg.fon.mmklab.peer.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rs.ac.bg.fon.mmklab.communication.peer_to_server.ListExchanger;
import rs.ac.bg.fon.mmklab.peer.app.components.request_books.RequestBooksTab;
import rs.ac.bg.fon.mmklab.peer.domain.Configuration;
import rs.ac.bg.fon.mmklab.peer.service.server_communication.ServerCommunicator;
import rs.ac.bg.fon.mmklab.peer.service.util.BooksFinder;

import java.io.IOException;
import java.net.InetAddress;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
/*
    String pathToFolder = "/home/lumar26/Public/AudioBooks", audioExtension = ".wav";
*/

    private Configuration configuration;

    @Override
    public void start(Stage primaryStage) {



        TabPane root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); //korisnik nema mogucnost da zatvori tab




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
            RequestBooksTab.updaTeConfiguration(configuration); // svaki put kad dodje do promene u knfiguraciji ona mora da se apdejtuje
//            onog trenutka kad popunimo konfiguracije svakako cemo da saljemo serveru sve
            sendListOfBooks(configuration);
            clearInputContent(textFields);
        });



        HBox configLayout = new HBox();
        configLayout.getChildren().addAll(labels, textFields, submitBtn);
        configTab.setContent(configLayout);

//        dodavanje tabova na TabPane
//        root.getTabs().addAll(configTab, requestBooksTab, audioPlayerTab);
        RequestBooksTab.display(root);
        root.getTabs().addAll(configTab, audioPlayerTab);


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

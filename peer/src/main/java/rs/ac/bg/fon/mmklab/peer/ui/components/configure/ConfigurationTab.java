package rs.ac.bg.fon.mmklab.peer.ui.components.configure;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rs.ac.bg.fon.mmklab.communication.peer_to_server.ListExchanger;
import rs.ac.bg.fon.mmklab.peer.domain.Configuration;
import rs.ac.bg.fon.mmklab.peer.service.config_service.ConfigurationService;
import rs.ac.bg.fon.mmklab.peer.service.server_communication.ServerCommunicator;
import rs.ac.bg.fon.mmklab.peer.service.stream.send.Sender;
import rs.ac.bg.fon.mmklab.peer.service.util.BooksFinder;
import rs.ac.bg.fon.mmklab.peer.ui.components.request_books.RequestBooksTab;

import java.io.IOException;
import java.net.InetAddress;

public class ConfigurationTab {

    private static Configuration configuration;

    public boolean IsAllNumber(String str) {
        char ch;
        boolean usp = true;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                usp = true;
            } else return false;

            return usp;
        }
        return usp;
    }
    public static BorderPane  display(){
        BorderPane page= new BorderPane();
        page.setPadding(new Insets(10,50,50,50));

        HBox hb=new HBox();
        hb.setPadding(new Insets(20,30,20,30));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label localPortTCP = new Label("Unesite lokalni broj porta za tcp vezu:");
        TextField localPortTCPTxt = new TextField();
        Label localPortUDP = new Label("Unesite lokalni broj porta za udp vezu:");
        TextField localPortUDPTxt = new TextField();
        Label pathToFolder = new Label("Unesite putanju do fascikle gde se nalaze audio knjige: ");
        final TextField pathToFolderTxt = new TextField();
        pathToFolderTxt.setText("/home/lumar26/Public/AudioBooks");
        Button submitBtn = new Button("Potvrdi");

        //design labela i txtbox
        pathToFolder.setStyle("-fx-font-family:'Courier New'; -fx-font-size: 12px;-fx-font-weight: BOLD; ");
        localPortTCP.setStyle("-fx-font-family:'Courier New'; -fx-font-size: 12px;-fx-font-weight: BOLD; ");
        localPortUDP.setStyle("-fx-font-family:'Courier New'; -fx-font-size: 12px;-fx-font-weight: BOLD; ");
        pathToFolderTxt.setStyle("-fx-font-family:'Courier New'; -fx-font-size: 11px;");
       localPortTCPTxt.setStyle("-fx-font-family:'Courier New'; -fx-font-size: 11px;");
        localPortUDPTxt.setStyle("-fx-font-family:'Courier New'; -fx-font-size: 11px;");

        //Dodavanje komponenta na grid pane
        gridPane.add(localPortTCP,2,0);
        gridPane.add(localPortTCPTxt,2,1);
        gridPane.add(localPortUDP,2,2);
        gridPane.add(localPortUDPTxt,2,3);
        gridPane.add(pathToFolder,2,4);
        gridPane.add(pathToFolderTxt,2,5);
        gridPane.add(submitBtn,2,7);

        //Dodavanje naslova
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2);
        dropShadow.setOffsetY(2);
        Text naslov = new Text("AUDIO BOOKS");
        naslov.setFont(Font.font("Courier New", FontWeight.BOLD, 33));
        naslov.setEffect(dropShadow);

        //Stilizacija dugmeta i dodavanje funkcionalnosti
        submitBtn.setStyle("-fx-background-color: linear-gradient(lightgrey, gray ); -fx-text-fill:BLACK;-fx-font-weight: BOLD ");
        DropShadow shadow = new DropShadow();

        submitBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                submitBtn.setEffect(shadow);
            }
        });

        submitBtn.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                submitBtn.setEffect(null);
            }
        });

        final Label lblMessage = new Label();

        submitBtn.setOnAction(new EventHandler()  {
            @Override
            public void handle(Event event) {
                String tcp = localPortTCPTxt.getText();
                String udp = localPortUDPTxt.getText();
                String path = pathToFolderTxt.getText();
                ConfigurationTab pom=new ConfigurationTab();
                if(tcp.equals("") || tcp.equals(" ") || !pom.IsAllNumber(tcp) ||
                        udp.equals("") ||  udp.equals(" ") || !pom.IsAllNumber(udp) ||
                        path.equals("") || path.equals(" ")){

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Neuspešno");
                    alert.setHeaderText("NEISPRAVAN UNOS");
                    alert.setContentText("Proverite da li su sva polja ispravno uneta.");
                    alert.showAndWait();


                }
                else{
                    configuration = configurationFactory(localPortTCPTxt, localPortUDPTxt, pathToFolderTxt);
                    RequestBooksTab.updaTeConfiguration(configuration); // svaki put kad dodje do promene u knfiguraciji ona mora da se apdejtuje
//            onog trenutka kad popunimo konfiguracije svakako cemo da saljemo serveru sve
                    sendListOfBooks(configuration);

//            (new Sender(configuration)).run(); //kad smo definisali konfiguraciju i poslali listu knjiga koju nudimo tada ocekujemo poziv od primaoca
                    (new Sender(configuration)).start();

                    //Otvaranje novog prozora
                    BorderPane root2 = new BorderPane();
                    Label label = new Label("Your are now in the second form");
                    root2.getChildren().add(label);


                    /*Scene secondScene = new Scene(root2, 500,500);
                    Stage secondStage = new Stage();
                    secondStage.setScene(secondScene); // set the scene
                    secondStage.setTitle("Second Form");
                    secondStage.show();
                    //primaryStage.close();*/

                    RequestBooksTab.display();

                }
            }});




        //final
        hb.getChildren().add(naslov);
        BorderPane pomTop=new BorderPane();
        pomTop.setCenter(naslov);
        page.setTop(pomTop);
        page.setCenter(gridPane);
        return page;
    }


/*
    public static void display(TabPane root) {
        Tab configTab = new Tab();
        configTab.setText("Enter configurations");
        TabDesign design=new TabDesign();
        design.configureTab(configTab,"Enter configurations");

        Label localPortTCP = new Label("Unesite lokalni broj porta za tcp vezu");
        TextField localPortTCPTxt = new TextField();
        Label localPortUDP = new Label("Unesite lokalni broj porta za udp vezu");
        TextField localPortUDPTxt = new TextField();
        Label pathToFolder = new Label("Unesite putanju do fascikle gde se nalaze audio knjige: ");
        TextField pathToFolderTxt = new TextField();
        pathToFolderTxt.setText("/home/lumar26/Public/AudioBooks");

//svaka stavka za unos ce biti poseban horizontal box koji sadrzi po labelu i polje za unos

        VBox labels = new VBox(25);
        labels.getChildren().addAll(localPortTCP, localPortUDP, pathToFolder);
        VBox textFields = new VBox(15);
        textFields.getChildren().addAll(localPortTCPTxt, localPortUDPTxt, pathToFolderTxt);

//        Submit dugme
        Button submitBtn = new Button("Potvrdi");

        submitBtn.setStyle("-fx-background-color: Gray; -fx-text-fill: WhiteSmoke ");
        DropShadow shadow = new DropShadow();

        submitBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                submitBtn.setEffect(shadow);
            }
        });

        submitBtn.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                submitBtn.setEffect(null);
            }
        });

        submitBtn.setOnAction(a -> {
            configuration = configurationFactory(localPortTCPTxt, localPortUDPTxt, pathToFolderTxt);
            RequestBooksTab.updaTeConfiguration(configuration); // svaki put kad dodje do promene u knfiguraciji ona mora da se apdejtuje
//            onog trenutka kad popunimo konfiguracije svakako cemo da saljemo serveru sve
            sendListOfBooks(configuration);
            clearInputContent(textFields);
//            (new Sender(configuration)).run(); //kad smo definisali konfiguraciju i poslali listu knjiga koju nudimo tada ocekujemo poziv od primaoca
            (new Sender(configuration)).start();
        });


        HBox configLayout = new HBox();
        configLayout.setPadding(new Insets(80,30,0,30));
        configLayout.getChildren().addAll(labels, textFields);

//        configLayout.getChildren().addAll(labels, textFields);
//        configLayout.getChildren().addAll(labels, textFields, submitBtn);
//        configTab.setContent(configLayout);

        BorderPane configpage=new BorderPane();
        configpage.setCenter(configLayout);
        BorderPane configpage1=new BorderPane();
        configpage1.setRight(submitBtn);
        configpage.setBottom(configpage1);

        configpage.setPadding(new Insets(0,50,350,50));

        configTab.setContent(configpage);

//        dodavanje korenom elementu
        root.getTabs().add(configTab);
    }
*/
    private static Configuration configurationFactory(TextField localPortTCPTxt, TextField localPortUDPTxt, TextField pathToFolderTxt) {
        return ConfigurationService.getConfiguration(localPortTCPTxt.getText(), localPortUDPTxt.getText(), pathToFolderTxt.getText());
    }

    private static void sendListOfBooks(Configuration configuration) {
        //            onog trenutka kad popunimo konfiguracije svakako cemo da saljemo serveru sve
        try {
            if (configuration == null) {
//                    korisnik nije lepo uneo konfiguraciju
                System.err.println("Vrati korisnika na prvi tab da lepo unese konfiguraciju i naznaci mu sta je zeznuo");
                return;
            }

            ServerCommunicator communicator = ServerCommunicator.getInstance(InetAddress.getByName(configuration.getServerName()), configuration.getServerPort());

            ListExchanger.sendAvailableBooks(
                    BooksFinder.fetchBooks(configuration),
                    communicator.getStreamToServer(),
                    communicator.getStreamFromServer());
        } catch (IOException e) {
//                e.printStackTrace();
            System.err.println("Greska(): ili je nepostojeca adresa servera prosledjena, ili los port, ili ");
        }
    }

   /* private static void clearInputContent(VBox textFields) {
        textFields.getChildren().forEach(field -> ((TextField) field).setText(""); // ovo je mozda rizicno ali ovde znamo da su tu sigurno samo ta polja za unos teksta
    }*/





}





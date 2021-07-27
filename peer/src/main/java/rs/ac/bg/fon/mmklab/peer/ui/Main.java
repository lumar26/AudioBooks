package rs.ac.bg.fon.mmklab.peer.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import rs.ac.bg.fon.mmklab.peer.ui.components.audio_player.AudioPlayerTab;
import rs.ac.bg.fon.mmklab.peer.ui.components.configure.ConfigurationTab;
import rs.ac.bg.fon.mmklab.peer.ui.components.request_books.RequestBooksTab;
import rs.ac.bg.fon.mmklab.peer.domain.Configuration;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
/*
    String pathToFolder = "
    /home/lumar26/Public/AudioBooks
    ", audioExtension = ".wav";
*/

    private Configuration configuration;

    @Override
    public void start(Stage primaryStage) {

        TabPane root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); //korisnik nema mogucnost da sam zatvara tabove



//        dodavanje tabova na TabPane
        ConfigurationTab.display(root);
        RequestBooksTab.display(root);
        AudioPlayerTab.display(root);

        primaryStage.setTitle("Audio Books");
        primaryStage.setWidth(700);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
//        Ovde cemo da implementiramo komunikaciju sa najpre peer-om ukoliko je prestanak rada aplikacije nasilan, tj pre završetka strimovanja
//        Nakon toga komunikacija sa serverom u smislu odjavljivanja sa servera
    }
}

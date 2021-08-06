package rs.ac.bg.fon.mmklab.peer.ui.components.audio_player;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rs.ac.bg.fon.mmklab.peer.service.stream.Signal;
import rs.ac.bg.fon.mmklab.peer.service.stream.receive.Receiver;
import rs.ac.bg.fon.mmklab.peer.service.stream.receive.Signaler;

public class AudioPlayer extends BorderPane {
    private static Receiver receiver;

    public static void setReceiver(Receiver r) {
        receiver = r;
    }

    private static Slider timeSlider;
    private static Label playTime;
    private static Slider volumeSlider;
    private static Flag flag;

    public static void display() {

        flag = Flag.RUNNING;

        Stage primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("Audio Player");

        HBox buttonBar=new HBox();
        HBox mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        buttonBar.setAlignment(Pos.CENTER);

        VBox vbox=new VBox();
        vbox.getChildren().addAll(buttonBar,mediaBar);
        vbox.setPadding(new Insets(15, 10, 10, 10));
        vbox.setSpacing(12);
        vbox.setStyle("-fx-background-color:#76a3aa; -fx-font-family:'Times New Roman'; ");

        final Button playButton = new Button(" >>");
        final Button pauseButton = new Button("  ||  ");
        Label space = new Label("    ");


        playButton.setStyle("-fx-background-color:#76a3aa; -fx-text-fill: Black;-fx-font-weight: BOLD; -fx-font-size: 13; -fx-border-style: solid; -fx-border-radius: 25; ");
        pauseButton.setStyle("-fx-background-color:#76a3aa;-fx-text-fill: Black;-fx-font-weight: BOLD; -fx-font-size: 13; -fx-border-style: solid; -fx-border-radius: 25;");
        buttonBar.getChildren().addAll(playButton,space,pauseButton);


//        ponasanje
        playButton.setOnAction(click -> {
            switch (flag) {
                case PAUSED: {
                    Signaler signaler = new Signaler(Signal.RESUME, receiver);
                    signaler.start();
                    flag = Flag.RUNNING;
                }
                break;
                case RUNNING: {
                    Signaler signaler = new Signaler(Signal.PAUSE, receiver);
                    signaler.start();
                    flag = Flag.PAUSED;

                }
                break;
                default:
                    break;
            }


        });





        Label timeLabel = new Label("Time: ");
        mediaBar.getChildren().add(timeLabel);

        timeSlider = new Slider();
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(40);
        timeSlider.setMaxWidth(Double.MAX_VALUE);
        mediaBar.getChildren().add(timeSlider);



        Label volumeLabel = new Label("Vol: ");
        mediaBar.getChildren().add(volumeLabel);

        volumeSlider = new Slider();
        volumeSlider.setPrefWidth(50);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(30);
        mediaBar.getChildren().add(volumeSlider);

        Scene scene = new Scene(vbox, 450, 110);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }
}

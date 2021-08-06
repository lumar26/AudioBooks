package rs.ac.bg.fon.mmklab.peer.ui.components.audio_player;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
        BorderPane borderPane = new BorderPane();

       /* Image img=new Image("../images/user-profile.png");
        ImageView imageView=new ImageView(img);
        borderPane.setCenter(imageView);
*/

        HBox mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new Insets(135, 10, 10, 10));
        mediaBar.setStyle("-fx-background-color:WhiteSmoke; -fx-font-family:'Baskerville Old Face'; -fx-font-size: 11; ");
        borderPane.setBottom(mediaBar);

        final Button playButton = new Button(">");

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

//        stilizacija
        playButton.setPadding(new Insets(5, 5, 5, 5));
        playButton.setStyle("-fx-background-color: Gray; -fx-text-fill: WhiteSmoke; -fx-border-style: none;");
        mediaBar.getChildren().add(playButton);

        Label space = new Label("    ");
        mediaBar.getChildren().add(space);

        Label timeLabel = new Label("Time: ");
        mediaBar.getChildren().add(timeLabel);

        timeSlider = new Slider();
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(40);
        timeSlider.setMaxWidth(Double.MAX_VALUE);
        mediaBar.getChildren().add(timeSlider);

       /* playTime = new Label();
        playTime.setPrefWidth(110);
        playTime.setMinWidth(50);
        mediaBar.getChildren().add(playTime);*/
        Label space1 = new Label("    ");
        mediaBar.getChildren().add(space1);

        Label volumeLabel = new Label("Vol: ");
        mediaBar.getChildren().add(volumeLabel);

        volumeSlider = new Slider();
        volumeSlider.setPrefWidth(50);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(30);
        mediaBar.getChildren().add(volumeSlider);

        Scene scene = new Scene(borderPane, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }
}

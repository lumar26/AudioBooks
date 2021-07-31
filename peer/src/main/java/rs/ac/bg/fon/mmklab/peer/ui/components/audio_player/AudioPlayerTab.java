package rs.ac.bg.fon.mmklab.peer.ui.components.audio_player;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import rs.ac.bg.fon.mmklab.peer.service.stream.Signal;
import rs.ac.bg.fon.mmklab.peer.service.stream.receive.Receiver;
import rs.ac.bg.fon.mmklab.peer.service.stream.receive.Signaler;

public class AudioPlayerTab {
    private static Receiver receiver;

    public static void setReceiver(Receiver r) {
        receiver = r;
    }

    public static void display(TabPane root){
        Tab audioPlayerTab = new Tab();
        audioPlayerTab.setText("Audio player");

        Button terminateBtn = new Button("Terminate");
        terminateBtn.setOnAction(action -> {
            Signaler signaler = new Signaler(Signal.TERMINATE, receiver);
            signaler.start();
        });

        Button pauseBtn = new Button("Pause");
        pauseBtn.setOnAction(action -> {
            Signaler signaler = new Signaler(Signal.PAUSE, receiver);
            signaler.start();
        });

        Button resumeBtn = new Button("Resume");
        pauseBtn.setOnAction(action -> {
            Signaler signaler = new Signaler(Signal.RESUME, receiver);
            signaler.start();
        });

        HBox buttons = new HBox(16);
        buttons.getChildren().addAll(pauseBtn, resumeBtn, terminateBtn);

        audioPlayerTab.setContent(buttons);

        root.getTabs().add(audioPlayerTab);
    }
}

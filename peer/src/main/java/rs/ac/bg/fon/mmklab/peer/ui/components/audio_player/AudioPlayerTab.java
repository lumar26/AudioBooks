package rs.ac.bg.fon.mmklab.peer.ui.components.audio_player;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class AudioPlayerTab {
    public static void display(TabPane root){
        Tab audioPlayerTab = new Tab();
        audioPlayerTab.setText("Audio player");

        root.getTabs().add(audioPlayerTab);
    }
}

package rs.ac.bg.fon.mmklab.peer.ui.components.design;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;

public class TabDesign {

    public void configureTab(Tab tab, String text) {

        Label label = new Label(text);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-font-size: 17; ");

        BorderPane tabPane = new BorderPane();
        tabPane.setPrefSize(150,30);
        tabPane.setCenter(label);

        tab.setText("");
        tab.setGraphic(tabPane);

    }




}

package ui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StatusBar {
    private Label statusLabel;
    private HBox statusBarContainer;

    public StatusBar() {
        statusLabel = new Label("Ready");
        statusBarContainer = new HBox();
        statusBarContainer.getChildren().add(statusLabel);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public HBox getStatusBarContainer() {
        return statusBarContainer;
    }
}

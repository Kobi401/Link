package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

/**
 * SearchBar class
 */
public class SearchBar {
    private TextField searchField;
    private HBox searchBarContainer;
    private Button backButton;
    private Button forwardButton;
    private Button refreshButton;

    public SearchBar() {
        searchField = new TextField();
        searchField.setPromptText("Enter URL...");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        //SVG icons
        backButton = createIconButton("M10 6L6 12 10 18");
        forwardButton = createIconButton("M14 6L18 12 14 18");
        refreshButton = createIconButton("M19.9381 13C19.979 12.6724 20 12.3387 20 12C20 7.58172 16.4183 4 12 4C9.49942 4 7.26681 5.14727 5.7998 6.94416M4.06189 11C4.02104 11.3276 4 11.6613 4 12C4 16.4183 7.58172 20 12 20C14.3894 20 16.5341 18.9525 18 17.2916M15 17H18V17.2916M5.7998 4V6.94416M5.7998 6.94416V6.99993L8.7998 7M18 20V17.2916");

        searchBarContainer = new HBox(5, backButton, forwardButton, refreshButton, searchField);
        searchBarContainer.setPadding(new Insets(2, 10, 2, 10));
        searchBarContainer.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #ccc; -fx-border-radius: 5;");
    }

    /**
     * Creates a button with an SVG icon.
     *
     * @param svgPathData The SVG path data string.
     * @return The styled button.
     */
    private Button createIconButton(String svgPathData) {
        Button button = new Button();
        SVGPath icon = new SVGPath();
        icon.setContent(svgPathData);
        icon.setFill(Color.DIMGRAY);
        button.setGraphic(icon);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-cursor: hand;");
        return button;
    }

    // Getters for external access
    public TextField getSearchField() {
        return searchField;
    }

    public HBox getSearchBarContainer() {
        return searchBarContainer;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getForwardButton() {
        return forwardButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }
}

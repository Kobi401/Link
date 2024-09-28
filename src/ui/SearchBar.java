package ui;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SearchBar {
    private TextField searchField;
    private HBox searchBarContainer;

    public SearchBar() {
        searchField = new TextField();
        searchField.setPromptText("Enter URL...");
        searchBarContainer = new HBox();
        searchBarContainer.getChildren().add(searchField);
    }

    public TextField getSearchField() {
        return searchField;
    }

    public HBox getSearchBarContainer() {
        return searchBarContainer;
    }
}

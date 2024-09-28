package api;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class BrowserView {

    private TextField searchBar;
    private WebView browserArea;
    private WebEngine webEngine;

    public BrowserView() {
        searchBar = new TextField();
        searchBar.setPromptText("Enter URL...");
        searchBar.setOnAction(e -> loadPage());

        browserArea = new WebView();
        webEngine = browserArea.getEngine();

        HBox searchBarContainer = new HBox();
        searchBarContainer.getChildren().add(searchBar);
    }

    public void loadPage() {
        String url = searchBar.getText();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        webEngine.load(url);
    }

    public TextField getSearchBar() {
        return searchBar;
    }

    public WebView getBrowserArea() {
        return browserArea;
    }
}


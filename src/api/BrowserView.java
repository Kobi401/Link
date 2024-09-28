package api;

import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class BrowserView {

    private SearchBar searchBar;
    private StatusBar statusBar;
    private WebView browserArea;
    private WebEngine webEngine;

    public BrowserView() {
        searchBar = new SearchBar();
        statusBar = new StatusBar();

        browserArea = new WebView();
        webEngine = browserArea.getEngine();

        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            statusBar.setStatus("Loading: " + newValue);
        });

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            switch (newState) {
                //i believe these are all the possible states
                case SCHEDULED -> statusBar.setStatus("Loading...");
                case RUNNING -> statusBar.setStatus("Running...");
                case SUCCEEDED -> statusBar.setStatus("Done");
                case FAILED -> statusBar.setStatus("Failed to load the page");
                case CANCELLED -> statusBar.setStatus("Loading cancelled");
            }
        });

        searchBar.getSearchField().setOnAction(e -> loadPage());
    }

    //Need to add some debugging here in case we have page loading fails
    public void loadPage() {
        String url = searchBar.getSearchField().getText();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        webEngine.load(url);
        statusBar.setStatus("Loading: " + url);
    }

    //This can be changed later when we add a UI
    public BorderPane createBrowserLayout() {
        BorderPane layout = new BorderPane();
        layout.setTop(searchBar.getSearchBarContainer());
        layout.setCenter(browserArea);
        layout.setBottom(statusBar.getStatusBarContainer());
        return layout;
    }

    public WebView getBrowserArea() {
        return browserArea;
    }
}

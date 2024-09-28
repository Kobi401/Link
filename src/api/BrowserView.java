package api;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

public class BrowserView {

    private SearchBar searchBar;
    private StatusBar statusBar;
    private WebView browserArea;
    private WebEngine webEngine;
    private Button aboutButton;
    private MenuButton menuButton;

    private TabManager tabManager;

    public BrowserView(TabManager tabManager) {
        this.tabManager = tabManager;
        searchBar = new SearchBar();
        statusBar = new StatusBar();

        browserArea = new WebView();
        webEngine = browserArea.getEngine();

        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.startsWith("link://open/")) {
                handleCustomUrl(newValue);
            } else {
                statusBar.setStatus("Loading: " + newValue);
                searchBar.getSearchField().setText(newValue);
            }
        });

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            switch (newState) {
                case SCHEDULED -> statusBar.setStatus("Loading...");
                case RUNNING -> statusBar.setStatus("Running...");
                case SUCCEEDED -> statusBar.setStatus("Done");
                case FAILED -> statusBar.setStatus("Failed to load the page");
                case CANCELLED -> statusBar.setStatus("Loading cancelled");
            }
        });

        searchBar.getSearchField().setOnAction(e -> loadPage());
        createRightButtons();

        loadPage("https://www.google.com");
    }

    private void createRightButtons() {
        aboutButton = new Button("About Link");
        aboutButton.setOnAction(e -> loadAboutPage());

        menuButton = new MenuButton("Menu");
        MenuItem refreshItem = new MenuItem("Refresh");
        MenuItem backItem = new MenuItem("Back");
        MenuItem forwardItem = new MenuItem("Forward");

        refreshItem.setOnAction(e -> webEngine.reload());
        backItem.setOnAction(e -> {
            if (webEngine.getHistory().getCurrentIndex() > 0) {
                webEngine.getHistory().go(-1);
            }
        });
        forwardItem.setOnAction(e -> {
            if (webEngine.getHistory().getCurrentIndex() < webEngine.getHistory().getEntries().size() - 1) {
                webEngine.getHistory().go(1);
            }
        });

        menuButton.getItems().addAll(refreshItem, backItem, forwardItem);
    }

    private void handleCustomUrl(String url) {
        if (url.equals("link://open/github")) {
            tabManager.createHtmlTab("GitHub", "https://github.com/Kobi401/Link");
        }
    }

    private void loadAboutPage() {
        String aboutUrl = "Link/AboutPage.html";
        tabManager.createHtmlTab("About Link", aboutUrl);
    }

    public void loadPage() {
        String url = searchBar.getSearchField().getText();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        webEngine.load(url);
        statusBar.setStatus("Loading: " + url);
    }

    public void loadPage(String url) {
        if (url.startsWith("Link/")) {
            webEngine.load(getClass().getResource("/AboutPage.html").toExternalForm());
        } else {
            webEngine.load(url);
        }
        statusBar.setStatus("Loading: " + url);
    }

    public BorderPane createBrowserLayout() {
        BorderPane layout = new BorderPane();

        HBox searchBarWithButtons = new HBox(5);
        searchBarWithButtons.setPadding(new Insets(5, 5, 5, 5));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        searchBarWithButtons.getChildren().addAll(searchBar.getSearchBarContainer(), spacer, aboutButton, menuButton);
        layout.setTop(searchBarWithButtons);
        layout.setCenter(browserArea);
        layout.setBottom(statusBar.getStatusBarContainer());
        return layout;
    }

    public WebView getBrowserArea() {
        return browserArea;
    }
}

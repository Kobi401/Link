package api;

import api.Flash.FlashHandler;
import api.Managers.TabManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class BrowserView {

    private SearchBar searchBar;
    private StatusBar statusBar;
    private WebView browserArea;
    private WebEngine webEngine;
    private Button aboutButton;
    private MenuButton menuButton;

    private TabManager tabManager;
    private FlashHandler flashHandler;

    public BrowserView(TabManager tabManager) {
        this.tabManager = tabManager;
        searchBar = new SearchBar();
        statusBar = new StatusBar();
        flashHandler = new FlashHandler();

        browserArea = new WebView();
        webEngine = browserArea.getEngine();

        webEngine.setUserAgent("LinkBrowser/1.0 (Windows; X64; Custom; rv:100.0) Gecko/20100101 LinkEngine/1.0.0");

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
                case SUCCEEDED -> {
                    statusBar.setStatus("Done");
                    flashHandler.injectRuffleScript(webEngine);
                }
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

        Button flashButton = new Button("Test Flash");
        flashButton.setOnAction(e -> loadFlashTestPage());

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

        // Add the flash button to the layout
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox rightButtonBox = new HBox(5, aboutButton, flashButton, menuButton);
    }

    private void handleCustomUrl(String url) {
        if (url.equals("link://open/github")) {
            tabManager.createHtmlTab("GitHub", "https://github.com/Kobi401/Link");
        }
    }

    public void loadFlashTestPage() {
        String flashTestUrl = getClass().getResource("/ruffle_integration.html").toExternalForm();
        webEngine.load(flashTestUrl);
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

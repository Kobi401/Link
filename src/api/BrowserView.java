package api;

import api.Flash.FlashHandler;
import api.Managers.ConfigManager;
import api.Managers.TabManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import ui.SearchBar;
import ui.StatusBar;

public class BrowserView {

    private SearchBar searchBar;
    private StatusBar statusBar;
    private WebView browserArea;
    private WebEngine webEngine;
    private MenuButton mainMenuButton;

    private ConfigManager configManager;
    private TabManager tabManager;
    private FlashHandler flashHandler;

    public BrowserView(TabManager tabManager) {
        this.tabManager = tabManager;
        searchBar = new SearchBar();
        statusBar = new StatusBar();
        configManager = new ConfigManager();
        flashHandler = new FlashHandler(configManager.isFlashEnabled());

        browserArea = new WebView();
        webEngine = browserArea.getEngine();

        webEngine.setUserAgent("LinkBrowser/1.0 (Windows; x64; Custom; rv:100.0) Gecko/20100101 LinkEngine/1.0.0");

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
                    if (configManager.isFlashEnabled()) {
                        flashHandler.injectRuffleScript(webEngine);
                    }
                }
                case FAILED -> statusBar.setStatus("Failed to load the page");
                case CANCELLED -> statusBar.setStatus("Loading cancelled");
            }
        });

        searchBar.getSearchField().setOnAction(e -> loadPage());
        createMainMenuButton();

        loadPage("https://www.google.com");
    }

    //when we add a UI we can change this completely
    private void createMainMenuButton() {
        MenuItem aboutItem = new MenuItem("About Link");
        aboutItem.setOnAction(e -> loadAboutPage());

        MenuItem settingsItem = new MenuItem("Settings");
        settingsItem.setOnAction(e -> loadSettingsPage());

        MenuItem refreshItem = new MenuItem("Refresh");
        refreshItem.setOnAction(e -> webEngine.reload());

        MenuItem backItem = new MenuItem("Back");
        backItem.setOnAction(e -> {
            if (webEngine.getHistory().getCurrentIndex() > 0) {
                webEngine.getHistory().go(-1);
            }
        });

        MenuItem forwardItem = new MenuItem("Forward");
        forwardItem.setOnAction(e -> {
            if (webEngine.getHistory().getCurrentIndex() < webEngine.getHistory().getEntries().size() - 1) {
                webEngine.getHistory().go(1);
            }
        });

        mainMenuButton = new MenuButton("≡");
        mainMenuButton.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        mainMenuButton.getItems().addAll(refreshItem, backItem, forwardItem, new SeparatorMenuItem(), aboutItem, settingsItem);
    }

    private void loadSettingsPage() {
        String settingsUrl = getClass().getResource("/SettingsPage.html").toExternalForm();
        tabManager.createHtmlTab("Settings", settingsUrl);
    }

    private void handleCustomUrl(String url) {
        switch (url) {
            case "link://open/github" -> tabManager.createHtmlTab("GitHub", "https://github.com/Kobi401/Link");
            case "link://open/settings" -> loadSettingsPage();
            case "link://open/about" -> loadAboutPage();
            case "link://settings/flash/on" -> {
                configManager.setFlashEnabled(true);
                flashHandler.setFlashEnabled(true);
                System.out.println("Flash enabled.");
            }
            case "link://settings/flash/off" -> {
                configManager.setFlashEnabled(false);
                flashHandler.setFlashEnabled(false);
                System.out.println("Flash disabled.");
            }
            default -> System.out.println("Unhandled URL: " + url);
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

    //this can also be changed when we add a UI
    public BorderPane createBrowserLayout() {
        BorderPane layout = new BorderPane();

        HBox searchBarWithButtons = new HBox(5);
        searchBarWithButtons.setPadding(new Insets(5, 5, 5, 5));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        searchBarWithButtons.getChildren().addAll(searchBar.getSearchBarContainer(), spacer, mainMenuButton);
        layout.setTop(searchBarWithButtons);
        layout.setCenter(browserArea);
        layout.setBottom(statusBar.getStatusBarContainer());
        return layout;
    }

    public WebView getBrowserArea() {
        return browserArea;
    }
}

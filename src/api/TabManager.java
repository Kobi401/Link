package api;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class TabManager {

    private TabPane tabPane;
    private Tab addTab;

    public TabManager() {
        tabPane = new TabPane();
        createAddTabButton();
    }

    private void createAddTabButton() {
        addTab = new Tab();

        Button addButton = new Button("+");
        addButton.setOnAction(e -> createNewTab("New Tab", new BrowserView(this)));

        HBox buttonContainer = new HBox(addButton);
        addTab.setGraphic(buttonContainer);
        addTab.setClosable(false);

        tabPane.getTabs().add(addTab);
    }

    public Tab createNewTab(String title, BrowserView browserView) {
        Tab tab = new Tab(title);
        tab.setContent(browserView.createBrowserLayout());

        tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
        tabPane.getSelectionModel().select(tab);

        return tab;
    }

    public Tab createHtmlTab(String title, String htmlUrl) {
        BrowserView browserView = new BrowserView(this);
        browserView.loadPage(htmlUrl);
        return createNewTab(title, browserView);
    }

    public TabPane getTabPane() {
        return tabPane;
    }
}

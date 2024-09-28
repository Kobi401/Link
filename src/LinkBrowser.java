import api.BrowserView;
import api.Managers.TabManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LinkBrowser extends Application {

    @Override
    public void start(Stage primaryStage) {
        TabManager tabManager = new TabManager();
        BrowserView browserView = new BrowserView(tabManager);

        tabManager.createNewTab("New Tab", browserView);

        BorderPane root = new BorderPane();
        root.setCenter(tabManager.getTabPane());

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle("Link");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

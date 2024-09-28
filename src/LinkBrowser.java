import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import api.BrowserView;

public class LinkBrowser extends Application {

    @Override
    public void start(Stage primaryStage) {
        BrowserView browserView = new BrowserView();

        BorderPane root = browserView.createBrowserLayout();

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle("Link Browser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

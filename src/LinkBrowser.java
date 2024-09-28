import api.BrowserView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LinkBrowser extends Application {

    @Override
    public void start(Stage primaryStage) {
        BrowserView browserView = new BrowserView();

        BorderPane root = new BorderPane();
        root.setTop(browserView.getSearchBar());
        root.setCenter(browserView.getBrowserArea());

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle("Link \t\t\t\t Prototype JavaFX Build");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

import api.BrowserView;
import api.Managers.TabManager;
import api.plugins.PluginManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;

//##POSSIBLE IDEAS##
//we could do an undecorated frame and do our own window frame?
//do we want to keep javaFX webView or make our own engine?
//how about making a Windows XP - 8.1 Build?
//Encryption for user data.
//Maybe Full customizability to the UI if the user wants? (Move UI elements to where ever, Colors, etc)
//Proxy service so we can bypass SESAC or whatever
//how are we doing versioning? (Link 1.0 or what?)

public class LinkBrowser extends Application {

    @Override
    public void start(Stage primaryStage) {
        TabManager tabManager = new TabManager();

        BrowserView initialView = new BrowserView(tabManager);
        tabManager.createNewTab("Home", initialView);

        BorderPane root = new BorderPane();
        root.setCenter(tabManager.getTabPane());

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle("Link");
        primaryStage.setScene(scene);

        PluginManager pluginManager = new PluginManager(initialView.getBrowserArea().getEngine());
        pluginManager.loadPlugins();

        primaryStage.show();
        optimizeUI(primaryStage);
    }

    private void optimizeUI(Stage stage) {
        stage.setResizable(true);
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.widthProperty().addListener((obs, oldVal, newVal) -> System.gc());
        stage.heightProperty().addListener((obs, oldVal, newVal) -> System.gc());
    }

    public static void main(String[] args) {
        launch(args);
    }
}


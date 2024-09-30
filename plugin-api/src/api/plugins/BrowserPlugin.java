package api.plugins;

import javafx.scene.web.WebEngine;

/**
 * Base interface that every plugin must implement.
 */
public interface BrowserPlugin {
    /**
     * Initializes the plugin.
     * @param engine The WebEngine instance of the browser.
     */
    void initialize(WebEngine engine);

    /**
     * Called when the plugin is about to be unloaded or shut down.
     */
    void shutdown();

    /**
     * Returns the name of the plugin.
     */
    String getName();

    /**
     * Returns a brief description of the plugin.
     */
    String getDescription();
}

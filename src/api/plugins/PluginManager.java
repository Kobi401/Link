package api.plugins;

import api.plugins.BrowserPlugin;
import javafx.scene.web.WebEngine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {

    private static final String USER_HOME = System.getProperty("user.home");
    private static final String PLUGIN_DIRECTORY = USER_HOME + "\\AppData\\Local\\LinkBrowser\\Plugins";

    private WebEngine webEngine;
    private List<BrowserPlugin> activePlugins;

    public PluginManager(WebEngine webEngine) {
        this.webEngine = webEngine;
        this.activePlugins = new ArrayList<>();
    }

    /**
     * Load all plugins from the specified directory.
     */
    public void loadPlugins() {
        File pluginDir = new File(PLUGIN_DIRECTORY);

        // Create the plugin directory if it does not exist
        if (!pluginDir.exists()) {
            boolean created = pluginDir.mkdirs();
            if (created) {
                System.out.println("Plugin directory created: " + PLUGIN_DIRECTORY);
            } else {
                System.err.println("Failed to create plugin directory: " + PLUGIN_DIRECTORY);
                return;
            }
        }

        // Ensure the directory is not empty
        File[] jarFiles = pluginDir.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles == null || jarFiles.length == 0) {
            System.out.println("No plugins found in: " + PLUGIN_DIRECTORY);
            return;
        }

        // Load each JAR file found in the directory
        for (File jarFile : jarFiles) {
            List<String> pluginClasses = findPluginClasses(jarFile);
            loadPluginClasses(jarFile, pluginClasses);
        }
    }

    /**
     * Find and return plugin classes from a JAR file that implement BrowserPlugin interface.
     */
    private List<String> findPluginClasses(File jar) {
        List<String> pluginClasses = new ArrayList<>();

        try (JarFile jarFile = new JarFile(jar)) {
            URL[] urls = {jar.toURI().toURL()};
            URLClassLoader loader = new URLClassLoader(urls, this.getClass().getClassLoader());

            jarFile.stream()
                    .filter(entry -> entry.getName().endsWith(".class") && !entry.isDirectory())
                    .forEach(entry -> {
                        String className = entry.getName().replace('/', '.').replace(".class", "");
                        try {
                            Class<?> cls = loader.loadClass(className);

                            // Check if the class implements the BrowserPlugin interface
                            if (BrowserPlugin.class.isAssignableFrom(cls) && !cls.isInterface() && !java.lang.reflect.Modifier.isAbstract(cls.getModifiers())) {
                                pluginClasses.add(className);
                            }
                        } catch (ClassNotFoundException e) {
                            System.err.println("Class not found: " + className);
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pluginClasses;
    }

    /**
     * Load each plugin class and initialize it.
     */
    private void loadPluginClasses(File jarFile, List<String> pluginClasses) {
        try {
            URL[] urls = {jarFile.toURI().toURL()};
            URLClassLoader loader = new URLClassLoader(urls);

            for (String className : pluginClasses) {
                Class<?> pluginClass = loader.loadClass(className);
                BrowserPlugin pluginInstance = (BrowserPlugin) pluginClass.getDeclaredConstructor().newInstance();
                pluginInstance.initialize(webEngine);
                activePlugins.add(pluginInstance);
                System.out.println("Loaded plugin: " + pluginInstance.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the list of active plugins.
     */
    public List<BrowserPlugin> getActivePlugins() {
        return activePlugins;
    }
}

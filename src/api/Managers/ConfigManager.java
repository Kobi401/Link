package api.Managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static final String USER_HOME = System.getProperty("user.home");
    private static final String CONFIG_DIRECTORY = USER_HOME + "\\AppData\\Local\\LinkBrowser\\UserSettings";
    private static final String CONFIG_FILE = CONFIG_DIRECTORY + "\\config.properties";

    private Properties properties;

    public ConfigManager() {
        properties = new Properties();

        File configDir = new File(CONFIG_DIRECTORY);
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFlashEnabled() {
        return Boolean.parseBoolean(properties.getProperty("enable_flash", "false"));
    }

    public void setFlashEnabled(boolean enabled) {
        properties.setProperty("enable_flash", Boolean.toString(enabled));
        saveConfig();
    }

    private void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "Link Browser User Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

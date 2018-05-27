package com.sukinsan.shot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsUtilImpl implements SettingsUtil {

    private final static String
            DEFAULT_HOST = "http://127.0.0.1:8080/",
            HOME_FOLDER_NAME = "koshot",
            PROPERTY_HOST = "host",
            PROPERTY_HOT_KEY_RUN = "hotKEyRun",
            SETTINGS_FILE_NAME = "settings.properties";

    private Properties props;

    public SettingsUtilImpl() {
        props = new Properties();
        load(props);

    }

    private File getHomeFolder() {
        File koshotFolder = new File(System.getProperty("user.home"), HOME_FOLDER_NAME);
        if (!koshotFolder.exists()) {
            koshotFolder.mkdirs();
        }
        return koshotFolder;
    }

    private File getSettings() {
        File koshotFolder = getHomeFolder();
        File koshotSettings = new File(koshotFolder, SETTINGS_FILE_NAME);
        if (!koshotSettings.exists()) {
            try {
                koshotSettings.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return koshotSettings;
    }

    private void save(Properties saveProps) {
        try {
            saveProps.store(new FileOutputStream(getSettings().getAbsolutePath()), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load(Properties loadProps) {
        try {
            loadProps.load(new FileInputStream(getSettings().getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHost() {
        return props.getProperty(PROPERTY_HOST, DEFAULT_HOST);
    }

    @Override
    public void setHost(String host) {
        props.setProperty(PROPERTY_HOST, host);
        save(props);
    }

    @Override
    public boolean getHotKeyCrop() {
        return Boolean.valueOf(props.getProperty(PROPERTY_HOT_KEY_RUN, "false"));
    }

    @Override
    public void setHotKeyCrop(boolean selected) {
        props.setProperty(PROPERTY_HOT_KEY_RUN, String.valueOf(selected));
        save(props);
    }
}

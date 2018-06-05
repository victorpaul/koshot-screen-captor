package com.sukinsan.shot.util;

public interface SettingsUtil {
    String getHost();

    void setHost(String host);

    boolean getHotKeyCrop();

    void setHotKeyCrop(boolean selected);

    void setSHowNotifications(boolean selected);

    boolean getSHowNotifications();

    void setRedmineAuth(String auth);

    String getRedmineAuth();

    String baseAuth(String username, String password);
}

package com.sukinsan.shot.util;

public interface SettingsUtil {
    String getHost();

    void setHost(String host);

    boolean getHotKeyCrop();

    void setHotKeyCrop(boolean selected);

    void setSHowNotifications(boolean selected);

    boolean getSHowNotifications();

    String baseAuth(String username, String password);

}

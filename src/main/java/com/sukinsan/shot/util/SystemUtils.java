package com.sukinsan.shot.util;

public class SystemUtils {

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("windows");
    }
}

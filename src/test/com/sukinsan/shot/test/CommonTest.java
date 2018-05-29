package com.sukinsan.shot.test;

import com.sukinsan.shot.frame.CropDesktopFrame;
import org.junit.Test;

import java.awt.*;

public class CommonTest {

    @Test
    public void check_screens() throws InterruptedException {
        GraphicsDevice[] gs = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (GraphicsDevice gd : gs) {
            new CropDesktopFrame(gd, null);
        }

        Thread.sleep(5000);
        System.exit(0);


    }
}

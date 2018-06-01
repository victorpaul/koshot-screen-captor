package com.sukinsan.shot.frame;

import com.sukinsan.shot.util.SystemUtils;
import com.sukinsan.shot.util.UiUtil;
import com.sukinsan.shot.util.UiUtilImpl;

import javax.swing.*;
import java.awt.*;

public abstract class BaseJFrame extends JFrame {

    public UiUtil uiUtil = new UiUtilImpl();

    public BaseJFrame() throws HeadlessException {
    }

    public BaseJFrame(GraphicsConfiguration gc) {
        super(gc);
    }

    public void enterFullScreen(GraphicsDevice gd) {
        setAlwaysOnTop(true);
        setUndecorated(true); // dis one is important!! Crop by Y will be wrong with top window panel
        if (SystemUtils.isWindows()) {
            setBounds(gd.getDefaultConfiguration().getBounds());
        } else {
            gd.setFullScreenWindow(this);
        }
    }

}

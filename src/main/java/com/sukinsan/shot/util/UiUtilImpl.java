package com.sukinsan.shot.util;

import javax.swing.*;
import java.awt.*;

public class UiUtilImpl implements UiUtil {

    @Override
    public void styleText(JComponent jComponent) {
        jComponent.setFont(new Font("Arial", Font.PLAIN, 40));
    }
}

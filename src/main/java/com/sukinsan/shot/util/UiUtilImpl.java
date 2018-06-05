package com.sukinsan.shot.util;

import javax.swing.*;
import java.awt.*;

public class UiUtilImpl implements UiUtil {

    @Override
    public void styleTextBold(JComponent jComponent) {
        jComponent.setFont(new Font("Arial", Font.BOLD, jComponent.getFont().getSize()));
    }

    @Override
    public void styleTextNormal40(JComponent jComponent) {
        jComponent.setFont(new Font("Arial", Font.PLAIN, 40));
    }

    @Override
    public void styleTextNormal20(JComponent jComponent) {
        jComponent.setFont(new Font("Arial", Font.PLAIN, 20));
    }
}

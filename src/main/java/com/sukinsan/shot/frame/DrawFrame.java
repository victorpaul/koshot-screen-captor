package com.sukinsan.shot.frame;

import com.sukinsan.shot.util.PubishUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawFrame extends JFrame {

    public interface OnReady {
        void OnReady(BufferedImage bi);
    }

    private JLabel editImage;
    private PubishUtil pubishUtil;
    private OnReady or;

    public DrawFrame(Rectangle rt, BufferedImage bi, GraphicsDevice gd, PubishUtil pubishUtil,OnReady or) {
        super(gd.getDefaultConfiguration());
        this.pubishUtil = pubishUtil;
        this.or = or;

        setBounds(rt);
        editImage = new JLabel(new ImageIcon(bi));
        editImage.setBorder(BorderFactory.createLineBorder(new Color(0f, 0f, 0f, 1f), 3));
        add(editImage);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setVisible(true);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dispose();
            or.OnReady(bi);
        }).start();
    }
}

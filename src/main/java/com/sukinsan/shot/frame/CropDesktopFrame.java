package com.sukinsan.shot.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CropDesktopFrame extends JFrame implements KeyListener, MouseListener, MouseMotionListener {

    public interface OnCropArea {
        void OnCropArea(int x, int y, int width, int height);
    }

    private OnCropArea onCrop;
    private JPanel cropPanel;
    private boolean startCrop = false;
    private int mouseStartX = 0;
    private int mouseStartY = 0;

    private int cropStartX = 0;
    private int cropStartY = 0;
    private int cropWidth = 0;
    private int cropHeight = 0;

    public CropDesktopFrame(OnCropArea onCrop) throws HeadlessException {
        this.onCrop = onCrop;
        cropPanel = new JPanel();
        cropPanel.setBackground(Color.BLACK);

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFullScreen();
        setVisible(true);
    }

    private void setFullScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        setState(Frame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setOpacity(0.25f);
        setAlwaysOnTop(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        add(cropPanel);
        startCrop = true;
        mouseStartX = e.getX();
        mouseStartY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        startCrop = false;
        remove(cropPanel);
        dispose();
        if (cropWidth > 0 && cropHeight > 0) {
            onCrop.OnCropArea(cropStartX, cropStartY, cropWidth, cropHeight);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (startCrop) {
            if (e.getX() > mouseStartX) {
                cropStartX = mouseStartX;
                cropWidth = e.getX() - cropStartX;
            } else {
                cropStartX = e.getX();
                cropWidth = mouseStartX - cropStartX;
            }
            if (e.getY() > mouseStartY) {
                cropStartY = mouseStartY;
                cropHeight = e.getY() - cropStartY;
            } else {
                cropStartY = e.getY();
                cropHeight = mouseStartY - cropStartY;
            }

            cropPanel.setBounds(cropStartX, cropStartY, cropWidth, cropHeight);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        dispose();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}

package com.sukinsan.shot.frame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class CropDesktopFrame extends BaseJFrame implements KeyListener, MouseListener, MouseMotionListener {

    public interface OnAction {
        void OnCancel();

        void OnCropArea(BufferedImage bi, Rectangle rt);
    }

    private BufferedImage desktopBufferedImage;
    private OnAction onCrop;
    private JLabel cropPanel;
    private boolean startCrop = false;
    private int mouseStartX = 0;
    private int mouseStartY = 0;

    private int cropStartX = 0;
    private int cropStartY = 0;
    private int cropWidth = 0;
    private int cropHeight = 0;

    public CropDesktopFrame(GraphicsDevice gd, OnAction onCrop) throws HeadlessException {
        super(gd.getDefaultConfiguration());
        this.onCrop = onCrop;
        cropPanel = new JLabel();
        Border border = BorderFactory.createLineBorder(new Color(0f, 0f, 0f, 0.7f), 1);
        cropPanel.setBorder(border);

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        captureScreenAndEnterFullScreenMode(gd);
        setVisible(true);
    }

    private void captureScreenAndEnterFullScreenMode(GraphicsDevice gd) {
        Rectangle rc = gd.getDefaultConfiguration().getBounds();
        JLabel jLabel = null;
        try {
            desktopBufferedImage = new Robot().createScreenCapture(rc);
            jLabel = new JLabel(new ImageIcon(desktopBufferedImage));
        } catch (AWTException e) {
            e.printStackTrace();
        }

        jLabel.setBounds(rc);
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        add(jLabel);
        setComponentZOrder(jLabel, 1);

        enterFullScreen(gd);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("mousePressed");
        add(cropPanel);
        setComponentZOrder(cropPanel, 0);
        startCrop = true;
        mouseStartX = e.getX();
        mouseStartY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("mouseReleased");
        startCrop = false;
        int relatedX = getGraphicsConfiguration().getBounds().x;
        int relatedY = getGraphicsConfiguration().getBounds().y;

        if (onCrop != null && cropWidth > 0 && cropHeight > 0) {
            BufferedImage subImage = desktopBufferedImage.getSubimage(cropPanel.getX(), cropPanel.getY(), cropPanel.getWidth(), cropPanel.getHeight());
            onCrop.OnCropArea(subImage, new Rectangle(cropPanel.getX() + relatedX, cropPanel.getY() + relatedY, cropPanel.getWidth(), cropPanel.getHeight()));
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
                cropWidth = mouseStartX - e.getX();
            }
            if (e.getY() > mouseStartY) {
                cropStartY = mouseStartY;
                cropHeight = e.getY() - cropStartY;
            } else {
                cropStartY = e.getY();
                cropHeight = mouseStartY - e.getY();
            }

            cropPanel.setBounds(cropStartX, cropStartY, cropWidth, cropHeight);
            //cropPanel.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (onCrop != null) {
            onCrop.OnCancel();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (onCrop != null) {
            onCrop.OnCancel();
        }
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

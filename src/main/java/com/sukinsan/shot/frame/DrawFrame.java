package com.sukinsan.shot.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class DrawFrame extends BaseJFrame implements MouseListener, MouseMotionListener, KeyListener {

    public interface OnReady {
        void OnReady(BufferedImage bi);
    }

    private JLabel editImage;
    private OnReady or;
    private BufferedImage editBufferIMage;
    private Graphics2D editGraphics;
    private boolean mouseIn = true;
    private boolean drawMode = false;
    private int mouseX, mouseY;

    public DrawFrame(Rectangle rt, BufferedImage bi, GraphicsDevice gd, OnReady or) {
        super(gd.getDefaultConfiguration());
        this.or = or;

        drawUI(rt, bi);
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
    }

    private void submit() {
        if (isDisplayable()) {
            dispose();
            or.OnReady(editBufferIMage);
        }
    }

    private void drawUI(Rectangle rt, BufferedImage bi) {
        setLocation(rt.x, rt.y);

        editBufferIMage = deepCopy(bi);
        editGraphics = editBufferIMage.createGraphics();

        editImage = new JLabel(new ImageIcon(editBufferIMage));
        editImage.setBorder(BorderFactory.createLineBorder(new Color(0f, 0f, 0f, 1f), 3));
        editImage.setBounds(rt);
        editImage.addMouseListener(this);
        editImage.addMouseMotionListener(this);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setVisible(true);

        JButton doneBtn = new JButton();
        doneBtn.setText("Done");
        doneBtn.addActionListener(e -> submit());
        uiUtil.styleText(doneBtn);

        JButton cleanBtn = new JButton();
        cleanBtn.setText("Clean");
        cleanBtn.addActionListener(e -> {
            editBufferIMage = deepCopy(bi);
            editGraphics = editBufferIMage.createGraphics();
            editImage.setIcon(new ImageIcon(editBufferIMage));
        });
        uiUtil.styleText(cleanBtn);

        JButton closeBtn = new JButton();
        closeBtn.setText("Close");
        closeBtn.addActionListener(e -> {
            dispose();
        });
        uiUtil.styleText(closeBtn);

        getContentPane().setLayout(new FlowLayout());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        getContentPane().add(editImage);
        getContentPane().add(bottomPanel);
        bottomPanel.add(doneBtn);
        bottomPanel.add(cleanBtn);
        bottomPanel.add(closeBtn);

        addKeyListener(this);
        editImage.addKeyListener(this);
        bottomPanel.addKeyListener(this);
        doneBtn.addKeyListener(this);
        cleanBtn.addKeyListener(this);
        closeBtn.addKeyListener(this);

        pack();
        setVisible(true);
    }

    private int color = 0;

    @Override
    public void mouseDragged(MouseEvent e) {
        if (drawMode && mouseIn) {

            if (color > 255) {
                color = 0;
            }

            editGraphics.setColor(new Color(0, 0, 0));
            editGraphics.drawLine(mouseX, mouseY, e.getX(), e.getY());
            editGraphics.setColor(new Color(255, color, 1));
            editGraphics.drawLine(mouseX, mouseY, e.getX() + 2, e.getY() + 2);

            color += 5;

            editImage.repaint();
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        drawMode = true;
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawMode = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseIn = true;
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseIn = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("e" + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dispose();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            submit();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
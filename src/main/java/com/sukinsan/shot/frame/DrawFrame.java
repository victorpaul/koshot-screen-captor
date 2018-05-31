package com.sukinsan.shot.frame;

import com.sukinsan.shot.util.PubishUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class DrawFrame extends JFrame implements MouseListener, MouseMotionListener {

    public interface OnReady {
        void OnReady(BufferedImage bi);
    }

    private JLabel editImage;
    private PubishUtil pubishUtil;
    private OnReady or;
    private BufferedImage editBUfferIMage;
    private boolean mouseIn = true;
    private boolean drawMode = false;
    private int mouseX, mouseY;

    public DrawFrame(Rectangle rt, BufferedImage bi, GraphicsDevice gd, PubishUtil pubishUtil, OnReady or) {
        super(gd.getDefaultConfiguration());
        this.pubishUtil = pubishUtil;
        this.or = or;

        drawUI(rt, bi);
        editBUfferIMage.getGraphics().setPaintMode();
        editBUfferIMage.getGraphics().drawLine(0, 0, 100, 100);
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private void drawUI(Rectangle rt, BufferedImage bi) {
        setLocation(rt.x, rt.y);

        editBUfferIMage = deepCopy(bi);
        editImage = new JLabel(new ImageIcon(editBUfferIMage));
        editImage.setBorder(BorderFactory.createLineBorder(new Color(0f, 0f, 0f, 1f), 3));
        editImage.setBounds(rt);
        editImage.addMouseListener(this);
        editImage.addMouseMotionListener(this);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setVisible(true);

        JButton doneBtn = new JButton();
        doneBtn.setText("Done");
        doneBtn.addActionListener(e -> {
            dispose();
            or.OnReady(editBUfferIMage);
        });
        doneBtn.setFont(new Font("Arial", Font.PLAIN, 40));

        JButton cleanBtn = new JButton();
        cleanBtn.setText("Clean");
        cleanBtn.addActionListener(e -> {
            editBUfferIMage = deepCopy(bi);
            editImage.setIcon(new ImageIcon(editBUfferIMage));
        });
        cleanBtn.setFont(new Font("Arial", Font.PLAIN, 40));

        JButton closeBtn = new JButton();
        closeBtn.setText("Close");
        closeBtn.addActionListener(e -> {
            dispose();
        });
        closeBtn.setFont(new Font("Arial", Font.PLAIN, 40));

        getContentPane().setLayout(new FlowLayout());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        getContentPane().add(editImage);
        getContentPane().add(bottomPanel);
        bottomPanel.add(doneBtn);
        bottomPanel.add(cleanBtn);
        bottomPanel.add(closeBtn);
        pack();
        setVisible(true);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (drawMode && mouseIn) {
            editBUfferIMage.getGraphics().drawLine(mouseX, mouseY, e.getX(), e.getY());
            editImage.repaint();
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
}

package com.sukinsan.shot.frame;

import com.sukinsan.shot.retrofit.Api;
import com.sukinsan.shot.util.*;
import org.apache.commons.validator.routines.UrlValidator;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetUpFrame extends JFrame implements KeyListener, MouseListener, ChangeListener, NativeKeyListener {
    private CropUtil cropUtil;
    private PubishUtil pubishUtil;
    private SettingsUtil settingsUtil;
    private Api api;

    private CropDesktopFrame frame;

    private JTextField hostJtext;
    private JCheckBox checkBoxHotKey;

    public SetUpFrame() throws HeadlessException {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        setDefaultCloseOperation(JFrame.ICONIFIED);

        settingsUtil = new SettingsUtilImpl();
        api = new Api(settingsUtil.getHost());
        cropUtil = new CropUtilImpl();
        pubishUtil = new PublishUtilImpl(api);

        setUpUi();
        pack();
        setVisible(true);
    }

    private void setUpUi() {
        JButton launchButton = new JButton();
        launchButton.setText("Crop");
        launchButton.addActionListener(e -> runCropping());
        launchButton.setFont(new Font("Arial", Font.PLAIN, 40));

        hostJtext = new JTextField();
        hostJtext.setText(settingsUtil.getHost());
        hostJtext.setFont(new Font("Arial", Font.PLAIN, 40));
        hostJtext.addKeyListener(this);
        checkBoxHotKey = new JCheckBox();
        checkBoxHotKey.setText("Crop with Alt+Alt");
        checkBoxHotKey.setSelected(settingsUtil.getHotKeyCrop());
        checkBoxHotKey.addChangeListener(this);

        Container pane = getContentPane();
        pane.setLayout(new FlowLayout());
        pane.add(hostJtext);
        pane.add(checkBoxHotKey);
        pane.add(launchButton);

        setUpHotKeyCrop(settingsUtil.getHotKeyCrop());
        setUpSystemTray();
    }

    private void setUpHotKeyCrop(boolean enable) {
        try {
            if (enable) {
                GlobalScreen.registerNativeHook();
                GlobalScreen.addNativeKeyListener(this);
            } else {
                GlobalScreen.removeNativeKeyListener(this);
                GlobalScreen.unregisterNativeHook();
            }
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
        }
    }

    private void setUpSystemTray() {
        if (SystemTray.isSupported()) {

            PopupMenu popup = new PopupMenu();
            MenuItem setUpItem = new MenuItem("Settings");
            MenuItem exitItem = new MenuItem("Exit");
            popup.add(setUpItem);
            popup.add(exitItem);

            setUpItem.addActionListener(e -> setVisible(true));
            exitItem.addActionListener(e -> System.exit(0));

            TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("tray.png")));
            trayIcon.addMouseListener(this);
            trayIcon.setPopupMenu(popup);
            SystemTray tray = SystemTray.getSystemTray();
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    private void setClipboard(String message) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(message);
        clipboard.setContents(stringSelection, null);
    }

    void runCropping() {
        if (frame != null) {
            frame.dispose();
        }
        frame = new CropDesktopFrame((x, y, width, height) -> {
            File file = cropUtil.crop(x, y, width, height);
            setClipboard("Cropping is publishing");
            pubishUtil.publish(file, new PubishUtil.OnPubish() {
                @Override
                public void success(String res) {
                    setClipboard(res);
                }

                @Override
                public void fail(String res) {

                }
            });
        });
    }

    @Override // input into text field host
    public void keyReleased(KeyEvent e) {
        String[] schemes = {"http", "https"}; // DEFAULT schemes = "http", "https", "ftp"
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (urlValidator.isValid(hostJtext.getText())) {
            settingsUtil.setHost(hostJtext.getText());
        }
    }

    @Override // click on tray
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            runCropping();
        }
    }

    @Override // enable crop by hotkey
    public void stateChanged(ChangeEvent e) {
        setUpHotKeyCrop(checkBoxHotKey.isSelected());
        settingsUtil.setHotKeyCrop(checkBoxHotKey.isSelected());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    long altClicked = 0;
    long doubleALtInterval = 300;
    boolean ctrlPressed = false;

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        if (NativeKeyEvent.VC_CONTROL == nativeKeyEvent.getKeyCode()) {
            ctrlPressed = true;
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        if (NativeKeyEvent.VC_CONTROL == nativeKeyEvent.getKeyCode()) {
            ctrlPressed = false;
        }

        if (ctrlPressed && NativeKeyEvent.VC_ALT == nativeKeyEvent.getKeyCode()) {
            if ((altClicked + doubleALtInterval) > System.currentTimeMillis()) {
                runCropping();
                System.out.println("Doubleclick");
            } else {
                altClicked = System.currentTimeMillis();
            }

        }
    }
}

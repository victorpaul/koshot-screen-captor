package com.sukinsan.shot.frame;

import com.sukinsan.shot.retrofit.Api;
import com.sukinsan.shot.util.*;
import org.apache.commons.validator.routines.UrlValidator;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetUpFrame extends JFrame implements KeyListener, NativeKeyListener {
    private CropUtil cropUtil;
    private PubishUtil pubishUtil;
    private SettingsUtil settingsUtil;
    private Api api;

    private List<CropDesktopFrame> cropFrames = new ArrayList<>();
    private TrayIcon trayIcon;
    private JTextField hostJtext;
    private JCheckBox checkBoxHotKey, checkboxNotification;

    private void toast(String message) {
        System.out.println("message " + message);
        if (settingsUtil.getSHowNotifications()) {
            try {
                trayIcon.displayMessage("koshot", message, TrayIcon.MessageType.NONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public SetUpFrame() throws HeadlessException {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        //setDefaultCloseOperation(JFrame.ICONIFIED);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        launchButton.addActionListener(e -> {
            System.out.println("runCropping() button");
            runCropping();
        });
        launchButton.setFont(new Font("Arial", Font.PLAIN, 40));

        hostJtext = new JTextField();
        hostJtext.setText(settingsUtil.getHost());
        hostJtext.setFont(new Font("Arial", Font.PLAIN, 40));
        hostJtext.addKeyListener(this);

        checkBoxHotKey = new JCheckBox();
        checkBoxHotKey.setText("Crop by holding Ctrl and Alt-Alt twice");
        checkBoxHotKey.setSelected(settingsUtil.getHotKeyCrop());
        checkBoxHotKey.addChangeListener(e -> {
            setUpHotKeyCrop(checkBoxHotKey.isSelected());
            settingsUtil.setHotKeyCrop(checkBoxHotKey.isSelected());
        });

        checkboxNotification = new JCheckBox();
        checkboxNotification.setText("Show publish notification");
        checkboxNotification.setSelected(settingsUtil.getSHowNotifications());
        checkboxNotification.addChangeListener(e -> settingsUtil.setSHowNotifications(checkboxNotification.isSelected()));

        Container pane = getContentPane();
        pane.setLayout(new FlowLayout());
        pane.add(hostJtext);
        pane.add(checkBoxHotKey);
        pane.add(checkboxNotification);
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
            MenuItem cropItem = new MenuItem("Crop");
            MenuItem exitItem = new MenuItem("Exit");
            popup.add(setUpItem);
            popup.add(cropItem);
            popup.add(exitItem);

            setUpItem.addActionListener(e -> setVisible(true));
            cropItem.addActionListener(e -> runCropping());
            exitItem.addActionListener(e -> System.exit(0));

            trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("tray.png")));
            trayIcon.setPopupMenu(popup);
            SystemTray tray = SystemTray.getSystemTray();
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("koshot");
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

    private void disposeAllCropFrames() {
        for (CropDesktopFrame cdf : cropFrames) {
            cdf.dispose();
        }
        cropFrames.clear();
    }

    void runCropping() {
        if (!cropFrames.isEmpty()) {
            System.out.println("NO");
            return;
        }

        // run frame per monitor
        GraphicsDevice[] gs = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (GraphicsDevice gd : gs) {
            CropDesktopFrame cdf = new CropDesktopFrame(gd, new CropDesktopFrame.OnAction() {
                @Override
                public void OnCancel() {
                    disposeAllCropFrames();
                }

                @Override
                public void OnCropArea(BufferedImage bi, Rectangle rt) {
                    //File file = cropUtil.crop(x, y, width, height);
                    disposeAllCropFrames();

                    new DrawFrame(rt, bi, gd, bi1 -> {
                        setClipboard("Cropped image is publishing");
                        File file = cropUtil.save(bi1);
                        pubishUtil.publish(file, new PubishUtil.OnPubish() {
                            @Override
                            public void success(String res) {
                                toast("Url is in your clipboard!");
                                setClipboard(res);
                            }

                            @Override
                            public void fail(String res) {
                                toast(res);
                            }
                        });
                    });


                }
            });
            cropFrames.add(cdf);
        }
    }

    @Override // input into text field host
    public void keyReleased(KeyEvent e) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (urlValidator.isValid(hostJtext.getText())) {
            settingsUtil.setHost(hostJtext.getText());
        }
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
                System.out.println("runCropping() native");
                runCropping();
            } else {
                altClicked = System.currentTimeMillis();
            }

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }
}

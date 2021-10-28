package com.srjons.screenshots;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.*;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import static java.awt.TrayIcon.MessageType.INFO;

public class Main {

    public static final String LOGO = "screenshot-logo.png";
    FileLock lock;
    FileChannel channel;
    public static boolean ASK_FILE_NAME = false;

    public static void main(String[] args) throws Exception {

        Main trayIconDemo = new Main();

        if (trayIconDemo.isAppActive()) {
            JOptionPane.showMessageDialog(null,
                    "Application is already running, Please check in notification/System tray");
            return;
        }

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        // Schedule a job for the event-dispatching thread:
        // adding TrayIcon.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public boolean isAppActive() throws Exception {
        File file = new File(System.getProperty("user.home"), "FireZeMissiles1111" + ".tmp");
        channel = new RandomAccessFile(file, "rw").getChannel();

        lock = channel.tryLock();
        if (lock == null) {
            return true;
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    lock.release();
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return false;
    }

    private static void createAndShowGUI() {
        // Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        Toolkit.getDefaultToolkit().getImage(SettingsFrame.class.getResource(LOGO));

        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(createImage(LOGO, "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();
        trayIcon.setImageAutoSize(true);

        // Create a popup menu components
        MenuItem takeScreenshotItem = new MenuItem("Take Screenshot");
        CheckboxMenuItem askFileNameCb = new CheckboxMenuItem("Ask File name");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem openFolderItem = new MenuItem("Open Screenshot Folder");
        MenuItem exitItem = new MenuItem("Exit");

        // Add components to popup menu
        popup.add(takeScreenshotItem);
        popup.add(askFileNameCb);
        popup.add(openFolderItem);
        popup.add(settingsItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

/*
		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Hi");
			}
		});
*/

        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    ScreenshotCamera screenshotCamera = new ScreenshotCamera();
                    screenshotCamera.start();
                }
            }
        });


        takeScreenshotItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ScreenshotCamera screenshotCamera = new ScreenshotCamera();
                screenshotCamera.start();
            }
        });

        askFileNameCb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ASK_FILE_NAME = true;
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    ASK_FILE_NAME = false;
                }
            }
        });

        openFolderItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desktop desktop = Desktop.getDesktop();
                File dirToOpen = null;
                try {
                    dirToOpen = new File(SettingsFrame.FOLDER_PATH);
                    desktop.open(dirToOpen);
                } catch (Exception iae) {
                    System.out.println("File Not Found");
                }
            }
        });

        settingsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SettingsFrame frame = new SettingsFrame();
                frame.setVisible(true);
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int flag = JOptionPane.showConfirmDialog(null, "Do you want to exit Screenshot Maker ?");
                if (flag == 0) {
                    tray.remove(trayIcon);
                    System.exit(0);
                }
            }
        });
    }

    // Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = Main.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}

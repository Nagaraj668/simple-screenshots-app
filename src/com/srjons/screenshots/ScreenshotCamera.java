package com.srjons.screenshots;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ScreenshotCamera extends Thread {

	public void run() {
		Graphics2D imageGraphics = null;
		try {
			Thread.sleep(500);
			Robot robot = new Robot();
			GraphicsDevice currentDevice = MouseInfo.getPointerInfo().getDevice();
			BufferedImage exportImage = robot.createScreenCapture(currentDevice.getDefaultConfiguration().getBounds());
			imageGraphics = (Graphics2D) exportImage.getGraphics();

			String fileName = System.currentTimeMillis() + "";
			if (Main.ASK_FILE_NAME) {
				fileName = JOptionPane.showInputDialog(null, "Enter file name", "File Name",
						JOptionPane.INFORMATION_MESSAGE);
			}
			File screenshotFile = new File(SettingsFrame.FOLDER_PATH + File.separator + fileName + ".png");
			if(!screenshotFile.exists()) {
				screenshotFile.mkdirs();
			}
			imageGraphics.dispose();
			ImageIO.write(exportImage, "png", screenshotFile);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
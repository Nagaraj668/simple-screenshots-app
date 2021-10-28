package com.srjons.screenshots;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SettingsFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String SETTINGS_TITLE = "Screenshot Settings";
	private JLabel folderNameLabel = new JLabel("Folder Path/Name");
	private JTextField folderNameField = new JTextField();
	private JButton setBtn = new JButton("Save Folder Name");
	private JButton browseBtn = new JButton("Browse");
	public static String FOLDER_PATH = "D:\\Screenshots";

	public SettingsFrame() {
		super(SETTINGS_TITLE);
		setLocationRelativeTo(null);
		setExtendedState(MAXIMIZED_BOTH);
		setLayout(null);
		folderNameLabel.setBounds(10, 10, 200, 30);
		folderNameField.setBounds(10, 50, 200, 30);
		browseBtn.setBounds(220, 50, 200, 30);
		setBtn.setBounds(10, 90, 200, 30);
		add(folderNameField);
		add(folderNameLabel);
		folderNameField.setText(FOLDER_PATH);
		add(setBtn);
		add(browseBtn);
		browseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FolderChooserDialog chooserDialog = new FolderChooserDialog(getContext());
				chooserDialog.show(new FolderCreateListener() {

					@Override
					public void onFolderCreated(String folderPath) {
						FOLDER_PATH = folderPath;
						folderNameField.setText(folderPath);
					}

					@Override
					public void onFolderCreateCancelled() {

					}
				});
			}
		});

		setBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setFolderPath();
			}
		});
		
		setLogo();
		
	}

	private void setLogo() {
		ImageIcon img = new ImageIcon(Main.LOGO);
		setIconImage(img.getImage());
	}

	private void setFolderPath() {
		FOLDER_PATH = folderNameField.getText().toString();
		File file = new File(FOLDER_PATH);

		try {
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			System.out.println("Folder creation error");
		}

	}

	private JFrame getContext() {
		return this;
	}
}

package com.WMC.Client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class ClientInitializeWindow {

	JFrame clientInitFrame;
	private JTextField displayNameTextField;
	private JTextField serverAddressTextField;
	private JTextField serverPortTextField;
	
	private JLabel displayNameLabel;
	private JLabel serverAddressLabel;
	private JLabel serverPortLabel;
	
	private ClientInformation clientInfo;
	private final ImageIcon NO_ICON;
	private final ImageIcon OK_ICON;
	
	public ClientInitializeWindow(ClientInformation clientInfo) {
		this.clientInfo = clientInfo;
		NO_ICON = new ImageIcon(ClientInitializeWindow.class.getResource("/res/NO_20px.png"));
		OK_ICON = new ImageIcon(ClientInitializeWindow.class.getResource("/res/OK_20px.png"));
		initialize();
	}

	private void initialize() {
		clientInitFrame = new JFrame();
		clientInitFrame.setResizable(false);
		clientInitFrame.setTitle("WMC Server Selection");
		clientInitFrame.setSize(325, 145);
		clientInitFrame.setLocationRelativeTo(null);
		clientInitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientInitFrame.getContentPane().setLayout(null);

		JButton connectButton = new JButton("Connect");
		connectButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					connectButton.doClick();
				}
			}
		});
		
		JLabel lblNewLabel = new JLabel("Display Name:");
		lblNewLabel.setBounds(10, 11, 100, 14);
		clientInitFrame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Server IP:");
		lblNewLabel_1.setBounds(10, 36, 100, 14);
		clientInitFrame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Server Port:");
		lblNewLabel_2.setBounds(10, 61, 100, 14);
		clientInitFrame.getContentPane().add(lblNewLabel_2);
		
		displayNameTextField = new JTextField();
		displayNameTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB) {
					serverAddressTextField.requestFocusInWindow();
				}
			}
		});
		displayNameTextField.setBounds(120, 8, 164, 20);
		clientInitFrame.getContentPane().add(displayNameTextField);
		displayNameTextField.setColumns(10);
		displayNameTextField.requestFocusInWindow();
		
		serverAddressTextField = new JTextField();
		serverAddressTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB) {
					serverPortTextField.requestFocusInWindow();
				}
			}
		});
		serverAddressTextField.setColumns(10);
		serverAddressTextField.setBounds(120, 33, 164, 20);
		clientInitFrame.getContentPane().add(serverAddressTextField);
		
		serverPortTextField = new JTextField();
		serverPortTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				handlePortInputChange();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				handlePortInputChange();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				handlePortInputChange();
			}
		});
		serverPortTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					connectButton.doClick();
				}
				else if (e.getKeyCode() == KeyEvent.VK_TAB) {
					displayNameTextField.requestFocusInWindow();
				}
			}
		});
		serverPortTextField.setColumns(10);
		serverPortTextField.setBounds(120, 58, 164, 20);
		clientInitFrame.getContentPane().add(serverPortTextField);		
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleButtonClick();
			}
		});
		connectButton.setBounds(109, 86, 100, 23);
		clientInitFrame.getContentPane().add(connectButton);
		
		displayNameLabel = new JLabel("");
		displayNameLabel.setIcon(null);
		displayNameLabel.setBounds(290, 8, 20, 20);
		clientInitFrame.getContentPane().add(displayNameLabel);
		
		serverAddressLabel = new JLabel("");
		serverAddressLabel.setIcon(null);
		serverAddressLabel.setBounds(290, 33, 20, 20);
		clientInitFrame.getContentPane().add(serverAddressLabel);
		
		serverPortLabel = new JLabel("");
		serverPortLabel.setIcon(null);
		serverPortLabel.setBounds(290, 58, 20, 20);
		clientInitFrame.getContentPane().add(serverPortLabel);
	}
	
	private void handleButtonClick() {
		boolean inputValid = true;

		if (validateServerPort())
			changeLabelIcon(serverPortLabel, OK_ICON);
		else {
			changeLabelIcon(serverPortLabel, NO_ICON);
			serverPortTextField.requestFocusInWindow();

			JOptionPane.showMessageDialog(new JFrame(), 
					"Port must be an integer between 1100 and 65535 inclusive", 
					"Invalid Port", 
					JOptionPane.ERROR_MESSAGE);
			
			inputValid = false;
		}
		
		if (validateServerAddress())
			changeLabelIcon(serverAddressLabel, OK_ICON);
		else {
			changeLabelIcon(serverAddressLabel, NO_ICON);
			serverAddressTextField.requestFocusInWindow();
			inputValid = false;
		}
		
		if (validateDisplayName())
			changeLabelIcon(displayNameLabel, OK_ICON);
		else {
			changeLabelIcon(displayNameLabel, NO_ICON);
			displayNameTextField.requestFocusInWindow();			
			inputValid = false;
		}
			
		if (!inputValid)
			return;
		
		clientInfo.setDisplayName(displayNameTextField.getText().trim());
		clientInfo.setServerAddress(serverAddressTextField.getText().trim());
		clientInfo.setServerPort(serverPortTextField.getText().trim());
		
		clientInitFrame.dispose();
	}	
	
	private void handlePortInputChange() {
		if (serverPortTextField.getText().length() > 0) {
			if (!validateServerPort())
				changeLabelIcon(serverPortLabel, NO_ICON);
			else
				changeLabelIcon(serverPortLabel, OK_ICON);
		}
		else
			changeLabelIcon(serverPortLabel, null);
	}
	
	private void changeLabelIcon(JLabel label, ImageIcon icon) {
		label.setIcon(icon);
	}

	private boolean validateDisplayName() {
		String dn = displayNameTextField.getText();
		return dn.trim().length() > 0;
	}
	
	private boolean validateServerAddress() {
		String sa = serverAddressTextField.getText();
		return sa.trim().length() > 0;
	}
	
	private boolean validateServerPort() {
		String sp = serverPortTextField.getText();
		boolean valid = true;
		valid = valid && sp.trim().length() > 0;
		try {
			int p = Integer.parseInt(sp);
			if (p < 1100 || p > 65536)
				valid = false;
		} catch(Exception e) {
			valid = false;
		}
		
		return valid;
	}
}

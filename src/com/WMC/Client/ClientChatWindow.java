package com.WMC.Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.WMC.WMCUtil;

import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;

public class ClientChatWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final String HELP_STRING = "Commands: /h[elp], /quit, /exit";
	
	private JPanel contentPane;
	private JTextArea messageTextArea;
	private JTextArea chatTextArea;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	
	private ClientInformation clientInfo;
	
	private boolean shiftPressed = false;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					ClientChatWindow frame = new ClientChatWindow(new ClientInformation("DummyName", "127.0.0.1", "8507"));
					frame.setVisible(true);
					frame.setMessageFocus();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientChatWindow(ClientInformation clientInfo) {
		this.clientInfo = clientInfo;
		
		setTitle("Where's My Chat, JC?");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0};
		gbl_contentPane.rowHeights = new int[] {4, 1};
		gbl_contentPane.columnWeights = new double[]{1.0};
		gbl_contentPane.rowWeights = new double[]{4.0, 1.0};
		contentPane.setLayout(gbl_contentPane);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		chatTextArea = new JTextArea();
		scrollPane.setViewportView(chatTextArea);
		chatTextArea.setEditable(false);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 0, 0);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 1;
		contentPane.add(scrollPane_1, gbc_scrollPane_1);
		
		messageTextArea = new JTextArea();
		scrollPane_1.setViewportView(messageTextArea);
		messageTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					shiftPressed = true;
				}
				else
					super.keyPressed(e);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && !shiftPressed) {
					String message = messageTextArea.getText();
					messageTextArea.setText("");					
					
					if (message.replace("\n", "").equals(""))
						return;
					
					if (message.startsWith("/"))
						handleCommand(message);
					else					
						sendUserMessage(message);
				}
				else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					messageTextArea.setText(messageTextArea.getText() + "\n");
				}
				else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					shiftPressed = false;
				}
				else
					super.keyReleased(e);
			}
		});
		
		String welcomeMessage = WMCUtil.getTimeStamp() + "\n" + 
				clientInfo.getServerAddress() + ":" + clientInfo.getServerPort() + "\n" +
				"Welcome to chat, " + clientInfo.getDisplayName() + "!\n\n";
		chatTextArea.setText(welcomeMessage);
	}	
	
	public void setMessageFocus() {
		messageTextArea.requestFocusInWindow();
	}
	
	public void handleCommand(String cmd) {
		cmd = cmd.substring(1).trim();
		
		if (cmd.equals("quit") || cmd.equals("exit")) {
			this.dispose();
		}
		else if (cmd.equals("h") || cmd.equals("help")) {
			printHelp();
		}
		else if (cmd.equals("secret") || cmd.equals("easteregg")) {
			systemMessage("You have discovered a secret command. Well played.");
		}
		else {
			systemMessage("Invalid command - " + cmd);
			printHelp();
		}
	}
	
	public void printHelp() {
		systemMessage(HELP_STRING);
	}
	
	public void sendUserMessage(String msg) {
		chatTextArea.setText(chatTextArea.getText() + 
			WMCUtil.getTimeStamp() + " " + clientInfo.getDisplayName() + ": " + msg);
	}
	
	public void systemMessage(String msg) {
		chatTextArea.setText(chatTextArea.getText() + "[SYSTEM]: " + msg + "\n");
	}
}

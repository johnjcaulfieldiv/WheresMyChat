package com.WMC.Client;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.WMC.NetworkMessage;
import com.WMC.WMCUtil;

import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientChatWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final String SYSTEM_TAG = "[ SYSTEM ]";
	private static final String HELP_STRING = "Commands: /h[elp], /quit, /exit";
	
	private static final String COLOR_FILENAME = "/res/clientChatWindowColorScheme";
	
	private JPanel contentPane;
	private JTextArea messageTextArea;
	private JTextArea chatTextArea;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem fileMenuItem_Exit;
	private JMenu viewMenu;
	private JMenuItem viewMenuItem_Colors;
	
	private ColorScheme colorScheme;
	
	private ClientInformation clientInfo;
	
	private NetworkIO netIO;
	private Thread networkReaderThread;
	
	private boolean shiftPressed = false;
	private JMenuItem fileMenuItem_Connect;

	public ClientChatWindow(ClientInformation clientInfo, NetworkIO net) {
		this.clientInfo = clientInfo;
		this.netIO = net;
		
		this.colorScheme = ColorScheme.getFromFile(COLOR_FILENAME);
		if (colorScheme == null)
			colorScheme = new ColorScheme();
		
		setTitle("Where's My Chat, JC?");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				closeWindow();
			}
		});
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		fileMenuItem_Exit = new JMenuItem("Exit");
		fileMenuItem_Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeWindow();
			}
		});
		
		fileMenuItem_Connect = new JMenuItem("Reconnect to Server");
		fileMenuItem_Connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reconnectToServer();
			}
		});
		fileMenu.add(fileMenuItem_Connect);
		fileMenu.add(fileMenuItem_Exit);
		
		viewMenu = new JMenu("View");
		menuBar.add(viewMenu);
		
		viewMenuItem_Colors = new JMenuItem("Change Colors");
		viewMenuItem_Colors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setColorSchemeWithDialog();
			}
		});
		viewMenu.add(viewMenuItem_Colors);
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
					
					if (message.trim().startsWith("/"))
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
		
		setColors();
		
		String welcomeMessage = WMCUtil.getTimeStamp() + "\n" + 
				clientInfo.getServerAddress() + ":" + clientInfo.getServerPort() + "\n" +
				"Welcome to chat, " + clientInfo.getDisplayName() + "!\n\n";
		chatTextArea.setText(welcomeMessage);

		if (netIO.connect()) {
			systemMessage("Connected to server\n");
			
			startServerReader();
		}
		else
			systemMessage("Failed to connect to server\n");
	}	
	
	private void setColors() {
		this.setBackground(colorScheme.getBackgroundColor());
		chatTextArea.setBackground(colorScheme.getForegroundColor());
		chatTextArea.setForeground(colorScheme.getTextColor());
		messageTextArea.setBackground(colorScheme.getForegroundColor());
		messageTextArea.setForeground(colorScheme.getTextColor());
		messageTextArea.setCaretColor(colorScheme.getTextColor());
	}
	
	private void setColorSchemeWithDialog() {
		colorScheme.setForegroundColor(JColorChooser.showDialog(null, "Select chat background color", colorScheme.getBackgroundColor()));
		colorScheme.setTextColor(JColorChooser.showDialog(null, "Select text color", colorScheme.getBackgroundColor()));
		setColors();
	}
	
	public void setMessageFocus() {
		messageTextArea.requestFocusInWindow();
	}
	
	private void scrollChatAreaToBottom() {
		chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength()-1);
	}
	
	public void handleCommand(String cmd) {
		cmd = cmd.trim().substring(1);
		
		if (cmd.equals("quit") || cmd.equals("exit")) {
			closeWindow();
		}
		else if (cmd.equals("h") || cmd.equals("help")) {
			printHelp();
		}
		else if (cmd.equals("secret") || cmd.equals("easteregg")) {
			systemMessage("You have discovered a secret command. Well played.\n");
		}
		else if (cmd.startsWith("color")) {
			setColorSchemeWithDialog();
		}
		else if (cmd.equals("reconnect")) {
			reconnectToServer();
		}
		else {
			systemMessage("Invalid command - " + cmd);
			printHelp();
		}
	}
	
	public void printHelp() {
		systemMessage(HELP_STRING + "\n");
	}
	
	public synchronized void sendUserMessage(String msg) {
		chatTextArea.append(WMCUtil.getTimeStamp() + " " + clientInfo.getDisplayName() + ": " + msg);
		scrollChatAreaToBottom();
		
		// network
//		netIO.send(msg);
		
		NetworkMessage nm = new NetworkMessage(NetworkMessage.MessageType.CHAT, clientInfo.displayName, msg);
		netIO.sendNetworkMessage(nm);
	}
	
	public synchronized void systemMessage(String msg) {
		chatTextArea.append(SYSTEM_TAG + " " + msg);
		scrollChatAreaToBottom();
	}
	
	/**
	 * handle cleanup then dispose of ClientChatWindow
	 */
	public void closeWindow() {
		netIO.disconnect();
		
		colorScheme.writeToFile(COLOR_FILENAME);
		
		try {
			networkReaderThread.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.dispose();
	}
	
	private void startServerReader() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				while (netIO.isActive()) {
					NetworkMessage networkMsg = netIO.receiveNetworkMessage();
					handleNetworkMessage(networkMsg);
				}
			}
		};
		networkReaderThread = new Thread(r);
		networkReaderThread.start();
	}

	/**
	 * currently just prints the message
	 * @TODO do actual handling
	 * @param msg
	 */
	private void handleNetworkMessage(NetworkMessage msg) {
		systemMessage(msg.toString());
	}

	private void reconnectToServer() {
		netIO.disconnect();
		
		try {
			networkReaderThread.join(1000);
		} catch (InterruptedException e) {}
		
		try {
			netIO = new NetworkIO(clientInfo);
			if (netIO.connect()) {
				systemMessage("Connected to server\n");
				startServerReader();
			}
			else
				systemMessage("Failed to connect to server\n");
		} catch (Exception e) {
			e.printStackTrace();
			systemMessage("Error connecting. Server may be down\n");
		}
	}
}

package com.WMC.Client;

import java.awt.EventQueue;
import java.util.logging.Logger;

import javax.swing.UIManager;

import com.WMC.WMCUtil;

public class ClientApplication {

	public static void main(String [] args) {
		
		Logger LOGGER = WMCUtil.createDefaultLogger(ClientApplication.class.getName());
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			LOGGER.severe(WMCUtil.stackTraceToString(e));
		}
		
		ClientInformation clientInfo = new ClientInformation();
		
		LOGGER.info("Starting ClientInitializeWindow");
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ClientInitializeWindow initWindow = new ClientInitializeWindow(clientInfo);
				initWindow.clientInitFrame.setVisible(true);
			}
		});
		
		// hang until clientInfo is set in the initWindow
		// not sure if this is the best method
		// probably not, but it works for now
		while (clientInfo.displayName.equals("")) {
			try {
				Thread.sleep(500, 0);
			} catch (InterruptedException e) {
				LOGGER.warning(WMCUtil.stackTraceToString(e));
			}
		}

		LOGGER.info("ClientInitializeWindow exited");
		
		StringBuilder statusSB = new StringBuilder("ClientInformation initialized to:\n");
		statusSB.append("Display Name  : ").append(clientInfo.getDisplayName()).append("\n");
		statusSB.append("Server Address: ").append(clientInfo.getServerAddress()).append("\n");
		statusSB.append("Server Port   : ").append(clientInfo.getServerPort()).append("\n");
		LOGGER.info(statusSB.toString());
		
		LOGGER.info("Starting ClientChatWindow");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NetworkIO net = new NetworkIO(clientInfo);
					ClientChatWindow chatWindow = new ClientChatWindow(clientInfo, net);
					chatWindow.setVisible(true);
					chatWindow.setMessageFocus();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}

package com.WMC.Client;

import java.awt.EventQueue;

import javax.swing.UIManager;

public class ClientApplication {

	public static void main(String [] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ClientInformation clientInfo = new ClientInformation();
		
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
				e.printStackTrace();
			}
		}
		
		/* debug */
		StringBuilder statusSB = new StringBuilder("ClientInformation initialized to:\n");
		statusSB.append("Display Name  : ").append(clientInfo.getDisplayName()).append("\n");
		statusSB.append("Server Address: ").append(clientInfo.getServerAddress()).append("\n");
		statusSB.append("Server Port   : ").append(clientInfo.getServerPort()).append("\n");
		System.out.println(statusSB.toString());
		/* end debug */
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientChatWindow chatWindow = new ClientChatWindow(clientInfo);
					chatWindow.setVisible(true);
					chatWindow.setMessageFocus();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}

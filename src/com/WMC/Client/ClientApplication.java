package com.WMC.Client;

import java.awt.EventQueue;

import javax.swing.UIManager;

public class ClientApplication {
	
	static ClientInformation clientInfo;

	public static void main(String [] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		clientInfo = new ClientInformation();
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ClientInitializeWindow window = new ClientInitializeWindow(clientInfo);
				window.clientInitFrame.setVisible(true);
			}
		});
		
		while (clientInfo.displayName.equals("")) {
			try {
				Thread.sleep(100, 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		StringBuilder statusSB = new StringBuilder("ClientInitializeWindow returned:\n");
		statusSB.append("Display Name  : ").append(clientInfo.getDisplayName()).append("\n");
		statusSB.append("Server Address: ").append(clientInfo.getServerAddress()).append("\n");
		statusSB.append("Server Port   : ").append(clientInfo.getServerPort()).append("\n");
		System.out.println(statusSB.toString());
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientChatWindow frame = new ClientChatWindow(clientInfo);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		System.out.println("End ClientApplication");
	}
	
}

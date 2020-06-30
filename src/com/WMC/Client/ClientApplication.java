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
		
		StringBuilder statusSB = new StringBuilder();
		statusSB.append("Display Name  : ").append(clientInfo.displayName).append("\n");
		statusSB.append("Server Address: ").append(clientInfo.serverAddress).append("\n");
		statusSB.append("Server Port   : ").append(clientInfo.serverPort).append("\n");
		System.out.print(statusSB.toString());
		
		System.out.println("End ClientApplication");
	}
	
}

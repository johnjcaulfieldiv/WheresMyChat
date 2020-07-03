package com.WMC.Client;

import java.awt.EventQueue;
import java.util.Scanner;

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
		
		try {
			NetworkIO net = new NetworkIO(clientInfo);
			net.connect();
			Scanner scan = new Scanner(System.in);
			while (true) {
				String msg = scan.nextLine();
				net.send(msg);
				if (msg.equalsIgnoreCase("exit"))
					break;
			}
			net.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ClientChatWindow chatWindow = new ClientChatWindow(clientInfo);
//					chatWindow.setVisible(true);
//					chatWindow.setMessageFocus();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}
	
}

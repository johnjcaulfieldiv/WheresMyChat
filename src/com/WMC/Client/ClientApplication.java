package com.WMC.Client;

import java.awt.EventQueue;
import javax.swing.UIManager;

public class ClientApplication {

	public static void main(String [] args) {
				
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ClientInitializeWindow initWindow = new ClientInitializeWindow(new ClientInformation());
				initWindow.clientInitFrame.setVisible(true);
			}
		});
	}
	
}

package com.WMC.Client;

import java.awt.EventQueue;
import java.util.logging.Logger;

import javax.swing.UIManager;

import com.WMC.WMCUtil;

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

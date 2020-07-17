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
	}
	
}

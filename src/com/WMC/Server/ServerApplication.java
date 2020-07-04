package com.WMC.Server;

import java.util.ArrayList;

public class ServerApplication {
	
	private static final int MAX_CONNECTIONS = 2;

	public static void main(String [] args) {
		if (args.length != 1) {
			System.err.println("Error - No port supplied\nUsage: ServerApplication [port]");
			return;
		}
		
		if ("-h".equals(args[0]) || "-help".equals(args[0])) {
			System.out.println("Usage: ServerApplication [port]");
			return;
		}
		
		int port;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.err.println("Error - port must be an integer\nUsage: ServerApplication [port]");
			return;
		}
		
		ArrayList<Thread> threadList = new ArrayList<Thread>();
		
		try {
			Server s = new Server(port);
			s.start();
			
			int connections = 0;
			while (connections++ < MAX_CONNECTIONS) {				
				ClientHandler handler = new ClientHandler(s.accept());
				Thread t = new Thread(handler);
				threadList.add(t);
				t.start();
			}
			
			for (Thread t : threadList) {
				t.join();
			}

			s.stop();
			
		} catch (IllegalArgumentException e) {
			System.err.println("Error - Port must be in range 1100-65535\nUsage: ServerApplication [port]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

package com.WMC.Server;

public class ServerApplication {

	public static void main(String [] args) {
		if (args.length != 1) {
			System.err.println("Error - No port supplied\nUsage: ServerApplication [port]");
			return;
		}
		
		int port = Integer.parseInt(args[0]);
		
		try {
			Server s = new Server(port);
			s.start();
			String clientMsg = "";
			while (!(clientMsg = s.read()).equals("exit")) {
				System.out.println(clientMsg);
			}
			s.disconnect();			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
}

package com.WMC.Client;

import java.net.InetAddress;
import java.net.Socket;

public class NetworkIO {
	private ClientInformation clientInfo;
	
	private Socket socket;
	private String ip;
	private int port;
	
	public NetworkIO(ClientInformation clientInfo) throws Exception {
		this.clientInfo = clientInfo;
		
		if (this.clientInfo.getServerAddress().equals("localhost"))
			this.ip = "127.0.0.1";
		else
			this.ip = clientInfo.getServerAddress();
		
		try {
			port = Integer.parseInt(this.clientInfo.getServerPort());
			if (port <= 1100 || port > 65535)
				throw new IllegalArgumentException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
		
		Socket socket = new Socket(this.clientInfo.getServerAddress(), Integer.parseInt(this.clientInfo.getServerPort()));
		
		
	}
	
	
}

package com.WMC.Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkIO {
	private ClientInformation clientInfo;
	
	private Socket socket;
	private String ip;
	private int port;
	
	DataOutputStream out;
			
	public NetworkIO(ClientInformation clientInfo) throws Exception {
		this.clientInfo = clientInfo;
		
		if (this.clientInfo.getServerAddress().equals("localhost"))
			this.ip = "127.0.0.1";
		else
			this.ip = clientInfo.getServerAddress();
		
		try {
			port = Integer.parseInt(this.clientInfo.getServerPort());
			if (port < 1100 || port > 65535)
				throw new IllegalArgumentException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}	
	}
	
	public void connect() {		
		try {
			socket = new Socket(ip, port);
			
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		try {
			out.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

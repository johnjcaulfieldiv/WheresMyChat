package com.WMC.Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkIO {
	private ClientInformation clientInfo;
	
	private Socket socket;
	private String ip;
	private int port;

	DataInputStream in;
	DataOutputStream out;
	
	private boolean isActive;
			
	public NetworkIO(ClientInformation clientInfo) throws Exception {
		this.clientInfo = clientInfo;
		isActive = true;
		
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

			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		try {
			System.err.println("writing...");
			out.writeUTF(msg);
			System.err.println("written");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String receive() {
		String msg = "Error - server failed to read from socket";
		try {
			System.err.println("reading...");
			msg = in.readUTF();
			System.err.println("read");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return msg;
	}
	
	public void disconnect() {
		isActive = false;
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
	
	public boolean isActive() {
		return isActive;
	}
}

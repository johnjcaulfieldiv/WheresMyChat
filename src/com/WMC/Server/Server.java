package com.WMC.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	public static final String MESSAGE_EOF_ERROR = "ERROR_EOF";
	
	private ArrayList<Socket> clients;
	private ServerSocket server;
//	private String ip;
	private int port;
	
	private boolean isBound;
	
	public Server(String port) throws Exception {
		
//		if (ip.equals("localhost"))
//			this.ip = "127.0.0.1";
//		else
//			this.ip = ip;
		
		try {
			this.port = Integer.parseInt(port);
			if (this.port < 1100 || this.port > 65535)
				throw new IllegalArgumentException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
		isBound = false;
		clients = new ArrayList<>();
	}
	
	public Server(int port) throws Exception {
		this.port = port;	
		if (this.port < 1100 || this.port > 65535)
			throw new IllegalArgumentException();

		isBound = false;
		clients = new ArrayList<>();
	}
	
	public void start() {
		try {
			server = new ServerSocket(this.port);	
			isBound = true;
			System.out.println("Server started");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Socket accept() {		  
        try {
        	System.out.println("Waiting for a client ...");

	        clients.add(server.accept());
	        
	        System.out.println("Client accepted\n");
	        
	        return clients.get(clients.size()-1);
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return null;
	}
	
	public void stop() {
		try {			
			server.close();
			isBound = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasClientConnections() {
		return clients.size() > 0;
	}
	
	public boolean isClosed() {
		return isBound;
	}
}

package com.WMC.Server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static final String MESSAGE_EOF_ERROR = "ERROR_EOF";
	
	private Socket client;
	private ServerSocket server;
//	private String ip;
	private int port;
	
	private DataInputStream in;
	
	private boolean isBound;
	private int clientConnections;
	
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
		in = null;
		isBound = false;
		clientConnections = 0;
	}
	
	public Server(int port) throws Exception {
		this.port = port;	
		if (this.port < 1100 || this.port > 65535)
			throw new IllegalArgumentException();
		in = null;
		isBound = false;
		clientConnections = 0;
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
	
	public void accept() {		  
        try {
        	System.out.println("Waiting for a client ...");

	        client = server.accept();
			clientConnections += 1;
	        
	        System.out.println("Client accepted\n");
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	public String read() {
        if (in == null)
			try {
				in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}            
        
        String message = "";
		try {
			message = in.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return MESSAGE_EOF_ERROR;
		}
		
		return message;
	}
	
	public void disconnect() {
		try {
			client.close();
			client = null;
			clientConnections -= 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		try {
			if (clientConnections > 0)
				System.err.println("Closing server while clients are still connected!");
			
			server.close();
			isBound = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasClientConnections() {
		return clientConnections > 0;
	}
	
	public boolean isClosed() {
		return isBound;
	}
}

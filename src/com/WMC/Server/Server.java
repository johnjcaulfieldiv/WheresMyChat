package com.WMC.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
	public static final String MESSAGE_EOF_ERROR = "ERROR_EOF";
	
	private List<Socket> clientSockets;
	private HashMap<String, ObjectOutputStream> clientOutStreams;
	private ServerSocket server;
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
		clientSockets = new ArrayList<>();
		clientOutStreams = new HashMap<>();
	}
	
	public Server(int port) throws Exception {
		this.port = port;	
		if (this.port < 1100 || this.port > 65535)
			throw new IllegalArgumentException();

		isBound = false;
		clientSockets = new ArrayList<>();
		clientOutStreams = new HashMap<>();
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

        	Socket newClientSocket = server.accept();
        	
	        clientSockets.add(newClientSocket);
	        
	        ObjectOutputStream out = new ObjectOutputStream(newClientSocket.getOutputStream());
	        out.flush();
	        
	        clientOutStreams.put(newClientSocket.getInetAddress().getHostAddress(), out);
	        
	        System.out.println("Client accepted\n");
	        
	        return newClientSocket;
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return null;
	}
	
	public void stop() {
		isBound = false;
		
		for (ObjectOutputStream oos : clientOutStreams.values()) {
			try {
				oos.close();
			} catch(Exception e) {}
		}
		
		for (Socket skt : clientSockets) {
			try {
				skt.close();
			} catch(Exception e) {}
		}
		
		try {
			server.close();
		} catch (IOException e) {}
	}
	
	public boolean hasClientConnections() {
		return clientSockets.size() > 0;
	}
	
	public boolean isClosed() {
		return isBound;
	}

	public HashMap<String, ObjectOutputStream> getClientOutStreams() {
		return clientOutStreams;
	}
}

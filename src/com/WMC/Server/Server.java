package com.WMC.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.WMC.WMCUtil;

public class Server {
	public static final String MESSAGE_EOF_ERROR = "ERROR_EOF";
	
	private Logger LOGGER;
	
	private List<Socket> clientSockets;
	private HashMap<String, ObjectOutputStream> clientOutStreams;
	private ServerSocket server;
	private int port;
	
	private boolean isBound;

	public Server(int port) throws Exception {
		this.port = port;	
		if (this.port < 1100 || this.port > 65535)
			throw new IllegalArgumentException();

		isBound = false;
		clientSockets = new ArrayList<>();
		clientOutStreams = new HashMap<>();
		
		LOGGER = WMCUtil.createDefaultLogger(Server.class.getName());
	}
	
	public void start() {
		try {
			server = new ServerSocket(this.port);	
			isBound = true;
			LOGGER.log(Level.INFO, "Server started");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, WMCUtil.stackTraceToString(e));
		}
	}
	
	public Socket accept() {		  
        try {
        	Socket newClientSocket = server.accept();
        	
	        clientSockets.add(newClientSocket);
	        
	        ObjectOutputStream out = new ObjectOutputStream(newClientSocket.getOutputStream());
	        out.flush();
	        
	        clientOutStreams.put(newClientSocket.getInetAddress().getHostAddress(), out);
	        
			LOGGER.log(Level.INFO, "Client accepted: " + newClientSocket.getInetAddress().getHostAddress());
	        
	        return newClientSocket;
        } catch (IOException e) {
        	LOGGER.log(Level.SEVERE, WMCUtil.stackTraceToString(e));
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
		
		LOGGER.log(Level.INFO, "Server stopped");
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

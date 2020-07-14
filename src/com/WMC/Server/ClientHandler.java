package com.WMC.Server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.WMC.NetworkMessage;
import com.WMC.WMCUtil;

public class ClientHandler implements Runnable {
	
	private Logger LOGGER;
	
	private Socket client;
	private String clientName;
	private boolean isActive;
	
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private HashMap<String, ObjectOutputStream> outStreams;
	
	public ClientHandler(Socket s, HashMap<String, ObjectOutputStream> os) {
		client = s;
		outStreams = os;
		objectOut = os.get(s.getInetAddress().getHostAddress());
		isActive = true;

		LOGGER = WMCUtil.createDefaultLogger(ClientHandler.class.getName());
	}
	
	@Override
	public void run() {
		try {
			objectIn = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, WMCUtil.stackTraceToString(e));
			isActive = false;
			return;
		}         
		
		// get username
		NetworkMessage userMsg = readNetworkMessage();
		clientName = userMsg.getUser();

		LOGGER.log(Level.INFO, client.getInetAddress().getHostAddress() + " connected with username: " + clientName);
		
		outStreams.remove(client.getInetAddress().getHostAddress());
		outStreams.put(clientName, objectOut);
		
		sendClientUserList();
		broadcastClientConnected();
				
		try {
			NetworkMessage netMsg = readNetworkMessage();			
			while (isActive && 
				   netMsg != null && 
				   netMsg.getType() != NetworkMessage.MessageType.ERROR && 
				   netMsg.getType() != NetworkMessage.MessageType.DISCONNECTION) {
				
				LOGGER.info(netMsg.toString());
				
				// @TODO handle all message types instead of echoing
				handleClientMessage(netMsg);
				
				netMsg = readNetworkMessage();
			}
		} catch (Exception e) {
			if (isActive)
				broadcastClientDisconnected();
			isActive = false;
			LOGGER.severe(WMCUtil.stackTraceToString(e));
			return;
		} finally {
			outStreams.remove(clientName);

			if (isActive)
				broadcastClientDisconnected();
			isActive = false;
			
			try {				
				client.close();
			} catch (IOException e) {
				LOGGER.warning(WMCUtil.stackTraceToString(e));
			}
		}
	}
	
	// @TODO add functionality to all message types
	private void handleClientMessage(NetworkMessage msg) {
		switch(msg.getType()) {
			case CONNECTION:
				break;
			case DISCONNECTION:
				broadcastClientDisconnected();
				isActive = false;
				break;
			case CHAT:
				break;
			case INFO:
				break;
			case HEARTBEAT:
				break;
			case ERROR:
				break;
			default:
				LOGGER.warning(msg.getUser() + " + sent invalid MessageType");
		}
		
		// echo
		if (isActive) {
			NetworkMessage echo = new NetworkMessage(NetworkMessage.MessageType.CHAT, msg.getUser(), msg.getBody());
			broadcastNetworkMessage(echo);
		}
	}
		
	private NetworkMessage readNetworkMessage() {
		NetworkMessage message = null;
		try {
			while (message == null) {
				message = (NetworkMessage) objectIn.readObject();
				Thread.sleep(100);
			}
		
		} catch (Exception e) {
			if (isActive)
				broadcastClientDisconnected();
			isActive = false;
			if (e.getClass() != EOFException.class)
				LOGGER.severe(WMCUtil.stackTraceToString(e));
			message = null;
		}
		
		return message;
	}
	
	private synchronized void broadcastNetworkMessage(NetworkMessage msg) {		
		try {
			for (String key : outStreams.keySet()) {
				ObjectOutputStream oOut = outStreams.get(key);
				oOut.writeObject(msg);
				oOut.flush();
				LOGGER.info("wrote to " + key + ": " + msg);
			}
		} catch (Exception e) {
			if (isActive && msg.getType() != NetworkMessage.MessageType.DISCONNECTION)
				broadcastClientDisconnected();
			isActive = false;
			LOGGER.severe(WMCUtil.stackTraceToString(e));
		}
	}
	
	private void sendClientMessage(NetworkMessage msg) {
		try {
			objectOut.writeObject(msg);
			objectOut.flush();
			LOGGER.info("wrote to " + clientName + ": " + msg);
		} catch (Exception e) {
			isActive = false;
			LOGGER.warning(WMCUtil.stackTraceToString(e));
		}
	}
	
	private void sendClientUserList() {		
		String users = "";
		for (String key : outStreams.keySet()) {
			users += key + NetworkMessage.DELIMITER;
		}
		NetworkMessage userListMessage = new NetworkMessage(NetworkMessage.MessageType.CONNECTION);
		userListMessage.setBody(users);
		sendClientMessage(userListMessage);
	}
	
	private void broadcastUserList() {		
		String users = "";
		for (String key : outStreams.keySet()) {
			users += key + NetworkMessage.DELIMITER;
		}
		NetworkMessage userListMessage = new NetworkMessage(NetworkMessage.MessageType.CONNECTION);
		userListMessage.setBody(users);
		broadcastNetworkMessage(userListMessage);
	}
	
	@SuppressWarnings("unused")
	private void broadcastClientConnected() {		
		NetworkMessage conn = new NetworkMessage(NetworkMessage.MessageType.CONNECTION);
		conn.setUser(this.clientName);
		this.broadcastNetworkMessage(conn);
	}
	
	private void broadcastClientDisconnected() {		
		NetworkMessage disc = new NetworkMessage(NetworkMessage.MessageType.DISCONNECTION);
		disc.setUser(this.clientName);
		this.broadcastNetworkMessage(disc);
	}
}

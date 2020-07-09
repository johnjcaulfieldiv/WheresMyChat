package com.WMC.Server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import com.WMC.NetworkMessage;

public class ClientHandler implements Runnable {
	
	private Socket client;
	private String clientName;
	
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private HashMap<String, ObjectOutputStream> outStreams;
	
	public ClientHandler(Socket s, HashMap<String, ObjectOutputStream> os) {
		client = s;
		outStreams = os;
		objectOut = os.get(s.getInetAddress().getHostAddress());
	}	
	
	@Override
	public void run() {
		try {
			objectIn = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}         
		
		// get username
		NetworkMessage userMsg = readNetworkMessage();
		assert userMsg.getType() == NetworkMessage.MessageType.CONNECTION;
		System.out.println(userMsg);
		clientName = userMsg.getUser();
		outStreams.remove(client.getInetAddress().getHostAddress());
		outStreams.put(clientName, objectOut);
		System.out.println("Users:");
		for (String u : outStreams.keySet()) {
			System.out.println(u);
		}
		
		try {
			NetworkMessage netMsg = readNetworkMessage();
			while (netMsg != null && 
				   netMsg.getType() != NetworkMessage.MessageType.ERROR && 
				   netMsg.getType() != NetworkMessage.MessageType.DISCONNECTION) {
				System.out.println(netMsg);
				NetworkMessage response = new NetworkMessage(NetworkMessage.MessageType.CHAT, netMsg.getUser(), netMsg.getBody());
				broadcastNetworkMessage(response);
				System.out.println("reading...");
				System.out.flush();
				netMsg = readNetworkMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			outStreams.remove(clientName);

			NetworkMessage dc = new NetworkMessage(NetworkMessage.MessageType.DISCONNECTION);
			dc.setUser(clientName);
			broadcastNetworkMessage(dc);
			
			try {				
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	private NetworkMessage readNetworkMessage() {
		NetworkMessage message = null;
		try {
			while (message == null) {
				message = (NetworkMessage) objectIn.readObject();
				Thread.sleep(100);
			}
		
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			if (e.getClass() != EOFException.class)
				e.printStackTrace();
			message = null;
		}
		
		return message;
	}
	
	private void writeNetworkMessage(NetworkMessage msg) {
		try {
			objectOut.writeObject(msg);
			objectOut.flush();
			System.out.println("wrote: " + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized void broadcastNetworkMessage(NetworkMessage msg) {
		try {
			for (String key : outStreams.keySet()) {
				ObjectOutputStream oOut = outStreams.get(key);
				oOut.writeObject(msg);
				oOut.flush();
				System.out.println("wrote to " + key + ": " + msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

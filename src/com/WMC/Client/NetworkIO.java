package com.WMC.Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.WMC.NetworkMessage;

public class NetworkIO {
	private ClientInformation clientInfo;
	
	private Socket socket;
	private String ip;
	private int port;

	DataInputStream  in;
	DataOutputStream out;
	ObjectOutputStream objectOut;
	ObjectInputStream objectIn;
	
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
	
	/**
	 * connect to address and port specified in member {@link ClientInformation}
	 * @return true if successfully connected and set up in/out streams else false
	 */
	public boolean connect() {		
		try {
			socket = new Socket(ip, port);

			//in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			//out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			objectOut.flush(); // flush the header
			objectIn = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public void send(String msg) {
		if (!isActive) {
			disconnect();
			return;
		}	
		
		try {
			out.writeUTF(msg);
			out.flush();
		} catch (IOException e) {
			System.err.println("Failed to send to server: " + msg);
		}
	}
	
	public String receive() {
		if (!isActive) {
			disconnect();
			return "Lost Connection to Server\n";
		}			
		
		String msg = "Error - server failed to read from socket";
		try {
			msg = in.readUTF();
		} catch (IOException e) {
			if (e.getClass().equals(SocketException.class)) {
				isActive = false;
				return "Lost Connection to Server\n";
			}
			else
				e.printStackTrace();
		}
		
		return msg;
	}
	
	public void sendNetworkMessage(NetworkMessage msg) {
		if (!isActive) {
			disconnect();
			return;
		}
		
		try {
			objectOut.writeObject(msg);
			objectOut.flush();
		} catch (IOException e) {
			System.err.println("Failed to send to server: " + msg);
		}
	}
	
	public NetworkMessage receiveNetworkMessage() {
		if (!isActive) {
			disconnect();
			return new NetworkMessage(NetworkMessage.MessageType.ERROR, "Lost Connection to Server\n");
		}			
		
		NetworkMessage msg = null;
		try {			
			msg = (NetworkMessage) objectIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			if (e.getClass().equals(SocketException.class)) {
				isActive = false;
				msg = new NetworkMessage(NetworkMessage.MessageType.ERROR, "Lost Connection to Server\n");
			}
			else
				e.printStackTrace();
		}
		
		return msg;
	}
	
	public void disconnect() {
		isActive = false;
		try {
			objectOut.close();
		} catch (Exception e) {
			// unhandled exception. shutting down anyway
		}
		
		try {
			objectIn.close();
		} catch (IOException | NullPointerException e) {
			// unhandled exception. shutting down anyway
		}
		
		try {
			socket.close();
		} catch (Exception e) {
			// unhandled exception. shutting down anyway
		}
	}
	
	public Socket getSocket() {
		return socket;
	}

	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean newState) {
		isActive = newState;
	}
}

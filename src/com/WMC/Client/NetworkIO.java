package com.WMC.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import com.WMC.NetworkMessage;
import com.WMC.WMCUtil;

public class NetworkIO {
	private Logger LOGGER;
	
	private Socket socket;
	private String ip;
	private int port;

	private ObjectOutputStream objectOut;
	private ObjectInputStream objectIn;
	
	private boolean isActive;
			
	public NetworkIO(ClientInformation clientInfo) throws Exception {
		LOGGER = WMCUtil.createDefaultLogger(NetworkIO.class.getName());
		
		if (clientInfo.getServerAddress().equals("localhost"))
			ip = "127.0.0.1";
		else
			ip = clientInfo.getServerAddress();
		
		try {
			port = Integer.parseInt(clientInfo.getServerPort());
			if (port < 1100 || port > 65535)
				throw new IllegalArgumentException();
		} catch (Exception e) {
			LOGGER.severe(WMCUtil.stackTraceToString(e));
			throw new IllegalArgumentException();
		}

		isActive = true;
	}
	
	/**
	 * connect to address and port specified in member {@link ClientInformation}
	 * @return true if successfully connected and set up in/out streams else false
	 */
	public boolean connect() {		
		try {
			socket = new Socket(ip, port);

			objectOut = new ObjectOutputStream(socket.getOutputStream());
			objectOut.flush(); // flush the header
			
			objectIn = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			LOGGER.severe(WMCUtil.stackTraceToString(e));
			return false;
		}
		
		return true;
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
			LOGGER.warning("Failed to send to server: " + msg);
		}
	}
	
	public NetworkMessage receiveNetworkMessage() {
		if (!isActive) {
			disconnect();
			NetworkMessage errMsg = new NetworkMessage(NetworkMessage.MessageType.ERROR);
			errMsg.setUser("[ SERVER ]");
			errMsg.setBody("Lost Connection to Server\n");
			return errMsg;
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
				LOGGER.severe(WMCUtil.stackTraceToString(e));
		}
		
		return msg;
	}
	
	public void disconnect() {
		isActive = false;
		
		try {
			objectOut.close();
		} catch (Exception e) {}
		
		try {
			objectIn.close();
		} catch (Exception e) {}
		
		try {
			socket.close();
		} catch (Exception e) {}
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

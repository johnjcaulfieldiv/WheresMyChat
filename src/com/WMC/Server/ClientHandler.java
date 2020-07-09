package com.WMC.Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.WMC.NetworkMessage;

public class ClientHandler implements Runnable {
	
	private Socket client;
	private DataInputStream in;
	private DataOutputStream out;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	
	public ClientHandler(Socket s) {
		client = s;
	}	
	
	@Override
	public void run() {
		try {
			in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
			objectOut = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
			objectOut.flush(); // flush the header
			objectIn = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}         
		
		try {
			NetworkMessage netMsg = readNetworkMessage();
			while (netMsg != null && 
				   netMsg.getType() != NetworkMessage.MessageType.ERROR && 
				   netMsg.getType() != NetworkMessage.MessageType.DISCONNECTION) {
				System.out.println("recv: " + netMsg);
				NetworkMessage response = new NetworkMessage(NetworkMessage.MessageType.CHAT, "servertron1000", "You said: " + netMsg.getBody());
				writeNetworkMessage(response);
				System.out.println("reading...");
				System.out.flush();
				netMsg = readNetworkMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String readLine() {
		String message = "";
		try {
			message = in.readUTF();
		
		} catch (IOException e) {
			if (e.getClass() != EOFException.class)
				e.printStackTrace();
			message = Server.MESSAGE_EOF_ERROR;
		}
		
		return message;
	}
	
	private void writeLine(String message) {
		try {
			out.writeUTF(message);
			out.flush();
			System.out.println("wrote: " + "Got your message: " + message);
		} catch (IOException e) {
			e.printStackTrace();
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
}

package com.WMC.Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
	
	private Socket client;
	private DataInputStream in;
	private DataOutputStream out;
	
	public ClientHandler(Socket s) {
		client = s;
	}	
	
	@Override
	public void run() {
		try {
			in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}         
		
		try {
			String clientMsg = readLine();
			while (!clientMsg.equalsIgnoreCase("exit") && !clientMsg.equals(Server.MESSAGE_EOF_ERROR)) {
				System.out.println(clientMsg);
				System.out.println("writing...");
				writeLine("Got your message: " + clientMsg);
				System.out.println("reading...");
				System.out.flush();
				clientMsg = readLine();
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
}

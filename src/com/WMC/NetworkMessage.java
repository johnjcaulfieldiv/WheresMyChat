package com.WMC;

import java.io.Serializable;

public class NetworkMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum MessageType {CONNECTION, DISCONNECTION, CHAT, INFO, HEARTBEAT, ERROR};
	
	private MessageType type;
	private String user;
	private String body;
	
	public NetworkMessage() {
		this.type = null;
		this.user = null;
		this.body = null;
	}
	
	public NetworkMessage(MessageType type) {
		this.type = type;
		user = null;
		body = null;
	}
	
	public NetworkMessage(MessageType type, String body) {
		this.type = type;
		user = null;
		body = this.body;
	}
	
	public NetworkMessage(MessageType type, String user, String body) {
		this.type = type;
		this.user = user;
		this.body = body;
	}	
	
	public MessageType getType() {
		return type;
	}
	
	public void setType(MessageType type) {
		this.type = type;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	@Override
	public String toString() {
		switch(type) {
		case CONNECTION:
			return "Connection: " + this.user;
		case DISCONNECTION:
			return "Disconnection: " + this.user;
		case CHAT:
			return this.user + ": " + this.body;
		case INFO:
			return "Info: " + this.body;
		case HEARTBEAT:
			return "Heartbeat: " + this.user;
		case ERROR:
			return "Error: " + this.body;
		}
		
		return "Invalid Message";
	}
}

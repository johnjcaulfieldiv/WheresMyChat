package com.WMC.Client;

public class ClientInformation {

	public String displayName;
	public String serverAddress;
	public String serverPort;

	public ClientInformation() {
		this.displayName = "";
		this.serverAddress = "";
		this.serverPort = "";
	}	
	
	public ClientInformation(String displayName, String serverAddress, String serverPort) {
		this.displayName = displayName;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}	
	
}

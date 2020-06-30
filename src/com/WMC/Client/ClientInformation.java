package com.WMC.Client;

class ClientInformation {

	String displayName;
	String serverAddress;
	String serverPort;

	ClientInformation() {
		this.displayName = "";
		this.serverAddress = "";
		this.serverPort = "";
	}	
	
	ClientInformation(String displayName, String serverAddress, String serverPort) {
		this.displayName = displayName;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}	
	
}

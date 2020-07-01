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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
}

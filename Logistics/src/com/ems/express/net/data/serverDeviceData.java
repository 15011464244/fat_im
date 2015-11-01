package com.ems.express.net.data;

import java.io.Serializable;

public class serverDeviceData implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ipAddress;
	private int port;
	private String version;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}

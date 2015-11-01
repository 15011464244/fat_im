package com.newcdc.chat.net;

import com.newcdc.chat.model.serverDeviceData;

public class LoginResp extends BaseResp {
	private static final long serialVersionUID = 1L;
	private serverDeviceData serverDevice;

	public serverDeviceData getServerDevice() {
		return serverDevice;
	}

	public void setServerDevice(serverDeviceData serverDevice) {
		this.serverDevice = serverDevice;
	}

}

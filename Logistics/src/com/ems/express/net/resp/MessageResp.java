package com.ems.express.net.resp;

import com.ems.express.net.data.MessageData;

public class MessageResp extends BaseResp {
	private static final long serialVersionUID = 1L;
	private MessageData message;

	public MessageData getMessage() {
		return message;
	}

	public void setMessage(MessageData message) {
		this.message = message;
	}

}

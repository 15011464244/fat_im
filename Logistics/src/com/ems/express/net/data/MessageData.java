package com.ems.express.net.data;

import java.io.Serializable;

public class MessageData implements Serializable {
	private static final long serialVersionUID = 1L;
	private String uri;
	private String source;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}

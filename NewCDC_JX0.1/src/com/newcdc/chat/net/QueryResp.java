package com.newcdc.chat.net;

import java.io.Serializable;
import java.util.List;

import com.newcdc.chat.ui.ChatMessageBean;

public class QueryResp implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<ChatMessageBean> result;

	public List<ChatMessageBean> getResult() {
		return result;
	}

	public void setResult(List<ChatMessageBean> result) {
		this.result = result;
	}

}

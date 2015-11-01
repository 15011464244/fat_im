package com.newcdc.chat.net;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.alibaba.fastjson.JSONObject;
import com.newcdc.chat.ui.ChatMessageBean;

public class HttpRequester {

	public static String post(String path, ChatMessageBean cm,
			com.newcdc.chat.ui.FormFile formFile) throws Exception {
		MultipartPostMethod filePost = new MultipartPostMethod(path);
		FilePart cbFile = new FilePart(formFile.getFileName(),
				formFile.getFile());
		cbFile.setContentType("audio/" + formFile.getParameterName());
		filePost.addParameter("message", new JSONObject().toJSONString(cm));
		filePost.addParameter("filename", formFile.getFileName());
		filePost.addParameter("uploadFiles", formFile.getFileName(),
				formFile.getFile());
		filePost.addPart(cbFile);
		filePost.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		HttpClient client = new HttpClient();
		int statusInt = client.executeMethod(filePost);
		System.out.println("statusInt:" + statusInt);
		return filePost.getResponseBodyAsString();
	}
}

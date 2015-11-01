package com.ems.express.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.alibaba.fastjson.JSONObject;
import com.ems.express.bean.ChatMessageBean;

public class HttpRequester {

	public static String post(String path, ChatMessageBean cm,
			FormFile file) throws Exception {
		MultipartPostMethod filePost = new MultipartPostMethod(path);
		FilePart cbFile = new FilePart(file.getFileName(), file.getFile());
		cbFile.setContentType("audio/" + file.getParameterName());
		filePost.addParameter("message", new JSONObject().toJSONString(cm));
		filePost.addParameter("filename", file.getFileName());
		filePost.addParameter("uploadFiles", file.getFileName(), file.getFile());
		filePost.addPart(cbFile);
		filePost.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		HttpClient client = new HttpClient();
		int statusInt = client.executeMethod(filePost);
		System.out.println("statusInt:"+statusInt);
		return filePost.getResponseBodyAsString();
	}
}

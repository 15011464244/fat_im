package com.ems.express.potter;

import java.util.HashMap;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import android.os.Message;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.bean.ChatMessageBean;
import com.ems.express.constant.Constant;
import com.ems.express.net.Request;
import com.ems.express.net.resp.QueryResp;
import com.ems.express.ui.chat.MainActivity;

public class MinaClientHanlder extends IoHandlerAdapter {

	/**
	 * 这个方法在连接被打开时调用，它总是在sessionCreated()方法之后被调用。对于TCP 来
	 * 说，它是在连接被建立之后调用，你可以在这里执行一些认证操作、发送数据等。
	 */
	public void sessionOpened(IoSession session) throws Exception {
		session.write("{'userId':'" + App.loginId + "'}");
		System.out.println("Client:sessionOpened");
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
		System.out.println("messageSent:" + message.toString());
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
//		System.out.println("messageReceived:" + message);
//
//		org.json.JSONObject json = new org.json.JSONObject(message.toString());
//		String str = json.getString("retType");
//		System.out.println("req：" + str);
//		if (str.equals("message")) {
//			System.out.println("if");
//			HashMap<String, String> params = new HashMap<String, String>();
//			params.put("userId", App.loginId);
//			Request.todo(params, QueryResp.class, Constant.Query, null,
//					new Response.Listener<QueryResp>() {
//
//						@Override
//						public void onResponse(QueryResp response) {
//							if (response.getResult().size() > 0)
//								for (int i = 0; i < response.getResult().size(); i++) {
//									ChatMessageBean cm = response.getResult()
//											.get(i);
//									Message msg = new Message();
//									msg.what = 1;
//									msg.obj = cm;
//									MainActivity.sHandler.sendMessage(msg);
//								}
//
//						}
//
//					}, new Response.ErrorListener() {
//						@Override
//						public void onErrorResponse(VolleyError error) {
//							error.printStackTrace();
//						}
//					});
//		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("Client:sessionClose");
		session.close(true);
	}
}
package com.newcdc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newcdc.db.helper.ChatMessageDaoHelper;
import com.newcdc.model.ChatMessageTabBean;
import com.newcdc.tools.Utils;

public class ChatMessageDao extends ChatMessageDaoHelper {

	private Context context;

	public ChatMessageDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
		this.context = context;
	}

	public synchronized boolean insertChatMessage(
			ArrayList<ChatMessageTabBean> beans) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (beans != null) {
			for (int i = 0; i < beans.size(); i++) {
				ChatMessageTabBean bean = beans.get(i);
				values = new ContentValues();
				values.put("createtime", Utils.parseMsgDate());
				values.put("pkid", bean.getPkid() != null ? bean.getPkid() : "");
				values.put("pkidname",
						bean.getPkidname() != null ? bean.getPkidname() : "");
				values.put("source",
						bean.getSource() != null ? bean.getSource() : "");
				values.put("sourcename",
						bean.getSourcename() != null ? bean.getSourcename()
								: "");
				values.put("target",
						bean.getTarget() != null ? bean.getTarget() : "");
				values.put("targetname",
						bean.getTargetname() != null ? bean.getTargetname()
								: "");
				values.put("messagetype",
						bean.getMessagetype() != null ? bean.getMessagetype()
								: "");
				values.put("content",
						bean.getContent() != null ? bean.getContent() : "");
				values.put("url", bean.getUrl() != null ? bean.getUrl() : "");
				values.put("app_id",
						bean.getApp_id() != null ? bean.getApp_id() : "");
				values.put("app_name",
						bean.getApp_name() != null ? bean.getApp_name() : "");
				values.put("tag_id",
						bean.getTag_id() != null ? bean.getTag_id() : "");
				values.put("tag_name",
						bean.getTag_name() != null ? bean.getTag_name() : "");
				values.put("sndStatus", bean.getSndStatus());
				values.put("speechMessageTime", bean.getSpeechMessageTime());
				values.put("messageId", bean.getMessageId());
				values.put("delvorgcode", "");
				values.put("username", "");
				values.put("isRead", "0");// 是否已读 未读？0 已读？1
				values.put("headphoto", "");// 是否已读 未读？0 已读？1
				values.put("sid", "");//
				db.insert(CHATMESSAGE_TABLE, null, values);
			}
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	/**
	 * 将Cursor转化成List
	 */
	public ArrayList<ChatMessageTabBean> parseCursorToList(Cursor cursor) {
		ArrayList<ChatMessageTabBean> list = new ArrayList<ChatMessageTabBean>();
		while (cursor.moveToNext()) {
			ChatMessageTabBean bean = new ChatMessageTabBean();
			bean.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
			bean.setCreatetime(cursor.getString(cursor
					.getColumnIndex("createtime")));
			bean.setPkid(cursor.getString(cursor.getColumnIndex("pkid")));
			bean.setPkidname(cursor.getString(cursor.getColumnIndex("pkidname")));
			bean.setSource(cursor.getString(cursor.getColumnIndex("source")));
			bean.setSourcename(cursor.getString(cursor
					.getColumnIndex("sourcename")));
			bean.setTarget(cursor.getString(cursor.getColumnIndex("target")));
			bean.setTargetname(cursor.getString(cursor
					.getColumnIndex("targetname")));
			bean.setMessagetype(cursor.getString(cursor
					.getColumnIndex("messagetype")));
			bean.setContent(cursor.getString(cursor.getColumnIndex("content")));
			bean.setUrl(cursor.getString(cursor.getColumnIndex("url")));
			bean.setApp_id(cursor.getString(cursor.getColumnIndex("app_id")));
			bean.setApp_name(cursor.getString(cursor.getColumnIndex("app_name")));
			bean.setTag_id(cursor.getString(cursor.getColumnIndex("tag_id")));
			bean.setTag_name(cursor.getString(cursor.getColumnIndex("tag_name")));
			bean.setDelvorgcode(cursor.getString(cursor
					.getColumnIndex("delvorgcode")));
			bean.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			bean.setHeadphoto(cursor.getString(cursor
					.getColumnIndex("headphoto")));
			bean.setSid(cursor.getString(cursor.getColumnIndex("sid")));
			bean.setSpeechMessageTime(cursor.getInt(cursor
					.getColumnIndex("speechMessageTime")));
			bean.setSndStatus(cursor.getString(cursor
					.getColumnIndex("sndStatus")));
			bean.setMessageId(cursor.getInt(cursor.getColumnIndex("messageId")));
			list.add(bean);
		}
		cursor.close();
		return list;
	}

	/**
	 * 获取到消息集合的条数 也就是消息id
	 */
	public int getMesNo() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + CHATMESSAGE_TABLE;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor).size();
	}

	/**
	 * 根据去重掉自己的clientID得到所有聊天列表
	 * 
	 * */
	public List<ChatMessageTabBean> queryFriendList(String clientID) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select source,_id,createtime,pkid,pkidname,source,sourcename,target,targetname,messagetype,content,app_id,app_name,tag_id,tag_name,delvorgcode,username,url,headphoto,sid,speechMessageTime,sndStatus,messageId, count(*) from "
				+ CHATMESSAGE_TABLE
				+ " where source!='"
				+ clientID
				+ "' group by source having count(*)>=1 order by createtime desc";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 聊天信息查询 String source, String delvorgcode, String username
	 */
	public List<ChatMessageTabBean> queryAllMessageAPP() {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + CHATMESSAGE_TABLE;
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 聊天信息查询
	 */
	public List<ChatMessageTabBean> queryAllMessage(String source) {
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + CHATMESSAGE_TABLE + " where source='"
				+ source + "' or target='" + source + "'";
		Cursor cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 查询用户名字是否为空
	 */
	public List<ChatMessageTabBean> querySourceList(String source) {
		Cursor cursor = null;
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + CHATMESSAGE_TABLE + " where source='"
				+ source + "' and sourcename!=''";
		cursor = db.rawQuery(sql, null);
		return parseCursorToList(cursor);
	}

	/**
	 * 更新消息发送的状态
	 */
	public void UpdateMegIDStatus(int messageId, String sndStatus) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + CHATMESSAGE_TABLE + " set sndStatus='"
				+ sndStatus + "' where messageId='" + messageId + "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 更新语音地址
	 */
	public void UpdateMessageUrle(int messageId, String url) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + CHATMESSAGE_TABLE + " set url='" + url
				+ "' where messageId='" + messageId + "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 更新用户名字
	 */
	public void UpdateSourcename(String source, String sourcename, String sid) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + CHATMESSAGE_TABLE + " set sourcename='"
				+ sourcename + "' ,sid='" + sid + "' where source='" + source
				+ "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 更新用户头像
	 */
	public void UpdateHeadphoto(String source, String headphoto) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + CHATMESSAGE_TABLE + " set headphoto='"
				+ headphoto + "' where source='" + source + "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 查询未读消息
	 */
	public boolean queryIsReadMessage(String source) {
		Cursor cursor = null;
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from " + CHATMESSAGE_TABLE + " where source='"
				+ source + "' and isRead='0'";
		cursor = db.rawQuery(sql, null);
		if (parseCursorToList(cursor).size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 设置所有消息未已读
	 */
	public synchronized void SetMessageIsRead(String source) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "update " + CHATMESSAGE_TABLE
				+ " set isRead='1' where source='" + source + "'";
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 根据createtime创建时间删除消息记录
	 * */
	public synchronized void DeleteMes(String createtime) {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL("delete from " + CHATMESSAGE_TABLE
					+ " where createtime=" + createtime);
			db.close();
		} catch (Exception e) {
		}
	}

	public synchronized void DeleteData() {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(CHATMESSAGE_TABLE, null, null);
		} catch (Exception e) {
		}
	}

}

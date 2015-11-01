package com.newcdc.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

public class ChatMessageDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	public ChatMessageDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
		if (!tabIsExist(db, CHATMESSAGE_TABLE)) {
			db.execSQL("create table " + CHATMESSAGE_TABLE
					+ " (_id integer primary key autoincrement," + "sid text,"
					+ "createtime text," // 创建时间
					+ "pkid text," //
					+ "pkidname text," //
					+ "source text,"// 源客户端号（发送人推送唯一标示）
					+ "sourcename text,"// 源客户端号姓名
					+ "target text," // 目标客户端号（接收人推送唯一标示）
					+ "targetname text," // 目标客户端号姓名
					+ "messagetype text," // 消息类型 0：文本消息；1：图片消息；2：音频消息
					+ "content text," // 消息内容
					+ "url text," // 资源地址
					+ "app_id text," // 应用编号
					+ "app_name text," // 应用名称
					+ "tag_id text," // 标签编号
					+ "tag_name text," // 标签名称
					+ "delvorgcode text," // 用户的机构号
					+ "username text," // 用户工号
					+ "isRead text," // 是否已读 未读？0 已读？1
					+ "speechMessageTime integer," // 语音的时间
					+ "sndStatus text," // 发送状态 成功0 失败1 未发送之前2
					+ "messageId integer," // 消息id
					+ "headphoto text" // 用户头像
					+ ")");
		}
	}

}

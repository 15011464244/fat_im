/**
 * 
 */
package com.newcdc.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;

/**
 * @author zhangfan 2015-1-26,上午11:18:57
 * 
 */
public class GroupDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	public GroupDaoHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, GROUP_TABLE)) {
			db.execSQL("create table " + GROUP_TABLE
					+ "(_id integer primary key autoincrement,"
					+ "group_content text,"// 分组名
					+ "sid text,"// 记录唯一标识
					+ "group_type text,"// 分组类型 //constant
					+ "create_by text,"// 创建人
					+ "create_time text,"// 创建时间
					+ "update_by text,"// 修改人
					+ "update_time text,"// 修改时间
					+ "latlng text,"// 经纬度 用逗号分隔
					+ "distance integer,"// 距离
					+ "mail_count integer)");// 邮件数量
		}
	}

}

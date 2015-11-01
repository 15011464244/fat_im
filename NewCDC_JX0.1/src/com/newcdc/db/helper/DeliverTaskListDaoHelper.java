package com.newcdc.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.cdc.BaseSQLiteOpenHelper;
import com.newcdc.db.DeliverDbConstants;
/**
 * @author hanrong
 * @version 创建时间：2015-4-9 下午4:28:44 类说明 投递任务daoHelper
 */
public class DeliverTaskListDaoHelper extends BaseSQLiteOpenHelper implements
		DeliverDbConstants {

	public DeliverTaskListDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onCreate(db);
		if (!tabIsExist(db, DELIVTERTASKLIST_TABLE)) {
			db.execSQL("create table " + DELIVTERTASKLIST_TABLE
					+ " (_id integer primary key autoincrement,"
					+ "createtime text," 	//创建时间
					+ "delvorgcode text,"	//用户的机构号
					+ "username text,"	//用户工号 
					+ "delivertasknum text," // 投递任务频次
					+ "delivertaskname text,"// 投递任务名称
					+ "delivertaskcode text,"// 投递任务code
					+ "delivertaskcount text," // 投递任务个数
					+ "delivertaskallcount text," // 投递任务总数
					+ "delivertaskstatus text," // 投递任务状态
					+ "delivertasktime text" //  任务时间
					+ ")");
		}
	} 

}

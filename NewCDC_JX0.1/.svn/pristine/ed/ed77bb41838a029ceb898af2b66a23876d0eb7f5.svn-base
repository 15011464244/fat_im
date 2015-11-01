package com.newcdc.db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.newcdc.db.helper.QsjGndmDaoHelper;

public class QsjGndmDao extends QsjGndmDaoHelper {


	public QsjGndmDao(Context context) {
		super(context, DELIVER_DBNAME, null, DELIVER_DBVERSION);
	}

	/**
	 * 根据输入内容，查询相应SQL，再封装成Cursor返回
	 * @param str
	 * @return
	 */
	private Cursor getCursorByInputStr(String str,SQLiteDatabase db,String[] colums){
		Cursor result = null;
		String strPattern = "^[0-9]*$";
		Pattern p = Pattern.compile(strPattern);
		Matcher matcher = p.matcher(str);
		// 输入有内容，并且输入两位起发起查询
		if(str != null && str.trim().length() > 1){
			int strLength = str.trim().length();
			if(strLength == 2){// 2位的优先查询名称[DMMC-地名名称]、[PYDM-拼音代码]、[QPM-全拼码]、[DSMC-地市名称]
				result = db.query(TABLE_NAME,colums,
						" (DMMC like '%" + str + "%') " + 
						" or (PYDM like '" + str + "%') " + 
						" or (QPM  like '" + str + "%') " + 
						" or (DSMC like '%" + str + "%') " + 
					    " or (DMBM like '" + str + "%')",null, null, null, " DMMC asc,PYDM asc,QPM asc,DSMC asc,DMBM asc"," 0,20 ");
			}else if(strLength > 2 && strLength < 5){// 3位或4位的优先查询区号
				if(matcher.matches()){// 输入的是数字[DHQH-区号]
					String querySql = "select DMDM, DMMC, SJDMDM, XZQH, DMJB, SFMC, SFJM, SMJC, SFDMDM, DSMC, DSJM, DSDMDM, XSMC, XSJM, XSDMDM, PYDM, WBDM, QPM, GATBZ, SYZT, YZBM, DHQH, DMBM, XSSX" +
							" from qsj_gndm " +
							" where DSDMDM in (select DSDMDM from qsj_gndm where DHQH='" + str + "') order by YZBM asc limit 0,20";
					result = db.rawQuery(querySql,null);
				}else{
					result = db.query(TABLE_NAME,colums,
							" (DMMC like '%" + str + "%') " + 
							" or (PYDM like '" + str + "%') " + 
							" or (QPM  like '" + str + "%') " + 
							" or (DSMC like '%" + str + "%') " + 
						    " or (DMBM like '" + str + "%')",null, null, null, " DMMC asc,PYDM asc,QPM asc,DSMC asc,DMBM asc"," 0,20 ");
				}
			}else if(strLength > 4){// 5位及以上的优先查询邮编
				if(matcher.matches()){// 输入的是数字[YZBM-邮政编码]
					result = db.query(TABLE_NAME,colums,
							" YZBM like '%" + str + "%' ",null, null, null, " YZBM asc"," 0,20 ");
				}else{
					result = db.query(TABLE_NAME,colums,
							" (DMMC like '%" + str + "%') " + 
							" or (PYDM like '" + str + "%') " + 
							" or (QPM  like '" + str + "%') " + 
							" or (DSMC like '%" + str + "%') " + 
						    " or (DMBM like '" + str + "%')",null, null, null, " DMMC asc,PYDM asc,QPM asc,DSMC asc,DMBM asc"," 0,20 ");
				}
			}
		}
		return result;
	}
	
	public List<Map<String, String>> FindData(String str) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "DMDM", "DMMC", "SJDMDM", "XZQH", "DMJB", "SFMC", "SFJM", "SMJC", "SFDMDM", "DSMC", "DSJM", "DSDMDM", "XSMC", "XSJM", "XSDMDM", "PYDM", "WBDM", "QPM", "GATBZ", "SYZT", "YZBM", "DHQH", "DMBM", "XSSX"};
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = this.getCursorByInputStr(str, db, colums);
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("DMDM", cursor.getString(0));
					paramsMap.put("DMMC", cursor.getString(1));
					paramsMap.put("SJDMDM", cursor.getString(2));
					paramsMap.put("XZQH", cursor.getString(3));
					paramsMap.put("DMJB", cursor.getString(4));
					paramsMap.put("SFMC", cursor.getString(5));
					paramsMap.put("SFJM", cursor.getString(6));
					paramsMap.put("SMJC", cursor.getString(7));
					paramsMap.put("SFDMDM", cursor.getString(8));
					paramsMap.put("DSMC", cursor.getString(9));
					paramsMap.put("DSJM", cursor.getString(10));
					paramsMap.put("DSDMDM", cursor.getString(11));
					paramsMap.put("XSMC", cursor.getString(12));
					paramsMap.put("XSJM", cursor.getString(13));
					paramsMap.put("XSDMDM", cursor.getString(14));
					paramsMap.put("PYDM", cursor.getString(15));
					paramsMap.put("WBDM", cursor.getString(16));
					paramsMap.put("QPM", cursor.getString(17));
					paramsMap.put("GATBZ", cursor.getString(18));
					paramsMap.put("SYZT", cursor.getString(19));
					paramsMap.put("YZBM", cursor.getString(20));
					paramsMap.put("DHQH", cursor.getString(21));
					paramsMap.put("DMBM", cursor.getString(22));
					paramsMap.put("XSSX", cursor.getString(23));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}
	
	public synchronized String FindDaName(String str) {
		String dataList = "";

		Cursor cursor = null;
		String[] colums = { "DMMC,DSMC" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums, " DMDM=?",
					new String[] { str }, null, null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					dataList = cursor.getString(0);
					dataList += "("+cursor.getString(1)+")";
				}
			}
		} catch (Exception e) {
			Log.e("QsjGndmDao-->FindDaName", e.getMessage());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public String trimString(String str) {
		if (str != null) {
			return str.trim();
		}
		return null;
	}

	public void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}

}

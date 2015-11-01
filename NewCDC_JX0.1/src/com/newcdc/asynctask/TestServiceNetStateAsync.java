/**
 * 
 */
package com.newcdc.asynctask;

import com.newcdc.service.TestServiceNetStateService;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.SharePreferenceUtilDaoFactory;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author hanrong 2015-3-23,上午11:25:45 服务器状态查询
 */
public class TestServiceNetStateAsync extends AsyncTask<String, String, String> {
	private onPostExecuteListener_NetState onPostExecuteListener_NetState;
	private SharePreferenceUtilDaoFactory shareDao;

	/**
	 * @param onPostExecuteListener_NetState
	 */
	public TestServiceNetStateAsync(Context mContext,
			onPostExecuteListener_NetState onPostExecuteListener_NetState) {
		super();
		shareDao = SharePreferenceUtilDaoFactory.getInstance(mContext);
		this.onPostExecuteListener_NetState = onPostExecuteListener_NetState;
	}

	@Override
	protected String doInBackground(String... arg0) {
		// Log.e("tag", Global.NETSTATE_URL);
		if (!"".equals(shareDao.getServiceAddress())) {
			Log.e("tag",
					"shareDao.getServiceAddress()=="
							+ shareDao.getServiceAddress());
			String result = NetHelper.doGet(shareDao.getServiceAddress()+"PhoneClctAction/connectWork");
			return result;
		} else {
			Log.e("tag", "Global.NETSTATE_URL==" + Global.NETSTATE_URL);
			String result = NetHelper.doGet(Global.NETSTATE_URL);
			return result;
		}

	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub

		TestServiceNetStateService.serviceState = false;
		 Log.e("tag", "返回结果"+result);
		if (!"".equals(shareDao.getServiceAddress())) {
			if (!"1".equals(result)) {
				if (shareDao.getServiceAddress().equals(Global.SERVICE1)) {
					Global.URL = Global.SERVICE2;
					shareDao.setServiceAddress(Global.SERVICE2);
				} else {
					Global.URL = Global.SERVICE1;
					shareDao.setServiceAddress(Global.SERVICE1);
				}
			}else {
				Global.URL = shareDao.getServiceAddress();
			}
		} else {
			if (!"1".equals(result)) {
				if (Global.URL.equals(Global.SERVICE1)) {
					Global.URL = Global.SERVICE2;
					shareDao.setServiceAddress(Global.SERVICE2);
				} else {
					Global.URL = Global.SERVICE1;
					shareDao.setServiceAddress(Global.SERVICE1);
				}
			}
		}

		Global.changUrl();
		// Log.e("tag", "URL=改变" + Global.URL);
		if (onPostExecuteListener_NetState != null) {
			onPostExecuteListener_NetState.onPostExecute(result);
		}
		super.onPostExecute(result);
	}

	public interface onPostExecuteListener_NetState {
		void onPostExecute(String result);
	}

}

package com.newcdc.asynctask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.newcdc.model.DeliverQueueBean;
import com.newcdc.tools.Constant;
import com.newcdc.tools.Global;
import com.newcdc.tools.NetHelper;
import com.newcdc.tools.UserInfoUtils;

public class CommitPictureTask extends AsyncTask<Void, Void, Integer> {
	private ArrayList<DeliverQueueBean> mListBean;
	private String orgCode;
	private String userName;
	private String picturePatch = Constant.DELIVER_OK;

	public CommitPictureTask(ArrayList<DeliverQueueBean> mListBean,
			Context context) {
		super();
		this.mListBean = mListBean;
		UserInfoUtils user = new UserInfoUtils(context);
		orgCode = user.getUserDelvorgCode();
		userName = user.getUserName();
	}

	@Override
	protected Integer doInBackground(Void... params) {
		for (int i = 0; i < Constant.REPEAT_TIME; i++) {
			mListBean = continueTimes(mListBean);
			if (mListBean.size() == 0) {
				break;
			}
		}
		return null;
	}

	public ArrayList<DeliverQueueBean> continueTimes(
			ArrayList<DeliverQueueBean> listBean) {
		ArrayList<DeliverQueueBean> beans = listBean;
		for (int i = 0; i < listBean.size(); i++) {
			DeliverQueueBean bean = listBean.get(i);
			try {
				JSONObject obj = commitPicture(bean);
				if ("1".equals(obj.getString("result"))) {
					// 成功则移出这个bean
					beans.remove(bean);
				}
			} catch (Exception e) {
			}
		}
		return beans;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
	}

	public JSONObject commitPicture(DeliverQueueBean bean) {
		if (bean.getPicture() != null && !"".equals(bean.getPicture())
				&& !"null".equals(bean.getPicture())) {// 上传图片
			JSONObject jsonObj = null;
			// 将图片的字节流数据加密成base64字符输出
			Bitmap map = BitmapFactory.decodeFile(picturePatch
					+ bean.getPicture());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			map.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				map.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				options -= 10;// 每次都减少10
			}
			byte[] imgs = baos.toByteArray();// 字节
			String photo = Base64.encodeToString(imgs, 0, imgs.length,
					Base64.DEFAULT);
			List<NameValuePair> paramObject = new ArrayList<NameValuePair>();
			paramObject.add(new BasicNameValuePair("dlvorgcode", orgCode));
			paramObject.add(new BasicNameValuePair("username", userName));
			paramObject.add(new BasicNameValuePair("mail_num", bean
					.getMail_num()));
			paramObject.add(new BasicNameValuePair("image", photo));
			try {
				jsonObj = new JSONObject(NetHelper.doPost(Global.SAVETTIMAGE,
						paramObject));
				return jsonObj;
			} catch (Exception e) {
			}
		}
		return null;
	}
}

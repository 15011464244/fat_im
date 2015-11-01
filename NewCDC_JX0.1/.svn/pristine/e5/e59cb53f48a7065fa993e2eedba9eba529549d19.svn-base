package com.newcdc.tools;

import android.content.Context;

import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.NewUserDao;
import com.newcdc.model.LoginNew_ContentBean;
import com.newcdc.model.UserInfoBean;

/**
 * @author hanrong
 * @version 创建时间：2014-12-25 上午10:45:14 类说明
 */
public class UserInfoUtils {
	private Context context = null;
//	private UserDao mUserDao = null;
	private NewUserDao mNewUserDao = null;
	private String userName = "";
	private String userDelvorgCode = "";
	private String userPWD = "";
	private String dlvsectionid = "";
	private String dlvsectionCode = "";
	private String sid = "";
	private String dlvflag = "";
	private String realname = "";
	private String mobile = "";
//	private UserInfoBean bean = null;
	private String postcode = null;
	private LoginNew_ContentBean contentBean = null; 
	public UserInfoUtils(Context context) {
		if (context != null) {
			this.context = context;
			userInfo();
		}
	}

	public synchronized void userInfo() {
//		mUserDao = DeliverDaoFactory.getInstance().getUserDao(context);
//		bean = mUserDao.getUserInfo();
		mNewUserDao = DeliverDaoFactory.getInstance().getNewUserDao(context);
		contentBean = mNewUserDao.getUserInfo();
		
		try {
			if (contentBean.getUsercode() != null
					&& !"".equals(contentBean.getUsercode())) {
				userName = contentBean.getUsercode();
				// Log.e("tag", userName);
			}
			if (contentBean.getOrgcode() != null
					&& !"".equals(contentBean.getOrgcode())) {
				userDelvorgCode = contentBean.getOrgcode();
				// Log.e("tag", userDelvorgCode);
			}
			if (contentBean.getSectioncode()!= null
					&& !"".equals(contentBean.getSectioncode())) {
				dlvsectionid = contentBean.getSectionname();
				// Log.e("tag", dlvsectionid);
			}
//			if (contentBean.getLub().getDlvflag() != null
//					&& !"".equals(contentBean.getLub().getDlvflag())) {
//				dlvflag = contentBean.getLub().getDlvflag();
//				// Log.e("tag", dlvflag);
//			}
			if (contentBean.getUsername() != null
					&& !"".equals(contentBean.getUsername())) {
				realname = contentBean.getUsername();
				// Log.e("tag", realname);
			}
//			if (contentBean.getLub().getMobile() != null
//					&& !"".equals(contentBean.getLub().getMobile())) {
//				mobile = contentBean.getLub().getMobile();
//				// Log.e("tag", mobile);
//			}
//			if (contentBean.getLub().getPostcode() != null
//					&& !"".equals(contentBean.getLub().getPostcode())) {
//				postcode = contentBean.getLub().getPostcode();
//				// Log.e("tag", mobile);
//			}
			if (contentBean.getSectioncode() != null
					&& !"".equals(contentBean.getSectioncode())) {
				dlvsectionCode = contentBean.getSectioncode();
			}

		} catch (Exception e) {
		}

	}

	public String getDlvsectionCode() {
		return dlvsectionCode;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserDelvorgCode() {
		return userDelvorgCode;
	}

	public String getUserPWD() {
		return userPWD;
	}

	public String getSid() {
		return sid;
	}

	public String getDlvflag() {
		return dlvflag;
	}

	public String getRealname() {
		return realname;
	}

	public String getMobile() {
		return mobile;
	}

	public String getDlvsectionid() {
		return dlvsectionid;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
}

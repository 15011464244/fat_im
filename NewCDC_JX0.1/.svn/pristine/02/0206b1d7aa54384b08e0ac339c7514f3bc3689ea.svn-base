package com.cn.cdc;

import android.content.Context;

public class DaoFactory {
	private static DaoFactory instance = null;
	private DlvStateDao dlvStateDao = null;
	private BaseDataDao baseDataDao = null;
	private StateFollowDao stateFollowDao = null;
	private FollowActionDao followActionDao = null;
	private MailDao mailDao = null;
	private DownLoadDao downLoadDao = null;
	private ResOrgDao resOrgDao = null;
	private TransferDao transferDao = null;
	private LocCountyNanJingDao locCountyNanJingDao = null;

	public static DaoFactory getInstance() {
		if (instance == null) {
			instance = new DaoFactory();
		}
		return instance;
	}

	public void init(Context globalContext) {
		baseDataDao = getInstance().getBaseDataDao(globalContext);
		dlvStateDao = getInstance().getDlvStateDao(globalContext);
		mailDao = getInstance().getMailDao(globalContext);
		downLoadDao = getInstance().getDownLoadDao(globalContext);
		resOrgDao = getInstance().getResOrgDao(globalContext);
		transferDao = getInstance().getTransferDao(globalContext);
	}

	private DaoFactory() {

	}

	public FollowActionDao getFollowActionDao(Context context) {

		if (followActionDao == null) {
			followActionDao = new FollowActionDao(context);
		}
		return followActionDao;
	}

	public LocCountyNanJingDao getLocCountyNanJingDao(Context context) {
		if (locCountyNanJingDao == null) {
			locCountyNanJingDao = new LocCountyNanJingDao(context);
		}
		return locCountyNanJingDao;
	}

	public ResOrgDao getResOrgDao(Context context) {

		if (resOrgDao == null) {
			resOrgDao = new ResOrgDao(context);
		}
		return resOrgDao;
	}

	public DownLoadDao getDownLoadDao(Context context) {

		if (downLoadDao == null) {
			downLoadDao = new DownLoadDao(context);
		}
		return downLoadDao;
	}

	public StateFollowDao getStateFollowDao(Context context) {

		if (stateFollowDao == null) {
			stateFollowDao = new StateFollowDao(context);
		}
		return stateFollowDao;
	}

	public MailDao getMailDao(Context context) {

		if (mailDao == null) {
			mailDao = new MailDao(context);
		}
		return mailDao;
	}

	public DlvStateDao getDlvStateDao(Context context) {

		if (dlvStateDao == null) {
			dlvStateDao = new DlvStateDao(context);
		}
		return dlvStateDao;
	}

	public BaseDataDao getBaseDataDao(Context context) {

		if (baseDataDao == null) {
			baseDataDao = new BaseDataDao(context);
		}
		return baseDataDao;
	}

	public TransferDao getTransferDao(Context context) {

		if (transferDao == null) {
			transferDao = new TransferDao(context);
		}
		return transferDao;
	}

}

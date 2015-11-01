package com.newcdc.tools;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;

import com.newcdc.db.DeliverDao;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.QueueDao;
import com.newcdc.model.DeliverQueueBean;
import com.newcdc.model.MessageInfoBean;
import com.newcdc.service.JXAsyncQueueService;

public class DealDeliverTools {
	/**
	 * 传入下段邮件集合，启动异步服务
	 * 
	 * @param messageBeanlist
	 * @param context
	 */
	public static void startServiceNormal(
			ArrayList<MessageInfoBean> messageBeanlist, int dealType,
			Context context) {
		DeliverDaoFactory daoFactory = DeliverDaoFactory.getInstance();
		QueueDao queueDao = daoFactory.getQueueDao(context);
		DeliverDao deliverDao = daoFactory.getDeliverDao(context);
		ArrayList<DeliverQueueBean> queueList = new ArrayList<DeliverQueueBean>();
		switch (dealType) {
		case Constant.TUOTOU:
			for (int i = 0; i < messageBeanlist.size(); i++) {
				MessageInfoBean bean = messageBeanlist.get(i);
				DeliverQueueBean queueBean = DeliverQueueBean.createBean(bean,
						"W", bean.getMoney(), "", "", "", "0", Constant.TUOTOU,
						context);
				queueBean.setCommit_result(Constant.UNCOMMIT);
				queueList.add(queueBean);
			}
			break;
		case Constant.WEITUOTOU:
			for (int i = 0; i < messageBeanlist.size(); i++) {
				MessageInfoBean bean = messageBeanlist.get(i);
				DeliverQueueBean queueBean = DeliverQueueBean.createBean(bean,
						"", bean.getMoney(), "14", "A", "", "",
						Constant.WEITUOTOU, context);
				queueBean.setCommit_result(Constant.UNCOMMIT);
				queueList.add(queueBean);
			}
			break;
		}
		queueDao.insert(queueList, context);
		// 标记位妥投
		deliverDao.updateListMailDealResult(
				Utils.parseBeanToIdList(messageBeanlist), dealType);
		context.startService(new Intent(context, JXAsyncQueueService.class));
	}

	public static void startServiceUserInput(
			ArrayList<DeliverQueueBean> queueList, Context context) {
		QueueDao queueDao = DeliverDaoFactory.getInstance()
				.getQueueDao(context);
		queueDao.insert(queueList, context);
		context.startService(new Intent(context, JXAsyncQueueService.class));
	}

	/**
	 * 从下段信息获取到妥投和未妥投的数据并插入到本表中
	 */
	public static void insertDeliverData(
			ArrayList<MessageInfoBean> messageBeanlist, Context context) {
		QueueDao queueDao = DeliverDaoFactory.getInstance()
				.getQueueDao(context);
		ArrayList<DeliverQueueBean> queueList = new ArrayList<DeliverQueueBean>();
		for (int i = 0; i < messageBeanlist.size(); i++) {
			MessageInfoBean bean = messageBeanlist.get(i);
			if (bean.getDealResult() != Constant.DAICHULI) {
				DeliverQueueBean queueBean = DeliverQueueBean.createBean(bean,
						bean.getSign_sts_code(), bean.getMoney(),
						bean.getUndlv_cause_code(),
						bean.getUndlv_next_actn_code(), bean.getSigner_name(),
						"0", bean.getDealResult(), context);
				queueBean.setCommit_result(Constant.COMMIT);
				queueList.add(queueBean);
			}
		}
		queueDao.insertXD(queueList, context);
	}
}

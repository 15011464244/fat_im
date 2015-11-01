package com.newcdc.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

import com.newcdc.asynctask.CommitPictureTask;
import com.newcdc.asynctask.DeliverTask;
import com.newcdc.db.DeliverDaoFactory;
import com.newcdc.db.QueueDao;
import com.newcdc.model.DeliverQueueBean;
import com.newcdc.tools.Utils;
import com.newcdc.ui.XFAudio;

/**
 * 
 * @author zhangfan
 *
 */
public class AsyncQueueService extends IntentService {
	private Handler mHandler;

	public AsyncQueueService() {
		super("aaa");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (Utils.isNetworkAvailable(getApplicationContext())) {
			// 上传任务每批次要提交的邮件个数
			int repeatCount = intent.getIntExtra("repeatCount", 20);
			mHandler = new Handler();
			QueueDao queueDao = DeliverDaoFactory.getInstance().getQueueDao(
					getApplicationContext());
			// 查出所有未提交的邮件
			final ArrayList<DeliverQueueBean> allList = queueDao.queryAll();
			if (allList.size() != 0) {
				// 当邮件个数不为0时，进行投递反馈提交
				final int result = DeliverTask.doDeliverRequest(
						getApplicationContext(), repeatCount);
				// sendBroadcast(new Intent(Constant.ACTION_ASYNCQUEUE));
				final int failMailCount = queueDao.queryAll().size();// 剩余的邮件数量，即失败的数量
				if (failMailCount != 0) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							// 语音提示处理结果
							String showResult = "投递信息上传完成，失败" + failMailCount
									+ "条";
							XFAudio audio = new XFAudio(
									getApplicationContext(), showResult);
							audio.startSay();
							// 发送广播更改主界面的跑马灯
							Utils.sendTitleBroadcast(getApplicationContext(),
									showResult);
						}
					});
				} else {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							// 语音提示处理结果
							String showResult = "投递信息上传成功";
							XFAudio audio = new XFAudio(
									getApplicationContext(), showResult);
							audio.startSay();
						}
					});
				}
				switch (result) {
				case 1:
					new CommitPictureTask(allList, getApplicationContext())
							.execute();
					break;
				case 0:
					break;
				}
			}
		}
	};

}

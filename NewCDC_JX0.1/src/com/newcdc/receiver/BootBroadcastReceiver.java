package com.newcdc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.newcdc.tools.Utils;

public class BootBroadcastReceiver extends BroadcastReceiver {
	static final String action_boot = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(action_boot)) {
			Utils.startIntentService(context);
		}
	}
}

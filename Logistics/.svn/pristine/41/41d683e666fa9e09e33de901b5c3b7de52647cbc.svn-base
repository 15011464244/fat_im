package com.ems.express.util;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class DeviceUuidFactory {
	protected static final String PREFS_FILE = "device_id.xml";
	protected static final String PREFS_DEVICE_ID = "device_id";
	protected static UUID uuid;

	public DeviceUuidFactory(Context context) {
		if (uuid == null) {
			synchronized (DeviceUuidFactory.class) {
				if (uuid == null) {
					// 使用SharedPreferences保存数据,其实质是采用了xml文件存放数据,路径为:/data/data/<package
					// name>/shared_prefs
					final SharedPreferences prefs = context
							.getSharedPreferences(PREFS_FILE,
									Context.MODE_PRIVATE);
					// Context.MODE_PRIVATE：为默认操作模式,代表该文件是私有数据,只能被应用本身访问,在该模式下,写入的内容会覆盖原文件的内容
					// Context.MODE_APPEND：模式会检查文件是否存在,存在就往文件追加内容,否则就创建新文件.
					// Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件.
					// MODE_WORLD_READABLE：表示当前文件可以被其他应用读取.
					// MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入.
					final String id = prefs.getString(PREFS_DEVICE_ID, null);
					if (id != null) {
						// Use the ids previously computed and stored in the
						// prefs file
						uuid = UUID.fromString(id);
					} else {
						final String androidId = Secure
								.getString(context.getContentResolver(),
										Secure.ANDROID_ID);
						// Use the Android ID unless it's broken, in which case
						// fallback on deviceId,
						// unless it's not available, then fallback on a random
						// number which we store
						// to a prefs file
						try {
							if (!"9774d56d682e549c".equals(androidId)) {
								uuid = UUID.nameUUIDFromBytes(androidId
										.getBytes("utf8"));
							} else {
								final String deviceId = ((TelephonyManager) context
										.getSystemService(Context.TELEPHONY_SERVICE))
										.getDeviceId();
								uuid = deviceId != null ? UUID
										.nameUUIDFromBytes(deviceId
												.getBytes("utf8")) : UUID
										.randomUUID();
							}
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
						// Write the value out to the prefs file
						prefs.edit()
								.putString(PREFS_DEVICE_ID, uuid.toString())
								.commit();
					}
				}
			}
		}
	}

	public UUID getDeviceUuid() {
		return uuid;
	}
}

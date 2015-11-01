package com.ems.express.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class VoiceUtil {

	/**
	 * 得到amr的时长
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static int getAmrDuration(File file) throws IOException {
		long duration = -1;
		int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0,
				0, 0 };
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "rw");
			long length = file.length();// 文件的长度
			int pos = 6;// 设置初始位置
			int frameCount = 0;// 初始帧数
			int packedPos = -1;

			byte[] datas = new byte[1];// 初始数据值
			while (pos <= length) {
				randomAccessFile.seek(pos);
				if (randomAccessFile.read(datas, 0, 1) != 1) {
					duration = length > 0 ? ((length - 6) / 650) : 0;
					break;
				}
				packedPos = (datas[0] >> 3) & 0x0F;
				pos += packedSize[packedPos] + 1;
				frameCount++;
			}

			duration += frameCount * 20;// 帧数*20
		} finally {
			if (randomAccessFile != null) {
				randomAccessFile.close();
			}
		}
		return (int) ((duration / 1000)-1);
	}
}

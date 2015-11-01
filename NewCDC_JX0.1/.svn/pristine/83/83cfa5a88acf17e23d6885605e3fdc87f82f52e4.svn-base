package com.newcdc.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.lidroid.xutils.util.LogUtils;
import com.newcdc.tools.Constant;

public class GetBitmap {
	public static byte[] getByteFromImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			options -= 10;// 每次都减少10
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		return baos.toByteArray();
	}

	/**
	 * 保存图片
	 * */
	public static void saveBitmap(Bitmap bm, String name) {
		File f = new File(Constant.SD);
		f.mkdirs();
		File file = new File(Constant.SD + name);
		try {
			FileOutputStream out = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存图片
	 * */
	public static void saveBitmapW(Bitmap bm, String name) {
		File f = new File(Constant.WEATHER);
		f.mkdirs();
		File file = new File(Constant.WEATHER + name);
		try {
			FileOutputStream out = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Bitmap放大的方法 */
	public static Bitmap big(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(3.0f, 3.0f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	/** Bitmap缩小的方法 */
	public static Bitmap small(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.8f, 0.8f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	public static File getimageyang(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		FileOutputStream fos = null;
		// String newPath;
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
		newOpts.inJustDecodeBounds = false;// 重新读入图片，注意此时已经把options.inJustDecodeBounds
											// 设回false了
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;
		float ww = 480f;
		// 缩放比，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		bitmap = compressImageyang(BitmapFactory.decodeFile(srcPath, newOpts));
		bitmap = getBitmapRotate(bitmap, srcPath);
		LogUtils.e("bitMap ====== " + bitmap);
		String newPath = GetBitmap.getPhotoFileName();// 新文件的名字
		GetBitmap.getFilePath(Constant.SD, newPath);
		try {
			fos = new FileOutputStream(GetBitmap.getFilePath(Constant.SD,
					newPath));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			LogUtils.e("错误信息:        " + e.getMessage().toString());
			e.printStackTrace();
		}
		return GetBitmap.getFilePath(Constant.SD, newPath);// 压缩好比例大小后再进行质量压缩
	}

	// 使用系统当前日期加以调整作为照片的名称
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date);
	}

	// 图片旋转
	public static Bitmap getBitmapRotate(Bitmap bitmap, String path) {
		Matrix matrix = new Matrix();
		float angle;
		int fa = 0;
		angle = readPictureDegree(path);
		matrix.postRotate(angle);
		if (angle != 0) {
			bitmap = Bitmap.createBitmap(bitmap, fa, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
		}

		return bitmap;
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			LogUtils.i("旋转角度：         " + orientation);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		LogUtils.i("旋转角度：         " + degree);
		return degree;
	}

	public static File getFilePath(String filePath, String fileName) {
		File file = null;
		makeRootDirectory(filePath);
		try {
			file = new File(filePath + fileName+".jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
		}
	}

	public static Bitmap compressImageyang(Bitmap image) {
		boolean sizeFlag = false;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		LogUtils.e("length ====== " + baos.toByteArray().length / 1024);
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			sizeFlag = true;
			LogUtils.e("length ===100=== " + baos.toByteArray().length / 1024);
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		if (sizeFlag) {
			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
			image = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		}

		return image;
	}
}

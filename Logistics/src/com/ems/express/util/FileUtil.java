package com.ems.express.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.zip.ZipInputStream;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * 文件类<br>
 */
public class FileUtil {

	// 复制资源文件
	public static String ASSETS_NAME = "cjsc.zip";
	private static final int ASSETS_SUFFIX_BEGIN = 101;
	private static final int ASSETS_SUFFIX_END = 102;
	//新增
	private static String mTempFilePath;


    /**
     * 判断SD卡是否存在
     *
     * @return
     */
    public static boolean isSdcardReady() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
	
	
	
	public static InputStream getInputStreamFromByte(byte[] data) {

		InputStream is = new ByteArrayInputStream(data);
		// Log.save(Log.INFO, "byte => inputStream. ");
		return is;
	}
	
	
	
	/**
	 * 判断文件夹是否存在
	 * 
	 * @param 文件路径
	 * @return返回是否存在
	 */
	public static void makeDir(String filePath) {
		File filetemp = new File(filePath);
		filetemp.mkdir();
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param 文件路径
	 * @return返回是否存在
	 */
	public static boolean exited(String path) {
		File filetemp = new File(path);
		return filetemp.exists();
	}

	/**
	 * 把图片路径生成图片
	 * 
	 * @param 文件路径
	 * @return
	 */
	public static Bitmap ResultImg(String path) {
		Bitmap bit=null;
		try {
			bit = BitmapFactory.decodeFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bit;
	}
	

	/**
	 * @描述： <br>
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] getByteFromInputStream(InputStream is)
			throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}

	/***
	 * 获取文件流
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static byte[] getByteFromFile(String fileName) throws IOException {
		String s;
		byte[] buf;

		File file = new File(fileName);
		// 读文件
		buf = new byte[(int) file.length()];
		new FileInputStream(fileName).read(buf);
		return buf;
	}

	public static void byteToFile(byte[] fb, String fileName)
			throws IOException {
		if (fb != null) {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(new File(fileName)));
			bos.write(fb);// my multile write
			bos.close();
		}
	}

	/**
	 * 将预先写入客户端的aFileIn.bak 重新写入到客户端aFileIn文件<br>
	 * 覆盖客户端同名文件，并删除客户端的aFileIn.bak文件
	 * 
	 * @param inFile
	 *            预先写入客户端的aFileIn.bak
	 * @param outFile
	 *            重新写入本地客户端，其文件名为aFileIn
	 */
	private void copyFile(String inFile, String outFile) {
		/* 定义文件输入流，用来读取数据 */
		FileInputStream fileInputSream = null;

		/* 定义文件输出流，用来向本地客户端写入数据 */
		FileOutputStream fileOutputStream = null;
		try {
			fileInputSream = new FileInputStream(new File(inFile));
			fileOutputStream = new FileOutputStream(new File(outFile));
			byte[] lReadOnetime = new byte[512];
			int lRead = 0;

			/* 通过while方法来读取文件信息，如果读到文件最后，则返回-1， 否则通过文件输出流，将流中数据写入本地客户端 */
			while ((lRead = fileInputSream.read(lReadOnetime)) != -1) {
				fileOutputStream.write(lReadOnetime, 0, lRead);
			}
		} catch (Exception e) {

		}

		/* 重写finally方法，将打开的流关闭 */
		finally {
			try {
				fileInputSream.close();
				fileOutputStream.close();
			} catch (Exception e) {

			}
		}
	}

	private static String getFileContext(String cssFile) throws Exception {
		// FileReader fr = new FileReader(cssFile);
		// int ch = 0;
		// while ((ch = fr.read()) != -1) {
		// }
		
		String context = "";
		BufferedReader br = new BufferedReader(new FileReader(cssFile));
		String data = br.readLine();// 一次读入一行，直到读入null为文件结束
		while (data != null) {
			data = br.readLine(); // 接着读下一行
			context += data+"\n";
		}
		br.close();
		return context;
	}
	
	 /**
     * 获取临时文件
     * @return
     */
    public static String getTempFileName() {
        String fName = DateFormat.format("yyyyMMddhhmmss", new Date())
                .toString() + ".jpg";
        if (mTempFilePath == null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                // 如果有SD卡为sd卡下路径
                mTempFilePath = ConfigData.SDCARD_PATH + "temp/";
            } else {
                // 否则为系统的应用空间下路径
                mTempFilePath = ConfigData.DATA_PATH + "temp/";
            }
        }
        File path = new File(mTempFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(mTempFilePath + fName);
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
        return mTempFilePath + fName;

    }
    /**
     * 获取临时音频文件
     * @return
     */
    public static String getRecordTempFileName() {
        String fName = DateFormat.format("yyyyMMddhhmmss", new Date())
                .toString() + ".amr";
        if (mTempFilePath == null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                // 如果有SD卡为sd卡下路径
                mTempFilePath = ConfigData.SDCARD_PATH + "temp/";
            } else {
                // 否则为系统的应用空间下路径
                mTempFilePath = ConfigData.DATA_PATH + "temp/";
            }
        }
        File path = new File(mTempFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(mTempFilePath + fName);
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
        return mTempFilePath + fName;

    }
    public static String getTempFileName(String mark) {
        String fName = DateFormat.format("yyyyMMddhhmmss", new Date())
                .toString() +mark+ ".jpg";
        if (mTempFilePath == null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                // 如果有SD卡为sd卡下路径
                mTempFilePath = ConfigData.SDCARD_PATH + "temp/";
            } else {
                // 否则为系统的应用空间下路径
                mTempFilePath = ConfigData.DATA_PATH + "temp/";
            }
        }
        File path = new File(mTempFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(mTempFilePath + fName);
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
        return mTempFilePath + fName;

    }

    public static File unzipFile(String path) throws Exception {
		File dir = Environment.getExternalStorageDirectory();
		File temp = new File(dir.getPath() + File.separator
				+ System.currentTimeMillis());
		FileOutputStream fos = new FileOutputStream(temp);
		FileInputStream fis = new FileInputStream(path);
		ZipInputStream zip = new ZipInputStream(fis);
		while (zip.getNextEntry() != null) {
			byte[] buf = new byte[1024 * 10];
			int len = 0;
			while ((len = zip.read(buf, 0, buf.length)) != -1) {
				fos.write(buf, 0, len);
			}
			fos.close();
		}
		zip.close();
		fis.close();
		return temp;
	}

	public static void applySQLs(SQLiteDatabase db, String path)
			throws Exception {
		try {
			db.beginTransaction();
			InputStream is = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			StringBuffer sql = new StringBuffer();
			String tmpStr = null;
			while ((tmpStr = br.readLine()) != null) {
				sql.append(tmpStr);
				sql.append('\n');
				if (tmpStr.trim().endsWith(";")) {
					db.execSQL(sql.toString());
					sql = new StringBuffer();
				}
			}
			br.close();
			isr.close();
			is.close();
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e(FileUtil.class.getName(), "", e);
			throw new RuntimeException("Database create error! Please contact the support or developer.", e);
		} finally {
			db.endTransaction();
		}
	}
	
	public static int updateSql(SQLiteDatabase db, String path)
			throws Exception {
		int i=0;
		try {
			db.beginTransaction();
			InputStream is = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			StringBuffer sql = new StringBuffer();
			String tmpStr = null;
			while ((tmpStr = br.readLine()) != null) {
				sql.append(tmpStr);
				sql.append('\n');
				if (tmpStr.trim().endsWith(";")) {
					db.execSQL(sql.toString());
					sql = new StringBuffer();
					i++;
				}
			}
			br.close();
			isr.close();
			is.close();
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e(FileUtil.class.getName(), "", e);
			throw new RuntimeException("Database create error! Please contact the support or developer.", e);
		} finally {
			db.endTransaction();
		}
		return i;
	}
	
	public static void deleteAllFiles(File root) {  
        File files[] = root.listFiles();  
        if (files != null)  
            for (File f : files) {  
                if (f.isDirectory()) { // 判断是否为文件夹  
                    deleteAllFiles(f);  
                    try {  
                        f.delete();  
                    } catch (Exception e) {  
                    }  
                } else {  
                    if (f.exists()) { // 判断是否存在  
                        deleteAllFiles(f);  
                        try {  
                            f.delete();  
                        } catch (Exception e) {  
                        }  
                    }  
                }  
            }  
    }  
}

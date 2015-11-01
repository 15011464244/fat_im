package com.newcdc.tools;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public abstract class BaseCommand  {


	public static final String TAB_STRING = "\t";

	public static final String ENTER_STRING = "\n";

	public static final String R_NAME = "r";

	public static final String C_NAME = "c";

	public static final String DES = "DES"; 
	
	//密钥Կ
	public static final String SECRETKEY = "4B40A73D";
	public static final String IV = "4B40A73D";

	
     
     /***
      * 压缩GZip
      * 
      * @param data
      * @return
     */
    public static byte[] gZip(byte[] data) {
        byte[] b = null;
        try {
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(bos);
             gzip.write(data);
             gzip.finish();
             gzip.close();
             b = bos.toByteArray();
             bos.close();
         } catch (Exception ex) {
             ex.printStackTrace();
         }
        return b;
     }

    /***
      * 解压GZip
      * 
      * @param data
      * @return
     */
    public static byte[] unGZip(byte[] data) {
        byte[] b = null;
        try {
             ByteArrayInputStream bis = new ByteArrayInputStream(data);
             GZIPInputStream gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num = -1;
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                 baos.write(buf, 0, num);
             }
             b = baos.toByteArray();
             baos.flush();
             baos.close();
             gzip.close();
             bis.close();
         } catch (Exception ex) {
             ex.printStackTrace();
         }
        return b;
     }

    /***
      * 压缩Zip
      * 
      * @param data
      * @return
     */
    public static byte[] zip(byte[] data) {
        byte[] b = null;
        try {
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ZipOutputStream zip = new ZipOutputStream(bos);
             ZipEntry entry = new ZipEntry("zip");
             entry.setSize(data.length);
             zip.putNextEntry(entry);
             zip.write(data);
             zip.closeEntry();
             zip.close();
             b = bos.toByteArray();
             bos.close();
         } catch (Exception ex) {
             ex.printStackTrace();
         }
        return b;
     }

    /***
      *解压Zip
      * 
      * @param data
      * @return
     */
    public static byte[] unZip(byte[] data) {
        byte[] b = null;
        try {
             ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ZipInputStream zip = new ZipInputStream(bis);
            while (zip.getNextEntry() != null) {
                byte[] buf = new byte[1024];
                int num = -1;
                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((num = zip.read(buf, 0, buf.length)) != -1) {
                     baos.write(buf, 0, num);
                 }
                 b = baos.toByteArray();
                 baos.flush();
                 baos.close();
             }
             zip.close();
             bis.close();
         } catch (Exception ex) {
             ex.printStackTrace();
         }
        return b;
     }
    
    

    /**
      *把字节数组转换成16进制字符串
      * 
      * @param bArray
      * @return
     */
    public static String bytesToHexString(byte[] bArray) {
         StringBuffer sb = new StringBuffer(bArray.length);
         String sTemp;
        for (int i = 0; i < bArray.length; i++) {
             sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                 sb.append(0);
             sb.append(sTemp.toUpperCase());
         }
        return sb.toString();
     }
     
	
	public static String format(String str){
			
			String s = "";
			
			if(str != null && !"".equals(str)){
				
				String st[] = str.split(" ");
				
				String s1[] = st[0].split("-");
				String s2[] = st[1].split(":");
				
				s = s1[0].concat(s1[1]).concat(s1[2]).concat(s2[0]).concat(s2[1]).concat(s2[2]);
			}
			
			return s;
		}

	

	/**
	 * 压缩
	 * 
	 * @param image
	 * @return
	 */
	public static byte[] compressImage(Bitmap bMap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			bMap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		bMap.recycle();
		return baos.toByteArray();

	}

	/**
	 * 旋转90
	 * 
	 * @param imgs
	 * @return
	 */
	public static Bitmap xuImage(byte[] imgs) {
		ByteArrayInputStream isBm = new ByteArrayInputStream(imgs);//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.postRotate(90);
		Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();//回收
		return bMapRotate;
	}

	/**
	 * 比例缩小
	 * 
	 * @param imgs
	 * @return
	 */
	public static  byte[] comp(Bitmap bm ) {
		byte[] imgs =Bitmap2Bytes(bm);
		BitmapFactory.Options options=new BitmapFactory.Options();
	    options.inDither=false;    /*不进行图片抖动处理*/
	    options.inPreferredConfig=null;  /*设置让解码器以最佳方式解码*/
	    options.inSampleSize=2;          /*图片长宽方向缩小倍数*/
	    Bitmap img=BitmapFactory.decodeByteArray(imgs, 0, imgs.length, options);
		return compressImage(img);// 压缩好比例大小后再进行质量压缩
	}
	
	 /**  
     * 把Bitmap转Byte   
     */    
    public static byte[] Bitmap2Bytes(Bitmap bm){    
        ByteArrayOutputStream baos = new ByteArrayOutputStream();    
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);    
        return baos.toByteArray();    
    }  
	
    
	
	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
	
	/**
	 * 解码
	 * @param encodeStr
	 * @return
	 */
	public static String  decodeStr(String encodeStr){
		BASE64Decoder base64Decoder=new BASE64Decoder();
		try {
			byte[] byt =base64Decoder.decodeBuffer( encodeStr);
			return new String(byt, "UTF-8");
		} catch (IOException e) {
			return "";
		}

	}
	
	/**
	 * 编码
	 * @param decodeStr
	 * @return
	 */
	public static String  encodeStr(String decodeStr){
		BASE64Encoder base64Encoder=new BASE64Encoder();
		try {
			String str=base64Encoder.encode(decodeStr.getBytes("UTF-8"));
			return str;
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	
	}
	
//	public static Bitmap CreateTwoDCode(String content) throws WriterException {
//
//		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
//		BitMatrix matrix = new MultiFormatWriter().encode(content,
//				BarcodeFormat.QR_CODE, 300, 300);
//
//		int width = matrix.getWidth();
//		int height = matrix.getHeight();
//		
//		//二维矩阵转为一维像素数组,也就是一直横着排了
//		int[] pixels = new int[width * height];
//
//		for (int y = 0; y < height; y++) {
//			for (int x = 0; x < width; x++) {
//				if (matrix.get(x, y)) {
//					pixels[y * width + x] = 0xff000000;
//				}
//			}
//		}
//
//		Bitmap bitmap = Bitmap.createBitmap(width, height,
//				Bitmap.Config.ARGB_8888);
//
//		//通过像素数组生成bitmap,具体参考api
//		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//		return bitmap;
//	}
}

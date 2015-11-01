package com.newcdc.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 工具类 ：根据位置信息获取经纬度，计算距离
 */
public class LocationDistance {
	public static double getDistatce(String cLatlng, String newLatlng) {
		double lat1 = Double.parseDouble(cLatlng.split(",")[0]);
		double lat2 = Double.parseDouble(newLatlng.split(",")[0]);
		double lon1 = Double.parseDouble(cLatlng.split(",")[1]);
		double lon2 = Double.parseDouble(newLatlng.split(",")[1]);
		double R = 6371;
		double distance = 0.0;
		// Log.e("111", lat1 + "-" + lon1 + "," + lat2 + "-" + lon2);
		double dLat = (lat2 - lat1) * Math.PI / 180;
		double dLon = (lon2 - lon1) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(lat1 * Math.PI / 180)
				* Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R;
		return distance;
	}

	/**
	 * @param addr
	 *            查询的地址
	 * @return
	 * @throws IOException
	 */
	public static String getCoordinate(String addr) throws IOException {
		String address = "";
		StringBuffer sb;
		if (addr != null) {
			try {
				address = java.net.URLEncoder.encode(addr, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			String key = "fKI8sgeeLTfFhV46L7w8yHyh";
			String url = String
					.format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s",
							address, key);

			URL myURL = null;
			URLConnection httpsConn = null;
			try {
				myURL = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			InputStreamReader insr = null;
			BufferedReader br = null;
			try {
				httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
				if (httpsConn != null) {
					insr = new InputStreamReader(httpsConn.getInputStream(),
							"UTF-8");
					br = new BufferedReader(insr);
					String data = null;
					sb = new StringBuffer();
					while ((data = br.readLine()) != null) {
						sb.append(data);
					}
					String latlng = parseJson(sb.toString());
					return latlng;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (insr != null) {
					insr.close();
				}
				if (br != null) {
					br.close();
				}
			}
		}
		return null;
	}

	public static String parseJson(String data) {
		String lat = "";
		String lng = "";
		try {
			JSONObject object = new JSONObject(data);
			try {

				JSONObject result = object.getJSONObject("result");
				JSONObject location = result.getJSONObject("location");
				lng = location.getString("lng");
				lat = location.getString("lat");
			} catch (Exception e) {
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lat + "," + lng;
	}
}

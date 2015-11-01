package com.newcdc.weather;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class PaserJson {
	public static List<WeatherBean> paserJson(String jsonString) throws Exception{
		List<WeatherBean> list = new ArrayList<WeatherBean>();
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("results");
			JSONObject jsonObject2 = jsonArray.getJSONObject(0);
			JSONArray jsonArray2 = jsonObject2.getJSONArray("weather_data");
			for(int i = 0;i<jsonArray2.length();i++){
				JSONObject jsonObject3 = jsonArray2.getJSONObject(i);
				WeatherBean weatherBean = new WeatherBean();
				weatherBean.setDayPictureUrl(jsonObject3.getString("dayPictureUrl"));
				weatherBean.setNightPictureUrl(jsonObject3.getString("nightPictureUrl"));
				weatherBean.setWeather(jsonObject3.getString("weather"));
				weatherBean.setWind(jsonObject3.getString("wind"));
				weatherBean.setTemperature(jsonObject3.getString("temperature"));
				list.add(weatherBean);
			}
		return list;
	}
}

















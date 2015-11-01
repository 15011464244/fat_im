package com.ems.express.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ems.express.R;
import com.ems.express.bean.City;
import com.ems.express.net.MyRequest;
import com.ems.express.net.UrlUtils;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.ToastUtil;

/**
 * 用于选择位置
 * */
public class ServiceRangeSelectActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	public final static String CITY_PRO = "city_pro";
	public final static String CITY_CITY = "city_city";
	public final static String CITY_COUNTY = "city_county";
	private int endCityType = City.TYPE_COUNTY;
	public final static int REQUEST_CODE_GET_ADRRESS = 1000;
	public final static String KEY_CITY_CODE = "key_city_code";
	public final static String KEY_CITY_NAME = "key_city_name";
	public final static String KEY_CITY_TYPE = "key_city_type";
	public final static String KEY_QUERY_TYPE = "key_query_type";
	public final static String KEY_END_CITY_TYPE = "key_end_city_type";
	public final static String KEY_TYPE_EXPRESS = "key_type_express";
	public final static String KEY_IS_SEND_CITY = "key_is_send_city";
	public final static int QUERY_TYPE_ADDRESS = 1;// 普通的获取地址的URL
	public final static int QUERY_TYPE_EXPRENSE = 2; // 获取费用地址的URL
	public final static int QUERY_TYPE_RANGE = 3;// 获取收送范文地址的URL
	public final static int QUERY_TYPE_TIME = 4;// 获取时效查询地址的Url
	private boolean isSendCity = true;
	public final static int EXPRESS_JIANJI = 1;
	public final static int EXPRESS_TEKUAI = 2;
	public final static int EXPRESS_TEKUAI_COUNTY = 3;
	public final static int NO_CITY_CODE = -100;
	private int citycode = -1;
	private String cityName = "";
	private int queryType = QUERY_TYPE_ADDRESS;
	private int cityType = City.TYPE_PROVINCE;// 默认是获取省
	private int expressType = -1;
	private String resultStr = "";
	private GridView gridView;
	List<City> cities = new ArrayList<City>();
	// private LocationListAdapter adapter;
	private GridAdapter adapterGridAdapter;
	private ImageView img_icon;
	private ImageView img_icon_next;
	// private CustomProgressDialog customProgressDialog;

	private TextView jumpSend;
	private AnimationUtil util;
	private String proname;
	private City city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_range_select);
		Intent intent = getIntent();
		citycode = intent.getIntExtra(KEY_CITY_CODE, -1);
		cityName = intent.getStringExtra(KEY_CITY_NAME);
		cityType = intent.getIntExtra(KEY_CITY_TYPE, City.TYPE_PROVINCE);// 默认是省
		queryType = intent.getIntExtra(KEY_QUERY_TYPE, QUERY_TYPE_ADDRESS);// 默认是普通查询
		endCityType = intent.getIntExtra(KEY_END_CITY_TYPE, City.TYPE_COUNTY);// 默认是普通查询
		expressType = intent.getIntExtra(KEY_TYPE_EXPRESS, -1);
		isSendCity = intent.getBooleanExtra(KEY_IS_SEND_CITY, true);
		TextView name = (TextView) findViewById(R.id.tex_title_);
		jumpSend = (TextView) findViewById(R.id.bt_jump_send);
		jumpSend.setVisibility(View.GONE);
		setHeadTitle("选择所在地");
		gridView = (GridView) findViewById(R.id.grid_detail);
		adapterGridAdapter = new GridAdapter(ServiceRangeSelectActivity.this);
		img_icon = (ImageView) findViewById(R.id.img_icon);
		img_icon_next = (ImageView) findViewById(R.id.img_icon_next);
		gridView.setAdapter(adapterGridAdapter);
		gridView.setOnItemClickListener(this);
		findViewById(R.id.layout_china).setOnClickListener(this);
		findViewById(R.id.layout_other).setOnClickListener(this);
		findViewById(R.id.layout_internation).setOnClickListener(this);
		if (citycode != -1) {
			findViewById(R.id.layout_other).setVisibility(View.GONE);
			findViewById(R.id.layout_internation).setVisibility(View.GONE);
			findViewById(R.id.line_1).setVisibility(View.GONE);
			findViewById(R.id.line_2).setVisibility(View.GONE);
			findViewById(R.id.line_3).setVisibility(View.GONE);
			findViewById(R.id.line_4).setVisibility(View.GONE);
			findViewById(R.id.line_5).setVisibility(View.GONE);
			name.setText(cityName);
		}

		initData();

		util = new AnimationUtil(this, R.style.dialog_animation);
		util.show();
	}

	private void initData() {
		gridView.setVisibility(View.VISIBLE);
		String params = "";
		HashMap<String, Object> json = new HashMap<String, Object>();
		String url = UrlUtils.URL_ADDRESS;
		switch (queryType) {
		case QUERY_TYPE_ADDRESS:
			url = UrlUtils.URL_ADDRESS;
			json.put("type", cityType);
			if (cityType != City.TYPE_PROVINCE) {// 如果是获取不是省的添加上级城市
				gridView.setVisibility(View.VISIBLE);
				
				if (citycode != NO_CITY_CODE) {
					json.put("parentID", citycode);
				}
			}
			params = ParamsUtil.getUrlParamsByMap(json);
			break;
		case QUERY_TYPE_EXPRENSE:
			if (cityType == City.TYPE_PROVINCE) {// 如果是获取省的
				url = UrlUtils.URL_EXPRENSE_PRO;
				json.put("queryFlag", expressType);
			} else {
				gridView.setVisibility(View.VISIBLE);
				
				//辽宁的后台数据有问题,这里做特殊处理
				if(cityName.contains("辽宁")){
					cityName = "辽宁省";
					json.put("province", cityName);
					//省名变回辽宁
					cityName = "辽宁";
				}else{
					json.put("province", cityName);
				}
				
				url = UrlUtils.URL_EXPRENSE_CITY;
			}
			params = ParamsUtil.getUrlParamsByMap(json);
			break;
		case QUERY_TYPE_RANGE:
			if (cityType == City.TYPE_PROVINCE) {// 如果是获取省的
				// gridView.setVisibility(View.GONE);
			} else {
				gridView.setVisibility(View.VISIBLE);
				
			}
			params = ParamsUtil.getUrlParamsByMap(json);
			break;
		case QUERY_TYPE_TIME:
			if (cityType == City.TYPE_PROVINCE) {// 如果是获取省的
				// gridView.setVisibility(View.GONE);
				url = UrlUtils.URL_TIME_PRO;
			} else {
				gridView.setVisibility(View.VISIBLE);
				
				url = UrlUtils.URL_TIME_CITY;
				if (cityType == City.TYPE_CITY) {

					json.put("queryFlag", expressType);
					if (isSendCity) {
						json.put("flag", "sendProvinceCode");
					} else {
						json.put("flag", "rcvProvinceCode");
					}
				} else {
					if (expressType == EXPRESS_TEKUAI) {
						json.put("queryFlag", expressType);
					} else {
						json.put("queryFlag", EXPRESS_TEKUAI_COUNTY);
					}
					if (isSendCity) {
						json.put("flag", "send");
					} else {
						json.put("flag", "recv");
					}
				}
				json.put("code", citycode);
				params = ParamsUtil.getUrlParamsByMap(json);
			}
			// 时效查询
			break;
		}
		Log.e("ajh", "params: " + params);
		MyRequest<Object> req = new MyRequest<Object>(Method.POST, null, url,
				new Listener<Object>() {
                         
					@Override
					public void onResponse(Object arg0) {
						if (arg0 != null) {
							if (arg0.toString().contains("traces")) {
								ToastUtil.show(ServiceRangeSelectActivity.this,
										"查询数据失败");
								util.dismiss();
								return;
							}
							handleReuslt(arg0 + "");
							//默认展开
							gridView.setVisibility(View.VISIBLE);
							
						} else {
							ToastUtil.show(ServiceRangeSelectActivity.this,
									"没有数据");
							util.dismiss();
						}
						System.out.println("obj:" + arg0);
						//默认是展开图标
						img_icon_next.setVisibility(View.GONE);
						img_icon.setVisibility(View.VISIBLE);
						util.dismiss();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(ServiceRangeSelectActivity.this,
								"加载数据失败", Toast.LENGTH_LONG).show();
						arg0.printStackTrace();
						util.dismiss();
					}
				}, params);

		RequestQueue queue = Volley.newRequestQueue(this);
		queue.add(req);
		queue.start();
	}

	public static class Location {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private List<City> cities = null;

		public List<City> getCities() {
			return cities;
		}

		public void setCities(List<City> cities) {
			this.cities = cities;
		}

	}

	private static class GridAdapter extends BaseAdapter {
		private List<City> cities = new ArrayList<City>();

		public void setCities(List<City> cities) {
			this.cities = cities;
			notifyDataSetChanged();
		}

		private Context context;

		public GridAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			return cities.size();
		}

		@Override
		public Object getItem(int position) {
			return cities.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_text, null);
			}
			TextView textView = (TextView) convertView
					.findViewById(R.id.tex_name);
			City city = (City) getItem(position);
			textView.setText(city.getName());
			return textView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_china:
			if (gridView.getVisibility() == View.VISIBLE) {
				gridView.setVisibility(View.GONE);
				img_icon.setVisibility(View.GONE);
				img_icon_next.setVisibility(View.VISIBLE);
			} else {
				gridView.setVisibility(View.VISIBLE);
				img_icon.setImageResource(R.drawable.icon_down);
				img_icon_next.setVisibility(View.GONE);
				img_icon.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.layout_internation:

			break;
		case R.id.layout_other:

			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (cityType) {
		case City.TYPE_CITY:// 市

			break;
		case City.TYPE_COUNTY:// 县

			break;
		case City.TYPE_PROVINCE:// 省
			if (citycode == NO_CITY_CODE) {// 应该就是直辖市了

			} else {// 非直辖市

			}
			break;
		default:
			break;
		}
		 city = (City) adapterGridAdapter.getItem(position);
		int tempcitytype = cityType;

		if (queryType == QUERY_TYPE_EXPRENSE) {
			if (tempcitytype == City.TYPE_COUNTY || tempcitytype >= endCityType) {
				doBack(city, new Intent(), true);
				return;
			} 
			
		} else {
			if (city.getCode() == -100 || tempcitytype == City.TYPE_COUNTY
					|| tempcitytype >= endCityType) {
				doBack(city, new Intent(), true);
				return;
			}
		}

		// if(cityType == City.TYPE_PROVINCE && city.isZhixiashi()){
		// start(queryType,tempcitytype+2,endCityType,city.getCode(),city.getName(),this);//直辖市没有市
		// }else{
		if (tempcitytype < City.TYPE_COUNTY) {
			if (QUERY_TYPE_TIME == queryType && city.isZhixiashi()) {
				start(queryType, tempcitytype + 2, endCityType,
						expressType + 1, isSendCity, city.getCode(),
						city.getName(), this);
			} else {
				start(queryType, tempcitytype + 1, endCityType,
						expressType + 1, isSendCity, city.getCode(),
						city.getName(), this);
			}
		}

		// }
		// if (citycode == -1) {
		//
		// Intent intent = new Intent(this, ServiceRangeSelectActivity.class);
		// intent.putExtra(KEY_CITY_CODE, city.getCode());
		// intent.putExtra(KEY_CITY_NAME, city.getName());
		// startActivity(intent);
		// }
	}

	@Override
	protected void onBackClick() {
		setResult(RESULT_CANCELED);
		super.onBackClick();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}

	/**
	 * 用于启动一个选择城市的选项，会使用startActivityForResult的方式启动， requestCode是
	 * {@link ServiceRangeSelectActivity}.REQUEST_CODE_GET_ADRRESS
	 * 
	 * @param queryType
	 *            必填， QUERY_TYPE_ADDRESS = 1;// 普通的获取地址的URL QUERY_TYPE_EXPRENSE
	 *            = 2; // 获取费用地址的URL QUERY_TYPE_RANGE = 3;// 获取范围的URL
	 * @param cityTyp
	 *            必填 {@link City}.TYPE_PROVINCE = 1;省 {@link City}.TYPE_CITY =
	 *            2; {@link City}.TYPE_COUNTY = 3;
	 * @param parentId
	 *            父级城市的id cityTyp是省不需要填
	 * @param parentName
	 *            父级城市的名字 cityType是省不需要填
	 * @param context
	 *            来源，从哪个activity启动到{@link ServiceRangeSelectActivity}
	 * 
	 * */
	private static void start(int queryType, int cityTyp, int endCityType,
			int expressType, boolean isSendCity, int parentId,
			String parentName, Activity context) {
		Intent intent = new Intent(context, ServiceRangeSelectActivity.class);
		if (parentId != -10) {
			intent.putExtra(KEY_CITY_CODE, parentId);
		}
		if (parentName != null) {
			intent.putExtra(KEY_CITY_NAME, parentName);
		}
		intent.putExtra(KEY_TYPE_EXPRESS, expressType);
		intent.putExtra(KEY_QUERY_TYPE, queryType);
		intent.putExtra(KEY_CITY_TYPE, cityTyp);
		intent.putExtra(KEY_END_CITY_TYPE, endCityType);
		intent.putExtra(KEY_IS_SEND_CITY, isSendCity);
		context.startActivityForResult(intent, REQUEST_CODE_GET_ADRRESS);
	}

	/**
	 * 用于启动一个选择城市的选项，会使用startActivityForResult的方式启动， requestCode是
	 * {@link ServiceRangeSelectActivity}.REQUEST_CODE_GET_ADRRESS
	 * 
	 * @param queryType
	 *            必填
	 * @param context
	 *            来源，从哪个activity启动到{@link ServiceRangeSelectActivity}
	 * @param endCityType
	 *            选择城市的层次
	 * @param expressType
	 *            快递类型，获取普通的地址时不需要
	 * @param isSendCity
	 *            是发送城市，还是接受城市，获取普通的地址时不需要
	 * 
	 * */
	public static void start(int queryType, int endCityType, int expressType,
			boolean isSendCity, Activity context) {
		start(queryType, City.TYPE_PROVINCE, endCityType, expressType,
				isSendCity, -10, null, context);
	}

	private void handleReuslt(String result) {
		switch (queryType) {
		case QUERY_TYPE_ADDRESS:
			handleNormalResult(result);
			break;
		case QUERY_TYPE_EXPRENSE:
			handleCommonResult(result);
			break;
		case QUERY_TYPE_RANGE:
			break;
		case QUERY_TYPE_TIME:
			handleCommonResult(result);
			break;
		}
	}

	private void handleCommonResult(String result) {
		JSONObject jsonObject = JSONObject.parseObject(result);
		if (jsonObject.containsKey("model")) {
			JSONObject object = jsonObject.getJSONObject("model");
			if (object != null && object.containsKey("sendProvinceMap")) {
				String str = object.getString("sendProvinceMap");
				handleNormalResult(str);
			} else if (object != null && object.containsKey("area")) {
				String str = object.getString("area");
				handleNormalResult(str);
			} else if (object != null && object.containsKey("ConomyProve")) {
				String str = object.getString("ConomyProve");
				handleNormalResult(str);
			} else if (object != null && object.containsKey("cities")) {
				String str = object.getString("cities");
				handleNormalResult(str);
			} else if (object != null && object.containsKey("addto")) {
				String str = object.getString("addto");
				handleNormalResult(str);
			}
		}
	}

	private void handleNormalResult(String result) {
		cities = new ArrayList<City>();
		JSONArray jsonObject = JSONArray.parseArray(result);
		String key = "key";
		String value = "value";
		if (jsonObject != null && jsonObject.size() > 0) {
			City city = null;
			for (int i = 0; i < jsonObject.size(); i++) {
				city = new City();
				city.setCode(NO_CITY_CODE);
				if (jsonObject.getJSONObject(i).containsKey(key)) {
					String str = jsonObject.getJSONObject(i).getString(key);
					if (!TextUtils.isEmpty(str)) {
						try {
							city.setCode(Integer.parseInt(str.trim()));
						} catch (NumberFormatException nfe) {
							nfe.printStackTrace();
							util.dismiss();
						}
					}
				} else if (jsonObject.getJSONObject(i).containsKey("code")) {
					key = "code";
					String str = jsonObject.getJSONObject(i).getString(key);
					if (!TextUtils.isEmpty(str)) {
						try {
							city.setCode(Integer.parseInt(str.trim()));
						} catch (NumberFormatException nfe) {
							nfe.printStackTrace();
							util.dismiss();
						}
					}
				} else if (jsonObject.getJSONObject(i).containsKey("provCode")) {
					key = "provCode";
					String str = jsonObject.getJSONObject(i).getString(key);
					if (!TextUtils.isEmpty(str)) {
						try {
							city.setCode(Integer.parseInt(str.trim()));
						} catch (NumberFormatException nfe) {
							nfe.printStackTrace();
							util.dismiss();
						}
					}
				}

				if (jsonObject.getJSONObject(i).containsKey(value)) {
					city.setName(jsonObject.getJSONObject(i).getString(value));
				} else if (jsonObject.getJSONObject(i).containsKey("provName")) {
					value = "provName";
					city.setName(jsonObject.getJSONObject(i).getString(value));
				} else if (jsonObject.getJSONObject(i).containsKey(
						"addressfromcity")) {
					value = "addressfromcity";
					city.setName(jsonObject.getJSONObject(i).getString(value));
				} else if (jsonObject.getJSONObject(i).containsKey("ADDREB")) {
					value = "ADDREB";
					city.setName(jsonObject.getJSONObject(i).getString(value));
				} 
				
				city.setType(cityType);
				cities.add(city);
				adapterGridAdapter.setCities(cities);
			}
		} else {
			/*if(queryType == QUERY_TYPE_EXPRENSE){
				City notData = new City();
				notData.setCode(citycode);
				notData.setName(cityName);
				notData.setType(cityType);
				doBack(notData, new Intent(), true);
			}*/
			util.dismiss();
		}
	}

	private void doBack(City city, Intent data, boolean isClick) {

		City pro = new City();
		if (isClick) {// 穿入的这个city对象当前类型
			if (cityType == City.TYPE_CITY) {
				data.putExtra(CITY_CITY, city);
				pro.setName(cityName);
				pro.setCode(citycode);
				pro.setType(cityType);
				data.putExtra(CITY_PRO, pro);
			} else if (cityType == City.TYPE_COUNTY) {
				data.putExtra(CITY_COUNTY, city);
				pro.setName(cityName);
				pro.setCode(citycode);
				pro.setType(cityType);
				data.putExtra(CITY_CITY, pro);
			}
		} else {// 穿入的对象时上一种类型
			if (cityType == City.TYPE_CITY) {
				data.putExtra(CITY_PRO, city);
			} else if (cityType == City.TYPE_COUNTY) {
				data.putExtra(CITY_CITY, city);
			}
		}
		 proname =city.getName();
		Log.e("ajh", "cityType: " + cityType + " name: " + city.getName());
		data.putExtra(KEY_IS_SEND_CITY, isSendCity);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_GET_ADRRESS) {
				City city = new City();
				city.setCode(citycode);
				city.setName(cityName);
				city.setType(cityType);
				doBack(city, data, false);
			}
		}
	}

}

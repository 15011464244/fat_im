package com.address;

import com.address.jifei.FeeAreaDao;
import com.address.jifei.StandardFeeDao;
import com.address.jifei.WeightRangeDao;

import android.content.Context;

public class DaoFactory {

	private Context globalContext = null;

	private static DaoFactory instance = null;

	private CityDao cityDao = null;
	private CountyDao countyDao = null;
	private ProvinceDao provinceDao = null;
	private FeeAreaDao feeAreaDao = null;
	private WeightRangeDao weightRangeDao = null;
	private StandardFeeDao standardFeeDao = null;

	public static DaoFactory getInstance() {
		if (instance == null) {
			instance = new DaoFactory();
		}
		return instance;
	}

	private DaoFactory() {
	}

	public void init() {
		cityDao = getInstance().getCityDao(globalContext);
		countyDao = getInstance().getCountyDao(globalContext);
		provinceDao = getInstance().getProvinceDao(globalContext);
		feeAreaDao = getInstance().getFeeAreaDao(globalContext);
		weightRangeDao = getInstance().getWeightRangeDao(globalContext);
		standardFeeDao = getInstance().getStandardFeeDao(globalContext);
	}

	public CityDao getCityDao(Context context) {

		if (cityDao == null) {
			cityDao = new CityDao(GetContext(context));
		}
		return cityDao;
	}

	public CountyDao getCountyDao(Context context) {

		if (countyDao == null) {
			countyDao = new CountyDao(GetContext(context));
		}
		return countyDao;
	}

	public ProvinceDao getProvinceDao(Context context) {

		if (provinceDao == null) {
			provinceDao = new ProvinceDao(GetContext(context));
		}
		return provinceDao;
	}

	public FeeAreaDao getFeeAreaDao(Context context) {

		if (feeAreaDao == null) {
			feeAreaDao = new FeeAreaDao(GetContext(context));
		}
		return feeAreaDao;
	}
	public WeightRangeDao getWeightRangeDao(Context context) {
		
		if (weightRangeDao == null) {
			weightRangeDao = new WeightRangeDao(GetContext(context));
		}
		return weightRangeDao;
	}
	public StandardFeeDao getStandardFeeDao(Context context) {
		
		if (standardFeeDao == null) {
			standardFeeDao = new StandardFeeDao(GetContext(context));
		}
		return standardFeeDao;
	}

	private Context GetContext(Context context) {
		if (globalContext != null) {
			return globalContext;
		} else {
			return context;
		}
	}

	public Context getGlobalContext() {
		return globalContext;
	}

	public void setGlobalContext(Context globalContext) {
		this.globalContext = globalContext;
	}
}

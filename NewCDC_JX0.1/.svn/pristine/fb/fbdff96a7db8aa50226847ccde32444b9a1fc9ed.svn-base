package com.newcdc.model;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import org.json.JSONObject;

public class CustomerBean {
	String KHDM;
	String KHCDM;
	String KHMC;
	String KHJC;
	String KHJM;
	String PIJM;

	public CustomerBean(JSONObject json) {

		try {
			KHDM = json.get("KHDM") == null ? "" : json.get("KHDM").toString();
			KHCDM = json.get("KHCDM") == null ? "" : json.get("KHCDM")
					.toString();
			KHMC = json.get("KHMC") == null ? "" : json.get("KHMC").toString();
			KHJC = json.get("KHJC") == null ? "" : json.get("KHJC").toString();
			KHJM = json.get("KHJM") == null ? "" : json.get("KHJM").toString();
			PIJM = getPingYinShort(KHMC);
			// Log.e("KHMC,KHJM,PIJM", KHMC+" "+KHJM+ " "+PIJM);
		} catch (Exception e) {

		}

	}

	/**
	 * 将字符串中的中文转化为拼音,并提取首字母
	 * 
	 * @param inputString
	 * @return
	 */
	public static String getPingYinShort(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();// 把字符串转化成字符数组
		String shortString = "";
		try {
			for (int i = 0; i < input.length; i++) {
				// \\u4E00是unicode编码，判断是不是中文
				if (java.lang.Character.toString(input[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					// 将汉语拼音的全拼存到temp数组
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							input[i], format);
					// 取拼音的第一个读音
					shortString += temp[0].substring(0, 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shortString;
	}

	public String getKHDM() {
		return KHDM == null ? "" : KHDM;
	}

	public void setKHDM(String KHDM) {
		this.KHDM = KHDM;
	}

	public String getKHCDM() {
		return KHCDM;
	}

	public void setKHCDM(String kHCDM) {
		KHCDM = kHCDM;
	}

	public String getKHMC() {
		return KHMC == null ? "" : KHMC;
	}

	public void setKHMC(String KHMC) {
		this.KHMC = KHMC;
	}

	public String getKHJC() {
		return KHJC == null ? "" : KHJC;
	}

	public void setKHJC(String KHJC) {
		this.KHJC = KHJC;
	}

	public String getKHJM() {
		return KHJM == null ? "" : KHJM;
	}

	public void setKHJM(String KHJM) {
		this.KHJM = KHJM;
	}

	public String getPIJM() {
		return PIJM == null ? "" : PIJM;
	}

	public void setPIJM(String PIJM) {
		this.PIJM = PIJM;
	}
}

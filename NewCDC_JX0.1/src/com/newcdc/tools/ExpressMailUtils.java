package com.newcdc.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hanrong
 * @version 创建时间：2014-12-5 下午3:01:36 类说明 : 邮件校验
 */
public abstract class ExpressMailUtils {

	private static Map<Integer, Integer> params = new HashMap<Integer, Integer>();

	private static final int M_DATA = 11;

	private static final Pattern pattern = Pattern
			.compile("^[A-Z]{2}\\d{9}[A-Z]{2}");
	private static final Pattern pattern2 = Pattern
			.compile("^[0-9]{2}\\d{9}[0-9]{2}");

	static {
		params.put(1, 8);
		params.put(2, 6);
		params.put(3, 4);
		params.put(4, 2);
		params.put(5, 3);
		params.put(6, 5);
		params.put(7, 9);
		params.put(8, 7);
	}

	/**
	 * 完全邮件号码校验
	 * 
	 * @return
	 */
	public static boolean checkMailNum(String mailNum) {
		mailNum = mailNum.toUpperCase();
		Matcher m = pattern.matcher(mailNum);
		Matcher m2 = pattern2.matcher(mailNum);
		boolean result = m.find();
		boolean result2 = m2.find();
		if (!result && !result2)
			return result;
		String mailNums = mailNum.substring(2, 10);
		int vCode = getVerificationCode(mailNums);
		return vCode == Integer
				.parseInt(Character.toString(mailNum.charAt(10)));
	}

	/**
	 * 获取给定8位数字的校验码
	 * 
	 * @param mailNums
	 * @return
	 */
	public static int getVerificationCode(String mailNums) {
		char[] chars = mailNums.toCharArray();
		int[] nums = new int[8];
		int sum = 0;
		for (int i = 0; i < chars.length; i++) {
			nums[i] = Integer.parseInt(Character.toString(chars[i]));
			sum = params.get(i + 1) * nums[i] + sum;
		}
		sum = sum % M_DATA;
		sum = M_DATA - sum;
		return getLastVerificationCode(sum);
	}

	private static int getLastVerificationCode(int mod) {
		if (mod < 10)
			return mod;
		switch (mod) {
		case 10:
			return 0;
		case 11:
			return 5;
		default:
			return mod;
		}
	}

	public static String blankString(Object value) {
		if (value == null) {
			return "";
		} else {
			if (value.toString() == null || value.toString().equals("")
					|| value.toString().toUpperCase().equals("NULL")) {
				return "";
			} else
				return value.toString();
		}
	}

	public static void main(String[] args) {
		System.out.println(ExpressMailUtils.checkMailNum("1093098082206"));

	}

}

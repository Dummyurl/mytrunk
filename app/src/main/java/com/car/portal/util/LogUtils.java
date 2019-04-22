package com.car.portal.util;

import android.util.Log;

import com.car.portal.application.MyApplication;

public class LogUtils {

	public static final boolean DEBUG = MyApplication.isDebug();

	public static void v(String tag, String message) {
		if (DEBUG && !StringUtil.isNullOrEmpty(message)) {
			Log.v(tag, message);
		}
	}

	public static void d(String tag, String message) {
		if (DEBUG && !StringUtil.isNullOrEmpty(message)) {
			Log.d(tag, message);
		}
	}

	public static void i(String tag, String message) {
		if (DEBUG && !StringUtil.isNullOrEmpty(message)) {
			Log.i(tag, message);
		}
	}

	public static void w(String tag, String message) {
		if (DEBUG && !StringUtil.isNullOrEmpty(message)) {
			Log.w(tag, message);
		}
	}

	public static void e(String tag, String message) {
		if (DEBUG && !StringUtil.isNullOrEmpty(message)) {
			Log.e(tag, message);
		}
	}

	public static void e(String tag, String message, Exception e) {
		if (DEBUG && !StringUtil.isNullOrEmpty(message)) {
			Log.e(tag, message, e);
		}
	}

}

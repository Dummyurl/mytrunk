package com.car.portal.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	private ToastUtil() {
	}

	public static void show(String mess, Context context) {
		mess = mess + "";
		if (context != null) {
			Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
		}
	}

	private static long last_show = -1;

	public static void showNotNet(String mess, Context context) {
		long time = System.currentTimeMillis() - 10000;
		if (StringUtil.isNullOrEmpty(mess) || context == null
				|| time <= last_show) {
			return;
		} else if (context instanceof Activity) {
			last_show = System.currentTimeMillis();
			Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
		}
	}
}

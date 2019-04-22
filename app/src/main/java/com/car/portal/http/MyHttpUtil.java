package com.car.portal.http;

import com.car.portal.application.MyApplication;
import com.car.portal.util.StringUtil;

import android.content.Context;

public class MyHttpUtil {
	
	private static String basePath;
	
	private Context context;
	
	public MyHttpUtil(Context context) {
		this.context = context;
		if(basePath == null) {
			basePath = StringUtil.getHost(context);
		}
	}
	
	public String getUrl(int redId) {
		return basePath + MyApplication.getContext().getResources().getString(redId);
	}

	public String getHost() {
		return basePath.substring(0, basePath.lastIndexOf("/") - 3);
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
}

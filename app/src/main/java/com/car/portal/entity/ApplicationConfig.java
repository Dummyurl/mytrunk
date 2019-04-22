package com.car.portal.entity;

import com.car.portal.application.MyApplication;

public class ApplicationConfig {
	
	private static String host;
	private static String port;
	private static String baidu_ak;
	private static int max_select;

	private static final String wx_APP_ID = "wxfb48eeb1c1737aaf";
	private static final String qq_APP_ID = "1105410353";

	private static ApplicationConfig config = new ApplicationConfig();

	private ApplicationConfig() {
		if (MyApplication.isDebug()) {
			host = "192.168.3.54";
			port = "8443";
			baidu_ak = "kP6sNAjRpYRUv9sFGswWQVpB";
			max_select = 10;
		} else {
			host = "www.56hmc.com";
			port = "443";
			baidu_ak = "kP6sNAjRpYRUv9sFGswWQVpB";
			max_select = 5;
		}
	}

	public static ApplicationConfig getIntance() {
		return config;
	}

	public String getHost() {
		return host;
	}

	public String getPort() {
		return port;
	}

	public String getBaidu_ak() {
		return baidu_ak;
	}

	public int getMax_select() {
		return max_select;
	}

	public ApplicationConfig getConfig() {
		return config;
	}

	public String getWxAppId() {
		return wx_APP_ID;
	}

	public String getQqAppId() {
		return qq_APP_ID;
	}

}

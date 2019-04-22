package com.car.portal.http;

import com.car.portal.application.MyApplication;
import com.car.portal.entity.User;
import com.car.portal.service.UserService;
import com.car.portal.util.SharedPreferenceUtil;
import com.orhanobut.hawk.Hawk;

import java.io.Serializable;

public class SessionStore implements Serializable {

	private static final long serialVersionUID = 322511607838949297L;
	private SessionStore (){}
	
	private static String sessionId;
	private static long lastTime;
	private static final long SESSION_TIME = 29 * 60 * 1000;
	private static SessionStore store;
	//private static String app_token;
	//private static long token_lastTime;  //最近一次更新时间，在第一次访问后，成功确认后，更新时间和app_token值
	private static final long TOKEN_SESSION_TIME = 30 * 60 * 1000;  //与服务器端认证的Token有效时间
	private static class Inner{
		public static SessionStore store = new SessionStore();
	}
	
	public static SessionStore getIntence() {
		if(store == null) {
			store = Inner.store;
		}
		return store;
	}

	public static String getSessionId() {
		long l= System.currentTimeMillis() - lastTime;
		if (l> SESSION_TIME) {
			return null;
		} else {
			lastTime = System.currentTimeMillis();
			if(sessionId==null || "".equals(sessionId))
				sessionId = SharedPreferenceUtil.getIntence().getSessionId(MyApplication.getContext());
			return sessionId;
		}
	}

	public static void setSessionId(String sessionId) {
		if(sessionId != null) {
			lastTime = System.currentTimeMillis();
			SessionStore.sessionId = sessionId;
		}
	}

	public static void resetSessionId() {
		lastTime = 0;
		sessionId = null;
		SharedPreferenceUtil.getIntence().saveSessionId(null,MyApplication.getContext());
	}
/*
	public static void setApp_token(String sign){
		if(!StringUtil.isNullOrEmpty(sign)) {
			Hawk.put("PassTokenLastTime",System.currentTimeMillis());
			Hawk.put("PassToken",sign);
		}
	}

	public static String getApp_token() {
		long token_lastTime =  Hawk.get("PassTokenLastTime")==null?0:Long.parseLong(Hawk.get("PassTokenLastTime").toString());
		if (System.currentTimeMillis() - token_lastTime > TOKEN_SESSION_TIME) {
			return null;
		} else {
			return Hawk.get("PassToken");
		}
	}

	public static void resetApp_Token() {
		Hawk.put("PassTokenLastTime",0);
		Hawk.put("PassToken",null);
	}*/
/*

	private static final String USERTOKEN_PASS ="user_token_pass";
	private static final String USERTOKEN_WX ="user_token_wx";
	private static final String USERTOKEN_QQ ="user_token_qq";
*/



	/**
	 * 微信登录方式的token
	 * @return
	 */
	public static void setMyToken(String token){
		/*if(!StringUtil.isNullOrEmpty(token)) {
			Hawk.put("MyTokenLastTime",System.currentTimeMillis());
			Hawk.put("MyToken",token);
		}*/
		UserService userService = new UserService(MyApplication.getContext());
		User u = userService.getSavedUser();
		if(u!=null)
			u.setToken(token);
	}

	/**
	 *
	 * @return
	 */
	public static String getMyToken(){
		/*long token_lastTime =  Hawk.get("MyTokenLastTime")==null?0:Long.parseLong(Hawk.get("MyTokenLastTime").toString());
		if (System.currentTimeMillis() - token_lastTime > TOKEN_SESSION_TIME) {
			return null;
		} else {
			return Hawk.get("MyToken");
		}*/
		UserService userService = new UserService(MyApplication.getContext());
		User u = userService.getSavedUser();
		if(u==null) return null;
		return userService.getSavedUser().getToken();
	}

	/**
	 * 一般在登录进进行一次设置
	 * 初始化这个变量，但其值是由程序运行决定
	 */
	private static String loginType;
	public static void setLoginType(){
		String t = Hawk.get("loginType");
		if(t==null){
			loginType = "pass";
		}else{
			loginType = Hawk.get("loginType").toString();
		}
	}

	public static String getLoginType(){
		if(loginType!=null)
			return loginType;
		String t = Hawk.get("loginType");
		if(t==null){
			return loginType = "pass";
		}else{
			return loginType =t.toString();
		}
	}
}

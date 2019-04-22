package com.car.portal.service;

import android.content.Context;
import android.util.Base64;

import com.car.portal.entity.User;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.SessionStore;
import com.car.portal.util.SharedPreferenceUtil;

public class BaseService {

	protected MyHttpUtil util;
	protected Context context;
	protected static User user;
	protected static SharedPreferenceUtil shareUtil = SharedPreferenceUtil
			.getIntence();
	
	public BaseService(Context context) {
		this.context = context;
		util = new MyHttpUtil(context);
	}

	public User getSavedUser() {
		User user = shareUtil.getUserFromShared(context);
		if (user != null && user.getPassword() != null) {
			String pass = new String(Base64.decode(user.getPassword()
					.getBytes(), Base64.NO_PADDING));
			user.setPassword(pass);
		}
		return user;
	}

	public static void setLoginUser(User _user) {
		user = _user;
	}

	public User getLoginUser() {
		return user;
	}

	public void saveLoginUser(User entity) {
		if (entity != null) {
			user = new User(entity);
			if (entity.getPassword() != null&&!"".equals(entity.getPassword())) {
				String pass = new String(Base64.encode(user.getPassword().getBytes(), Base64.NO_PADDING));
				entity.setPassword(pass);
			}
			shareUtil.saveUserFromShared(entity, context);
		}else {
			shareUtil.clearUserInfo();
		}
	}

	public void saveSessionId(String token) {
		SessionStore.setSessionId(token);
		shareUtil.saveSessionId(token, context);
	}
}

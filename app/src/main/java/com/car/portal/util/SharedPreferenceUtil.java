package com.car.portal.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.car.portal.application.MyApplication;
import com.car.portal.entity.Address;
import com.car.portal.entity.CityLoadStateEnum;
import com.car.portal.entity.User;
import com.car.portal.entity.VersionInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SharedPreferenceUtil {

	private static SharedPreferenceUtil util;
	private static final String USERINFO = "users";
	private static final String USERTOKEN = "app_sessionId";
	private static final String CITYLOADSTATUE = "city_load_statue";
	private static final String NOMAL_PARAMS = "nomal_param";
	private static final String SHAREP_NAME = "porcal_call";
	private static final String GOODSADD_TYPE = "goodsAdd_type";
	private static int LOGIN_TYPE = 0; //0:用户密码正常登录，1：微信授权登录，2：QQ授权登录
	public static String LOGIN_TYPE_S = "loginType";
	private static int DIFF = 25*60*1000;  //有效期25分钟
	private static int DIFF_PASS = 25*60*1000;  //有效期25分钟
	private static int DIFF_WX = 2*60*60*1000;  //微信token有效期2个小时



	private static class InnerCreate {
		private static SharedPreferenceUtil util = new SharedPreferenceUtil();
		public static SharedPreferenceUtil getUtil() {
			return util;
		}
	}

	public static SharedPreferenceUtil getIntence() {
		if(util == null) {
			util = InnerCreate.getUtil();
		}
		return util;
	}

	public void saveUserFromShared(User user, Context context) {
		if(user == null) {
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(USERINFO, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("uid", user.getUid());
		editor.putString("userName", user.getUsername()==null?"":user.getUsername());
		editor.putString("power", user.getPower()==null?"":user.getPower());
		if(user.getPassword()!=null && !"".equals(user.getPassword())) {  //密码是不会在网络中明文传的，所以user对象返回会为空，应检查后再保存
			editor.putString("password", user.getPassword());
		}
		editor.putString("cname", user.getCname()==null?"":user.getCname());
		editor.putString("position", user.getPosition()==null?"":user.getPosition());
		editor.putString("alias", user.getAlias()==null?"":user.getAlias());
		editor.putInt("focuses", user.getFocuses());
		editor.putInt("focusmax", user.getFocusmax());
		editor.putString("focuslist", user.getFocuslist()==null?"":user.getFocuslist());
		editor.putInt("types", user.getTypes());
		editor.putString("phone", user.getPhone()==null?"":user.getPhone());
		editor.putString("invalidate", user.getInvalidate()==null?"":user.getInvalidate());
		editor.putString("fistDate", user.getFistDate()==null?"":user.getFistDate());
		editor.putString("overTime", user.getOverTime()==null?"":user.getOverTime());
		editor.putString("rate", user.getRate() + "");
		editor.putString("rate2", user.getRate2() + "");
		editor.putInt("company", user.getCompany());
		editor.putInt("companyId", user.getCompanyId());
		editor.putInt("userType", user.getUserType());
		editor.putInt("auth", user.getAuth());
		editor.putString("authType", user.getLoginType());
        if(user.getToken()!=null && !"".equals(user.getToken())) {//token不是每一次都会生成，所以user对象返回会为空，应检查后再保存
            editor.putString("token", user.getToken());
        }
		editor.putString("unionId", user.getUnionId());
		editor.putString("wx_openId",user.getWx_openId());
		editor.putString("openId",user.getOpenId());
		editor.putString("invtier", user.getInvtier());
		editor.commit();
	}

	public User getUserFromShared(Context context) {
		User user = new User();
		SharedPreferences sp = context.getSharedPreferences(USERINFO, Activity.MODE_PRIVATE);
		user.setUid(sp.getInt("uid", 0));
		if(user.getUid() == 0) {
			return null;
		}
		user.setAlias(sp.getString("alias", null));
		user.setUsername(sp.getString("userName", null));
		user.setPower(sp.getString("power", null));
		user.setPassword(sp.getString("password", null));
		user.setCname(sp.getString("cname", null));
		user.setPosition(sp.getString("position", null));
		user.setFocuses(sp.getInt("focuses", 0));
		user.setFocusmax(sp.getInt("focusmax", 0));
		user.setFocuslist(sp.getString("focuslist", null));
		user.setTypes(sp.getInt("types", 0));
		user.setPhone(sp.getString("phone", null));
		user.setInvalidate(sp.getString("invalidate", null));
		user.setFistDate(sp.getString("fistDate", null));
		user.setOverTime(sp.getString("overTime", null));
		user.setRate(Double.parseDouble(sp.getString("rate", "0.0")));
		user.setRate2(Double.parseDouble(sp.getString("rate2", "0.0")));
		user.setCompany(sp.getInt("company", 0));
		user.setCompanyId(sp.getInt("companyId", 0));
		user.setUserType(sp.getInt("userType", 0));
		user.setAuth(sp.getInt("auth", 0));
		user.setLoginType(sp.getString("authType", null));
		user.setToken(sp.getString("token", null));
		user.setUnionId(sp.getString("unionId", null));
		user.setWx_openId(sp.getString("wx_openId", null));
		user.setOpenId(sp.getString("openId", null));
		user.setInvtier(sp.getString("invtier", null));
		return user;
	}



	/**
	 *
	 *@作者 舒椰
	 *@时间 2019/4/11 0011 上午 10:40
	 * 存储是否推荐联系人点了取消
	 */
	public void saveiscancel(String yes) {
		Context context = MyApplication.getContext();
		SharedPreferences preferences = context.getSharedPreferences(USERINFO,Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString("cancelbinding",yes);
		editor.commit();
	}



	public String getiscancel() {
		Context context = MyApplication.getContext();
		SharedPreferences preferences = context.getSharedPreferences(USERINFO,Activity.MODE_PRIVATE);
		return preferences==null?"":preferences.getString("cancelbinding", null);
	}





	public void clearUserInfo() {
		SharedPreferences sp = MyApplication.getContext().getSharedPreferences(USERINFO, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove("uid");
		editor.remove("userName");
		editor.remove("power");
		editor.remove("password");
		editor.remove("cname");
		editor.remove("token");
		editor.commit();
	}

	public void saveSessionId(String sessionId, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(USERTOKEN, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("sessionId", sessionId);
		editor.putLong("time", (sessionId==null||"".equals(sessionId))?0:System.currentTimeMillis());
		editor.commit();
	}

	public String getSessionId(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(USERTOKEN, Activity.MODE_PRIVATE);
		String sessionId = preferences.getString("sessionId", null);
		return sessionId;
	}

	public String findSessionId(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(USERTOKEN, Activity.MODE_PRIVATE);
		long time = preferences.getLong("time", 0);
		String token = preferences.getString("token", null);
		if(System.currentTimeMillis() - time > DIFF) {
			token = null;
		}
		return token;
	}
	public void setToken(String token, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(USERINFO, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		if(token!=null && !"".equals(token)) {//token不是每一次都会生成，所以user对象返回会为空，应检查后再保存
			editor.putString("token", token);
		}
		editor.commit();
	}
	public void saveCityLoadInfo(Context context,CityLoadStateEnum stateEnum) {
		SharedPreferences.Editor editor = context.getSharedPreferences(CITYLOADSTATUE, Activity.MODE_PRIVATE).edit();
		editor.putInt("statue", stateEnum.ordinal());
		editor.putLong("time", System.currentTimeMillis());
		editor.commit();
	}

	public CityLoadStateEnum getCityLoadStatue(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(CITYLOADSTATUE, Activity
				.MODE_PRIVATE);
		return CityLoadStateEnum.getByOrder(preferences.getInt("statue", 0));
	}

	public void setNomalValue(String key,String value, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(NOMAL_PARAMS, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}


	public String getNomalValue(String key, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(NOMAL_PARAMS, Activity.MODE_PRIVATE);
		String token = preferences.getString(key, null);
		return token;
	}

	public void setModelValue(Address address,Context context){
		SharedPreferences preferences=context.getSharedPreferences(NOMAL_PARAMS,Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString("address",address.getAddress());
		editor.putString("city",address.getCity());
		editor.putString("provice",address.getProvice());
		editor.putString("longitude",address.getLongitude());
		editor.putString("latitude",address.getLatitude());
		editor.putBoolean("hasSubmit", address.getHasSubmit());
		editor.putString("cityCode",address.getCityCode());
		editor.putInt("cid",address.getCid());
		editor.commit();
	}

	public Address getModelValue(Context context){
		SharedPreferences sp=context.getSharedPreferences(NOMAL_PARAMS,Activity.MODE_PRIVATE);
		Address address = new Address();
		address.setAddress(sp.getString("address", null));
		if(address.getAddress() == null) {
			return null;
		}
		address.setCity(sp.getString("city", ""));
		address.setProvice(sp.getString("provice", ""));
		address.setLongitude(sp.getString("longitude", ""));
		address.setLatitude(sp.getString("latitude", ""));
		address.setHasSubmit(sp.getBoolean("hasSubmit", false));
		address.setCityCode(sp.getString("cityCode",""));
		address.setCid(sp.getInt("cid",0));
		return address;
	}

	/**
	 * 保存上传时间
	 * @param save_id 保存最近一通电话的phoneID
	 */
	public static void saveDate(int save_id, Context context) {
		SharedPreferences.Editor editor = context.getSharedPreferences(SHAREP_NAME, Activity.MODE_PRIVATE).edit();
		// 保存数据
		editor.putInt("save_id", save_id);
		LogUtils.e("saveData", "                     " + save_id);
		editor.commit();
	}

	/**
	 * 保存上传的电话ID
	 * @param beanId 通话ID
	 * @param isCurrentComplete 当前通话是否上传成功
	 * @param isLoadCntCom 历史通话记录是否上传成功
	 * @param context
	 */
	public static void saveData(int beanId, boolean isCurrentComplete, boolean isLoadCntCom, Context context) {
//		if(!isCurrentComplete && isLoadCntCom) {//如果是是上传历史完成，并且当前记录上传成功，返回不保存
//			return;
//		}
		SharedPreferences.Editor editor = context.getSharedPreferences(SHAREP_NAME, Activity.MODE_PRIVATE).edit();
		editor.putInt("save_id", beanId);
		LogUtils.e("saveData", "                     " + beanId);
		editor.commit();
	}

	/**
	 * 读取上次上传id
	 * @return int read_id
	 */
	public static int readData(Context context) {
		int read_id = 0;
		// 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREP_NAME, Activity
				.MODE_PRIVATE);
		// 获得value，注意第2个参数是value的默认值
		read_id = sharedPreferences.getInt("save_id", 0);
//		LogUtils.e("readData", "                     " + read_id);
		return read_id;
	}

	public void saveVersion(VersionInfo info) {
		if(info != null && info.getVersion() > 0) {
			SharedPreferences sp = MyApplication.getContext().getSharedPreferences(NOMAL_PARAMS,Activity.MODE_PRIVATE);
			SharedPreferences.Editor editer = sp.edit();
			editer.putInt("versionInfo_version", info.getVersion() == null ? 0 : info.getVersion());
			editer.putInt("versionInfo_clientName", info.getClientName() == null ? 0 : info.getClientName());
			editer.putString("versionInfo_content", info.getContent() == null ? "" : info.getContent());
			editer.putLong("versionInfo_create", info.getCreateTime() == null ? 0 : info.getCreateTime().getTime());
			editer.putString("versionInfo_url", info.getDownUrl() == null ? "" : info.getDownUrl());
			editer.putLong("versionInfo_invalid", info.getInvalidTime() == null ? 0 : info.getInvalidTime().getTime());
			editer.putString("versionInfo_os", info.getOs() == null ? "" : info.getOs());
			editer.putString("versionInfo_code", info.getVersionCode() == null ? "" : info.getVersionCode());
			editer.commit();
		}
	}

	public VersionInfo getVersion() {
		VersionInfo info = null;
		SharedPreferences sp = MyApplication.getContext().getSharedPreferences(NOMAL_PARAMS,Activity.MODE_PRIVATE);
		int version = sp.getInt("versionInfo_version", 0);
		if(version > 0) {
			info = new VersionInfo();
			info.setVersion(version);
			info.setClientName((short) sp.getInt("versionInfo_clientName", 0));
			info.setContent(sp.getString("versionInfo_content", ""));
			info.setCreateTime(new Date(sp.getLong("versionInfo_create",0)));
			info.setDownUrl(sp.getString("versionInfo_url", ""));
			info.setInvalidTime(new Date(sp.getLong("versionInfo_invalid", 0)));
			info.setOs(sp.getString("versionInfo_os", ""));
			info.setVersionCode(sp.getString("versionInfo_code", ""));
			return info;
		}
		return null;
	}

	// 保存货物添加类型信息
	public void saveGoodsTypeInfo(Context context, List<String> list) {
		SharedPreferences.Editor editor = context.getSharedPreferences(GOODSADD_TYPE, Activity.MODE_PRIVATE).edit();
		Gson gson = new Gson();
		//转换成json数据，再保存
		String strJson = gson.toJson(list);
		editor.clear();
		editor.putString("goodsTypeAdd", strJson);
		editor.commit();
	}

	// 获得货物添加类型的本地信息
	public  List<String> getGoodsTypeInfo(Context context) {
		List<String> datalist = new ArrayList<String>();
		SharedPreferences preferences = context.getSharedPreferences(GOODSADD_TYPE, Activity
				.MODE_PRIVATE);
		String strJson = preferences.getString("goodsTypeAdd", null);
		if (null == strJson) {
			return datalist;
		}
		Gson gson = new Gson();
		datalist = gson.fromJson(strJson, new TypeToken<List<String>>() {
		}.getType());
		return datalist;
	}


	public static void SaveCity(String action,String name){
		List<String> city=ObtainCity(action);
		if (!city.contains(name))
			city.add(0,name);
		if (city.size()>3){
			city.remove(3);
		}
		Hawk.put(action,city);
	}

	public static List<String> ObtainCity(String action){
		List<String> city=Hawk.get(action);
		if (city==null) city=new ArrayList<>();
		return city;
	}
}

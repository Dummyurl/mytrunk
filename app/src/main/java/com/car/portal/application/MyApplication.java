package com.car.portal.application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.car.portal.R;
import com.car.portal.activity.MainActivity;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.SessionStore;
import com.car.portal.service.UserService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.PicassoImageLoader;
import com.car.portal.util.SharedPreferenceUtil;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;
import org.xutils.common.util.DensityUtil;
import org.xutils.x;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

@SuppressLint("NewApi")
public class MyApplication extends Application {

	public static int localVersion = 0;
	public static int serverVersion = 0;
	public final static int osVersion = android.os.Build.VERSION.SDK_INT;
	public static String packName;
	public static String versionName;
	private static boolean isDebug = true;
	private static boolean isNetOpen = false;
	private UserService userService;
	private User user;
	private static MyApplication instance;

	public static MyApplication getContext() {
		return instance;
	}

	private static String IMEI;
	private List<Activity> activityList = new LinkedList<Activity>();

//	public static RefWatcher getRefWatcher(Context context) {
//		MyApplication application = (MyApplication) context.getApplicationContext();
//		return application.refWatcher;
//	}
//	private RefWatcher refWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
//		refWatcher = LeakCanary.install(this);
		//初始化XUtils3
		x.Ext.init(this);
		// 设置debug模式
		x.Ext.setDebug(true);
		instance = this;
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext
		//注意该方法要再setContentView方法之前实现
		applicationListener();
		LitePal.initialize(this);

//		if (LeakCanary.isInAnalyzerProcess(this)) {
//			// This process is dedicated to LeakCanary for heap analysis.
//			// You should not init your app in this process.
//			return;
//		}
//		LeakCanary.install(this);
		LitePal.initialize(this);
		initnotifimsg();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			closeAndroidPDialog();
		}
		try {
			PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			localVersion = info.versionCode;
			packName = info.packageName;
			versionName = info.versionName;
			ConnectivityManager netManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netinfo = netManager.getActiveNetworkInfo();
			isNetOpen = (netinfo != null && netinfo.isConnectedOrConnecting());
			SDKInitializer.initialize(getApplicationContext()); //百度地图初始化
			Context context = MyApplication.getContext();
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
					MyApplication.packName, PackageManager.GET_META_DATA);
			Bundle bundle = appInfo.metaData;
			isDebug = bundle.getBoolean("Application_debug");
			TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			IMEI = TelephonyMgr.getDeviceId();


		} catch (Exception e) {
			System.out.println(e.getMessage());
			BaseUtil.writeFile("MyApplication", e);
		}

		Hawk.init(this).build();
		ImagePicker imagePicker = ImagePicker.getInstance();
		imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
		imagePicker.setShowCamera(true);  //显示拍照按钮
		imagePicker.setCrop(true);        //允许裁剪（单选才有效）
		imagePicker.setSaveRectangle(true); //是否按矩形区域保存
		imagePicker.setMultiMode(false);
		imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
		imagePicker.setFocusWidth(DensityUtil.dip2px(400));   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
		imagePicker.setFocusHeight(DensityUtil.dip2px(300));  //裁剪框的高度。单位像素（圆形自动取宽高最小值）


		CrashReport.setIsDevelopmentDevice(getApplicationContext(), true);
		CrashReport.initCrashReport(getApplicationContext());
		//CrashReport.testJavaCrash();

	}

	private void initnotifimsg() {
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
		builder.statusBarDrawable = R.drawable.ic_launcher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
				| Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
		builder.notificationDefaults = Notification.DEFAULT_SOUND
				| Notification.DEFAULT_VIBRATE
				| Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
		JPushInterface.setPushNotificationBuilder(1, builder);
		String regis = JPushInterface.getRegistrationID(this);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//			String channelId = "test1";
//			String channelName = "货物信息";
//			int importance = NotificationManager.IMPORTANCE_HIGH;
//			createNotificationChannel(channelId, channelName, importance);
//
//			channelId = "test2";
//			channelName = "平台通知";
//			importance = NotificationManager.IMPORTANCE_DEFAULT;
//			createNotificationChannel(channelId, channelName, importance);
//
//
//			channelId = "test3";
//			channelName = "营销活动";
//			importance = NotificationManager.IMPORTANCE_DEFAULT;
//			createNotificationChannel(channelId, channelName, importance);
//
//			channelId = "test4";
//			channelName = "平台通告";
//			importance = NotificationManager.IMPORTANCE_DEFAULT;
//			createNotificationChannel(channelId, channelName, importance);
//		}
	}

	@TargetApi(Build.VERSION_CODES.O)
	private void createNotificationChannel(String channelId, String channelName, int importance) {
		NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
		NotificationManager notificationManager = (NotificationManager) getSystemService(
				NOTIFICATION_SERVICE);
		notificationManager.createNotificationChannel(channel);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(base);
	}


	public static boolean isDebug() {
		return isDebug;
	}

	@SuppressWarnings("deprecation")
	public static void resetNetState(boolean isConnect) {
		if(isNetOpen != isConnect) {
			if(isConnect) {
				isNetOpen = isConnect;
			} else {
				try{
					boolean isScreenOn;
					PowerManager powerManager = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
		            if(osVersion > 20) {
		            	isScreenOn = powerManager.isInteractive();
		            } else {
		            	isScreenOn = powerManager.isScreenOn();
		            }
		            if(isScreenOn) {
		            	isNetOpen = isConnect;
		            }
				} catch(Exception e) {
					System.out.println(e.getMessage());
					BaseUtil.writeFile("MyApplication", e);
				}
			}
		}
		LogUtils.e("MyApplication", "params:" + isConnect + " result: " + isNetOpen);
	}
	
	public static boolean getNetState() {
		if(!isNetOpen) {
			try{
				ConnectivityManager netManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo netinfo = netManager.getActiveNetworkInfo();
				isNetOpen = (netinfo != null && netinfo.isConnectedOrConnecting());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isNetOpen;
	}

	public static String getIMEI() {
		return IMEI;
	}

	public void applicationListener() {
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				//Log.e("applicationListener", "onActivityCreated");
			}

			@Override
			public void onActivityStarted(Activity activity) {
				//Log.e("applicationListener", "onActivityStarted");
				String firstLogin = "true";
				if (userService != null) {
					firstLogin = userService.getNomalValue("isfirstlogin", "true");
				}
				if ("false".equals(firstLogin)) {
					user = userService == null ? null : userService.getSavedUser();
					long loginTime = Long.parseLong(userService.getNomalValue("loginTime","0"));
					long loginTimeDiff  = System.currentTimeMillis()-loginTime;
					if(loginTimeDiff>StringUtil.RELOGIN_TIME) { //1000*60*25 超过25分钟进行会话超时登 Jimi.Li
						userService.relogin(user, new HttpCallBack(MyApplication.getContext()) {
							@Override
							public void onSuccess(Object... objects) {
								if (objects.length > 1 && objects[1] != null  && objects[1].toString() != null && !"".equals(objects[1].toString())) {
									user.setToken(objects[1].toString());
								}
								if(objects[0]!=null) {
									user = (User) objects[0];
									userService.saveLoginUser(user);
									SharedPreferenceUtil.getIntence().setToken(user.getToken(), getContext());
								}
								userService.saveValue("loginTime", Long.toString(System.currentTimeMillis()));
								/*Intent intent = new Intent(MyApplication.this, MainActivity.class);
                            	startActivity(intent);*/

							}
							public void onFail(int result, String message, boolean show) {
								//super.onFail(result, message, show);
								ToastUtil.show(message, MyApplication.this);
							}
						});
					}
				}
			}

			@Override
			public void onActivityResumed(Activity activity) {
				/**
				 * 据据不同的loginType方式分别调用不同的接口进行重新连接，连接的目的只是获取sessionId.由于大多数接口都是使用
				 * sessionId进行连接的。
				 */
				Hawk.init(activity).build();
				if (SessionStore.getSessionId() == null) {
					if (userService == null)
						userService = new UserService(getContext());
					User user = userService.getSavedUser();
					long loginTime = Long.parseLong(userService.getNomalValue("loginTime","0"));
					long loginTimeDiff  = System.currentTimeMillis()-loginTime;
					if(loginTimeDiff>StringUtil.RELOGIN_TIME) { //1000*60*25 超过25分钟进行会话超时登 Jimi.Li
						if (user == null) {
						} else {
							String token = user.getToken();
							SortedMap<String, String> param = new TreeMap<String, String>();
							user = userService.getSavedUser();
							if (token == null || "".equals(token)) { //token失效，用加密的方式重连接
								if (user != null) {
									param.put("username", user.getUsername());
									param.put("password", user.getPassword());
									param.put("unionId", user.getUnionId());
									userService.login(param, loginCall);
								}
							} else {  //token有效
								if (user != null) {
									param.put("username", user.getUsername());
									param.put("password", user.getPassword());
									param.put("unionId", user.getUnionId());
									userService.login(param, loginCall);
								}
							}
						}
					}
				}
			}

			private HttpCallBack loginCall = new HttpCallBack(MyApplication.this) {
				public void onSuccess(Object... objects) {
					if (objects != null && objects.length > 0) {
						if(objects[0]!=null)
							user = (User) objects[0];
						if(objects.length>1 && objects[1]!=null && objects[1].toString()!=null&&!"".equals(objects[1].toString())){
							user.setToken(objects[1].toString());
						}
						userService.saveLoginUser(user);
						userService.saveValue("loginTime", Long.toString(System.currentTimeMillis()));
					} else {
						ToastUtil.show("数据传输错误", getContext());
					}
				}
			};

			@Override
			public void onActivityPaused(Activity activity) {
				//Log.e("applicationListener", "onActivityPaused");
			}

			@Override
			public void onActivityStopped(Activity activity) {
				//Log.e("applicationListener", "onActivityStopped");
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
				//Log.e("applicationListener", "onActivitySaveInstanceState");
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				//Log.e("applicationListener", "onActivityDestroyeds");
			}
		});
	}

	private void closeAndroidPDialog(){
		try {
			Class aClass = Class.forName("android.content.pm.PackageParser$Package");
			Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
			declaredConstructor.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Class cls = Class.forName("android.app.ActivityThread");
			Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
			declaredMethod.setAccessible(true);
			Object activityThread = declaredMethod.invoke(null);
			Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
			mHiddenApiWarningShown.setAccessible(true);
			mHiddenApiWarningShown.setBoolean(activityThread, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		activityList.clear();
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

}

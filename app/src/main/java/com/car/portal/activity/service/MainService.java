package com.car.portal.activity.service;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Service;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.car.portal.activity.reviecer.CallLogReciever;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.Address;
import com.car.portal.entity.ApplicationConfig;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.CallLogBean;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.service.GeocoderService;
import com.car.portal.service.UserService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.DownLoadUtil;
import com.car.portal.util.FileUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.SharedPreferenceUtil;
import com.car.portal.util.StringUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

public class MainService extends Service {

	protected LocationListener listener;
	private LocationManager manager;
	private Location location;
	public static final String deleteMsg = "delete from appmessage where id = ";
	public static final int NOTIFICAT_ID = 100;
	private static final String TAG = "MainService";
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private int readId = 0;
	private static final String RECIEVER_ACTION = "com.car.portal.service.Download";
	private UserService userService;
	private GeocoderService geocoderService;
	private boolean hasLocation;
	private int tryTime;
	private boolean hasUpload;
	private Address address;
	private boolean isError;
	protected MyHttpUtil util;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private DownLoadReceiver receiver;

	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate() {
		super.onCreate();
		//initListBack();
		userService = new UserService(this);
		geocoderService = new GeocoderService(this);
		/*if (SharedPreferenceUtil.readData(MainService.this) == 0){
			initqueryCallrecord();
		}*/
		receiver = new DownLoadReceiver();
		final IntentFilter filter = new IntentFilter();
		filter.addAction(RECIEVER_ACTION);
		registerReceiver(receiver, filter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flags = START_FLAG_REDELIVERY;
		mylocation();
		return super.onStartCommand(intent, flags, startId);
	}


	public void onDestroy() {
		Intent intent = new Intent("com.car.portal.receiver.KeepAlive");
		sendBroadcast(intent);
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	public void initqueryCallrecord(){
		Uri uri = android.provider.CallLog.Calls.CONTENT_URI;
		MyAsyncQueryHandler asyncQuery = new MyAsyncQueryHandler(
				this.getContentResolver());
		String limit = android.provider.CallLog.Calls.DEFAULT_SORT_ORDER + " limit 1 offset 0" ;
		asyncQuery.startQuery(0, null, uri, CallLogReciever.projection, android.provider.CallLog.Calls.TYPE + " != 2",
				null, limit);
	}

	public class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				List<CallLogBean> callLogs = getCallLog(cursor);
				readId = callLogs.get(0).getId();
				final List<CallLogBean> callLogBeans = new ArrayList<CallLogBean>();
				UserService service = new UserService(MainService.this);
				int uid = service.getLoginUser().getUid();
				callLogBeans.add(callLogs.get(0));
				service.uploadCallLog(callLogBeans, uid, uploadListBack);
			}
		}
	}

	private MyCallBack<BaseEntity<String>> uploadListBack;

	public void initListBack(){
		if(uploadListBack == null) {
			uploadListBack = new MyCallBack<BaseEntity<String>>() {
				@Override
				public void onSuccess(BaseEntity<String> entity) {
					if(entity.getResult() == 1) {
						SharedPreferenceUtil.saveDate(readId, MainService.this);
						CallLogBean.setmIsComming(false, 448);
						LogUtils.e("CallLogRec", "历史电话上传成功：" + CallLogBean.ismIsComming());
						BaseUtil.writeFile("CallLogRec", "历史电话上传成功：" + CallLogBean.ismIsComming());
						CallLogBean.hasLoading = false;
					} else {
						LogUtils.e("CallLogRec", "历史电话上传成功：" + CallLogBean.ismIsComming());
						BaseUtil.writeFile("CallLogRec", "历史电话上传成功：" + CallLogBean.ismIsComming());
						CallLogBean.hasLoading = false;
					}
				}

				@Override
				public void onError(Throwable throwable, boolean b) {
					BaseUtil.writeFile("UserService", throwable);
					throwable.printStackTrace();
					CallLogBean.hasLoading = false;
				}
			};
		}
	}

	public List<CallLogBean> getCallLog(Cursor cursor) {
		List<CallLogBean> callLogs = new ArrayList<CallLogBean>();
		try {
			while (cursor.moveToNext()) {
				Date date = new Date(cursor.getLong(cursor.getColumnIndex(android.provider.CallLog.Calls.DATE)));
				String number = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
				int type = cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
				String cachedName = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));//
				// 缓存的名称与电话号码，如果它的存在
				int id = cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls._ID));
				long time = cursor.getLong(cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION));
				CallLogBean callLogBean = new CallLogBean();
				if (id <= SharedPreferenceUtil.readData(MainService.this) || time < 0) {
					return null;
				} else if (!StringUtil.isPhone(number) && !StringUtil.isFixedPhone(number)) {
					return null;
				}
				if (number.startsWith("+") && number.length() > 3) {
					number = number.substring(3).trim();
				}
				callLogBean.setId(id);
				callLogBean.setNumber(number);
				callLogBean.setName(cachedName);
				if (type == 3)
					callLogBean.setTime(0);
				else
					callLogBean.setTime(time);
				if (null == cachedName || "".equals(cachedName)) {
					callLogBean.setName(number);
				}
				callLogBean.setType(type);
				callLogBean.setDate(sfd.format(date));
				callLogs.add(callLogBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			BaseUtil.writeFile("searchPhone", e);
		}finally {
			cursor.close();
		}
		return callLogs;
	}
	
	private void downLoad(final String url) {
		if(FileUtil.loadBySelf()) {
			userService.downLoadUpdate(url.substring(1), new HttpCallBack(this) {
				@Override
				public void onSuccess(Object... objects) {
					if(objects != null && objects.length > 0) {
						try{
							BaseUtil.installApk(MainService.this, (File) objects[0]);
						} catch (Exception e) {
							BaseUtil.writeFile(TAG, e);
						}
					} else {
						DownLoadUtil.getIntance(MainService.this).downLoadBySystem(url);
					}
				}
			});
		} else {
			DownLoadUtil.getIntance(MainService.this).downLoad(url);
		}
	}


	class DownLoadReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(RECIEVER_ACTION)) {
				String url = intent.getStringExtra("url");
				if(!StringUtil.isNullOrEmpty(url)) {
					downLoad(url);
				}
			}
		}
	}

	public void mylocation(){
		try {
			LocationClient locationClient = new LocationClient(getApplicationContext());
			MyLocationListener myLocationListener = new MyLocationListener();
			locationClient.registerLocationListener(myLocationListener);
			LocationClientOption locationOption = new LocationClientOption();
			locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
			locationOption.setCoorType("bd09ll");
			locationOption.setScanSpan(1000);
			locationClient.setLocOption(locationOption);
			locationClient.start();
			changeToAdress();
		}catch (Exception  e){
			hasLocation = false;
			BaseUtil.writeFile(TAG, "AlarmService.getLocation 用户没有允许定位授权， 无法获取位置信息");
		}

	}

	private LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location l) {
			tryTime++;
			if (l != null && l.getLongitude() > 0) {
				location = l;
				closeLocation();
				changeToAdress();
			}
			if (tryTime > 10) {
				tryTime = 0;
				closeLocation();
			}
		}

		public void onProviderDisabled(String provider) {
			location = null;
			hasLocation = false;
		}

		public void onProviderEnabled(String provider) {
			Location l = null;
			try {
				l = manager.getLastKnownLocation(provider);
			} catch (SecurityException e) {
				BaseUtil.writeFile(TAG, "\t\tSecurityException :" + e.getMessage());
			}
			if (l != null && location == null && l.getLongitude() > 0) {
				location = l;
				changeToAdress();
				closeLocation();
			}
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	public void closeLocation() {
		hasLocation = false;
		if (manager != null) {
			try {
				manager.removeUpdates(locationListener);
				manager = null;
			} catch (SecurityException e) {
				BaseUtil.writeFile(TAG, "\t\t MainService.closeLocation SecurityException :" + e.getMessage());
			}
		}
	}

	public void changeToAdress() {
		if (hasUpload || location == null) {
			return;
		}
		hasUpload = true;
		DecimalFormat df = new DecimalFormat("####.000000");
		final String lng = df.format(location.getLongitude());
		final String lat = df.format(location.getLatitude());
		location = null;
		address = new Address();
		address.setLatitude(lat);
		address.setLongitude(lng);
		address.setDate(System.currentTimeMillis());
		address.setHasSubmit(false);
		uploadLoaction();
	}

	public void uploadLoaction() {
		//LogUtils.e(TAG, "upLoadLocation    " + getNowDate());
		//BaseUtil.writeFile(TAG, "upLoadLocation    " + getNowDate());
		User user = userService.getSavedUser();
		if (user == null || user.getUid() <= 0) {
			address.setHasSubmit(false);
			userService.saveAddress(address);
			getAddrDetail();
			hasUpload = false;
		} else {
			userService.sendLocation(address.getLongitude(), address.getLatitude(), new HttpCallBack(this) {
				@Override
				public void onSuccess(Object... objects) {
					hasUpload = false;
					if (objects != null && objects.length > 0) {
						@SuppressWarnings("unchecked")
						BaseEntity<String> entity = (BaseEntity<String>) objects[0];
						if (entity.getResult() == 1) {
							BaseUtil.writeFile(TAG, "\t\t定位信息上传成功");
							Log.e("定位成功","定位信息上传成功" + address.getLongitude() + "" + address.getLatitude());
							address.setHasSubmit(true);
							String detail = entity.getData();
							if (!StringUtil.isNullOrEmpty(detail)) {
								String[] ds = detail.split(",");
								address.setCity(ds[0]);
								address.setProvice(ds[1]);
								address.setAddress(ds[2]);
								address.setCityCode(ds[3]);
								address.setCid(Integer.parseInt(ds[4]));
							}
							userService.saveAddress(address);
						} else if (entity.getResult() == 0) {
							BaseUtil.writeFile(TAG, "\t\t定位信息保存失败," + entity.getMessage());
						}
					}
				}

				@Override
				public void onFail(int result, String message, boolean show) {
					BaseUtil.writeFile(TAG, "\t\t定位信息保存失败," + message);
					stopBySelf();
					hasUpload = false;
				}

				@Override
				public void onError(Object... objects) {
					BaseUtil.writeFile(TAG, (objects != null && objects.length > 0) ? objects[0] : "定位出现异常");
					isError = true;
					stopBySelf();
					hasUpload = false;
				}
			});
		}
	}

	private void stopBySelf() {
		BaseUtil.writeFile(TAG, "AlarmService  >>>>> stopSelf");
		//		stopSelf();
	}

	public void getAddrDetail() {
		geocoderService.execute(address.getLatitude(), address.getLongitude(), new HttpCallBack(this) {
			@Override
			public void onSuccess(Object... objects) {
				String addr = (String) objects[0];
				String province = (String) objects[1];
				String city = (String) objects[2];
				address.setAddress(addr);
				address.setCity(city);
				address.setProvice(province);
				userService.saveAddress(address);
				stopBySelf();
			}

			@Override
			public void onError(Object... objects) {
				BaseUtil.writeFile(TAG, "\t\t百度数据访问出错");
				userService.saveAddress(address);
				stopBySelf();
			}

			@Override
			public void onFail(int result, String message, boolean show) {
				userService.saveAddress(address);
			}
		});
	}

	class MyLocationListener extends BDAbstractLocationListener {

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			DecimalFormat df = new DecimalFormat("####.000000");
			final String lng = df.format(bdLocation.getLongitude());
			final String lat = df.format(bdLocation.getLatitude());
			address = new Address();
			address.setLatitude(lat);
			address.setLongitude(lng);
			address.setDate(System.currentTimeMillis());
			address.setHasSubmit(false);
			uploadLoaction();
		}
	}


}

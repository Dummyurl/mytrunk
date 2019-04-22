package com.car.portal.activity.reviecer;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.SystemClock;

import com.car.portal.application.MyApplication;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.CallLogBean;
import com.car.portal.http.MyCallBack;
import com.car.portal.service.UserService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.SharedPreferenceUtil;
import com.car.portal.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressLint("SimpleDateFormat") public class MyNetworkReceiver extends BroadcastReceiver {

    private UserService service;
    private static final String TAG = "MyNetWorkReciever";
    private Context context;
    private int uid;
    public static final String[] projection = { android.provider.CallLog.Calls.DATE, // 日期
            android.provider.CallLog.Calls.NUMBER, // 号码
            android.provider.CallLog.Calls.TYPE, // 类型
            android.provider.CallLog.Calls.CACHED_NAME, // 名字
            android.provider.CallLog.Calls._ID, // id
            android.provider.CallLog.Calls.DURATION };
    private int read_id = 0;

    /**
     * 查询电话号码
     */
    public MyNetworkReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    	this.context = context;
    	//initCallBack();
    	ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable() && info.isConnected()) {
        	MyApplication.resetNetState(true);
        	//service = new UserService(context);
        	//User user = service.getSavedUser();
        	//if(user != null) {
        	//	uid = service.getSavedUser().getUid();
        	//}
            //LogUtils.e(TAG, "手机联网成功");
           // BaseUtil.writeFile("MyNetworkReceiver", "      手机联网成功！");
           // SystemClock.sleep(500);
            /*if(uid > 0) {
            	uploadCall();
            }*/
        } else {
        	MyApplication.resetNetState(false);
        }
    }

    /**
     * 上传通话记录
     */
    private void uploadCall() {
    	while (CallLogBean.hasLoading) {
			SystemClock.sleep(1700);
		}
        read_id = SharedPreferenceUtil.readData(context);
        Uri uri = android.provider.CallLog.Calls.CONTENT_URI;
        MyQueryHandler asyncQuery = new MyQueryHandler(context.getContentResolver());
        if (read_id == 0) {
            asyncQuery.startQuery(0, null, uri, projection, android.provider.CallLog.Calls.TYPE + " != 2",
                    null, android.provider.CallLog.Calls.DEFAULT_SORT_ORDER);
        } else {
            asyncQuery.startQuery(0, null, uri, projection, android.provider.CallLog.Calls.TYPE + " != 2 and "+ android.provider.CallLog.Calls._ID + " > ?", new String[] {  read_id + "" },
                    android.provider.CallLog.Calls.DEFAULT_SORT_ORDER);
        }
    }

    /**
     * 查询手机通话记录
     */
    public class MyQueryHandler extends AsyncQueryHandler {

		public MyQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			LogUtils.e("CallLogReciver", "获取到的通话记录：" + (cursor == null ? 0 : cursor.getCount()));
			BaseUtil.writeFile("CallLogReciver", "获取到的通话记录：" + (cursor == null ? 0 : cursor.getCount()));
			
			if (cursor != null && cursor.getCount() > 0) {
				List<CallLogBean> callLogs = getCallLog(cursor);
				if (callLogs == null || callLogs.size() <= 0 || callLogs.get(0) == null ){
					return;
				}
				read_id = callLogs.get(0).getId();
				try {
					SystemClock.sleep(1000);
					LogUtils.e("CallLogReciver", "开始上传通话：" + (cursor == null ? 0 : cursor.getCount()));
					BaseUtil.writeFile("CallLogReciver", "开始上传通话：" + (cursor == null ? 0 : cursor.getCount()));
					if (SharedPreferenceUtil.readData(context) == 0) {
						firstPhoneNumberUpload(callLogs);
					} else {
						otherPhoneNumberUpload(callLogs);
					}
				} catch (Exception e) {
					e.printStackTrace();
					BaseUtil.writeFile("LoadPhones", e);
				}
			}
			cursor.close();
		}
		
		public List<CallLogBean> getCallLog(Cursor cursor) {
			List<CallLogBean> callLogs = new ArrayList<CallLogBean>();
			try {
				for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
		            Date date = new Date(cursor.getLong(cursor.getColumnIndex(android.provider.CallLog.Calls.DATE)));
		            String number = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
		            int type = cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
		            String cachedName = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));//
		            // 缓存的名称与电话号码，如果它的存在
		            int id = cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls._ID));
		            long time = cursor.getLong(cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION));
		            CallLogBean callLogBean = new CallLogBean();
		            if (id <= SharedPreferenceUtil.readData(context) || time < 0) {
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
		            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		            callLogBean.setDate(sfd.format(date));
		            callLogs.add(callLogBean);
				}
			} catch (Exception e) {
				e.printStackTrace();
				BaseUtil.writeFile("searchPhone", e);
			}
			return callLogs;
		}

		/**
		 * 非第一次上传电话号码
		 */
		private void otherPhoneNumberUpload(List<CallLogBean> callLogs) {
			if(CallLogBean.isComplete) {
				final CallLogBean b = callLogs.remove(0);
				if(CallLogBean.phoneId > 0) {
					BaseUtil.writeFile("CallLogRec", "更新时间：phoneId=" + CallLogBean.phoneId);
					LogUtils.e("CallLogRec", "更新时间：phoneId=" + CallLogBean.phoneId);
					service.upLoadCallInfo(CallLogBean.phoneId, b.getId(), b.getTime(), updateTimeBack);
				}
			}
			if (callLogs.size() > 0){
				CallLogBean.hasLoading = true;
				BaseUtil.writeFile("CallLogRec", "上传历史记录");
				LogUtils.e("CallLogRec", "上传历史记录");
				service.uploadCallLog(callLogs, uid, uploadListBack);
			}
		}

		/**
		 * 第一个号码上传
		 */
		private void firstPhoneNumberUpload(List<CallLogBean> callLogs) {
			if (CallLogBean.isComplete && CallLogBean.phoneId != 0) {
				LogUtils.e("CallLogReceiver", "更新来电时间");
				BaseUtil.writeFile("CallLogReceiver", "更新来电时间");
				service.upLoadCallInfo(CallLogBean.phoneId, callLogs.get(0).getId(), callLogs.get(0).getTime(), updateTimeBack);
			} else {
				LogUtils.e("CallLogReceiver", "上传第一通电话");
				BaseUtil.writeFile("CallLogReceiver", "上传第一通电话");
				final List<CallLogBean> callLogBeans = new ArrayList<CallLogBean>();
				callLogBeans.add(callLogs.get(0));
				service.uploadCallLog(callLogBeans, uid, uploadListBack);
			}
		}
	}


	public void initCallBack() {
		if(uploadListBack == null) {
			uploadListBack = new MyCallBack<BaseEntity<String>>() {
				@Override
				public void onSuccess(BaseEntity<String> entity) {
					if(entity.getResult() == 1) {
						SharedPreferenceUtil.saveDate(read_id, context);
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
		if(updateTimeBack == null) {
			updateTimeBack = new MyCallBack<BaseEntity<String>>() {
				@Override
				public void onSuccess(BaseEntity<String> stringBaseEntity) {
					if (stringBaseEntity.getResult() == 1) {
						LogUtils.e("UserService", "    上传时间OK");
						SharedPreferenceUtil.saveDate(read_id, context);
						CallLogBean.phoneId = 0;
						LogUtils.e("CallLogReciver", "来电时间更新成功:" + CallLogBean.phonenumber);
						BaseUtil.writeFile("CallLogReciver", "来电时间更新成功:" + CallLogBean.phonenumber);
					} else {
						LogUtils.e("CallLogReceiver", "time onfail");
					}
					CallLogBean.phoneId = 0;
				}

				@Override
				public void onError(Throwable throwable, boolean b) {
					LogUtils.e("UserService", "    上传时间失败");
					CallLogBean.phoneId = 0;
					LogUtils.e("CallLogReceiver", "time onerror");
					LogUtils.e("CallLogReceiver", "onerror:" + throwable);
				}
			};
		}
	}

	private MyCallBack<BaseEntity<String>> uploadListBack;
	private MyCallBack<BaseEntity<String>> updateTimeBack;
}
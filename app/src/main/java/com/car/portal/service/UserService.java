package com.car.portal.service;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.car.portal.R;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.Address;
import com.car.portal.entity.AllotRechargeBean;
import com.car.portal.entity.ApplicationConfig;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.CallLogBean;
import com.car.portal.entity.Company;
import com.car.portal.entity.ContactEntity;
import com.car.portal.entity.Contacts;
import com.car.portal.entity.MyTypes;
import com.car.portal.entity.ParseReturn;
import com.car.portal.entity.ProcomMissBean;
import com.car.portal.entity.User;
import com.car.portal.entity.UserDetail;
import com.car.portal.entity.VersionInfo;
import com.car.portal.fragment.DriverPersonFragment;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.SessionStore;
import com.car.portal.http.XUtil;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.FileUtil;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.MyDBUtil;
import com.car.portal.util.SharedPreferenceUtil;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.orhanobut.hawk.Hawk;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class UserService extends BaseService {

    private static long last_visit;
    private final String PHONE_ISEXIT = "SELECT COUNT(*) FROM outcallbean c WHERE phone = ?";
    private final String SELECT_PHONE = "SELECT * FROM callbean";
    private final String SELECT_ONE_PHONE = "SELECT * FROM callbean WHERE id = ?";
    private final String DELETE_ALL_DATA = "TRUNCATE TABLE callbean";
    private final String DELETE_ONE_CALL = "DELETE * FROM callbean WHERE id = ?";
    private final String ADD_ONE_CALL = "INSERT INTO callbean (id, name, phone, date, time) VALUES";

    public UserService(Context context) {
        super(context);
    }

    /**
     * 获取微信登陆授权结果
     *
     * @param code
     * @param callBack
     */
    public void wechatLogin(String code, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_weixinLogin) + "?code=" + code + "&type=0";//type = 1表示司机端 0表示三方端
        XUtil.Post(url, null, new MyCallBack<BaseEntity<String>>() {
            @Override
            public void onSuccess(BaseEntity<String> stringBaseEntity) {
                if (stringBaseEntity != null) {
                    LogUtils.e("UserService", "    result" + stringBaseEntity.getResult());
                    callBack.onSuccess(stringBaseEntity);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                callBack.onError(throwable, b);
            }
        });
    }

    /**
     * @param username
     * @param password
     * @param unionId
     * @param type     0微信 1QQ
     * @param callBack
     */
    public void bindUser(String username, String password, String unionId, String code,int type, final HttpCallBack callBack) {
        String url = null;
        if (type == -1) { //绑定微信用户
            url = util.getUrl(R.string.url_bindWxUser);
        } else if (type == 1) { //绑定QQ用户
            url = util.getUrl(R.string.url_bindQQUser);
        }
        if (url == null) {
            ToastUtil.show("您的授权方式不正确", context);
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("passwordP", password);
        params.put("unionId", unionId);
        params.put("wx_login_type", "0");
        params.put("userType",0); //公司
        params.put("code",code);
        XUtil.Post(url, params, new MyCallBack<BaseEntity>() {
            @Override
            public void onError(Throwable throwable, boolean b) {
                callBack.onError(throwable, b);
            }

            @Override
            public void onSuccess(BaseEntity baseEntity) {
                if (baseEntity.getResult() == 1) {
                    User user = new User();
                    LinkMapToObjectUtil.getObject((LinkedTreeMap<String, Object>) baseEntity.getData(), user);
                    callBack.onSuccess(user,1);
                } else {
                    callBack.onFail(baseEntity.getResult(), baseEntity.getMessage(), true);
                }
            }
        });
    }

    public void registerWXNewUser(String unionId, int type, final HttpCallBack callBack){

    }
    public void loginByQq(String code, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_qqLogin) + "?code=" + code + "&type=0";//type = 表示司机端
        XUtil.Post(url, null, new MyCallBack<BaseEntity>() {
            @Override
            public void onSuccess(BaseEntity stringBaseEntity) {
                if (stringBaseEntity != null) {
                    LogUtils.e("UserService", "    result" + stringBaseEntity.getResult());
                    callBack.onSuccess(stringBaseEntity);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                callBack.onError(throwable, b);
            }
        });
    }

    /**
     * 检查网络是否连接
     *
     * @return
     */
    public boolean CheckINetConnect() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable() && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     *
     *@作者 舒椰
     *@时间 2019/4/10 0010 下午 5:56
     *	弹出对话框查询推荐人
     */
    public void bindingreferrer(final Context context,boolean ischick){
        if(ischick){
            showbinding(context);
        }else {
            String isshow = shareUtil.getiscancel();
            if (isshow == null || "".equals(isshow)) {
                showbinding(context);
            }
        }
    }

    /**
     *
     *@作者 舒椰
     *@时间 2019/4/10 0010 下午 5:57
     *  通过号码查询推荐人
     */
    public void showbinding(final Context context){
        new MaterialDialog.Builder(context)
                .title("提示")
                .content("检测到您未绑定推荐人，是否绑定推荐人电话号码？")
                .contentColor(context.getResources().getColor(R.color.title_black))
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new MaterialDialog.Builder(context).title("绑定推荐人")
                                .iconRes(R.drawable.ic_launcher)
                                .content("请输入推荐人手机号码")
                                .widgetColorRes(R.color.colorPrimary)
                                .inputRangeRes(0, 15, R.color.light_red)
                                .inputType(InputType.TYPE_CLASS_PHONE)
                                .input("电话号码", null, new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                        if(input.toString()==user.getPhone()){
                                            Toast.makeText(context, "不能绑定自己为推荐人哦", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        getreferrer(input.toString(),context);
                                    }
                                })
                                .positiveText("查找")
                                .show();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        shareUtil.saveiscancel("yes");
                    }
                })
                .show();
    }



    public void getreferrer(final String phone, final Context context){
        util = new MyHttpUtil(context);
        String url = util.getUrl(R.string.url_findCompanyByPhone) + "?phone=" + phone;
        Log.d("url", url);
        XUtil.Post(url, null, new MyCallBack<BaseEntity<Object>>() {
            @Override
            public void onSuccess(BaseEntity baseEntity) {
                if (baseEntity.getResult() == 1) {
                    final Company company = new Company();
                    LinkMapToObjectUtil.getObject((LinkedTreeMap<String, Object>) baseEntity.getData(), company);
                    if(company.getName()==null||"".equals(company.getName())){
                        Toast.makeText(context, "该用户未通过审核，无法绑定", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new MaterialDialog.Builder(context).title("查找成功")
                            .iconRes(R.drawable.ic_launcher)
                            .content(company.getName()+"  "+company.getAlias()+"\n"+company.getTel())
                            .contentColorRes(R.color.black)
                            .positiveText("申请")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    setbindingphone(phone);
                                }
                            })
                            .show();

                } else {
                    new MaterialDialog.Builder(context).title("查询失败")
                            .iconRes(R.drawable.ic_launcher)
                            .content("未查找到该用户，请确认手机号是否输入正确！")
                            .positiveText("确认")
                            .show();
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("onError", arg0.toString());
                new MaterialDialog.Builder(context).title("查询失败")
                        .iconRes(R.drawable.ic_launcher)
                        .content("网络异常，请稍后重试")
                        .positiveText("确认")
                        .show();
            }
        });
    }


    public void setbindingphone(String phone){
        String url=util.getUrl(R.string.url_bindbyphone);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("invtierPhone", phone);
        XUtil.Post(url, params,new MyCallBack<BaseEntity<String>>() {
            @Override
            public void onSuccess(BaseEntity<String> stringBaseEntity) {
                if(stringBaseEntity.getResult()==1){
                    Toast.makeText(context, stringBaseEntity.getMessage()+"", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Toast.makeText(context, throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }






    /**
     * 从app数据库中读取通话记录
     *
     * @return
     * @throws DbException
     */
    public List<CallLogBean> queryCallbeans() throws DbException {
        List<CallLogBean> callLogBeans = new ArrayList<CallLogBean>();
        DbManager dbManager = MyDBUtil.getManager();
        Cursor cursor = null;
        try {
            cursor = dbManager.execQuery(SELECT_PHONE);
            int idColumn = cursor.getColumnIndex("id");
            int phoneColumn = cursor.getColumnIndex("phone");
            int date = cursor.getColumnIndex("date");
            int nameColumn = cursor.getColumnIndex("name");
            int timeColumn = cursor.getColumnIndex("time");
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    CallLogBean callLogBean = new CallLogBean();
                    callLogBean.setId(cursor.getInt(idColumn));
                    callLogBean.setNumber(cursor.getString(phoneColumn));
                    callLogBean.setDate(cursor.getString(date));
                    callLogBean.setName(cursor.getString(nameColumn));
                    callLogBean.setTime(cursor.getLong(timeColumn));
                    callLogBeans.add(callLogBean);
                }
                cursor.close();
            } else {
                callLogBeans = null;
            }
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    BaseUtil.writeFile("queryCallbeans", "   " + e);
                }
            }
        }
        return callLogBeans;
    }

    /**
     * 添加通话记录到app数据库
     *
     * @param callLogBean
     * @throws DbException
     */
    public void addCallbean(CallLogBean callLogBean) throws DbException {
        DbManager dbManager = MyDBUtil.getManager();
        dbManager.save(callLogBean);
    }

    /**
     * 根据电话ID 查找一通通话记录
     *
     * @param id 电话ID
     * @throws DbException
     */
    public void queryOneCall(int id) throws DbException {
        DbManager dbManager = MyDBUtil.getManager();
        Cursor cursor = null;
        String sql = SELECT_ONE_PHONE.replace("?", id + "");
        cursor = dbManager.execQuery(sql);
        int idColumn = cursor.getColumnIndex("id");
        int phoneColumn = cursor.getColumnIndex("phone");
        int date = cursor.getColumnIndex("date");
        int nameColumn = cursor.getColumnIndex("name");
        int timeColumn = cursor.getColumnIndex("time");
        if (cursor.getCount() > 0) {
            CallLogBean callLogBean = new CallLogBean();
            callLogBean.setId(cursor.getInt(idColumn));
            callLogBean.setNumber(cursor.getString(phoneColumn));
            callLogBean.setDate(cursor.getString(date));
            callLogBean.setName(cursor.getString(nameColumn));
            callLogBean.setTime(cursor.getLong(timeColumn));
        }
        cursor.close();
    }

    /**
     * 删除app数据库里所有通话记录
     *
     * @throws DbException
     */
    public void deleteCall() throws DbException {
        DbManager dbManager = MyDBUtil.getManager();
        dbManager.execNonQuery(DELETE_ALL_DATA);
    }

    /**
     * 删除一条通话记录
     *
     * @param id
     * @throws DbException
     */
    public void deleteOneCall(int id) throws DbException {
        DbManager dbManager = MyDBUtil.getManager();
        String sql = DELETE_ONE_CALL.replace("?", id + "");
        dbManager.execNonQuery(sql);
    }

    /**
     * 查询手机本地通话记录的方法
     *
     * @param cursor
     */
    public CallLogBean queryCursor(Cursor cursor) {
        CallLogBean callLogBean = null;
        try {
            Date date = new Date(cursor.getLong(cursor.getColumnIndex(android.provider.CallLog.Calls.DATE)));

            String number = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls
                    .NUMBER));
            int type = cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
            String cachedName = cursor.getString(cursor.getColumnIndex(android.provider.CallLog
                    .Calls
                    .CACHED_NAME));//
            // 缓存的名称与电话号码，如果它的存在
            int id = cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls._ID));
            long time = cursor.getLong(cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION));
            callLogBean = new CallLogBean();
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
        } catch (Exception e) {
            e.printStackTrace();
            BaseUtil.writeFile("searchPhone", e);
        }
        return callLogBean;
    }

    /**
     * 拨打电话
     */
    public static void callLog(Map<String, Object> map, final Context context, final HttpCallBack callBack) {
        MyHttpUtil myUtil = new MyHttpUtil(context);
        String url = myUtil.getUrl(R.string.url_call);
        XUtil.Post(url, map, new MyCallBack<ParseReturn>() {
            @Override
            public void onSuccess(ParseReturn parseReturn) {
                callBack.onSuccess(parseReturn);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
                LogUtils.e("0000000   ", throwable.getMessage());
                callBack.onError(throwable, b);
            }
        });
    }

    /**
     * 添加号码到无效号码表中
     */
    public void savePhone() {
        CallLogBean callLogBean = new CallLogBean();
        callLogBean.setNumber("18476656253");
        DbManager dbManager = MyDBUtil.getManager();
        try {
            dbManager.save(callLogBean);
        } catch (DbException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 该号码是否在过期的号码表中
     *
     * @param phone 要上传的电话号码
     * @return
     */
    public boolean isExitThisPhone(String phone) {
        boolean isExit = false;
        DbManager dbManager = MyDBUtil.getManager();
        Cursor cursor = null;
        try {
            String sql = PHONE_ISEXIT.replace("?", phone);
            cursor = dbManager.execQuery(sql);
            LogUtils.e("UserService", "    cursor.size:" + cursor.getCount());
            if (cursor.getCount() > 0)
                isExit = true;
            else
                isExit = false;
        } catch (DbException e) {
            System.out.println(e.getMessage());
        } finally {
            if (dbManager != null) {
                try {
                    dbManager.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                dbManager = null;
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return isExit;
    }

    /**
     * 上传来电号码及通话时长给服务器
     *
     * @param
     */
    public void uploadCallLog(List<CallLogBean> callLogBeans, int uid, MyCallBack<BaseEntity<String>> back) {
        String url = util.getUrl(R.string.url_uploadCallList);
        CallLogBean callLogBean = callLogBeans.get(0);
        if (callLogBean != null) {
            StringBuffer phonelist = new StringBuffer(callLogBean.getNumber());
            StringBuffer timeList = new StringBuffer(callLogBean.getTime() + "");
            StringBuffer dateList = new StringBuffer(callLogBean.getDate());
            StringBuffer idlist = new StringBuffer(callLogBean.getId() + "");
            for (int i = 1; i < callLogBeans.size(); i++) {
                callLogBean = callLogBeans.get(i);
                phonelist.append(",").append(callLogBean.getNumber());
                timeList.append(",").append(callLogBean.getTime());
                dateList.append(",").append(callLogBean.getDate());
                idlist.append(",").append(callLogBean.getId());
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("phoneList", phonelist.toString());
            map.put("timeList", timeList.toString());
            map.put("uid", uid);
            map.put("dateList", dateList.toString());
            map.put("localList", idlist.toString());
            map.put("phoneUnionId", MyApplication.getIMEI());
            XUtil.Post(url, map, back);
        }
    }

    /**
     * 上传电话号码
     *
     * @param phone
     * @param uid
     * @return
     */
    public int uploadPhone(String phone, int localId, int uid, MyCallBack callBack) {
        String url = util.getUrl(R.string.url_uploadPhone);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", phone);
        map.put("uid", uid);
        map.put("localId", localId);
        map.put("phoneUnionId", MyApplication.getIMEI());
        XUtil.Post(url, map, callBack);
        return CallLogBean.phoneId;
    }

    /**
     * 上传时间
     *
     * @param id
     * @param time
     * @param callBack
     */
    public void upLoadCallInfo(int id, int localId, long time, MyCallBack callBack) {
        String url = util.getUrl(R.string.url_upCallInfo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phoneId", id);
        map.put("time", time);
        map.put("localId", localId);
        map.put("phoneUnionId", MyApplication.getIMEI());
        XUtil.Post(url, map, callBack);
    }

    public List<CallLogBean> getCallsFromDB(Cursor cursor) {
        List<CallLogBean> callLogBeans = new ArrayList<CallLogBean>();
        int idColumn = cursor.getColumnIndex("id");
        int phoneColumn = cursor.getColumnIndex("phone");
        int dateColumn = cursor.getColumnIndex("date");

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            CallLogBean callLogBean = new CallLogBean();
            callLogBean.setId(cursor.getInt(idColumn));
            callLogBean.setNumber(cursor.getString(phoneColumn));
            callLogBean.setDate(cursor.getString(dateColumn));
            callLogBeans.add(callLogBean);
        }
        return callLogBeans;
    }



    /**
     * 此登录方式为用户、密码的方式，另外还有自建加密、微信、QQ等3种
     * @param mapAvg
     * @param callBack
     */
    @SuppressWarnings("rawtypes")
    public void login(final Map<String,String> mapAvg, final HttpCallBack callBack) {
        SessionStore.setSessionId(getSessionId());
        String url = util.getUrl(R.string.url_login);
        //TODO 要求尽快实现Https防问服务器进行安全访问，用户密码保存于APP中时加密处理
        XUtil.Post(url, mapAvg, new MyCallBack<BaseEntity<LinkedTreeMap>>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(BaseEntity<LinkedTreeMap> result) {
                //TODO 安全性有待加强，只有简单进行返回性检查，在一般业务调用可以，但登录就不好了
                if (result.getResult() == 1) {
                    User user = new User();
                    LinkMapToObjectUtil.getObject(result.getData(), user);
                    user.setPassword(mapAvg.get("password"));
                    Hawk.put("uid", user.getUid().toString());
                    UserService.user = user;
                    JPushInterface.setAlias(context,user.getUid(),"Push_Alias_"+user.getUid());
                    callBack.onSuccess(user);
                } else {
                    callBack.onFail(result.getResult(), result.getMessage(), true);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.onError(ex, isOnCallback);
            }
        });
    }



    public void checkMessage(String phone, String messege, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_regCheckMessage);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", phone);
        map.put("messege", messege);
        XUtil.Post(url, map, new MyCallBack<BaseEntity<String>>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<String> arg0) {
                if (arg0.getResult() == -2) {
                    callBack.onFail(-2, "", false);
                    return;
                }
                callBack.onSuccess(arg0);
            }
        });
    }

    public void getMessege(String phone, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_getPhoneCode);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", phone);
        XUtil.Post(url, map, new MyCallBack<ParseReturn>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(ParseReturn arg0) {
                callBack.onSuccess(arg0);
            }
        });
    }

    /**
     * 查找当前用户其推荐的注册用户
     * @param map
     * @param callBack
     */
    public void findSalesRechange(Map<String,Object> map,final HttpCallBack callBack){
        String url = util.getUrl(R.string.url_saleRechange);
        XUtil.Post(url, map, new MyCallBack<BaseEntity<ArrayList>>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<ArrayList> arg0) {
                ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
                ArrayList<ProcomMissBean> pmbList = new ArrayList<ProcomMissBean>();
                if (list != null) {
                    Log.d("ProcomMissBean", "onSuccess: "+list.toString());
                    for (int i = 0; i < list.size(); i++) {
                        ProcomMissBean procomMissBean = new ProcomMissBean();
                        LinkMapToObjectUtil.getObject(list.get(i), procomMissBean);
                        pmbList.add(procomMissBean);
                    }
                }
                callBack.onSuccess(pmbList);
            }
        });
    }

    /**
     * 推广用户提成流水列表
     * @param map
     * @param callBack
     */
    public void findAllotRechange(Map<String,Object>map,final HttpCallBack callBack){
        String url=util.getUrl(R.string.url_allotrechange);
        XUtil.Post(url, map, new MyCallBack<BaseEntity<ArrayList>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0,arg1);
            }
            @Override
            public void onSuccess(BaseEntity<ArrayList> arg0) {
                ArrayList<LinkedTreeMap<String,Object>>list=arg0.getData();
                ArrayList<AllotRechargeBean> allotList=new ArrayList<>();

                if (list!=null){
                    Log.d("AllotRechargeBean", "onSuccess: "+list.toString());
                    for (int i = 0; i <list.size(); i++) {
                        AllotRechargeBean allotRechargeBean=new AllotRechargeBean();
                        LinkMapToObjectUtil.getObject(list.get(i),allotRechargeBean);
                        allotList.add(allotRechargeBean);
                    }
                }
                callBack.onSuccess(allotList);
            }

        });
    }


    public void getPhoneCodeOfForgot(String phone, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_getPhoneCodeForgot);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", phone);
        XUtil.Post(url, map, new MyCallBack<ParseReturn>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(ParseReturn arg0) {
                callBack.onSuccess(arg0);
            }
        });
    }

    public void register(Map<String, Object> map, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_register);
        XUtil.Post(url, map, new MyCallBack<BaseEntity<String>>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<String> arg0) {
                if (arg0.getResult() == -2) {
                    callBack.onFail(-2, "", false);
                    return;
                }
                callBack.onSuccess(arg0);
            }
        });
    }

    public void bindImage(final ImageView checkNumer, long time) {
        if (last_visit == 0L || time - last_visit > 500) {
            x.image().bind(checkNumer, util.getUrl(R.string.url_getCheckNumber), new MyCallBack<Drawable>() {
                @Override
                public void onError(Throwable arg0, boolean arg1) {
                    LogUtil.e(arg0.getLocalizedMessage());
                }

                @Override
                public void onSuccess(Drawable arg0) {
                    checkNumer.setImageDrawable(arg0);
                }
            });
        } else {
            ToastUtil.show("你的请求太频繁或者网络错误，请稍后再试", context);
            return;
        }
    }

    public void sendLocation(String lng, String lat, String address, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_location);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lng", lng);
        map.put("lat", lat);
        map.put("address", address);
        XUtil.Post(url, map, new MyCallBack<BaseEntity<String>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<String> arg0) {
                if (arg0.getResult() == -2) {
                    callBack.onFail(-2, "", false);
                    return;
                }
                callBack.onSuccess(arg0);
            }
        });
    }

    public void sendLocation(String lng, String lat, final HttpCallBack callBack) {
        LogUtils.e("userService", lng + ":" + lat);
        if (StringUtil.isNullOrEmpty(lng) || StringUtil.isNullOrEmpty(lat)) {
            LogUtils.e("userService", "position is null");
            return;
        }
        final String url = util.getUrl(R.string.url_location);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lng", lng);
        map.put("lat", lat);
        User user = getSavedUser();
        if (user != null) {
            map.put("uid", user.getUid());
        }
        XUtil.Post(url, map, new MyCallBack<BaseEntity<String>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<String> arg0) {
                if (arg0.getResult() == -2) {
                    callBack.onFail(-2, "", false);
                    return;
                }
                callBack.onSuccess(arg0);
                LogUtils.e("res", url + " result:  " + arg0.getResult());
            }
        });
    }

    public void checkForget(Map<String, Object> params, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_checkForget);
        XUtil.Post(url, params, new MyCallBack<BaseEntity<String>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<String> arg0) {
                if (arg0.getResult() == -2) {
                    callBack.onFail(-2, "", false);
                    return;
                }
                callBack.onSuccess(arg0);
            }
        });
    }

    public void resetPass(Map<String, Object> params, final HttpCallBack back) {
        String url = util.getUrl(R.string.url_resetPass);
        XUtil.Post(url, params, new MyCallBack<BaseEntity<String>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                back.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<String> arg0) {
                if (arg0.getResult() == -2) {
                    back.onFail(-2, "", false);
                    return;
                }
                back.onSuccess(arg0);
            }
        });
    }

    public void saveSessionId(String token) {
        SessionStore.setSessionId(token);
        shareUtil.saveSessionId(token, context);
    }

    public String getSessionId() {
        String session = SessionStore.getIntence().getSessionId();
        if (StringUtil.isNullOrEmpty(session)) {
            String s = shareUtil.findSessionId(context);
            if (StringUtil.isNullOrEmpty(s)) {
                SessionStore.setSessionId(s);
            }
            return s;
        } else {
            return session;
        }
    }

    public void changePass(Map<String, Object> params, final HttpCallBack back) {
        String url = util.getUrl(R.string.url_editPasswordByPhoneCode);
        XUtil.Post(url, params, new MyCallBack<BaseEntity<String>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                back.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<String> arg0) {
                if (arg0.getResult() == -2) {
                    back.onFail(-2, "", false);
                    return;
                }
                back.onSuccess(arg0);
            }
        });
    }

    public void changeDriverInfo(Map<String, Object> params, final HttpCallBack callback) {
        String url = util.getUrl(R.string.url_update);
        XUtil.Post(url, params, new MyCallBack<BaseEntity<String>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callback.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<String> arg0) {
                if (arg0.getResult() == 1) {
                    callback.onSuccess(arg0);
                } else {
                    callback.onFail(arg0.getResult(), arg0.getMessage(), true);
                }
            }
        });
    }

    public void getUserDetail(final HttpCallBack callback) {
        String url = util.getUrl(R.string.url_getDetail);
        XUtil.Post(url, null, new MyCallBack<BaseEntity<LinkedTreeMap<String, Object>>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callback.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<LinkedTreeMap<String, Object>> arg0) {
                //if (arg0.getResult() == 1) {
                UserDetail detail = new UserDetail();
                LinkMapToObjectUtil.getObject(arg0.getData(), detail);
                callback.onSuccess(detail);
                // } else {
                //    callback.onFail(arg0.getResult(), arg0.getMessage(), false);
                // }
            }
        });
    }

    /**
     *
     *@作者 shuye
     *@时间 2019/3/28 0028 上午 11:00
     *通过验证码更新手机号
     */
    public void Revisephone(Map<String, Object> map,final HttpCallBack callBack){
        String url = util.getUrl(R.string.url_Revisephone);
        XUtil.Post(url, map, new MyCallBack<BaseEntity<LinkedTreeMap<String,Object>>>() {
            @Override
            public void onSuccess(BaseEntity<LinkedTreeMap<String, Object>> ag1) {
                callBack.onSuccess(ag1);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }
        });
    }


    public void logout(final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_logout);
        XUtil.Post(url, null, new MyCallBack<BaseEntity<LinkedTreeMap<String, Object>>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<LinkedTreeMap<String, Object>> arg0) {
                callBack.onSuccess(arg0);
            }
        });
        saveSessionId(null);
        SessionStore.resetSessionId();
        SessionStore.setMyToken(null);
        shareUtil.clearUserInfo();
        user = null;
    }

    /**
     * 获取客户的联系人数据
     * @param bossId
     * @param callBack
     */
    @SuppressWarnings("rawtypes")
    public void getBossContracts(int bossId, final HttpCallBack callBack) {
        String path = util.getUrl(R.string.url_getcontacts) + "?bossId=" + bossId;
        XUtil.Post(path, null, new MyCallBack<ContactEntity<ArrayList>>() {
            @Override
            public void onError(Throwable throwable, boolean b) {
                callBack.onError(throwable, b);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(ContactEntity<ArrayList> arg0) {
                if (arg0.getBossId() != 0) {
                    ArrayList<LinkedTreeMap<String, Object>> list = arg0.getContacts();
                    List<Contacts> contacts = new ArrayList<Contacts>();
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            Contacts con = new Contacts();
                            LinkMapToObjectUtil.getObject(list.get(i), con);
                            contacts.add(con);
                        }
                    }
                    callBack.onSuccess(contacts);
                } else {
                    callBack.onFail(-1, "获取失败", true);
                }
            }
        });
    }

    /**
     * 保存一条通话记录
     * @param bossId
     * @param uid
     * @param phone
     */
    @SuppressWarnings("rawtypes")
    public void saveOfflineCall(int bossId,int uid,String phone ,final HttpCallBack callBack) {
        String path = util.getUrl(R.string.url_offlineCall) + "?bossId=" + bossId+"&uid="+uid+"&descr=from APP&phone="+phone;
        XUtil.Post(path, null, new MyCallBack<BaseEntity<String>>() {
            @Override
            public void onError(Throwable throwable, boolean b) {
                callBack.onError(throwable, b);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(BaseEntity<String> arg0) {
                callBack.onSuccess(arg0);
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void getCurrentVersion(int currentVersion, MyCallBack callBack) {
        String url = util.getUrl(R.string.url_checkVersion);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("os", "android");
        params.put("clientName", "company");
        params.put("version", currentVersion);
        XUtil.Post(url, params, callBack);
    }

    public void downLoadUpdate(String url, final HttpCallBack back) {
        url = util.getHost() + url + "?t=" + System.currentTimeMillis();
        if(MyApplication.isDebug()){
            url = url.replace(ApplicationConfig.getIntance().getPort(), "25606");
        }else {
            url = url.replace(ApplicationConfig.getIntance().getPort(), "443");
        }
        /*url = url.replace("//", "/");
        url = url.replace("https:/", "http://");
        url = url.replace("/portalWeb", "");*/
        XUtil.DownLoadFile(url, FileUtil.getDownPath(), new MyCallBack<File>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {

                back.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(File arg0) {
                back.onSuccess(arg0);
            }
        });
    }

    public void saveValue(String key, String value) {
        shareUtil.setNomalValue(key, value, context);
    }

    public String getNomalValue(String key, String defValue) {
        String value = shareUtil.getNomalValue(key, context);
        return (StringUtil.isNullOrEmpty(value) ? defValue : value);
    }

    /**
     * 获取当前和用户所在的公司
     *
     * @param callBack
     */
    public void getMyCompany(final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_getMycompany);
        XUtil.Post(url, null, new MyCallBack<BaseEntity<LinkedTreeMap<String, Object>>>() {
            @Override
            public void onError(Throwable throwable, boolean b) {
                callBack.onError(throwable, b);
            }

            @Override
            public void onSuccess(BaseEntity<LinkedTreeMap<String, Object>> stringBaseEntity) {
                if (stringBaseEntity != null && stringBaseEntity.getResult() == 1) {
                    Company company = new Company();
                    LinkMapToObjectUtil.getObject(stringBaseEntity.getData(), company);
                    callBack.onSuccess(company);
                }
            }
        });
    }

    public void relogin(User user, final HttpCallBack callBack) {
        if (user != null && user.getToken() != null && user.getUnionId() != null) {
            final String url = util.getUrl(R.string.url_login);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("unionId", user.getUnionId());
            map.put("loginType", user.getLoginType());
            XUtil.Post(url, map, new MyCallBack<BaseEntity<LinkedTreeMap<String, Object>>>() {
                @Override
                public void onError(Throwable throwable, boolean b) {
                    callBack.onError(throwable, b);
                }

                @Override
                public void onSuccess(BaseEntity<LinkedTreeMap<String, Object>> baseEntity) {
                    if (baseEntity.getResult() == 1) {
                        User user = new User();
                        LinkMapToObjectUtil.getObject(baseEntity.getData(), user);
                        UserService.user = user;
                        callBack.onSuccess(user,baseEntity.getToken());
                    } else {
                        callBack.onFail(baseEntity.getResult(), baseEntity.getMessage(), false);
                    }
                }
            });
        }
    }

    public String getHost() {
        return util.getHost();
    }

    public void saveVersionInfo(VersionInfo info) {
        shareUtil.saveVersion(info);
    }

    public VersionInfo getVersionInfo() {
        return shareUtil.getVersion();
    }


    //获取司机认证信息
    public void saveCompanyInfo(Company companyInfo, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_saveCompanyInfo);
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("company.name", companyInfo.getName());
        params.put("company.alias", companyInfo.getAlias());
        params.put("company.address", companyInfo.getAddress());
        params.put("company.office_tel", companyInfo.getOffice_tel());
        params.put("company.fax", companyInfo.getFax());
        params.put("company.org_id", companyInfo.getOrg_id());
        params.put("company.legaler", companyInfo.getLegaler());
        params.put("company.tel", companyInfo.getTel());
        params.put("company.decs", companyInfo.getDecs());
        params.put("company.logo", companyInfo.getLogo());
        params.put("company.cityCode", companyInfo.getCityCode());
        params.put("company.city", companyInfo.getCity());

        XUtil.Post(url, params, new MyCallBack<BaseEntity<ArrayList>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<ArrayList> arg0) {
                callBack.onSuccess(arg0);
            }

        });
    }

    /**
     * 保存当前位置信息
     *
     * @param address
     */
    public void saveAddress(final Address address) {
        shareUtil.setModelValue(address, context);
        if (address != null && !StringUtil.isNullOrEmpty(address.getCity())) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String city = address.getCity();
                    if (!StringUtil.isNullOrEmpty(city)) {
                        if (city.endsWith("市") || city.endsWith("县")) {
                            city = city.substring(0, address.getCity().length() - 1);
                        }
                        LogUtils.e("UserService", "         saveAddress:city: " + city);
                        //new PushTagsService(MyApplication.getContext()).addCurrentCityTag(city);
                    }
                }
            });
            thread.start();
        }
    }

    public Address getAddress() {
        return shareUtil.getModelValue(context);
    }
}

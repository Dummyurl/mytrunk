 package com.car.portal.http;

 import android.app.ActivityManager;
 import android.content.Context;
 import android.util.Base64;
 import android.util.Log;

 import com.car.portal.application.AppConfig;
 import com.car.portal.application.MyApplication;
 import com.car.portal.entity.NotNetWorkException;
 import com.car.portal.entity.User;
 import com.car.portal.util.BaseUtil;
 import com.car.portal.util.LogUtils;
 import com.car.portal.util.SharedPreferenceUtil;
 import com.orhanobut.hawk.Hawk;

 import org.xutils.common.Callback.Cancelable;
 import org.xutils.common.Callback.CommonCallback;
 import org.xutils.common.util.MD5;
 import org.xutils.http.RequestParams;
 import org.xutils.x;

 import java.io.File;
 import java.io.IOException;
 import java.io.InputStream;
 import java.security.KeyStore;
 import java.security.SecureRandom;
 import java.security.cert.Certificate;
 import java.security.cert.CertificateFactory;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Random;
 import java.util.Set;
 import java.util.SortedMap;
 import java.util.TreeMap;

 import javax.net.ssl.SSLContext;
 import javax.net.ssl.SSLSocketFactory;
 import javax.net.ssl.TrustManagerFactory;

 import okio.Buffer;
 public class XUtil {

     /**
      * 获得当前Activity的名字，包括完整包名的名字
      * @param context
      * @return
      */
     static String getTopActivity(Context context)
     {
         ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE) ;
         List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;
         if(runningTaskInfos != null)
             return (runningTaskInfos.get(0).topActivity).toString() ;
         else
             return null ;
     }


     /**
      * 产生随机数
      * @param
      * @return
      */
     public static String createNoncestr()
     {
         String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
         String res = "";
         for (int i = 0; i < 16; i++)
         {
             Random rd = new Random();
             res += chars.charAt(rd.nextInt(chars.length() - 1));
         }
         return res;
     }

     /**
      * 返回当前时间字符串
      *
      * @return yyyyMMddHHmmss
      */
     public static String getDateStr()
     {
         String TIME = "yyyyMMddHHmmss";
         SimpleDateFormat sdf = new SimpleDateFormat(TIME);
         return sdf.format(new Date());
     }
     /**
      * @Description：创建sign签名
      * @param characterEncoding
      *            编码格式
      * @param parameters
      *            请求参数
      * @return
      */
     public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters)
     {
         StringBuffer sb = new StringBuffer();
         Set es = parameters.entrySet();
         Iterator it = es.iterator();
         while (it.hasNext())
         {
             Map.Entry entry = (Map.Entry) it.next();
             String k = (String) entry.getKey();
             Object v = entry.getValue();
             if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
             {
                 sb.append(k + "=" + v + "&");
             }
         }
         sb.append("key=14e1b600b1fd579f47433b88e8d85111");  //密钥先写死，服务器要与这里一致
         String sign = MD5.md5(sb.toString()).toUpperCase();
         return sign;
     }
     /**
      * 与后台服务器的连接改用Token验证
      * 构建连接Token
      * @User user
      * @
      */
     public static SortedMap<Object,Object> generateTokenMapParam(String username,String password){
         SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
         parameters.put("username", username);
         parameters.put("password", MD5.md5(password).toUpperCase());
         parameters.put("noncestr", createNoncestr().toUpperCase());
         parameters.put("timestamp",Long.toString(System.currentTimeMillis()/1000));
         String sign = createSign("UTF-8",parameters);
         parameters.remove("password");  //不需要传密码
         parameters.put("sign",sign);
         //SessionStore.setApp_token(sign); //产生新token，验证通过后，更新Token有效时间，如果验证不通过，更新有效时间为0（即时过期），否则为当前时间微秒数值
         return parameters;
     }
     /**
      * 处理发送服务器的参数
      * @param params
      */
     private static void processSignParams( RequestParams params,Map<String,String> map){
         User user = SharedPreferenceUtil.getIntence().getUserFromShared(MyApplication.getContext());
         String pass = "",username ="";
         if(map!=null){
             for (Map.Entry<String,String> entry : map.entrySet()) {
                 if("username".equals(entry.getKey())){
                     username = entry.getValue();
                 }else if("password".equals(entry.getKey())){
                     pass = entry.getValue();
                 }else{
                     params.addParameter(entry.getKey(), entry.getValue());
                 }
             }
         }
         if(user!=null) {
             pass = new String(Base64.decode(user.getPassword().getBytes(), Base64.NO_PADDING));
             username = user.getUsername();
         }
         SortedMap<Object, Object> mapSorted =null;
         mapSorted = XUtil.generateTokenMapParam(username,pass);
         params.addParameter("noncestr",mapSorted.get("noncestr").toString());
         params.addParameter("timestamp",mapSorted.get("timestamp").toString());
         params.addParameter("username",username);
         params.addParameter("userId_P",user==null?0:user.getUid());
         params.addParameter("sign",mapSorted.get("sign").toString());
     }

     private static void processSignParams2( RequestParams params,Map<String, ? extends Object> map){
         User user = SharedPreferenceUtil.getIntence().getUserFromShared(MyApplication.getContext());
         String pass = "",username ="";
         if(map!=null){
             for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
                 if("username".equals(entry.getKey())){
                     username = entry.getValue()==null?"":entry.getValue().toString();
                 }else if("password".equals(entry.getKey())){
                     pass = entry.getValue()==null?"":entry.getValue().toString();
                 }else{
                     params.addParameter(entry.getKey(), entry.getValue());
                 }
             }
         }
         if(user!=null) {
             if(user.getPassword()!=null) {
                 pass = new String(Base64.decode(user.getPassword().getBytes(), Base64.NO_PADDING));
                 username = user.getUsername();
             }
         }

         SortedMap<Object, Object> mapSorted =null;
         mapSorted = XUtil.generateTokenMapParam(username,pass);
         params.addParameter("noncestr",mapSorted.get("noncestr").toString());
         params.addParameter("timestamp",mapSorted.get("timestamp").toString());
         params.addParameter("username",username);
         params.addParameter("userId_P",user==null?0:user.getUid());
         params.addParameter("sign",mapSorted.get("sign").toString());
     }

     /**
      * 发送get请求
      * @param <T>
      */
     public static <T> Cancelable Get(String url, Map<String,String> map, CommonCallback<T> callback){
         if(MyApplication.getNetState()) {
             RequestParams params = new RequestParams(url);
             String loginType = Hawk.get("loginType");
             if("pass".equals(loginType)) {
                 processSignParams2(params, map);
             }else if("wx".equals(loginType)){
                 if(map!=null) {
                     for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
                         if(!"loginType".equals(entry.getKey().toString())) {
                             params.addParameter(entry.getKey(), entry.getValue());
                         }
                     }

                 }
             }else if("qq".equals(loginType)){
                 if(map!=null) {
                     for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
                         if(!"loginType".equals(entry.getKey().toString()))
                             params.addParameter(entry.getKey(), entry.getValue());
                     }

                 }
             }
             params.addParameter("loginType",loginType);
             params.addParameter("token",SessionStore.getMyToken());
             params.setCharset("utf-8");

             String sessionId = SessionStore.getSessionId();
            //BaseUtil.writeFile("XUtil", url);
             LogUtils.e("sessionId", sessionId + "");
             LogUtils.e("XUtil", url);
             if(sessionId != null) {
                 params.setHeader("Cookie", sessionId);
             }
             params.setUseCookie(true);


             //----------
             try {

                 InputStream inputStream=new Buffer().writeUtf8(MyApplication.isDebug()?AppConfig.cerTest:AppConfig.cer).inputStream();
                 //String key = "file:///android_asset/tomcat";
                 SSLContext tls = SSLContext.getInstance("TLS");
                 KeyStore keystore=KeyStore.getInstance(KeyStore.getDefaultType());	//创建一个keystore来管理密钥库
                 keystore.load(null,null);
                 Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(inputStream);
                 keystore.setCertificateEntry("tomcat", certificate);
                 try
                 { if (inputStream != null)
                     inputStream.close();
                 } catch (IOException e)
                 {
                 }
                 TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                 instance.init(keystore);
                 tls.init(null, instance.getTrustManagers(), new SecureRandom());
                 SSLSocketFactory socketFactory = tls.getSocketFactory();
                 params.setSslSocketFactory(socketFactory);
             }catch (Exception e){
                 e.printStackTrace();
             }

             //----------
             Cancelable cancelable = x.http().get(params, callback);
             return cancelable;
         } else {
             callback.onError(new NotNetWorkException(), true);
             callback.onFinished();
             return null;
         }
     }

     /**
      * 发送post请求
      * @param <T>
      */
     public static <T> Cancelable Post(String url, Map<String, ? extends Object> map, CommonCallback<T> callback){
         if(MyApplication.getNetState()) {
             RequestParams params = new RequestParams(url);
             String loginType = Hawk.get("loginType");
             if("pass".equals(loginType)) {
                 processSignParams2(params, map);
             }else if("wx".equals(loginType)){
                 if(map!=null) {
                     for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
                         if(!"loginType".equals(entry.getKey().toString())) {
                             params.addParameter(entry.getKey(), entry.getValue());
                         }
                     }
                 }
             }else if("qq".equals(loginType)){
                 if(map!=null) {
                     for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
                         if(!"loginType".equals(entry.getKey().toString()))
                            params.addParameter(entry.getKey(), entry.getValue());
                     }
                 }
             }
             params.addParameter("loginType",loginType);
             params.addParameter("token",SessionStore.getMyToken());
             params.setCharset("utf-8");
             String sessionId = SessionStore.getSessionId();
             //BaseUtil.writeFile("XUtil", url);
             //LogUtils.e("XUtil", url);
        //     BaseUtil.writeFile("SessionId", sessionId + "");
             LogUtils.e(sessionId, sessionId + "");
             if (sessionId != null) {
                 params.setHeader("Cookie", sessionId);
             }
             params.setUseCookie(false);
             Cancelable cancelable = null;
             try {
                 InputStream inputStream = new Buffer().writeUtf8(MyApplication.isDebug()?AppConfig.cerTest:AppConfig.cer).inputStream();
                 //String key = "file:///android_asset/tomcat";
                 SSLContext tls = SSLContext.getInstance("TLS");
                 KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());    //创建一个keystore来管理密钥库
                 keystore.load(null, null);
                 Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(inputStream);
                 keystore.setCertificateEntry("tomcat", certificate);
                 try {
                     if (inputStream != null)
                         inputStream.close();
                 } catch (IOException e) {
                 }
                 TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                 instance.init(keystore);
                 tls.init(null, instance.getTrustManagers(), new SecureRandom());
                 SSLSocketFactory socketFactory = tls.getSocketFactory();
                 params.setSslSocketFactory(socketFactory);
                 //-------
                 cancelable = x.http().post(params, callback);
             } catch (Exception e) {
                 e.printStackTrace();
             }
             return cancelable;

         } else {
             callback.onError(new NotNetWorkException(), true);
             callback.onFinished();
             return null;
         }
     }

     /**
      * 上传文件
      * @param <T>
      */
     public static <T> Cancelable UpLoadFile(String url, Map<String, File> map, CommonCallback<T> callback){
         if(MyApplication.getNetState()) {
             RequestParams params=new RequestParams(url);
             String sessionId = SessionStore.getSessionId();
             if(sessionId != null) {
                 params.setHeader("Cookie", sessionId);
             }
             if(null!=map){
                 for(Map.Entry<String, File> entry : map.entrySet()){
                     params.addBodyParameter(entry.getKey().toString(), entry.getValue());
                 }
             }
             params.addParameter("token",SessionStore.getMyToken());
             BaseUtil.writeFile("XUtil", url);
             //LogUtils.e("XUtil", url);
             BaseUtil.writeFile("SessionId", sessionId + "");
             //LogUtils.e("session", sessionId + "");
             params.setMultipart(true);
             params.setCharset("utf-8");
             params.setUseCookie(false);
             Cancelable cancelable = x.http().post(params, callback);
             return cancelable;
         } else {
             callback.onError(new NotNetWorkException(), true);
             callback.onFinished();
             return null;
         }
     }


     /**
      * 下载文件
      * @param <T>
      */
     public static <T> Cancelable DownLoadFile(String url, String filepath, CommonCallback<T> callback){
         if(MyApplication.getNetState()) {
             RequestParams params=new RequestParams(url);
             //设置断点续传
             params.setAutoResume(true);
             params.setSaveFilePath(filepath);
             params.setCharset("utf-8");
             String sessionId = SessionStore.getSessionId();
             //BaseUtil.writeFile("XUtil", url);
             //LogUtils.e("XUtil", url);
             BaseUtil.writeFile("SessionId", sessionId + "");
            // LogUtils.e("session", sessionId + "");
             params.addParameter("token",SessionStore.getMyToken());
             if(sessionId != null) {
                 params.setHeader("Cookie", sessionId);
                 params.setHeader("Cache-Control", "no-cache");
             }
             params.setUseCookie(true);
             //启动ssl通讯
             try {
                 InputStream inputStream = new Buffer().writeUtf8(MyApplication.isDebug()?AppConfig.cerTest:AppConfig.cer).inputStream();
                 //String key = "file:///android_asset/tomcat";
                 SSLContext tls = SSLContext.getInstance("TLS");
                 KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());    //创建一个keystore来管理密钥库
                 keystore.load(null, null);
                 Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(inputStream);
                 keystore.setCertificateEntry("tomcat", certificate);
                 try {
                     if (inputStream != null)
                         inputStream.close();
                 } catch (IOException e) {
                 }
                 TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                 instance.init(keystore);
                 tls.init(null, instance.getTrustManagers(), new SecureRandom());
                 SSLSocketFactory socketFactory = tls.getSocketFactory();
                 params.setSslSocketFactory(socketFactory);
             } catch (Exception e) {
                 e.printStackTrace();
             }
             //结束SSL通讯
             Cancelable cancelable = x.http().get(params, callback);
             return cancelable;
         } else {
             callback.onError(new NotNetWorkException(), true);
             callback.onFinished();
             return null;
         }
     }
 }

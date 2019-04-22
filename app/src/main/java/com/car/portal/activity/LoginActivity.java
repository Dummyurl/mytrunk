package com.car.portal.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.car.portal.R;
import com.car.portal.entity.ApplicationConfig;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.MyJsonReturn;
import com.car.portal.entity.TencetEntity;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.SessionStore;
import com.car.portal.model.MainscheduleModel;
import com.car.portal.service.GoodsService;
import com.car.portal.service.UserService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.InitApplicationUtil;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.orhanobut.hawk.Hawk;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import cn.jpush.android.api.JPushInterface;

@SuppressWarnings("unchecked")
public class LoginActivity extends BaseActivity {

	@ViewInject(R.id.login_user)
	private EditText userEidt;
	@ViewInject(R.id.login_pass)
	private EditText passEidt;
	private UserService userService;
	private boolean hasFind;
	private IWXAPI iwxapi;
	public static QQAuth mQQAuth;
	private UserInfo mInfo;
	private TencetEntity tencet;
	private BroadcastReceiver receiver;

	private String username;
	private String password;
	private User user;

	private GoodsService goodsService;
	private InputMethodManager imm;
    private MaterialDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		x.view().inject(this);
		userService = new UserService(LoginActivity.this);
		goodsService=new GoodsService(LoginActivity.this);
		receiver = new WxAuthFinishReceiver();
	}


	//登陆弹框
    private void onshow(boolean b) {
	    if(b) {
            dialog = new MaterialDialog.Builder(this)
                    .title("登陆中")
                    .content("请稍后...")
                    .progress(true, 0)
                    .show();
        }else if (dialog!=null){
	        dialog.dismiss();
        }
    }

    @Event(value = { R.id.submit, R.id.login_forgetPass, R.id.login_reg, R.id.img_wechat, R.id.img_qq })
	private void login(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.login_reg:
			Hawk.put("loginType","pass");
			Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.login_forgetPass:
			Intent intent2 = new Intent(LoginActivity.this, ForgotPassActivity.class);
			startActivity(intent2);
			break;
		case R.id.submit:
			imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            onshow(true);
			username = userEidt.getText().toString();
			password = passEidt.getText().toString();
			String loginType = Hawk.get("loginType");
			if(StringUtil.isNullOrEmpty(loginType)) Hawk.put("loginType","pass");
			if (StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password)) {
				ToastUtil.show("请检查用户名和密码是否完整", LoginActivity.this);
				onshow(false);
				return;
			} else {
				username = username.trim();
				password = password.trim();
			}
			if (userService.CheckINetConnect()) {
				//SessionStore.resetApp_Token(); //清空Token重新登录
				SortedMap<String, String> param = new TreeMap<String, String>();
				param.put("username",username);
				param.put("password",password);
				Hawk.put("loginType","pass");
				userService.login(param, loginCall);
			} else {
                onshow(false);
				ToastUtil.show("网络未连接，请检查您的网络", LoginActivity.this);
			}
			break;
		case R.id.img_wechat:
			Hawk.put("loginType","wx");
			wxAuthorizationLogin();
			break;
		case R.id.img_qq:
			Hawk.put("loginType","qq");
			qqAuthorizationLogin();
			break;
		}
	}

	/**
	 * 微信授权登陆
	 */
	public void wxAuthorizationLogin(){
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.car.portal.wxAuthFinish");
		registerReceiver(receiver, filter);
		String wxid = ApplicationConfig.getIntance().getWxAppId();
		iwxapi= WXAPIFactory.createWXAPI(this, wxid, true);	//通过微信api工厂实例化api
		iwxapi.registerApp(wxid);								//注册自己的appid到微信
		boolean sIsWXAppInstalledAndSupported = iwxapi.isWXAppInstalled()
				&& iwxapi.isWXAppSupportAPI();								//检测是否安装微信客户端
		if(sIsWXAppInstalledAndSupported) {
			//授权
			SendAuth.Req req = new SendAuth.Req();
			//授权读取用户信息
			req.scope = "snsapi_userinfo";
			//自定义信息
			req.state = "wechat_sdk_demo_test";
			//向微信发送请求
			boolean result = iwxapi.sendReq(req);
			LogUtils.e("LoginActivity", "调用微信返回结果：" + result);
		} else {
			ToastUtil.show("检测到您手机还未安装微信，请先下载安装微信客户端", this);
		}
	}

	/**
	 * QQ授权登陆
	 */
	public void qqAuthorizationLogin(){
		final Context context = LoginActivity.this;
		final Context ctxContext = context.getApplicationContext();
		mQQAuth = QQAuth.createInstance(ApplicationConfig.getIntance().getQqAppId(), ctxContext);
		if(isQQClientAvailable(this)) {
			if (!mQQAuth.isSessionValid()) {
				mQQAuth.login(LoginActivity.this, "get_user_info", qqListener);
			} else {
				mQQAuth.logout(LoginActivity.this);
			}
		}else {
			ToastUtil.show("检测到您手机还未安装qq，请先下载安装qq客户端", this);
		}
	}

	//判断是否安装了qq
	public static boolean isQQClientAvailable(Context context) {
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * 获取qq用户信息
	 */
	private void updateUserInfo() {
		if (mQQAuth != null && mQQAuth.isSessionValid()) {
			IUiListener listener = new IUiListener() {
				@Override
				public void onError(UiError e) {
					System.out.println(e);
				}

				@Override
				public void onComplete(Object response) {
					try{
						if(response == null) {
							return;
						}
						JSONObject obj = (JSONObject) response;
						tencet.setNickname(obj.getString("nickname"));
						tencet.setCity(obj.getString("city"));
						tencet.setGender(obj.getString("gender"));
						tencet.setProvince(obj.getString("province"));
					} catch (JSONException e) {
						BaseUtil.writeFile("Login_QQLogin", e);
					}
				}

				@Override
				public void onCancel() {
				}
			};
			mInfo = new UserInfo(this, mQQAuth.getQQToken());
			mInfo.getUserInfo(listener);
		}
	}

	/**
	 * QQ登陆授权登陆的回调监听
	 * 可以在这一步获取openid，access_token
	 */
	private IUiListener qqListener = new IUiListener() {
		@Override
		public void onComplete(Object response) {
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {
			ToastUtil.show("授权成功！",LoginActivity.this);
			tencet = new TencetEntity();
			doAfterComplete(values);
			LogUtils.e("BaseUiListener", values.toString());
			updateUserInfo();
		}

		@Override
		public void onError(UiError e) {
			ToastUtil.show("授权出错:" + e.errorDetail, LoginActivity.this);
		}

		@Override
		public void onCancel() {
			ToastUtil.show("取消授权", LoginActivity.this);
		}
	};

	private void doAfterComplete(JSONObject values) {
		try {
			tencet.setAccess_token(values.getString("access_token"));
			tencet.setOpenId(values.getString("openid"));
			tencet.setPay_token(values.getString("pay_token"));
			tencet.setPf(values.getString("pf"));
			tencet.setPfkey(values.getString("pfkey"));
			userService.loginByQq(tencet.getOpenId(), new HttpCallBack(LoginActivity.this) {
				@SuppressWarnings("rawtypes")
				@Override
				public void onSuccess (Object... objects) {
					if(objects != null && objects.length > 0) {
						BaseEntity entity = (BaseEntity) objects[0];
						LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) entity.getData();
						if(entity.getResult() == 1) {
							User u = new User();
							LinkMapToObjectUtil.getObject(data, u);
							LogUtils.e("LoginActivity", "          userType: " + u.getUserType());
							if (u.getUserType() == 0) {
								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
								startActivity(intent);
								u.setLoginType("qq");
								userService.saveLoginUser(u);
								finish();
							} else {
								ToastUtil.show("该qq已经绑定其他用户！", LoginActivity.this);
							}
						} else {
							Intent intent = new Intent(LoginActivity.this, DriverBindActivity.class) ;
							intent.putExtra("AuthType", 1);
							intent.putExtra("unionId", entity.getMessage());
							LogUtils.e("userLoginAction", entity.getMessage());
							startActivity(intent);
						}
					}
				}

				@Override
				public void onFail (int result, String message, boolean show) {
					super.onFail(result, message, show);
				}

				@Override
				public void onError (Object... objects) {
					super.onError(objects);
				}
			});
		} catch (Exception e) {
            onshow(false);
			BaseUtil.writeFile("LoginAction_QQLogin", e);
			e.printStackTrace();
			ToastUtil.show("登录失败", LoginActivity.this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(userService != null) {
			String firstLogin = userService.getNomalValue("isfirstlogin", "true");
			if("true".equals(firstLogin)) {
				InitApplicationUtil.getInitance().initCity();
				userService.saveValue("isfirstlogin", "false");
			} else {
				userService.saveValue("isfirstlogin", "false");
			}
		}
		if (!hasFind) {
			hasFind = true;


			//在MyApplication中，如果没有sessionId==null，则根据本地存储的数据进行静默登录，获取
			//sessionId和刷新Token
			//在此方法中，进行sessionId检查，如果为null，则不同进行静默登录，只是显示主界面或显示
			//正常的登录界面，但会将用户名和密友显示到界面中，由用户操作登录


			String loginType = Hawk.get("loginType");
			if(StringUtil.isNullOrEmpty(loginType)){
				loginType = "pass";
				Hawk.put("loginType",loginType);
			}
			String token = SessionStore.getMyToken();
			if (StringUtil.isNullOrEmpty(token) || SessionStore.getSessionId()==null) {
					user = userService.getSavedUser();
					if (user != null) {
						userEidt.setText(user.getUsername());
						passEidt.setText(user.getPassword());
					}
			} else {
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}

	private HttpCallBack loginCall = new HttpCallBack(LoginActivity.this) {
		public void onSuccess(Object... objects) {
			onshow(false);
			if (objects != null && objects.length > 0) {
				if(objects[0]!=null)
					user = (User) objects[0];
				if (user != null && user.getUserType() == 0) {
					user.setFocusmax(10);
					user.setLoginType("pass");//设定当前的登录方式为一般方式，即非微信或QQ授权
					userService.saveLoginUser(user);
					findContactPhone();
					/*String wxopid;
					if(user.getWx_openId()==null){
						wxopid="";
					}else {
						wxopid = user.getWx_openId();
					}*/
					userService.saveValue("loginTime", Long.toString(System.currentTimeMillis()));
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.putExtra("name",username);
					intent.putExtra("pwd",password);
					startActivity(intent);
					finish();
				} else if(user!=null && user.getUserType() != 0) {
					ToastUtil.show("您不是公司用户，无法使用该客户端登录", LoginActivity.this);
				} else {
					ToastUtil.show("没有找到您的信息", LoginActivity.this);
				}
			} else {
				ToastUtil.show("数据传输错误", LoginActivity.this);
			}
		}

		@Override
		public void onFail(int result, String message, boolean show) {
			onshow(false);
			ToastUtil.show(message,LoginActivity.this);
		}
	};
    //登录时初始化公司内同事数据，如果有变新，需要用户退出再登录才能更新
	public void findContactPhone(){
		//contactTelsList等一般情况下此列表都是不会变的，在第一次登录时可以进行下载更新本地
		goodsService.findContactPhone(null, new HttpCallBack(LoginActivity.this) {
			@Override
			public void onSuccess(Object... objects) {
				String[] contactNameList,telsList; //后台获取的联系人电话号码跟名字数组
				int uids[];
				MyJsonReturn<String> returnJson = (MyJsonReturn<String>) objects[0];
				contactNameList = returnJson.getContactsNames();
				telsList = returnJson.getTels();
				uids = returnJson.getUids();
				if (contactNameList.length > 0 && telsList.length > 0 && uids.length > 0) {
					Hawk.put("contactTelsList", telsList);
					Hawk.put("contactUids", uids);
					Hawk.put("contactNameList", contactNameList);
				}

				if(returnJson.getToken()!=null && !"".equals(returnJson.getToken())){
					User us = userService.getSavedUser();
					us.setToken(returnJson.getToken());
					userService.saveLoginUser(us);
				}

			}

			@Override
			public void onError(Object... objects) {
				super.onError(objects);
				Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}
	protected void onDestroy() {
		super.onDestroy();
		hasFind = false;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	class WxAuthFinishReceiver extends BroadcastReceiver{
		@Override
		public void onReceive (Context context, Intent intent) {
			if(intent.getAction().equals("com.car.portal.wxAuthFinish")) {
				authFinished(intent.getStringExtra("code"));
			}
		}
	}

	private void authFinished(final String code){
		unregisterReceiver(receiver);
		Hawk.put("loginType","wx"); //进行微信授权方式登录
		//有且只有一种验证方式是有效的，所以要进行新方式验证时应清除之前已经存在的验证token
//		SessionStore.resetApp_Token();
		userService.wechatLogin(code, new HttpCallBack(getApplicationContext()) {
			@SuppressWarnings("rawtypes")
			@Override
			public void onSuccess(Object... objects) {
				if (objects != null && objects.length > 0){
					BaseEntity baseEntity = (BaseEntity) objects[0];
					int result = baseEntity.getResult();
					if (result == -1) {
						ToastUtil.show("授权失败！", LoginActivity.this);
					/*} else if (result == 0) {  //没有绑定，转入绑定界面
//						Intent intent = new Intent(LoginActivity.this, DriverBindActivity.class);
//						intent.putExtra("AuthType", -1);
//						intent.putExtra("type","");
//						intent.putExtra("unionId", baseEntity.getData().toString());
//						startActivity(intent);
						//直接绑定，发送空用户和空密码，默认发送unionId,如果需要code,可直接修改，后台根据code拿到用户信息注册
						userService.bindUser("", "", null,code, -1, new HttpCallBack(LoginActivity.this){
							@Override
							public void onSuccess (Object... objects) {
								if(objects != null && objects.length > 0) {
									User user = (User) objects[0];
									user.setLoginType("wx");
									userService.saveLoginUser(user);
									//SessionStore.setMyToken(user.getToken());
									Intent intent = new Intent(LoginActivity.this, MainActivity.class);
									startActivity(intent);
								}
							}
						});*/
					} else if (result == 1) { //授权成功
                        User user = new User();
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						if(baseEntity.getCounts()==999){
							intent.putExtra("isnewuser","yes");
						}
						startActivity(intent);
						LinkMapToObjectUtil.getObject((LinkedTreeMap) baseEntity.getData(), user);
						userService.saveLoginUser(user);
						JPushInterface.setAlias(getApplicationContext(),user.getUid(),"Push_Alias_"+user.getUid());
						findContactPhone();
                        Hawk.put("user",user);
						finish();
					}
				}
			}
		});
	}
}

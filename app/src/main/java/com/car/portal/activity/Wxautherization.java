package com.car.portal.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.entity.ApplicationConfig;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.SessionStore;
import com.car.portal.model.MainscheduleModel;
import com.car.portal.service.UserService;
import com.car.portal.util.LogUtils;
import com.car.portal.util.ToastUtil;
import com.orhanobut.hawk.Hawk;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Wxautherization extends AppCompatActivity {

    @BindView(R.id.btn_wxauther)
    Button btnWxauther;
    @BindView(R.id.btn_close)
    TextView btnClose;
    BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxautherization);
        ButterKnife.bind(this);
        inittooler();
    }

    private void inittooler() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("绑定微信");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab!=null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @OnClick({R.id.btn_wxauther, R.id.btn_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_wxauther:
                wxAuthorizationLogin(Wxautherization.this);
                break;
            case R.id.btn_close:
                finish();
                break;
        }
    }

    private void wxAuthorizationLogin(Context context){
        receiver = new WxAuthFinishReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.car.portal.wxAuthFinish");
        context.registerReceiver(receiver, filter);
        String wxid = ApplicationConfig.getIntance().getWxAppId();
        IWXAPI iwxapi= WXAPIFactory.createWXAPI(context, wxid, true);	//通过微信api工厂实例化api
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
            ToastUtil.show("检测到您手机还未安装微信，请先下载安装微信客户端", context);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return  true;
    }

    /**
     *
     *@作者 shuye
     *@时间 2019/3/20 0020 下午 2:07
     *  isauthorization 判断是否授权，1为授权，0为未授权
     */
    class WxAuthFinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive (final Context context, final Intent intent) {
            if(intent.getAction().equals("com.car.portal.wxAuthFinish")) {
                String code = intent.getStringExtra("code");
                context.unregisterReceiver(receiver);
                Hawk.put("loginType","pass"); //进行微信授权方式登录


                final UserService  userService= new UserService(context);
                User user = new  UserService(Wxautherization.this).getSavedUser();
                userService.bindUser(user.getUsername(), user.getPassword(),null, code, -1, new HttpCallBack(Wxautherization.this) {
                    @Override
                    public void onSuccess(Object... objects) {
                        if(objects!=null && objects.length>1 && "1".equals(objects[1].toString())) {
                            Toast.makeText(Wxautherization.this, "您已成功绑定", Toast.LENGTH_SHORT).show();
                            User user = (User) objects[0];
                            userService.saveLoginUser(user);
                            Intent intent1 = new Intent();
                            intent1.putExtra("result", "success");
                            Wxautherization.this.setResult(RESULT_OK, intent1);
                            finish();
                        }else{
                            Toast.makeText(Wxautherization.this, "绑定失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}

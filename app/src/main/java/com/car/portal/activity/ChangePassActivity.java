package com.car.portal.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.car.portal.R;
import com.car.portal.activity.service.MainService;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.ParseReturn;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.UserService;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;

import org.xutils.common.util.MD5;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.xutils.common.util.MD5.md5;

//import org.xutils.view.annotation.ViewInject;

public class ChangePassActivity extends AppCompatActivity {


    @BindView(R.id.check_pwd_code)
    EditText checkPwdCode;
    @BindView(R.id.btn_border)
    MaterialButton btnBorder;
    @ViewInject(R.id.change_pass)
    private EditText newEdit;
    @ViewInject(R.id.change_confirm)
    private EditText confEdit;
    @ViewInject(R.id.change_sub)
    private Button sub;

    private String pass;
    private String confirm;
    private String check_code;
    private UserService service;
    private User user;
    private Map<String, Object> params;
    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass);
        ButterKnife.bind(this);
        x.view().inject(ChangePassActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("修改密码");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);


        service = new UserService(ChangePassActivity.this);
        user = service.getLoginUser();
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_code =checkPwdCode.getText().toString();

                if(StringUtil.isNullOrEmpty(check_code)){
                    ToastUtil.show("请输入验证码",ChangePassActivity.this);
                    return;
                }

                pass = newEdit.getText().toString();
                confirm = confEdit.getText().toString();
                if (StringUtil.isNullOrEmpty(pass)) {
                    ToastUtil.show("请输入新密码", ChangePassActivity.this);
                    newEdit.requestFocus();
                    return;
                } else if (StringUtil.isNullOrEmpty(confirm)) {
                    ToastUtil.show("请重新输入密码", ChangePassActivity.this);
                    confEdit.requestFocus();
                    return;
                } else if (!pass.equals(confirm)) {
                    ToastUtil.show("您两次输入的密码不一致", ChangePassActivity.this);
                    confEdit.requestFocus();
                    return;
                } else if (pass.trim().length() < 6) {
                    ToastUtil.show("为了安全，您的密码至少6位", ChangePassActivity.this);
                    newEdit.requestFocus();
                    return;
                } else {
                    params = new HashMap<String, Object>();
                    params.put("passwordP", MD5.md5(pass).toUpperCase());
                    params.put("vcode", check_code);
                    service.changePass(params, back);
                }
            }
        });
    }

    private HttpCallBack back = new HttpCallBack(ChangePassActivity.this) {
        @Override
        public void onSuccess(Object... objects) {
            if (objects != null && objects.length > 0) {
                @SuppressWarnings("unchecked")
                BaseEntity<String> entity = (BaseEntity<String>) objects[0];
                if (entity.getResult() == -1) {
                    ToastUtil.show(entity.getMessage(), ChangePassActivity.this);
                    finish();
                } else {
                    ToastUtil.show("密码更新成功,请重新登陆", ChangePassActivity.this);
                    service.logout(new HttpCallBack(getApplicationContext()) {
                        @Override
                        public void onSuccess(Object... objects) {

                        }
                    });
                    try {
                        NotificationManager manger = (NotificationManager) getApplicationContext()
                                .getSystemService(Activity.NOTIFICATION_SERVICE);
                        manger.cancel(MainService.NOTIFICAT_ID);
                    } catch (Exception e) {}
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    MyApplication.getContext().exit();
                    finish();
                }
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @OnClick({R.id.btn_border})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_border:
                String phone = user.getPhone();
                if (phone == null || "".equals(phone)) {
                    new MaterialDialog.Builder(ChangePassActivity.this)
                            .title("绑定手机号")
                            .content("检测到您还未绑定手机号，请前往个人信息页面绑定手机号!")
                            .contentColor(getResources().getColor(R.color.title_black))
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                }
                            })
                            .show();
                } else {
                    service.getPhoneCodeOfForgot(phone, new HttpCallBack(ChangePassActivity.this) {
                        @Override
                        public void onSuccess(Object... objects) {
                            if (objects != null && objects.length > 0) {
                                ParseReturn entity = (ParseReturn) objects[0];
                                if (entity == null) {
                                    btnBorder.setText("获取验证码");
                                    btnBorder.setEnabled(true);
                                    ToastUtil.show("获取数据有错！", ChangePassActivity.this);
                                } else {
                                    if ("0".equals(entity.getMessage().getCode())) {
                                        ToastUtil.show(entity.getMessage().getResult(), ChangePassActivity.this);
                                    } else if ("1".equals(entity.getMessage().getCode())) {
                                        if (timer != null) {
                                            timer.cancel();
                                        }
                                        btnBorder.setText("获取验证码");
                                        ToastUtil.show(entity.getMessage().getResult() + "", ChangePassActivity.this);
                                        btnBorder.setEnabled(true);
                                    } else if ("-1".equals(entity.getMessage().getCode())) {
                                        if (timer != null) {
                                            timer.cancel();
                                        }
                                        btnBorder.setText("获取验证码");
                                        ToastUtil.show(entity.getMessage().getResult() + "", ChangePassActivity.this);
                                        btnBorder.setEnabled(true);
                                    } else {
                                        if (timer != null) {
                                            timer.cancel();
                                        }
                                        btnBorder.setText("获取验证码");
                                        ToastUtil.show("验证码获取有误", ChangePassActivity.this);
                                        btnBorder.setEnabled(true);
                                    }
                                }
                            }
                        }
                    });

                    timer = new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            btnBorder.setText(millisUntilFinished / 1000 + "秒后获取");
                            btnBorder.setEnabled(false);
                        }

                        @Override
                        public void onFinish() {
                            btnBorder.setText("获取验证码");
                            btnBorder.setEnabled(true);
                        }
                    }.start();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null) {
            timer.cancel();
        }
    }
}

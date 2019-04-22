package com.car.portal.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.ParseReturn;
import com.car.portal.entity.User;
import com.car.portal.fragment.DriverPersonFragment;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.UserService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.ToastUtil;
import com.google.gson.internal.LinkedTreeMap;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RevisephoneActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_new_phone)
    EditText editNewPhone;
    @BindView(R.id.edit_reg_phonecheck)
    EditText editRegPhonecheck;
    @BindView(R.id.btn_checkcode)
    MaterialButton btnCheckcode;
    @BindView(R.id.btn_phonecheck)
    MaterialButton btnPhonecheck;
    CountDownTimer timer;
    String phone,code;
    UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisephone);
        ButterKnife.bind(this);
        inittooler();
    }

    private void inittooler() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("修改手机号");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab!=null;
        ab.setDisplayHomeAsUpEnabled(true);
        userService = new UserService(RevisephoneActivity.this);
    }

    @OnClick({R.id.btn_checkcode, R.id.btn_phonecheck})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_checkcode:
                phone = editNewPhone.getText().toString().trim();
                if(phone.equals("")){
                    Toast.makeText(this, "请输入新的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!BaseUtil.isMobileNO(phone)){
                    Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                userService.getMessege(phone, new HttpCallBack(RevisephoneActivity.this) {
                    @Override
                    public void onSuccess(Object... objects) {
                        if (objects != null && objects.length > 0) {
                            ParseReturn entity = (ParseReturn) objects[0];
                            if(entity==null){
                                btnCheckcode.setText("获取验证码");
                                btnCheckcode.setEnabled(true);
                                ToastUtil.show( "获取数据有错！", RevisephoneActivity.this);
                            }else {
                                if ("0".equals(entity.getMessage().getCode())) {
                                    ToastUtil.show(entity.getMessage().getResult(), RevisephoneActivity.this);
                                } else if ("1".equals(entity.getMessage().getCode())) {
                                    if(timer!=null){
                                        timer.cancel();
                                    }
                                    btnCheckcode.setText("获取验证码");
                                    ToastUtil.show(entity.getMessage().getResult()+"", RevisephoneActivity.this);
                                    btnCheckcode.setEnabled(true);
                                } else if("-1".equals(entity.getMessage().getCode())){
                                    if(timer!=null){
                                        timer.cancel();
                                    }
                                    btnCheckcode.setText("获取验证码");
                                    ToastUtil.show(entity.getMessage().getResult() + "", RevisephoneActivity.this);
                                    btnCheckcode.setEnabled(true);
                                }else{
                                    if(timer!=null){
                                        timer.cancel();
                                    }
                                    btnCheckcode.setText("获取验证码");
                                    ToastUtil.show( "验证码获取有误", RevisephoneActivity.this);
                                    btnCheckcode.setEnabled(true);
                                }
                            }
                        }
                    }
                });

                timer = new CountDownTimer(60000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        btnCheckcode.setText(millisUntilFinished/1000+"秒后获取");
                        btnCheckcode.setEnabled(false);
                    }

                    @Override
                    public void onFinish() {
                        btnCheckcode.setText("获取验证码");
                        btnCheckcode.setEnabled(true);
                    }
                }.start();
                break;
            case R.id.btn_phonecheck:
                if(!BaseUtil.isMobileNO(phone)){
                    Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }

                code = editRegPhonecheck.getText().toString().trim();
                if(code.equals("")){
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("newPhone", phone);
                map.put("codePhone", code);
                userService.Revisephone(map, new HttpCallBack(RevisephoneActivity.this) {
                    @Override
                    public void onSuccess(Object... objects) {
                        if (objects != null && objects.length > 0) {
                            BaseEntity<LinkedTreeMap<String, Object>> entity = (BaseEntity<LinkedTreeMap<String, Object>>) objects[0];
                            if(entity==null){
                                ToastUtil.show( "网络异常!", RevisephoneActivity.this);
                            }else {
                                int r1 =  entity.getResult();
                                if(1==r1){
                                    ToastUtil.show( "更新成功!", RevisephoneActivity.this);
                                    User user = userService.getSavedUser();
                                    user.setPhone(phone);
                                    userService.saveLoginUser(user);
                                    Intent intent = new Intent(RevisephoneActivity.this,DriverInformActivity.class);
                                    intent.putExtra("phone",phone);
                                    setResult(1,intent);
                                    finish();
                                }else {
                                    ToastUtil.show( entity.getMessage()+"", RevisephoneActivity.this);
                                }
                            }

                        }
                    }

                    @Override
                    public void onFail(int result, String message, boolean show) {
                        super.onFail(result, message, show);
                        ToastUtil.show( "验证码错误!", RevisephoneActivity.this);
                    }

                    @Override
                    public void onError(Object... objects) {
                        super.onError(objects);
                    }
                });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return  true;
    }
}

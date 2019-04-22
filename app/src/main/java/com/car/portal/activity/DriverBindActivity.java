package com.car.portal.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.SessionStore;
import com.car.portal.service.UserService;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;
import com.orhanobut.hawk.Hawk;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class DriverBindActivity extends AppCompatActivity {
    @ViewInject(R.id.bind_user)
    private EditText userEdit;
    @ViewInject(R.id.bind_pass)
    private EditText passEdit;
    @ViewInject(R.id.bind_register)
    private TextView regText;
    @ViewInject(R.id.bind_forgetPass)
    private TextView forgetText;
    @ViewInject(R.id.bind_submit)
    private Button submit;

    private String unionId;
    private UserService userService;
    private int type;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_bind);
        x.view().inject(this);
        inittooler();
        unionId = getIntent().getStringExtra("unionId");
        regText.setOnClickListener(onClickListener);
        forgetText.setOnClickListener(onClickListener);
        userEdit.setText(getIntent().getStringExtra("name"));
        passEdit.setText(getIntent().getStringExtra("pwd"));
        submit.setOnClickListener(onClickListener);
        userService = new UserService(this);
        type = getIntent().getIntExtra("AuthType", 0);
        forgetText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DriverBindActivity.this, ForgotPassActivity.class);
				startActivity(intent);
			}
		});
    }

    private void inittooler() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("绑定用户");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab!=null;

        ab.setDisplayHomeAsUpEnabled(true);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.bind_submit:
                    final String username = userEdit.getText().toString();
                    final String password = passEdit.getText().toString();
                    if(StringUtil.isNullOrEmpty(password) || StringUtil.isNullOrEmpty(username)) {
                        ToastUtil.show("用户名、密码不能为空", DriverBindActivity.this);
                        return;
                    } else {
                        userService.bindUser(username, password, unionId, null, type, bindBack);
                    }
                    break;
                case R.id.bind_forgetPass:
                    Intent intent = new Intent(DriverBindActivity.this, ForgotPassActivity.class);
                    startActivity(intent);
                    break;
                case R.id.bind_register:
                    //userService.registerWXNewUser(unionId,type,newWxBack);
                    break;
            }
        }
    };
    
    public void onWindowFocusChanged(boolean hasFocus) {
    	if(hasFocus) {
    		User user = userService.getLoginUser();
    		if(user != null && StringUtil.isNullOrEmpty(user.getPassword())
    				&& StringUtil.isNullOrEmpty(user.getPassword())) {
    			  userService.bindUser(user.getUsername(), user.getPassword(), unionId, null,type, bindBack);
    		}
    	}
    }
    
    private HttpCallBack bindBack = new HttpCallBack(DriverBindActivity.this){
        @Override
        public void onSuccess (Object... objects) {
            if(objects != null && objects.length > 0) {
                User user = (User) objects[0];
                if(type == -1){
                	user.setLoginType("wx");
                    Hawk.put("loginType","wx");
                } else if(type == 1) {
                	user.setLoginType("qq");
                    Hawk.put("loginType","qq");
                }
                userService.saveLoginUser(user);  //保存绑定后的用户信息
                //SessionStore.setMyToken(user.getToken());
                Intent intent = new Intent(DriverBindActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    };
    private HttpCallBack newWxBack = new HttpCallBack(DriverBindActivity.this){
        @Override
        public void onSuccess (Object... objects) {
            if(objects != null && objects.length > 0) {
                User user = (User) objects[0];
                if(type == -1){
                    user.setLoginType("wx");
                    Hawk.put("loginType","wx");
                } else if(type == 1) {
                    user.setLoginType("qq");
                    Hawk.put("loginType","qq");
                }
                userService.saveLoginUser(user);  //保存绑定后的用户信息

                Intent intent = new Intent(DriverBindActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    };


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

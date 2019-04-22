package com.car.portal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.UserService;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;

import java.io.UnsupportedEncodingException;
import java.util.SortedMap;
import java.util.TreeMap;

public class ReloginActivity extends AppCompatActivity {

    private ImageView loadImg;
    private User user;
    private UserService userService;
    private EditText userEdit;
    private EditText passEdit;
    private TextView forgotPass;
    private TextView regText;
    private Button submit;
    private LinearLayout layout;
    public static boolean hasShow = false;
    private boolean hasPass;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_driver_bind);
            initTooler();
        hasShow = true;
        userService = new UserService(this);
        user = userService.getLoginUser();
        if(user == null) {
            user = userService.getLoginUser();
            if(user != null && StringUtil.isNullOrEmpty(user.getPassword())) {
                String pass;
                try {
                    pass = new String(Base64.decode(user.getPassword().getBytes("utf-8"),  Base64.NO_PADDING));
                    user.setPassword(pass);
                } catch (UnsupportedEncodingException e) {
                    user.setPassword(null);
                    e.printStackTrace();
                }
            }
        }
        if(user == null || (StringUtil.isNullOrEmpty(user.getLoginType()) && StringUtil.isNullOrEmpty(user.getPassword()))) {
            hasPass = false;
            regText =  findViewById(R.id.bind_register);
            regText.setVisibility(View.GONE);
            layout =  findViewById(R.id.addContent);
            userEdit = findViewById(R.id.bind_user);
            passEdit = findViewById(R.id.bind_pass);
            forgotPass =  findViewById(R.id.bind_forgetPass);
            //userEdit.setText(user.getUsername());
            userEdit.setText("");
            forgotPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    Intent intent = new Intent(ReloginActivity.this, ForgotPassActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            TextView text = new TextView(this);
            text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            text.setGravity(Gravity.CENTER);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            text.setText("你的登录信息已过期，需要重新验证");
            text.setTextColor(getResources().getColor(R.color.blue3));
            text.setPadding(10, 40, 10, 20);
            text.setSingleLine(false);
            layout.addView(text);
            submit = (Button) findViewById(R.id.bind_submit);
            submit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View v) {
                    String username = userEdit.getText().toString();
                    String password = passEdit.getText().toString();
                    if(StringUtil.isNullOrEmpty(password) || StringUtil.isNullOrEmpty(username)) {
                        ToastUtil.show("用户名、密码不能为空", ReloginActivity.this);
                        return;
                    } else {
                        SortedMap<String, String> param = new TreeMap<String, String>();
                        param.put("username",username);
                        param.put("password",password);
                        userService.login(param, callBack);
                    }
                }
            });
        } else {
        	hasPass = true;
            setContentView(R.layout.activity_relogin);
            loadImg = (ImageView) findViewById(R.id.loading_img);
            RotateAnimation rotateAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setRepeatMode(Animation.RESTART);
            rotateAnim.setRepeatCount(-1);//设置动画循环进行
            rotateAnim.setInterpolator(new LinearInterpolator());//旋转速率 ，设置为匀速旋转
            rotateAnim.setDuration(2000);
            loadImg.startAnimation(rotateAnim);
        }
    }

    private void initTooler() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("重新登录");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab!=null;

        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private HttpCallBack callBack = new HttpCallBack(this) {
        @Override
        public void onSuccess (Object... objects) {
            if(objects != null && objects.length > 0) {
                User u = (User) objects[0];
                u.setPassword(user.getPassword());
                u.setLoginType(user.getLoginType());
                userService.saveLoginUser(u);
                finish();
            }
        }

        @Override
        public void onFail (int result, String message, boolean show) {
            super.onFail(result, message, true);
            user.setPassword(null);
            user.setLoginType(null);
            userService.saveLoginUser(user);
            finish();
        }

        @Override
        public void onError (Object... objects) {
            super.onError(objects);
            finish();
        }
    };

    @Override
    protected void onDestroy () {
        hasShow = false;
        super.onDestroy();
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	/*if(hasFocus && hasPass) {
    		if(!StringUtil.isNullOrEmpty(user.getPassword())) {
                SortedMap<String, String> param = new TreeMap<String, String>();
                param.put("username",user.getUsername());
                param.put("password",user.getPassword());
				userService.login(param, callBack);
            } else {
                userService.relogin(user, callBack);
            }
    	}*/
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

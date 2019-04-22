package com.car.portal.activity;

import java.util.HashMap;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.ParseReturn;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.UserService;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;

public class ForgotPassActivity extends AppCompatActivity {

	@ViewInject(R.id.forgot_user)
	private EditText userEdit;
	@ViewInject(R.id.forgot_name)
	private EditText nameEdit;
	@ViewInject(R.id.forgot_idcard)
	private EditText idEdit;
//	@ViewInject(R.id.forgot_type)
//	private Spinner typeSpinner;
	@ViewInject(R.id.forgot_mess)
	private EditText messEdit;
	@ViewInject(R.id.forgot_sendMess)
	private Button getMessage;
	@ViewInject(R.id.forgot_next)
	private Button next;
	private boolean time_stop;
	private UserService userService;
	private String phone;
	private String cname;
//	private String type;
	private String message;
	private String idcard;
	private Handler handler;
	protected HttpCallBack callBack;
	private static final int HANDLE_MESS = 0x123;
	private static final int RESETINFO = 0x124;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_pass);
		x.view().inject(this);
		init();
	}

	@SuppressLint("HandlerLeak")
	private void init() {
		android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("找回密码");
		setSupportActionBar(toolbar);
		ActionBar ab = getSupportActionBar();

		assert ab!=null;

		ab.setDisplayHomeAsUpEnabled(true);


		userService = new UserService(this);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == HANDLE_MESS) {
					String time = getMessage.getText().toString();
					if (time.equals("获取验证码")) {
						time_stop = true;
					} else {
						int t = Integer.parseInt(time.trim());
						if (t != 0) {
							getMessage.setText(t - 1 + "");
						} else {
							time_stop = true;
							getMessage.setText("获取验证码");
						}
					}
				} else if (msg.what == RESETINFO) {
					userEdit.setText("");
					idEdit.setText("");
					messEdit.setText("");
					nameEdit.setText("");
				}
			}
		};
		String[] mItems = getResources().getStringArray(R.array.userType_s);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		callBack = new HttpCallBack(ForgotPassActivity.this) {
			@Override
			public void onSuccess(Object... objects) {
				if (objects != null && objects.length > 0) {
					@SuppressWarnings("unchecked")
					BaseEntity<String> entity = (BaseEntity<String>) objects[0];
					if (entity.getResult() == -1) {
						ToastUtil.show(entity.getMessage(), ForgotPassActivity.this);
						return;
					} else if (entity.getResult() == 1) {
						Intent intent = new Intent(ForgotPassActivity.this, ResetPassActivity.class);
						intent.putExtra("phone", phone);
						ResetPassActivity.cls = ForgotPassActivity.class;
						startActivity(intent);
						finish();
					} else {
						ToastUtil.show(entity.getMessage(), ForgotPassActivity.this);
						return;
					}
				}
			}
		};
	}

	@Event(value = { R.id.forgot_sendMess, R.id.forgot_next })
	private void clickView(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.forgot_sendMess:
			phone = userEdit.getText().toString();
			if (StringUtil.isNullOrEmpty(phone) || !StringUtil.isPhone(phone)) {
				ToastUtil.show("请输入正确的手机号", ForgotPassActivity.this);
				return;
			}
			String messInfo = getMessage.getText().toString();
			char[] ches = messInfo.toCharArray();
			if (ches != null && ches.length > 0) {
				if (ches[0] > '9' || ches[0] <= '0') {
					sendMessage(v);
				} else {
					ToastUtil.show("短信已发送，请稍后重试", ForgotPassActivity.this);
				}
			}
			break;
		case R.id.forgot_next:
			phone = userEdit.getText().toString();
			idcard = idEdit.getText().toString();
			message = messEdit.getText().toString();
			cname = nameEdit.getText().toString();
			if (StringUtil.isNullOrEmpty(phone) || !StringUtil.isPhone(phone)) {
				ToastUtil.show("请输入正确的手机号", ForgotPassActivity.this);
				return;
			} else if (StringUtil.isNullOrEmpty(idcard) || !StringUtil.isIDCardValidate(idcard).equals("")) {
				ToastUtil.show("请输入正确的身份证号", ForgotPassActivity.this);
				return;
			} else if (StringUtil.isNullOrEmpty(message)) {
				ToastUtil.show("请输入短信验证码", ForgotPassActivity.this);
				return;
			}
			params = new HashMap<String, Object>();
			params.put("userType", 0);
			params.put("phone", phone);
			params.put("cname", cname);
			params.put("idcard", idcard);
			params.put("messege", message);
			userService.checkForget(params, callBack);
			break;
		}
	}
	
	private Map<String, Object> params;

	private void sendMessage(View v) {
		String time = ((Button) v).getText().toString();
		getMessage.setText("300");
		if (time.equals("获取验证码")) {
			time_stop = false;
			userService.getPhoneCodeOfForgot(phone, timeBack);
			new Thread() {
				public void run() {
					while (!time_stop) {
						try {
							sleep(500);
							Message msg = new Message();
							msg.what = HANDLE_MESS;
							handler.sendMessage(msg);
							sleep(500);
						} catch (InterruptedException e) {
							System.out.println(e.getMessage());
						}
					}
				};
			}.start();
		} else {
			ToastUtil.show("短信已发送，请稍后再试", ForgotPassActivity.this);
		}
	}

	private HttpCallBack timeBack = new HttpCallBack(ForgotPassActivity.this) {
		@Override
		public void onSuccess(Object... objects) {
			if (objects != null && objects.length > 0) {
				ParseReturn entity = (ParseReturn) objects[0];
				if (StringUtil.isNullOrEmpty(entity.getResult())) {
					time_stop = true;
					getMessage.setText("获取验证码");
					ToastUtil.show("短信发送失败", ForgotPassActivity.this);
				}else{
					String r = entity.getMessage().getCode();
					if("0".equals(r)){
						next.setEnabled(true);
						ToastUtil.show(entity.getMessage().getResult(), ForgotPassActivity.this);
					}else if("1".equals(r)||"-1".equals(r)){
						time_stop = true;
						getMessage.setText("获取验证码");
						ToastUtil.show(entity.getMessage().getResult(), ForgotPassActivity.this);
					}else {
						time_stop = true;
						getMessage.setText("获取验证码");
						ToastUtil.show(entity.getMessage().getResult(), ForgotPassActivity.this);
					}
				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
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

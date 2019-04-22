package com.car.portal.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.ParseReturn;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.UserService;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
public class RegisterActivity extends AppCompatActivity {

	@ViewInject(R.id.reg_phone)
	private EditText phoneEdit;
	@ViewInject(R.id.reg_phonecheck)
	private EditText messCheckEdit;
	@ViewInject(R.id.reg_sendCheck)
	private Button getMess;

	@ViewInject(R.id.reg_username)
	private EditText userEdit;
//	@ViewInject(R.id.reg_userType)
//	private Spinner typeSpinner;
	@ViewInject(R.id.reg_pass)
	private EditText passEdit;
	@ViewInject(R.id.reg_passomfirm)
	private EditText confirmEdit;

	@ViewInject(R.id.reg_messLay)
	private RelativeLayout messegeLay;
	@ViewInject(R.id.reg_detailLay)
	private LinearLayout detailLay;
	@ViewInject(R.id.reg_invite_view)
	private RelativeLayout reginviteview;
	@ViewInject(R.id.reg_hasAccount)
	private TextView hasAccout;
	@ViewInject(R.id.reg_confirm)
	private Button next;
	@ViewInject(R.id.box_ok)
	private CheckBox box_ok;
	@ViewInject(R.id.reg_invite)
	private EditText inviteEdit;

	public int current_step = 1;
	private UserService userService;
	private String phone;
	private String mess;
	private String username;
	private String password;
	private String passconf;
	private String cname;
	private boolean time_stop;

	private View.OnClickListener listener;

	private MyHandler handler = new MyHandler(this);

	private static final int HANDLE_IMAGE = 0x123;
	private static final int HANDLE_MESS = 0x124;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		x.view().inject(RegisterActivity.this);
		android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("注册");
		setSupportActionBar(toolbar);
		ActionBar ab = getSupportActionBar();

		assert ab!=null;

		ab.setDisplayHomeAsUpEnabled(true);
		userService = new UserService(RegisterActivity.this);
		detailLay.setVisibility(View.GONE);
		init();
	}

	@SuppressLint("HandlerLeak")
	private void init() {
		String[] mItems = getResources().getStringArray(R.array.userType_s);
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				userType = parent.getItemAtPosition(position).toString();
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				userType = "司机";
//			}
//		});
		listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				switch (id) {
				case R.id.reg_sendCheck: // 点击发送短信后触发
					if(box_ok.isChecked()) {
						sendMessage(v);
					}else{
						ToastUtil.show("请同意协议后再操作！", RegisterActivity.this);
					}
					break;
				case R.id.reg_hasAccount: // 点击以后账号，返回登录后触发
					finish();
					break;
				case R.id.reg_confirm: // 确认 下一步
					if(box_ok.isChecked()) {
						doNext();
					}else{
						ToastUtil.show("请同意协议后再操作！", RegisterActivity.this);
					}
					break;
				}
			}
		};
		next.setOnClickListener(listener);
		hasAccout.setOnClickListener(listener);
		getMess.setOnClickListener(listener);
	}
	private MyThread thread = new MyThread(this);
	static class MyThread extends Thread{
		WeakReference<RegisterActivity> reference;
		public MyThread(RegisterActivity activity){
			reference = new WeakReference<RegisterActivity>(activity);
		}
		public void run() {
			RegisterActivity activity = reference.get();
			while (!activity.time_stop) {
				try {
					sleep(500);
					Message msg = new Message();
					msg.what = HANDLE_MESS;
					activity.handler.sendMessage(msg);
					sleep(500);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	static class MyHandler extends Handler{
		WeakReference<RegisterActivity> reference;
		public MyHandler(RegisterActivity activity) {
			reference = new WeakReference<RegisterActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			RegisterActivity activity = reference.get();
			if(activity!=null) {
				if (msg.what == HANDLE_IMAGE) {
				} else if (msg.what == HANDLE_MESS) {
					String time = activity.getMess.getText().toString();
					if (time.equals("获取验证码")) {
						activity.time_stop = true;
					} else {
						int t = Integer.parseInt(time.trim());
						if (t != 0) {
							activity.getMess.setText(t - 1 + "");
						} else {
							activity.time_stop = true;
							activity.getMess.setText("获取验证码");
						}
					}
				}
			}
		}
	}
	private void sendMessage(View v) {
		phone = phoneEdit.getText().toString();
		if (phone == null || !StringUtil.isPhone(phone)) {
			ToastUtil.show("请输入正确的手机号码", RegisterActivity.this);
			return;
		}
		String time = ((Button) v).getText().toString();
		if (time.equals("获取验证码")) {
			getMess.setText("300");
			time_stop = false;
			userService.getMessege(phone, timeBack);
			/*new Thread() {
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
			}.start();*/
			if(thread!=null){
				this.time_stop=true;
				//SystemClock.sleep(600);  //停一会，以提高上一个线程终止的机率
				thread = null;
				thread = new MyThread(this);
				this.time_stop = false;
				thread.start();
			}
		} else {
			ToastUtil.show("短信已发送，请稍后再试", RegisterActivity.this);
		}
	}

	private void doNext() {
		if (current_step == 1) {
			phone = phoneEdit.getText().toString();
			if (phone == null || !StringUtil.isPhone(phone)) {
				ToastUtil.show("请输入正确的手机号码", RegisterActivity.this);
				return;
			}
			mess = messCheckEdit.getText().toString();
			if (mess == null || "".equals(mess)) {
				ToastUtil.show("请输入手机验证码", RegisterActivity.this);
				return;
			}
			userService.checkMessage(phone, mess, checkMessBack);
		} else {
			password = passEdit.getText().toString();
			passconf = confirmEdit.getText().toString();
			username = phone;
			String inviter   =  inviteEdit.getText().toString();
			if (phone == null) {
				phone = phoneEdit.getText().toString();
			}
			if (phone == null || !StringUtil.isPhone(phone)) {
				ToastUtil.show("请输入正确的手机号码", RegisterActivity.this);
				return;
			} else if (StringUtil.isNullOrEmpty(username) || username.trim().length() < 2 || username.length() > 16) {
				ToastUtil.show("请输入4-16个字符作为用户名，中文算两个", RegisterActivity.this);
				return;
			} else if (StringUtil.isNullOrEmpty(password) || password.trim().length() < 6) {
				ToastUtil.show("您输入的密码不符合要求", RegisterActivity.this);
				return;
			} else if (StringUtil.isNullOrEmpty(passconf) || !passconf.equals(password)) {
				ToastUtil.show("两次输入的密码不一致，请输入重新确认密码", RegisterActivity.this);
				confirmEdit.setText("");
				return;
			} else {
				map = new HashMap<String, Object>();
				map.put("phone", phone);
				map.put("username", username.trim());
				map.put("passwordP", password.trim());
				map.put("inviter",inviter.trim());
				map.put("cname", cname);
//				if (userType.contains("三方") || userType.contains("公司")) {
//					map.put("userType", 0);
//				} else {
//					map.put("userType", 1);
//				}
				map.put("userType", 0);
				this.time_stop=true;
				userService.register(map, regBack);
			}
		}
	}

	private HttpCallBack checkMessBack = new HttpCallBack(RegisterActivity.this) {
		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(Object... objects) {
			if (objects != null && objects.length > 0) {
				BaseEntity<String> entity = (BaseEntity<String>) objects[0];
				if (entity.getResult() != 1) {
					ToastUtil.show(entity.getMessage(), RegisterActivity.this);
				} else {
					messegeLay.setVisibility(View.GONE);
					phoneEdit.setKeyListener(null);
					detailLay.setVisibility(View.VISIBLE);
					reginviteview.setVisibility(View.GONE);
					current_step = 3;
				}
			}
		}
	};

	private HttpCallBack regBack = new HttpCallBack(RegisterActivity.this) {
		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(Object... objects) {
			if (objects != null && objects.length > 0) {
				BaseEntity<String> entity = (BaseEntity<String>) objects[0];
				if (entity.getResult() != 1) {
					ToastUtil.show(entity.getMessage(), RegisterActivity.this);
				} else {
					//ToastUtil.show("注册成功，货满车欢迎您！\n\r请登录,完善资料，通过审核！\n\r我们团结一致做好一件事！", RegisterActivity.this);
					showWellcome();
				}
			}
		}
	};
	public void showWellcome(){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(RegisterActivity.this);
		normalDialog.setTitle("货满车温馨提示");
		normalDialog.setMessage("注册成功，“货满车”欢迎您！\n" +
				"\n" +
				"请登录,完善资料，通过审核！\n" +
				"\n" +
				"我们团结一致做好一件事！");
		normalDialog.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
					}
				});

		normalDialog.show();
	}
	private Map<String, Object> map; 

	private HttpCallBack timeBack = new HttpCallBack(RegisterActivity.this) {
		@Override
		public void onSuccess(Object... objects) {
			if (objects != null && objects.length > 0) {
				ParseReturn entity = (ParseReturn) objects[0];
				if(entity==null){
					time_stop=false;
					getMess.setText("获取验证码");
					ToastUtil.show( "获取数据有错！", RegisterActivity.this);
				}else {
					if ("0".equals(entity.getMessage().getCode())) {
						time_stop = false;
						ToastUtil.show(entity.getMessage().getResult(), RegisterActivity.this);
					} else if ("1".equals(entity.getMessage().getCode())) {
						time_stop = true;
						getMess.setText("获取验证码");
						ToastUtil.show(entity.getMessage().getResult()+"", RegisterActivity.this);
					} else if("-1".equals(entity.getMessage().getCode())){
						time_stop = true;
						getMess.setText("获取验证码");
						ToastUtil.show(entity.getMessage().getResult() + "", RegisterActivity.this);
					}else{
						time_stop = true;
						getMess.setText("获取验证码");
						ToastUtil.show( "验证码获取有误", RegisterActivity.this);
					}
				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//将Handler里面消息清空了。
		handler.removeCallbacksAndMessages(null);
		thread = null;
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

package com.car.portal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.BaseEntity;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.UserService;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;

import org.xutils.common.util.MD5;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class ResetPassActivity extends Activity {

	@ViewInject(R.id.reset_pass)
	private EditText passEdit;
	@ViewInject(R.id.reset_title)
	private BaseTitleView title;
	@ViewInject(R.id.reset_confirm)
	private EditText confirmEdit;
	@ViewInject(R.id.reset_user)
	private TextView phoneText;
	@ViewInject(R.id.reset_sub)
	private TextView rtn;
	private String pass;
	private String confirm;
	private String phone;
	private UserService service;
	public static Class<? extends Activity> cls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_pass);
		x.view().inject(this);
		init();
	}

	private void init() {
		phone = getIntent().getStringExtra("phone");
		phoneText.setText(phone);
		service = new UserService(ResetPassActivity.this);
		rtn.setOnClickListener(listener);
		title.setOnClickListener(listener);
	}

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.reset_sub:
				pass = passEdit.getText().toString();
				confirm = confirmEdit.getText().toString();
				if (StringUtil.isNullOrEmpty(pass)) {
					ToastUtil.show("请输入新密码", ResetPassActivity.this);
					passEdit.requestFocus();
					return;
				} else if (StringUtil.isNullOrEmpty(confirm)) {
					ToastUtil.show("请重新输入密码", ResetPassActivity.this);
					confirmEdit.requestFocus();
					return;
				} else if (!pass.equals(confirm)) {
					ToastUtil.show("您两次输入的密码不一致", ResetPassActivity.this);
					confirmEdit.requestFocus();
					return;
				} else if (pass.trim().length() < 6) {
					ToastUtil.show("为了安全，您的密码至少6位", ResetPassActivity.this);
					passEdit.requestFocus();
					return;
				} else {
					final Map<String, Object> params = new HashMap<String, Object>();
					params.put("phone", phone);
					params.put("passwordNew", MD5.md5(pass).toUpperCase());
					service.resetPass(params, new HttpCallBack(ResetPassActivity.this) {
						@Override
						public void onSuccess(Object... objects) {
							if (objects != null && objects.length > 0) {
								@SuppressWarnings("unchecked")
								BaseEntity<String> entity = (BaseEntity<String>) objects[0];
								if (entity.getResult() == -1) {
									ToastUtil.show(entity.getMessage(), ResetPassActivity.this);
									if (cls != null) {
										startActivity(new Intent(ResetPassActivity.this, cls));
									} else {
										startActivity(new Intent(ResetPassActivity.this, ForgotPassActivity.class));
									}
									finish();
								} else {
									ToastUtil.show("密码更新成功", ResetPassActivity.this);
									service.logout(new HttpCallBack(ResetPassActivity.this) {
										@Override
										public void onSuccess(Object... objects) {
											finish();
										}
									});
								}
							}
						}
					});
				}
				break;
			case R.id.reset_title:
				if (cls != null) {
					startActivity(new Intent(ResetPassActivity.this, cls));
				}
				finish();
				break;
			}
		}
	};
}

package com.car.portal.activity;

import java.util.HashMap;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import com.car.portal.R;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.User;
import com.car.portal.entity.UserDetail;
import com.car.portal.fragment.DriverPersonFragment;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.UserService;
import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DriverInformActivity extends AppCompatActivity {

	@ViewInject(R.id.driver_info_phone)
	private TextView phone;
	@ViewInject(R.id.driver_info_username)
	private EditText username;
	@ViewInject(R.id.driver_info_cname)
	private EditText cnameEdit;
	@ViewInject(R.id.driver_info_idcard)
	private EditText idcardEdit;
	@ViewInject(R.id.driver_info_qq)
	private EditText qqEdit;
	@ViewInject(R.id.driver_info_sub)
	private Button sub;
	private User user;
	private UserService service;
	private UserDetail detail;
	@ViewInject(R.id.btn_border)
	MaterialButton btn_border;
	private static final int REQUEST_SELECTCITY = 0x1230;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getContext().addActivity(this);
		setContentView(R.layout.driver_inform);
		x.view().inject(DriverInformActivity.this);
		android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("个人信息");
		setSupportActionBar(toolbar);
		ActionBar ab = getSupportActionBar();
		assert ab != null;
		ab.setDisplayHomeAsUpEnabled(true);

		service = new UserService(DriverInformActivity.this);
		user = service.getLoginUser();
		if (user != null) {
			phone.setText(user.getPhone());
			username.setText(user.getUsername());
			cnameEdit.setText(user.getCname());
			idcardEdit.setText(user.getIdcard());
			qqEdit.setText(user.getQq());
		}
		sub.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String cname = cnameEdit.getText().toString();
				String idcard = idcardEdit.getText().toString();
				String qq = qqEdit.getText().toString();
				if (StringUtil.isNullOrEmpty(cname)) {
					ToastUtil.show("请填写中文名称", DriverInformActivity.this);
					cnameEdit.requestFocus();
					return;
				} else if (StringUtil.isNullOrEmpty(idcard)) {
					ToastUtil.show("请填写身份证号", DriverInformActivity.this);
					idcardEdit.requestFocus();
					return;
				} else if (!StringUtil.isIDCardValidate(idcard).equals("")) {
					ToastUtil.show("您的身份证号不正确", DriverInformActivity.this);
					cnameEdit.requestFocus();
					return;
				} else {
					params = new HashMap<String, Object>();
					params.put("phone", user.getPhone());
					params.put("cname", cname);
					params.put("idcard", idcard);
					params.put("qq", qq);
					service.changeDriverInfo(params, callback);
				}
			}
		});
		btn_border.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(DriverInformActivity.this,RevisephoneActivity.class);
				startActivityForResult(intent,REQUEST_SELECTCITY);
			}
		});
	}
	
	private Map<String, Object> params;

	private HttpCallBack callback = new HttpCallBack(DriverInformActivity.this) {
		@Override
		public void onSuccess(Object... objects) {
			Intent intent = new Intent(DriverInformActivity.this,DriverPersonFragment.class);

			setResult(5,intent);
			finish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_SELECTCITY){
			if(resultCode==1){
				String newphone = data.getStringExtra("phone");
				phone.setText(newphone);
			}
		}
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus) {
			service.getUserDetail(new HttpCallBack(DriverInformActivity.this) {
				@Override
				public void onSuccess(Object... objects) {
					if (objects != null && objects.length > 0) {
						detail = (UserDetail) objects[0];
						if (detail != null) {
							if (!StringUtil.isNullOrEmpty(detail.getCname())) {
								cnameEdit.setText(detail.getCname());
							}
							if (!StringUtil.isNullOrEmpty(detail.getIdcard())) {
								idcardEdit.setText(detail.getIdcard());
							}
							if (!StringUtil.isNullOrEmpty(detail.getQq())) {
								qqEdit.setText(detail.getQq());
							}
						}
					}
				}

				@Override
				public void onError(Object... objects) {
					ToastUtil.show("网络不佳，请重试", DriverInformActivity.this);
				}

				@Override
				public void onFail(int result, String message, boolean show) {
					ToastUtil.show("网络不佳，请重试", DriverInformActivity.this);
				}
			});
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

package com.car.portal.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.car.portal.R;
import com.car.portal.application.MyApplication;
import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AboutActivity extends AppCompatActivity {

	@ViewInject(R.id.about_versionInfo)
	private TextView versionText;
	//@ViewInject(R.id.inviterCode)
	//private Button inviterCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		x.view().inject(this);
		MyApplication.getContext().addActivity(this);
		init();
	}

	@SuppressLint("SimpleDateFormat")
	private void init() {

		android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("关于我们");
		setSupportActionBar(toolbar);

		ActionBar ab = getSupportActionBar();

		assert ab!=null;

		ab.setDisplayHomeAsUpEnabled(true);


		PackageManager packageManager = this.getPackageManager();
		try {

			PackageInfo packageInfo = packageManager.getPackageInfo(
					this.getPackageName(), 0);
			long firstInstallTime = packageInfo.firstInstallTime;	//安装时间
			long lastUpdateTime = packageInfo.lastUpdateTime;		//上次更新时间
			int versionCode = packageInfo.versionCode;				//应用现在的版本号
			String versionName = packageInfo.versionName;			// 应用现在的版本名称

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
			String str = "V" + versionName + "\n" + dateFormat.format(new Date(lastUpdateTime));
			Spannable sp = StringUtil.createDiffrentStyle(str, new int[]{R.style.text_Sixteen_Black_i,
					R.style.text_twelve_darkGray}, new int[]{str.length() - 10, 10});
			
			LogUtils.e("AboutUs", sp.toString());
			versionText.setText(sp);
			// 如下可获得更多信息
//			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//			String name = applicationInfo.name;
//			String packageName = applicationInfo.packageName;
//			String permission = applicationInfo.permission;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Event(value = {R.id.inviterCode})
	private void login(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.inviterCode:
				isExit();
		}
	}

	public void isExit() {
		new MaterialDialog.Builder(AboutActivity.this)
				.title("获取注册邀请码")
				.content("注册邀请码为123456")
				.positiveText("确认")
				.show();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:
				finish();
				break;
		}

		return true;
	}
}

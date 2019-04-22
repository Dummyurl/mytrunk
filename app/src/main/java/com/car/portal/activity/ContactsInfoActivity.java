package com.car.portal.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.view.BaseTitleView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@SuppressLint("UseSparseArrays")
public class ContactsInfoActivity extends AppCompatActivity {
	@ViewInject(R.id.focus_city_title)
	private BaseTitleView title;
	@ViewInject(R.id.txt_contacts_name)
	private TextView txt_contacts_name;
	@ViewInject(R.id.txt_company)
	private TextView txt_company;
	@ViewInject(R.id.txt_boss_name)
	private TextView txt_boss_name;
	@ViewInject(R.id.txt_goodsType)
	private TextView txt_goodsType;
	@ViewInject(R.id.txt_route)
	private TextView txt_route;
	@ViewInject(R.id.txt_connect_data)
	private TextView txt_connect_data;
	@ViewInject(R.id.txt_other)
	private TextView txt_other;
	private String contacts_name, company, boss_name, goodsType, route,
			connect_data, other;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_info);
		Intent intent = getIntent();
		contacts_name = intent.getStringExtra("contacts_name");
		company = intent.getStringExtra("company");
		boss_name = intent.getStringExtra("boss_name");
		connect_data = intent.getStringExtra("connect_data");
		other = intent.getStringExtra("other");
		goodsType = intent.getStringExtra("type");
		route = intent.getStringExtra("route");
		initTitle();
		initData();
	}

	/**
	 * 初始化标题栏
	 */
	public void initTitle() {
		x.view().inject(ContactsInfoActivity.this);
//		title.setTitle(getResources().getString(R.string.contactsinfo));
		android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("联系人信息");
		setSupportActionBar(toolbar);

		ActionBar ab = getSupportActionBar();

		assert ab!=null;

		ab.setDisplayHomeAsUpEnabled(true);

	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		txt_route.setText( route);
		txt_goodsType.setText( goodsType);
		txt_other.setText(other);
		txt_contacts_name.setText(contacts_name);
		txt_boss_name.setText( boss_name);
		txt_connect_data.setText(connect_data);
		txt_company.setText(company);
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

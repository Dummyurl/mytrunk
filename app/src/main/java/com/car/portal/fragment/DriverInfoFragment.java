package com.car.portal.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.car.portal.R;
import com.car.portal.activity.AddDriverActivity;
import com.car.portal.activity.DriverArriveActivity;
import com.car.portal.activity.DriverDeletedActivity;
import com.car.portal.activity.DriverNewActivity;
import com.car.portal.activity.DriverParkActivity;
import com.car.portal.activity.DriverPauseActivity;
import com.car.portal.activity.DriverSearchActivity;
import com.car.portal.activity.MapDriverActivity;
import com.car.portal.activity.MapDriverListActivity;
import com.car.portal.view.DriverNavView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class DriverInfoFragment extends MainBaseFragment {
	private View view;
	@ViewInject(R.id.info_arrive)
	private LinearLayout arriveNav;
	@ViewInject(R.id.online_driver)
	private LinearLayout onlineNav;
	@ViewInject(R.id.info_driver)
	private LinearLayout driverNav;
	@ViewInject(R.id.info_park)
	private LinearLayout parkNav;
	@ViewInject(R.id.info_new)
	private DriverNavView newNav;
	@ViewInject(R.id.info_pause)
	private DriverNavView pauseNav;
	@ViewInject(R.id.info_delete)
	private DriverNavView deletedNav;
	@ViewInject(R.id.info_add)
	private LinearLayout addNav;
	@ViewInject(R.id.info_public_driver)
	private LinearLayout public_driver;
	@ViewInject(R.id.line_map)
	private LinearLayout line_map;
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = LayoutInflater.from(getActivity()).inflate(
				R.layout.driver_info_fragment, null);
		x.view().inject(DriverInfoFragment.this, view);
		initView();
		return view;
	}

	private void initView() {
		onlineNav.setOnClickListener(listener);
		arriveNav.setOnClickListener(listener);
		driverNav.setOnClickListener(listener);
		parkNav.setOnClickListener(listener);
		newNav.setOnClickListener(listener);
		pauseNav.setOnClickListener(listener);
		deletedNav.setOnClickListener(listener);
		addNav.setOnClickListener(listener);
		public_driver.setOnClickListener(listener);
		line_map.setOnClickListener(listener);
	}

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			Intent intent = null;
			switch (id) {
			case R.id.online_driver:
				intent = new Intent(getActivity(), MapDriverListActivity.class);
				intent.putExtra("length", 0.0d);
				intent.putExtra("length2", 0.0d);
				intent.putExtra("typeId", 0);
				intent.putExtra("target", "");
				intent.putExtra("searchX", "");
				intent.putExtra("searchY", "");
				break;
			case R.id.info_arrive:
				intent = new Intent(getActivity(), DriverArriveActivity.class);
				break;
			case R.id.info_driver:
				intent = new Intent(getActivity(), DriverSearchActivity.class);
				intent.putExtra("public_driver","0");  //共用一个DriverSearchActivity
				break;
			case R.id.info_public_driver:
				intent = new Intent(getActivity(), DriverSearchActivity.class);
				intent.putExtra("public_driver","1");  //共用一个DriverSearchActivity
				break;
			case R.id.info_park:
				intent = new Intent(getActivity(), DriverParkActivity.class);
				break;
			case R.id.info_new:
				intent = new Intent(getActivity(), DriverNewActivity.class);
				break;
			case R.id.info_pause:
				intent = new Intent(getActivity(), DriverPauseActivity.class);
				break;
			case R.id.info_delete:
				intent = new Intent(getActivity(), DriverDeletedActivity.class);
				break;
			case R.id.info_add:
				intent = new Intent(getActivity(), AddDriverActivity.class);
				break;
			case R.id.line_map:
			    intent = new Intent(getActivity(), MapDriverActivity.class);
					break;
			}
			if(intent !=  null) {
				startActivity(intent);
			}
		}
	};
	

	@Override
	public void search(String condition) {

	}

	@SuppressLint("InflateParams")
	@Override
	public void onWindowFoucusChanged(boolean hasFocus) {

	}
}

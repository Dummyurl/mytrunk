package com.car.portal.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.entity.Driver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class DriverListAdapter extends BaseAdapter {
	private List<Driver> drivers;
	private Context context;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public DriverListAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return drivers == null ? 0 : drivers.size();
	}

	@Override
	public Object getItem(int position) {
		if (drivers != null && drivers.size() > 0)
			return drivers.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private static final HashMap<Integer, String> bodyTypeData = new HashMap<>();

	{
		bodyTypeData.put(0, "");
		bodyTypeData.put(1, "高低板 ");
		bodyTypeData.put(2, "高栏 ");
		bodyTypeData.put(3, "冷藏货柜 ");
		bodyTypeData.put(4, "平板 ");
		bodyTypeData.put(5, "高低高 ");
		bodyTypeData.put(6, "货柜 ");
		bodyTypeData.put(7, "超低板 ");
		bodyTypeData.put(8, "自缷车 ");
		bodyTypeData.put(9, "高栏平板 ");
		bodyTypeData.put(10, "高栏高低板 ");

	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.good_address_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.driverName = (TextView) convertView.findViewById(R.id.txt_contacts_name);
			viewHolder.connectTime = (TextView) convertView.findViewById(R.id.txt_connect_count);
			viewHolder.connectDate = (TextView) convertView.findViewById(R.id.txt_connect_data);
			viewHolder.startAddr = (TextView) convertView.findViewById(R.id.txt_start_point);
			viewHolder.carLength = (TextView) convertView.findViewById(R.id.txt_car_length);
			viewHolder.remark = (TextView) convertView.findViewById(R.id.txt_remark);
			viewHolder.img_state = (ImageView) convertView.findViewById(R.id.img_state);
			viewHolder.goods_phone = (ImageView) convertView.findViewById(R.id.call_driver);
			viewHolder.car_type = (TextView) convertView.findViewById(R.id.car_type);
			viewHolder.carId = (TextView) convertView.findViewById(R.id.carId);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Driver driver = drivers.get(position);
		viewHolder.goods_phone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				//viewHolder.tv3.setText(mList.get(index).getErrorSolution());
				//Toast.makeText(MyApplication.getContext(), "" + driver.getTels(), 0).show();
				//Intent intent = new Intent(MyApplication.getContext());
				//Uri data = Uri.parse("tel:" + driver.getTels());
				//intent.setData(data);
				//intent.putExtra("vid",v.getId());
				//intent.putExtra("tels",driver.getTels());
				if (driver.getTels() != null) {
					Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + driver.getTels()));
					if (PermissionChecker.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
						Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + driver.getTels()));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
						return;
					}else {
						context.startActivity(intentPhone);
					}
				}
			}
		});
		viewHolder.driverName.setText(driver.getName());
		viewHolder.connectTime.setText( driver.getConnectCount() + " : " + driver.getIncar());
		viewHolder.connectDate.setText((driver.getConnectDate() != null ? driver.getConnectDate().substring(0,10) : ""));
		viewHolder.carId.setText(driver.getCarId());
		viewHolder.car_type.setText(bodyTypeData.get(driver.getBodyType()));
		if (driver.getLength2() > 0) {
			viewHolder.carLength.setText( driver.getLength() + "~"  + driver.getLength2() + "米");
		} else {
			viewHolder.carLength.setText("" + driver.getLength() + "米");
		}
		viewHolder.startAddr.setText((driver.getRoute() == null ? "" : driver.getRoute().length() > 15
				? driver.getRoute().substring(0, 15) + "…" : driver.getRoute()));
		if(driver.getDelFlag() > 0) {
			viewHolder.driverName.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			viewHolder.connectTime.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			viewHolder.connectDate.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			viewHolder.startAddr.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			viewHolder.carLength.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			viewHolder.remark.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			viewHolder.img_state.setImageDrawable(context.getResources().getDrawable(R.drawable.driver_wrong));
		} else {
			viewHolder.driverName.getPaint().setFlags(0);
			viewHolder.connectTime.getPaint().setFlags(0);
			viewHolder.connectDate.getPaint().setFlags(0);
			viewHolder.startAddr.getPaint().setFlags(0);
			viewHolder.carLength.getPaint().setFlags(0);
			viewHolder.remark.getPaint().setFlags(0);
		}
		if (driver.getMemo() != null && !driver.getMemo().equals(""))
			viewHolder.remark.setText(driver.getMemo());
		else
			viewHolder.remark.setText("暂无");
		if (driver.getPauseUid() > 0)
			viewHolder.img_state.setImageDrawable(context.getResources().getDrawable(R.drawable.driver_pauset));
		if(driver.getDelFlag() <= 0 && driver.getPauseUid() <= 0)
			viewHolder.img_state.setImageDrawable(context.getResources().getDrawable(R.drawable.more));
		return convertView;
	}

	public void setList(List<Driver> drivers) {
		if (drivers != null)
			this.drivers = drivers;
		else
			this.drivers = new ArrayList<Driver>();
	}

	public class ViewHolder {
		TextView connectTime, connectDate, startAddr,
				carLength, driverName, remark,car_type,carId;
		ImageView img_state,goods_phone;
	}

	public void addValue(Driver d) {
		if (d != null && drivers != null && !drivers.contains(d)) {
			drivers.add(d);
		}
	}

	public void removeValue(Driver d) {
		if (d != null && drivers != null && drivers.contains(d)) {
			drivers.remove(d);
		}
	}

	/**
	 * 判断该条司机信息是否已被删除
	 * @param position
	 * @return true已删除  false未删除
	 */
	public boolean isDelFlag(int position){
		if (drivers.get(position).getDelFlag()>0||drivers.get(position).getPauseUid()>0)
			return true;
		else
			return false;
	}
}

package com.car.portal.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.DriverListAdapter;
import com.car.portal.entity.CarArrived;
import com.car.portal.entity.Driver;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.view.PopUpView;
import com.car.portal.view.QueryTitleView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class DriverNewActivity extends Activity {
	@ViewInject(R.id.driver_list_title)
	private QueryTitleView titleView;
	@ViewInject(R.id.driver_list_listView)
	private ListView listView;
	@ViewInject(R.id.txt_no_one_warn)
	private TextView txt_driver_warn;

	private DriverListAdapter adapter;
	private DriverService service;
	private int currentPage = 1;
	private View popupView;
	private PopUpView line_error;
	private Driver driver;
	private PopupWindow popupWindow;
	private Intent intent;
	private LinearLayout line_arrived,line_drivers,line_new,line_pause,line_delete,line_park, line_add_driver;
	private PopupWindow driverPopup;
	private View driverPopuView;
	
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_list);
		x.view().inject(this);
		adapter = new DriverListAdapter(this);
        listView.setAdapter(adapter);
		titleView.setMoreListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDriverPopu();
			}
		});
		titleView.setQueryIsVisible(false,"新收录司机");
        service = new DriverService(this);
		txt_driver_warn.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				driver = (Driver) adapter.getItem(position);
				showPopupWindow();
			}
		});
	}

	/**
	 * 初始化司机的popupWindow
	 */
	private void initDriverPopup(){
		if (driverPopup==null){
			driverPopuView=LayoutInflater.from(this).inflate(R.layout.popu_driver,null);
			driverPopup=new PopupWindow(driverPopuView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,true);
			driverPopuView.getBackground().setAlpha(30);
			line_arrived= (LinearLayout) driverPopuView.findViewById(R.id.line_arrived);
			line_delete= (LinearLayout) driverPopuView.findViewById(R.id.line_delete);
			line_drivers= (LinearLayout) driverPopuView.findViewById(R.id.line_drivers);
			line_new= (LinearLayout) driverPopuView.findViewById(R.id.line_new);
			line_pause= (LinearLayout) driverPopuView.findViewById(R.id.line_pause);
			line_park= (LinearLayout) driverPopuView.findViewById(R.id.line_parking);
			line_add_driver = (LinearLayout) driverPopuView.findViewById(R.id.line_add_driver);
		}
	}

	/**
	 * 显示司机的popupWindow
	 */
	public void showDriverPopu(){
		initDriverPopup();
		driverPopup.setTouchable(true);
		driverPopup.showAsDropDown(titleView);
		driverPopuView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				driverPopup.dismiss();
			}
		});
		line_pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverNewActivity.this, DriverPauseActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_new.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverNewActivity.this, DriverNewActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverNewActivity.this, DriverDeletedActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_arrived.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverNewActivity.this, DriverArriveActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_park.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverNewActivity.this, DriverParkActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_drivers.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverNewActivity.this, DriverSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_add_driver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverNewActivity.this, AddDriverActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	public void getData() {
		service.getNewDriver(new HttpCallBack(this) {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object... objects) {
				if(objects != null && objects.length > 0) {
					if(currentPage == 1) {
						adapter.setList((List<Driver>) objects[0]);
					} else {
						List<Driver> list = (List<Driver>) objects[0];
						if(list != null) {
							for (Driver driver : list) {
								adapter.addValue(driver);
							}
						}
					}
					if (((List<CarArrived>) objects[0]).size()==0){
						txt_driver_warn.setVisibility(View.VISIBLE);
						listView.setVisibility(View.GONE);
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	@SuppressLint("InflateParams")
	public void initPopupWindow(){
        if(popupWindow==null){
            popupView=LayoutInflater.from(this).inflate(R.layout.driverlist_popwindow,null);
            popupWindow = new PopupWindow(popupView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            line_error= new PopUpView(this);
            line_error.init(R.drawable.error, "放弃新司机", true);
			LinearLayout layout = (LinearLayout) popupView.findViewById(R.id.pop_context);
			layout.addView(line_error);
            popupView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
    }
	
	 /**
     * 初始化Popupwindow
     */
    @SuppressLint("InflateParams")
    public void showPopupWindow() {
        initPopupWindow();
        View view= LayoutInflater.from(this).inflate(R.layout.activity_good_for_address,null);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER, 0, 0);
        popupOnClick();
    }

	private void popupOnClick() {
		line_error.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(driver != null) {
					service.removeNewDriver(driver.getId(), new HttpCallBack(DriverNewActivity.this) {
						@Override
						public void onSuccess(Object... objects) {
							adapter.removeValue(driver);
							adapter.notifyDataSetChanged();
						}
					});
				}
				popupWindow.dismiss();
			}
		});
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus && adapter.getCount() <= 0) {
			getData();
		}
	}
}

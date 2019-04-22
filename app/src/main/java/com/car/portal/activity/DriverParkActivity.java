package com.car.portal.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.DriverForAddressAdapter;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.CarArrived;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.util.LogUtils;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.PopUpView;
import com.car.portal.view.QueryTitleView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class DriverParkActivity extends Activity {
	@ViewInject(R.id.driver_list_title)
	private QueryTitleView titleView;
	@ViewInject(R.id.driver_list_listView)
	private ListView listView;
	@ViewInject(R.id.txt_no_one_warn)
	private TextView txt_driver_warn;
	private DriverForAddressAdapter adapter;
	private DriverService service;
	private int currentPage = 1;
	private View footer;
	private View popupView;
    private LinearLayout rela_popu;
	private PopUpView line_error,line_call;
	private CarArrived arrived;
	private Intent intent;
	private LinearLayout line_arrived,line_drivers,line_new,line_pause,line_delete,line_park,line_add_driver;
	private PopupWindow driverPopup;
	private View driverPopuView;
	private PopupWindow popupWindow;
	private Boolean iscreate = false;
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_list);
		x.view().inject(this);
        titleView.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
		service = new DriverService(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        footer = inflater.inflate(R.layout.listview_footer, null);
		adapter = new DriverForAddressAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(l);
		titleView.setQueryIsVisible(false,"停车场");

		titleView.setMoreListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDriverPopu();
			}
		});
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				arrived = (CarArrived) adapter.getItem(position);
				showPopupWindow();
			}
		});
	}
	
	private OnScrollListener l = new OnScrollListener() {
		private boolean isFooter;
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (isFooter && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                currentPage++;
                getData();
                isFooter = false;
            }
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			int count = adapter.getCount();
			if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0
                    && count >= currentPage * 10) {
                isFooter = true;
                if (listView.getFooterViewsCount() == 0) {
                    listView.addFooterView(footer);
                }
            }
		}
	};


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
				intent = new Intent(DriverParkActivity.this, DriverPauseActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_new.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverParkActivity.this, DriverNewActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverParkActivity.this, DriverDeletedActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_arrived.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverParkActivity.this, DriverArriveActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_park.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverParkActivity.this, DriverParkActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_drivers.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverParkActivity.this, DriverSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_add_driver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverParkActivity.this, AddDriverActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	public void getData() {
		service.getParkDriver(currentPage, new HttpCallBack(this) {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object... objects) {
				Log.e("succe",objects+"");
				if(objects != null && objects.length > 0) {
					LogUtils.e("DriverPark", "         " + ((List<CarArrived>) objects[0]).size());
						if (currentPage == 1) {
							adapter.setList((List<CarArrived>) objects[0]);
						} else {
							List<CarArrived> list = (List<CarArrived>) objects[0];
							if (list != null) {
								for (CarArrived driver : list) {
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

			@Override
			public void onError(Object... objects) {
				Log.e("succe",objects+"");
			}
		});
	}
	
	public void initPopupWindow(){
        if(popupWindow==null){
        	 popupView=LayoutInflater.from(this).inflate(R.layout.driverlist_popwindow,null);
             popupWindow = new PopupWindow(popupView,
                     LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
             line_call= new PopUpView(this);
             line_error= new PopUpView(this);
             line_call.init(R.drawable.phone_call, "拨打电话", true);
             line_error.init(R.drawable.traffic_forbidden, "过期", false);
             LinearLayout layout = (LinearLayout) popupView.findViewById(R.id.pop_context);
             layout.addView(line_error);
             layout.addView(line_call);
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
		line_call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Bohao
				popupWindow.dismiss();
			}
		});
		line_error.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(arrived != null) {
					service.expireDriver(arrived.getId(), arrived.getDriverId(), arrived.getOwen()
							, new HttpCallBack(DriverParkActivity.this) {
						@Override
						public void onSuccess(Object... objects) {
							BaseEntity r = (BaseEntity) objects[0];
							if (r.getResult() > 0) {
								adapter.removeValue(arrived);
								adapter.notifyDataSetChanged();
								ToastUtil.show("操作成功完成",DriverParkActivity.this);
							}else{
								ToastUtil.show(r.getMessage(),DriverParkActivity.this);
							}
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
		if(iscreate==true){
			return;
		}else
		if(hasFocus && adapter.getCount() <= 0) {
			getData();
			iscreate = true;
		}
	}
}

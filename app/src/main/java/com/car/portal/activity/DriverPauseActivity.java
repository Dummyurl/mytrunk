package com.car.portal.activity;

import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.car.portal.adapter.DriverListAdapter;
import com.car.portal.entity.Driver;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.view.PopUpView;
import com.car.portal.view.QueryTitleView;

public class DriverPauseActivity extends Activity {
	@ViewInject(R.id.driver_list_title)
	private QueryTitleView titleView;
	@ViewInject(R.id.driver_list_listView)
	private ListView listView;
	@ViewInject(R.id.txt_no_one_warn)
	private TextView txt_driver_warn;
	private DriverListAdapter adapter;
	private DriverService service;
	private int currentPage = 1;
	private View footer;
	private String condition;
	private View popupView;
	private PopUpView line_error,line_call;
	private PopupWindow popupWindow;
	private Driver driver;
	private Intent intent;
	private LinearLayout line_arrived,line_drivers,line_new,line_pause,line_delete,line_park,line_add_driver;
	private PopupWindow driverPopup;
	private View driverPopuView;
	
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_list);
		x.view().inject(this);
		adapter = new DriverListAdapter(this);
		LayoutInflater inflater = LayoutInflater.from(this);
        footer = inflater.inflate(R.layout.listview_footer, null);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(l);
        titleView.setOnQueryListener(new QueryTitleView.QueryClickListener() {
			@Override
			public void onQuery(String condition) {
				DriverPauseActivity.this.condition = condition;
				currentPage = 1;
				getData();
			}
		});
		titleView.setMoreListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDriverPopu();
			}
		});
        service = new DriverService(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				driver = (Driver) adapter.getItem(position);
				showPopupWindow();
			}
		});
        titleView.setOnQueryListener(new QueryTitleView.QueryClickListener() {
			@Override
			public void onQuery(String con) {
				condition = con;
				getData();
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
				intent = new Intent(DriverPauseActivity.this, DriverPauseActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_new.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverPauseActivity.this, DriverNewActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverPauseActivity.this, DriverDeletedActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_arrived.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverPauseActivity.this, DriverArriveActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_park.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverPauseActivity.this, DriverParkActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_drivers.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverPauseActivity.this, DriverSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_add_driver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverPauseActivity.this, AddDriverActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	public void getData() {
		service.getPauseDriver(condition, currentPage, new HttpCallBack(this) {
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
					if (((List<Driver>) objects[0]).size()==0){
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
            line_call= new PopUpView(this);
            line_error= new PopUpView(this);
            line_call.init(R.drawable.phone_call, "拨打电话", true);
            line_error.init(R.drawable.play, "解除暂停", false);
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
				// TODO Auto-generated method stub
				
			}
		});
		line_error.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(driver != null) {
					service.removePause(driver.getId(), new HttpCallBack(DriverPauseActivity.this) {
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

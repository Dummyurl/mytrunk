package com.car.portal.activity;

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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.DriverListAdapter;
import com.car.portal.adapter.LinePopuGridAdapter;
import com.car.portal.entity.CarArrived;
import com.car.portal.entity.Driver;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.service.UserService;
import com.car.portal.util.StringUtil;
import com.car.portal.view.QueryTitleView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class DriverSearchActivity extends Activity {
	@ViewInject(R.id.driver_list_title)
	private QueryTitleView titleView;
	@ViewInject(R.id.driver_list_listView)
	private ListView listView;
	@ViewInject(R.id.driver_slt_length)
	private EditText lengthEdit;
	@ViewInject(R.id.driver_slt_length2)
	private EditText length2Edit;
	@ViewInject(R.id.driver_slt_width)
	private EditText widthEdit;
	@ViewInject(R.id.driver_slt_day)
	private RelativeLayout selectText;
	@ViewInject(R.id.txt_day)
	private TextView txt_day;
	@ViewInject(R.id.img_day)
	private ImageView img_day;
	@ViewInject(R.id.txt_no_one_warn)
	private TextView txt_driver_warn;
	@ViewInject(R.id.view_split)
	private View view_split;
	private DriverListAdapter adapter;
	private DriverService service;
	private int currentPage = 1;
	private String condition;
	private double length;
	private double length2;
	private double width;
	private int dayOff;
	private View footer;
	private View popupView;
    private LinearLayout rela_popu,line_popu;
	private RelativeLayout line_error,line_end,line_connect,line_call,line_addArrived,view_call;
	private PopupWindow popupWindow,popupWindow_filter;
	private LinePopuGridAdapter linePopuGridAdapter;
	private PopupWindow driverPopup;
	private View driverPopuView, popup_filter;
	private GridView gridView;
	private ArrayList<String> arr_date;
	private boolean isCheck=false;
	private List<Driver> drivers;
	private Intent intent;
	private LinearLayout line_arrived,line_drivers,line_new,line_pause,line_delete,line_park,line_add_driver;
	private String public_driver = "0";  //共用一个DriverSearchActivity
	private Boolean iscreate = false;
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_search);
		intent = getIntent();
		public_driver = intent.getStringExtra("public_driver");
		x.view().inject(this);
		drivers=new ArrayList<Driver>();
		adapter = new DriverListAdapter(this);
		LayoutInflater inflater = LayoutInflater.from(this);
        footer = inflater.inflate(R.layout.listview_footer, null);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(l);
		titleView.setMoreListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDriverPopu();
			}
		});
        titleView.setOnQueryListener(new QueryTitleView.QueryClickListener() {
			@Override
			public void onQuery(String condition) {
				String len = lengthEdit.getText().toString();
				String w = widthEdit.getText().toString();
				String len2 = length2Edit.getText().toString();
				if (StringUtil.isNullOrEmpty(len)) {
					length = 0;
				} else {
					length = Double.parseDouble(len);
				}
				if (StringUtil.isNullOrEmpty(len2)) {
					length2 = 0;
				} else {
					length2 = Double.parseDouble(len2);
				}
				if (StringUtil.isNullOrEmpty(w)) {
					width = 0;
				} else {
					width = Double.parseDouble(w);
				}
				DriverSearchActivity.this.condition = condition;
				currentPage = 1;
				getData();
			}
		});
        service = new DriverService(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				//已删除的司机信息点击后不弹出popupWindow
				//int vid = view.getId();
				if (adapter.isDelFlag(position)) {
					return;
				//}else if(vid == R.id.call_driver) {

				//	Toast.makeText(getApplicationContext(), "" + view.getId(), 0).show();
				}else {
					showPopupWindow(position);
				}
			}
		});
		selectText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeChecked();
				showFilterPopup();
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
				intent = new Intent(DriverSearchActivity.this, DriverPauseActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_new.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverSearchActivity.this, DriverNewActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverSearchActivity.this, DriverDeletedActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_arrived.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverSearchActivity.this, DriverArriveActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_park.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverSearchActivity.this, DriverParkActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_drivers.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverSearchActivity.this, DriverSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_add_driver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverSearchActivity.this, AddDriverActivity.class);
				startActivity(intent);
				finish();
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
				/*if (dialog == null) {
					dialog = new LoadingDialog(DriverSearchActivity.this);
				}
				dialog.show();*/
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
	 * 查询司机信息
	 */
	public void getData() {
		service.searchDriver(length, length2, width, dayOff, condition, currentPage,public_driver, new HttpCallBack(this) {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object... objects) {
				if(objects != null && objects.length > 0) {
					if(currentPage == 1) {
						drivers=(List<Driver>) objects[0];
						adapter.setList(drivers);
					} else {
						List<Driver> list = (List<Driver>) objects[0];
						if(list != null) {
							for (Driver driver : list) {
								drivers.add(driver);
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
	
	public void initPopupWindow(){
        if(popupWindow==null){
            popupView=LayoutInflater.from(this).inflate(R.layout.good_driver_item_popu,null);
            popupWindow = new PopupWindow(popupView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            line_call= (RelativeLayout) popupView.findViewById(R.id.line_connect);
            line_connect= (RelativeLayout) popupView.findViewById(R.id.line_link);
            line_end= (RelativeLayout) popupView.findViewById(R.id.line_end);
            line_error= (RelativeLayout) popupView.findViewById(R.id.line_error);
			line_addArrived= (RelativeLayout) popupView.findViewById(R.id.line_addArrived);
            rela_popu= (LinearLayout) popupView.findViewById(R.id.rela_popu);
			view_call= (RelativeLayout) popupView.findViewById(R.id.line_view_call);
            rela_popu.getBackground().setAlpha(100);
			line_addArrived.setVisibility(View.VISIBLE);
			line_end.setVisibility(View.GONE);
			line_error.setVisibility(View.GONE);
			line_connect.setVisibility(View.GONE);
			line_call.setVisibility(View.GONE);
			view_call.setVisibility(View.GONE);
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
    public void showPopupWindow(int position) {
        initPopupWindow();
        View view= LayoutInflater.from(this).inflate(R.layout.activity_good_for_address,null);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER, 0, 0);
        popupOnClick(position);
    }

	public void popupOnClick(final int position){
		line_addArrived.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UserService userService=new UserService(DriverSearchActivity.this);
				Intent intent=new Intent(DriverSearchActivity.this,AddArrideDriverActivity.class);
				intent.putExtra("driverId",drivers.get(position).getId());
				intent.putExtra("driverName",drivers.get(position).getName());
				intent.putExtra("driver_owner",drivers.get(position).getUser());
				intent.putExtra("userId",userService.getLoginUser().getUid());
				intent.putExtra("owen",userService.getLoginUser().getUid());
				startActivity(intent);
				popupWindow.dismiss();
			}
		});
		line_call.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				
			}
		});
	}

	/**
	 * 初始化过滤的popupWindow
	 */
	public void initFilterPopup(){
		if (popupWindow_filter==null){
			arr_date=new ArrayList<String>();
			popup_filter=LayoutInflater.from(this).inflate(R.layout.popu_show_line,null);
			popupWindow_filter = new PopupWindow(popup_filter,
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
			gridView= (GridView) popup_filter.findViewById(R.id.grid_popu);
			line_popu= (LinearLayout) popup_filter.findViewById(R.id.grid_bgLine_line);
			line_popu.getBackground().setAlpha(100);

			linePopuGridAdapter=new LinePopuGridAdapter(this);
			gridView.setAdapter(linePopuGridAdapter);

			initFilterData();
		}
	}

	/**
	 * 显示过滤的popupWindow
	 */
	public void showFilterPopup(){
		initFilterPopup();

		popupWindow_filter.setTouchable(true);
		popupWindow_filter.showAsDropDown(view_split, 0, 0);
		initFilterListener();
	}

	/**
	 * 初始化过滤popupWindow的监听事件
	 */
	public void initFilterListener(){
		popup_filter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeChecked();
				popupWindow_filter.dismiss();
//				selectText.setCompoundDrawables(null, null, nav_up2, null);
			}
		});
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				linePopuGridAdapter.setSelectId(position);
				linePopuGridAdapter.notifyDataSetChanged();
				changeChecked();
				dayOff = position;
				getData();
				txt_day.setText(arr_date.get(position));
				popupWindow_filter.dismiss();
			}
		});
	}

	/**
	 * 初始化过滤器里时间的数据
	 */
	public void initFilterData(){
		for (int i=0;i<12;i++){
			if (i==0)
				arr_date.add("全部");
			else
				arr_date.add(i+"天前");
		}
		linePopuGridAdapter.setList(arr_date);
		linePopuGridAdapter.notifyDataSetChanged();
	}

	public void changeChecked(){
		isCheck =! isCheck;
		if (isCheck){
			img_day.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
		}else{
			img_day.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (isCheck == true) {
			return;
		} else if (hasFocus && adapter.getCount() <= 0) {
			String len = lengthEdit.getText().toString();
			String w = widthEdit.getText().toString();
			String len2 = length2Edit.getText().toString();
			if (StringUtil.isNullOrEmpty(len)) {
				length = 0;
			} else {
				length = Double.parseDouble(len);
			}
			if (StringUtil.isNullOrEmpty(len2)) {
				length2 = 0;
			} else {
				length2 = Double.parseDouble(len2);
			}
			if (StringUtil.isNullOrEmpty(w)) {
				width = 0;
			} else {
				width = Double.parseDouble(w);
			}
			getData();
			isCheck = true;
			/*if (dialog == null) {
				dialog = new LoadingDialog(DriverSearchActivity.this);
			}
			dialog.show();*/
		}
	}
}

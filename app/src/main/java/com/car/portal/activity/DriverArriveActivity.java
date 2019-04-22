package com.car.portal.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.adapter.ContactsAdapter;
import com.car.portal.adapter.DriverForAddressAdapter;
import com.car.portal.entity.CarArrived;
import com.car.portal.entity.Goods_For_Address;
import com.car.portal.entity.ParnerShipBase;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.service.GoodsService;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.QueryTitleView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverArriveActivity extends AppCompatActivity {
	@ViewInject(R.id.driver_list_title)
	private QueryTitleView titleView;
	@ViewInject(R.id.driver_list_listView)
	private ListView listView;
	@ViewInject(R.id.txt_no_one_warn)
	private TextView txt_driver_warn;
	private DriverService service;
	private GoodsService goodsService;
	private int currentPage = 1;
	private DriverForAddressAdapter adapter;
	private View footer;
	private View popupView;
    private LinearLayout rela_popu;
	private RelativeLayout line_error,line_end,line_connect,line_call,line_outTime;
	private String condition;
	private PopupWindow popupWindow,l_popupWindow;
	private Intent intent;
	private LinearLayout line_arrived,line_drivers,line_new,line_pause,line_delete,line_park,line_add_driver;
	private PopupWindow driverPopup;
	private View driverPopuView,l_popupView;
	private LinearLayout line_choice,rela_close,line_driver,line_goods,line_cencel,line_center;
	private TextView txt_contacts_name,txt_loading_date,txt_receipt_point,txt_driver_name,txt_arrived_date,txt_no_choice_goods,txt_no_choice_drivers;
	private RelativeLayout rela_link;
	private View view;
	private CarArrived carArrived;
    private LinearLayout linearLayout;
    private ListView list_contats;

	@SuppressLint("InflateParams") @Override
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
		view= LayoutInflater.from(this).inflate(R.layout.activity_driver_list,null);
        service = new DriverService(this);
		adapter = new DriverForAddressAdapter(this);
		carArrived=new CarArrived();
		goodsService=new GoodsService(this);
		LayoutInflater inflater = LayoutInflater.from(this);
        footer = inflater.inflate(R.layout.listview_footer, null);
		listView.setAdapter(adapter);
		// TODO: 2019/1/24 0024 改成进去此页面之后直接进行数据初始化
		adapter.setImageClick(new DriverForAddressAdapter.ImageClickListener() {
			@Override
			public void onClick(View v, CarArrived b) {
				Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + b.getTels()));
				if (PermissionChecker.checkSelfPermission(DriverArriveActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +  b.getTels()));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					return;
				}else {
					startActivity(intentPhone);
				}
			}
		});
		getData();
		listView.setOnScrollListener(l);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				carArrived= (CarArrived) adapter.getItem(position);
				showPopupWindow();
			}
		});
		titleView.setOnQueryListener(new QueryTitleView.QueryClickListener() {
			@Override
			public void onQuery(String condition) {
				DriverArriveActivity.this.condition = condition;
				getData();
			}
		});
		titleView.setBackClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titleView.setMoreListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDriverPopu();
			}
		});
	}



    public void showPopupWindow(String num) {
        initPopupWindow(num);
        View view=LayoutInflater.from(this).inflate(R.layout.goods_register,null);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER, 0, 0);
        popupOnClick();
    }


    public void initPopupWindow(String num){

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
				intent = new Intent(DriverArriveActivity.this, DriverPauseActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_new.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverArriveActivity.this, DriverNewActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverArriveActivity.this, DriverDeletedActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_arrived.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverArriveActivity.this, DriverArriveActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_park.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverArriveActivity.this, DriverParkActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_drivers.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverArriveActivity.this, DriverSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});
		line_add_driver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(DriverArriveActivity.this, AddDriverActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	/**
	 * 加载
	 */
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

	// TODO: 2019/1/23 0023 {"result":-1,"message":"用户名或密码错误！"} {"result":-2,"message":"未登录或者登录过期"}
	private void getData() {
		service.getArrivedDriver(condition, currentPage, new HttpCallBack(this) {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object... objects) {
				if(objects != null && objects.length > 0) {
						if(currentPage == 1) {
							adapter.setList((ArrayList<CarArrived>) objects[0]);
						} else {
							List<CarArrived> list = (ArrayList<CarArrived>) objects[0];
							if(list != null) {
								for (CarArrived carArrived : list) {
									adapter.addValue(carArrived);
								}
							}
						}
					if (((List<CarArrived>) objects[0]).size()==0){
//						if (adapter.getCount()<=0){
//							ToastUtil.show("当前页面没有获取到任何数据",DriverArriveActivity.this);
//						}
							ToastUtil.show("当前页面没有获取到任何数据",DriverArriveActivity.this);
						txt_driver_warn.setVisibility(View.VISIBLE);
						listView.setVisibility(View.GONE);

					}
						adapter.notifyDataSetChanged();

						listView.removeFooterView(footer);
				}
			}

			@Override
			public void onError(Object... objects) {
				Log.e("succe",objects+"ss");
			}

			@Override
			public void onFail(int result, String message, boolean show) {
				super.onFail(result, message, show);
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
			line_outTime= (RelativeLayout) popupView.findViewById(R.id.line_outTime);
			rela_popu= (LinearLayout) popupView.findViewById(R.id.rela_popu);
            rela_popu.getBackground().setAlpha(100);
			line_outTime.setVisibility(View.VISIBLE);
			line_error.setVisibility(View.GONE);
			line_end.setVisibility(View.GONE);
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

	/**
	 * 车货配对
	 */
	public void carGoodsLink(){
		final Map<String,Object> map=new HashMap<String, Object>();
		map.put("arriveId",carArrived.getId());
		map.put("t",System.currentTimeMillis());
		goodsService.carGoodsLink(map, new HttpCallBack(this) {
			@Override
			public void onSuccess(Object... objects) {
				ParnerShipBase parnerShipReturn = (ParnerShipBase) objects[0];
				Goods_For_Address goods_for_address = parnerShipReturn.getGoodDto();
				CarArrived carArrived = parnerShipReturn.getDriver();
				txt_no_choice_drivers.setVisibility(View.GONE);
				line_driver.setVisibility(View.VISIBLE);
				txt_arrived_date.setText(carArrived.getArriveDate());
				txt_driver_name.setText(carArrived.getDriverName());
				if (goods_for_address != null) {
					txt_no_choice_goods.setVisibility(View.GONE);
					line_goods.setVisibility(View.VISIBLE);
					txt_contacts_name.setText(goods_for_address.getContractName());
					txt_loading_date.setText(goods_for_address.getEndDate());
					txt_receipt_point.setText(goods_for_address.getRoute());
					line_choice.setVisibility(View.VISIBLE);
					linkCencel();
					linkCenter();
				} else {
					line_goods.setVisibility(View.GONE);
					txt_no_choice_goods.setVisibility(View.VISIBLE);
					line_choice.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * 取消订单配对
	 */
	public void linkCencel(){
		line_cencel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goodsService.linkCencel(new HttpCallBack(DriverArriveActivity.this) {
					@Override
					public void onSuccess(Object... objects) {
						if (objects != null && objects.length > 0) {
							Integer ret = (Integer) objects[0];
							if (ret == 1) {
								ToastUtil.show("取消订单配对成功", DriverArriveActivity.this);
								l_popupWindow.dismiss();
							} else
								ToastUtil.show("取消订单配对失败", DriverArriveActivity.this);
						}
					}
				});
			}
		});
	}

	/**
	 * 确认配对
	 */
	public void linkCenter(){
		line_center.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goodsService.linkCenter(new HttpCallBack(DriverArriveActivity.this) {
					@Override
					public void onSuccess(Object... objects) {
						if (objects != null && objects.length > 0) {
							Integer ret = (Integer) objects[0];
							if (ret == 1) {
								ToastUtil.show("配对成功", DriverArriveActivity.this);
								Intent intent1 = new Intent(DriverArriveActivity.this,
										PairedListActivity.class);
								startActivity(intent1);
								finish();
							} else
								ToastUtil.show("配对失败失败", DriverArriveActivity.this);
						}
					}
				});
			}
		});
	}

	/**
	 * 初始化关联信息的popupWindow
	 */
	public void initLinkPopup(){
		if (l_popupWindow==null){
			l_popupView=LayoutInflater.from(this).inflate(R.layout.popu_link,null);
			l_popupWindow=new PopupWindow(l_popupView,LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT,true);
			txt_contacts_name= (TextView) l_popupView.findViewById(R.id.txt_contacts_name);
			txt_loading_date= (TextView) l_popupView.findViewById(R.id.txt_loading_date);
			txt_receipt_point= (TextView) l_popupView.findViewById(R.id.txt_receipt_point);
			txt_driver_name= (TextView) l_popupView.findViewById(R.id.txt_driver_name);
			txt_arrived_date= (TextView) l_popupView.findViewById(R.id.txt_arrived_date);
			rela_link= (RelativeLayout) l_popupView.findViewById(R.id.rela_link);
			line_driver= (LinearLayout) l_popupView.findViewById(R.id.line_drivers);
			line_goods= (LinearLayout) l_popupView.findViewById(R.id.line_goods);
			txt_no_choice_goods= (TextView) l_popupView.findViewById(R.id.txt_no_choice_goods);
			txt_no_choice_drivers= (TextView) l_popupView.findViewById(R.id.txt_no_choice_driver);
			rela_close= (LinearLayout) l_popupView.findViewById(R.id.close);
			line_choice= (LinearLayout) l_popupView.findViewById(R.id.line_choice);
			line_cencel= (LinearLayout) l_popupView.findViewById(R.id.line_cencel);
			line_center= (LinearLayout) l_popupView.findViewById(R.id.line_center);
			rela_link.getBackground().setAlpha(100);
			l_popupView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					l_popupWindow.dismiss();
				}
			});
		}
	}

	/**
	 * 显示关联信息的popupWindow
	 */
	public void showLinkPopu(){
		initLinkPopup();
		carGoodsLink();
		l_popupWindow.setTouchable(true);
		l_popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER, 0, 0);
		rela_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				l_popupWindow.dismiss();
			}
		});
	}

	private void popupOnClick() {
		line_outTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				service.expireDriver(carArrived.getId(), carArrived.getDriverId(), carArrived.getOwen()
					, new HttpCallBack(DriverArriveActivity.this) {
					@Override
					public void onSuccess(Object... objects) {
						ToastUtil.show("操作成功！",DriverArriveActivity.this);
						adapter.removeValue(carArrived);
						adapter.notifyDataSetChanged();
					}
				});
				popupWindow.dismiss();
			}
		});
		line_connect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showLinkPopu();
				popupWindow.dismiss();
			}
		});
		line_call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus && adapter.getCount()<=0) {
			// TODO: 2019/1/23 0023 每次进入该页面焦点被获取到，而该页面数据请求不到数据的时候（adapter数据长度值小于零），会不停的去初始化数据，死循环
//                getData();

		}



	}
}

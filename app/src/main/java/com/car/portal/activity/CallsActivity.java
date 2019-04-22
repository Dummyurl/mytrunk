package com.car.portal.activity;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.CallsPhoneAdapter;
import com.car.portal.entity.OrderList;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.ContractService;
import com.car.portal.view.BaseTitleView;
import com.car.portal.view.CallsHeadView;
import com.car.portal.view.PopUpView;

public class CallsActivity extends Activity {
    @ViewInject(R.id.base_title_view)
    private BaseTitleView baseTitleView;
    @ViewInject(R.id.txt_no_one_warn)
    private TextView txt_no_one_warn;
    @ViewInject(R.id.head)
    private CallsHeadView mHead;
    @ViewInject(R.id.calls_list)
    private ListView calls_list;
    private ContractService contractService;
    private List<OrderList> orderLists;
    private List<String> dates;
    private CallsPhoneAdapter callsPhoneAdapter;
    private boolean hasLoadDate;
	private int previous = 0;
	private int year = 0;
	private static final int MIN_YEAR = 2015;
	private static int MIN_PREVIOUS;

	private PopUpView pre, current, preYear, thisYear;
	private PopupWindow popupWindow;
	private Calendar calendar;
	private boolean showYear;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls);
        x.view().inject(this);
        baseTitleView.setTitle(getResources().getString(R.string.titlle_calls));
        txt_no_one_warn.setVisibility(View.GONE);
        mHead = (CallsHeadView) findViewById(R.id.head);
        mHead.setFocusable(true);
        mHead.setClickable(true);
        mHead.setBackgroundColor(Color.parseColor("#b2d235"));
        mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
        mHead.setTitle("日期");
        calls_list.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
        contractService = new ContractService(this);
        callsPhoneAdapter = new CallsPhoneAdapter(this, mHead);
        calls_list.setAdapter(callsPhoneAdapter);
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        MIN_PREVIOUS = 0 - ((year - 2015) * 12 + month);
        initTitle();
    }
    
    
    private void initTitle() {
    	baseTitleView.setSetInfo(R.drawable.mail_list, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initPopwindow();
			}
		});
    }
    
    private void initPopwindow() {
    	if(popupWindow == null) {
    		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.driverlist_popwindow, null);
    		layout.setGravity(Gravity.TOP | Gravity.RIGHT);
			popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true);
			layout.getBackground().setAlpha(30);
			pre = new PopUpView(this);
            preYear = new PopUpView(this);
            current = new PopUpView(this);
            thisYear = new PopUpView(this);
            pre.init(R.drawable.pop_left, "上一个月", false);
            current.init(R.drawable.pop_current, "本月", false);
            preYear.init(R.drawable.pop_left, "月合计(上年)", false);
            thisYear.init(R.drawable.pop_current, "月合计(本年)", true);
            LinearLayout mainLay = (LinearLayout) layout.findViewById(R.id.pop_context);
            mainLay.addView(pre);
            mainLay.addView(current);
            mainLay.addView(preYear);
            mainLay.addView(thisYear);
            pre.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (previous > MIN_PREVIOUS || showYear) {
						previous --;
						getDataByMon();
					}
					popupWindow.dismiss();
				}
			});
            current.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(previous != 0 || showYear) {
						previous = 0;
						getDataByMon();
					}
					popupWindow.dismiss();
				}
			});
            preYear.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(year > MIN_YEAR || !showYear) {
						year --;
						getDataByYear();
					}
					popupWindow.dismiss();
				}
			});
            thisYear.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(year != calendar.get(Calendar.YEAR) || !showYear) {
						year = calendar.get(Calendar.YEAR);
						getDataByYear();
					}
					popupWindow.dismiss();
				}
			});
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
    	}
    	popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(baseTitleView);
    }

    /**
     * 根据月份查询电话统计
     */
    public void getDataByMon(){
        contractService.getCallsByMonths(previous , new HttpCallBack(this) {
            @Override
            @SuppressWarnings("unchecked")
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                	hasLoadDate = true;
					Map<String, String> map = (Map<String, String>) objects[0];
                    orderLists = (List<OrderList>) objects[1];
                    dates = (List<String>) objects[2];
                    showYear = false;
                    callsPhoneAdapter.initDates(map, dates, orderLists, showYear);
                    mHead.addTitle(dates, CallsActivity.this, showYear);
                    callsPhoneAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 根据年份查询电话统计
     */
    public void getDataByYear(){
        contractService.getCallsByYears(year, new HttpCallBack(this) {
            @SuppressWarnings("unchecked")
			@Override
            public void onSuccess(Object... objects) {
            	hasLoadDate = true;
                if (objects != null && objects.length > 0){
                    Map<String, String> map = (Map<String, String>) objects[0];
                    orderLists = (List<OrderList>) objects[1];
                    dates = (List<String>)objects[2];
                    showYear = true;
                    callsPhoneAdapter.initDates(map, dates, orderLists, showYear);
                    mHead.addTitle(dates, CallsActivity.this, showYear);
                    callsPhoneAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            //当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
            HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
                    .findViewById(R.id.horizontalScrollView1);
            headSrcrollView.onTouchEvent(arg1);
            return false;
        }
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	if(hasFocus && !hasLoadDate) {
    		getDataByMon();
    	}
    }
}

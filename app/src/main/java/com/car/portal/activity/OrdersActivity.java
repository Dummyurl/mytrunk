package com.car.portal.activity;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.LinePopuGridAdapter;
import com.car.portal.adapter.OrdersAdapter;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.OrderList;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.GoodsService;
import com.car.portal.util.LogUtils;
import com.car.portal.util.SharedPreferenceUtil;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;
import com.car.portal.view.HorizontalListView;

public class OrdersActivity extends Activity {
    @ViewInject(R.id.base_title_view)
    private BaseTitleView baseTitleView;
    @ViewInject(R.id.txt_no_one_warn)
    private TextView txt_no_one_warn;
    @ViewInject(R.id.list_orders)
    private HorizontalListView list_orders;
    @ViewInject(R.id.order_right_title)
    private LinearLayout orderList;
    
    private List<OrderList> orderLists = null;
//    private String[] month_str = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    private OrdersAdapter ordersAdapter = null;
    private GoodsService goodsService;
    private List<User> companyUsers = null;
    private ArrayList<String> arr_citys = null;
    private LinePopuGridAdapter linePopuGridAdapter;
    private View popupView;
    private GridView grid_popu;
    private LinearLayout line_popu;
    private PopupWindow popupWindow = null;
    private int userid = 0;
    private int selectNum = 0;
	private boolean hasFind = false;
	private User user;
    protected static SharedPreferenceUtil shareUtil = SharedPreferenceUtil.getIntence();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        x.view().inject(this);
        baseTitleView.setTitle(getResources().getString(R.string.title_orders));
        ordersAdapter = new OrdersAdapter(this);
        list_orders.setAdapter(ordersAdapter);
        companyUsers = new ArrayList<User>();
        goodsService = new GoodsService(this);
        baseTitleView.setIconIsShow(true);
        linePopuGridAdapter = new LinePopuGridAdapter(this);
        baseTitleView.setOnCheckListener(new BaseTitleView.ContactsClickListener() {
            @Override
            public void onQuery() {
                showPupoWindow();
            }
        });
    }

    /**
     * 获取本公司所有用户
     */
    public void getContacts(){
        arr_citys = new ArrayList<String>();
        User u = goodsService.getSavedUser();
        int companyId = 0;
        if(u != null) {
        	companyId = u.getCompanyId();
        }
        goodsService.getGoods(companyId, new HttpCallBack(this) {
            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                    companyUsers = (ArrayList<User>) objects[0];
                    if (user != null && "Y".equals(user.getPower())) {
                        User u = new User();
                        u.setUid(0);
                        u.setCname("全部");
                        u.setAlias("全部");
                        u.setCompanyId(user.getCompanyId());
                        companyUsers.add(0, u);
                    }
                    arr_citys.clear();
                    for (int i = 0; i < companyUsers.size(); i++) {
                        arr_citys.add(companyUsers.get(i).getCname());
                        if (companyUsers.get(i).getUid() == userid) {
                            linePopuGridAdapter.setSelectId(arr_citys.size() - 1);
                        }
                    }
                    linePopuGridAdapter.setList(arr_citys);
                    linePopuGridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(int result, String message, boolean show) {
                //super.onFail(result, message, show);
                ToastUtil.show(message, getApplicationContext());
            }
        });
    }

    /**
     * 初始化popupWindow
     */
    public void initPopuWindow() {
        popupView = LayoutInflater.from(this).inflate(R.layout.popu_show_line, null);
        line_popu = (LinearLayout) popupView.findViewById(R.id.grid_bgLine_line);
        grid_popu = (GridView) popupView.findViewById(R.id.grid_popu);
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                true);
        line_popu.getBackground().setAlpha(100);

        linePopuGridAdapter = new LinePopuGridAdapter(this);
        grid_popu.setAdapter(linePopuGridAdapter);
        linePopuGridAdapter.setSelectId(selectNum);
        linePopuGridAdapter.notifyDataSetChanged();
        getContacts();
        grid_popu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userid = companyUsers.get(position).getUid();
                getData();
                selectNum = position;
                baseTitleView.setContact(arr_citys.get(position));
                popupWindow.dismiss();
                baseTitleView.changeCheck(false);
            }
        });
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                baseTitleView.changeCheck(false);
            }
        });
    }

    /**
     * 显示popupWindow
     */
    public void showPupoWindow() {
        baseTitleView.changeCheck(true);
        initPopuWindow();
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(baseTitleView);
    }

    /**
     * 获取订单统计数据
     */
    public void getData(){
        final GoodsService goodsService = new GoodsService(this);
        goodsService.getOrders(userid, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0){
                	hasFind = true;
                    orderLists = (List<OrderList>) objects[0];
                    if (orderLists != null && orderLists.size() > 0) {
                        txt_no_one_warn.setVisibility(View.GONE);
                        orderList.setVisibility(View.VISIBLE);
                        LogUtils.e("OrdersActivity", "        size:" + orderLists.size());
                        String header_str = (String) objects[1];
                        ordersAdapter.getList(orderLists, header_str.replace("'", "").split(","));
                        ordersAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                txt_no_one_warn.setVisibility(View.VISIBLE);
                orderList.setVisibility(View.GONE);
            }
        });
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	if(hasFocus && !hasFind) {
    		user = goodsService.getLoginUser();
    		userid = user != null ? user.getUid() : -1;
    		baseTitleView.setContact(user.getCname());
    		getData();
    	}
    }
}

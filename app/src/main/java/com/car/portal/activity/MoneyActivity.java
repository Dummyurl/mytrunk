package com.car.portal.activity;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.AbsListView.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.AllMoneyAdapter;
import com.car.portal.adapter.ContactMoneyAdapter;
import com.car.portal.adapter.LinePopuGridAdapter;
import com.car.portal.entity.Money;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.GoodsService;
import com.car.portal.util.StringUtil;
import com.car.portal.view.BaseTitleView;

@SuppressLint("InflateParams")
public class MoneyActivity extends Activity {
    @ViewInject(R.id.base_title_view)
    private BaseTitleView baseTitleView;
    @ViewInject(R.id.txt_no_one_warn)
    private TextView txt_no_one_warn;
    @ViewInject(R.id.list_contact)
    private ListView list_contact;
    @ViewInject(R.id.list_allMoney)
    private ListView list_allMoney;
    private GoodsService goodsService;
    private PopupWindow popupWindow = null;
    
    private int selectNum = 0;
    private int userid = 0;
    
    private List<Money> moneys = null;
    private boolean hasFind = false;
    private User user;
    
    private GridView grid_popu;
    private LinearLayout line_popu;
    private LinePopuGridAdapter linePopuGridAdapter;
    private View popupView;
    private List<User> companyUsers = null;
    private ArrayList<String> arr_citys = null;
    
    private AllMoneyAdapter allMoneyAdapter;
    private ContactMoneyAdapter contactMoneyAdapter;
    private ListViewAndHeadViewTouchLinstener listLinstener;
    
    private LinearLayout headerAll;
    private LinearLayout headAllContent;
    private LinearLayout nomalHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        x.view().inject(this);
        baseTitleView.setTitle(getResources().getString(R.string.title_money));
        txt_no_one_warn.setVisibility(View.GONE);
        baseTitleView.setIconIsShow(true);
        goodsService = new GoodsService(this);
        baseTitleView.setOnCheckListener(new BaseTitleView.ContactsClickListener() {
            @Override
            public void onQuery() {
                showPupoWindow();
            }
        });
        listLinstener = new ListViewAndHeadViewTouchLinstener();
        init();
    }
    
    @SuppressWarnings("deprecation")
	private void init() {
    	headerAll = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.money_contract_item, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        headerAll.setLayoutParams(params);
        headerAll.setFocusable(true);
        headerAll.setClickable(true);
        headerAll.setBackgroundColor(getResources().getColor(R.color.light_gray_bg));
        ((TextView) headerAll.findViewById(R.id.txt_month)).setText("年份");
        headerAll.setOnTouchListener(listLinstener);
        headAllContent = (LinearLayout) headerAll.findViewById(R.id.lay_content);
        list_allMoney.addHeaderView(headerAll);
        allMoneyAdapter = new AllMoneyAdapter(this, headerAll);
        list_allMoney.setAdapter(allMoneyAdapter);
        list_allMoney.setOnTouchListener(listLinstener);
        
        nomalHeader = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.contact_money_item, null);
        TextView year = (TextView) nomalHeader.findViewById(R.id.txt_year);
        year.setText("时间");
        year.setTextColor(getResources().getColor(R.color.gray));
//		TextView year = (TextView) nomalHeader.findViewById(R.id.txt_month)).setText("月份");
        TextView sing = (TextView) nomalHeader.findViewById(R.id.txt_singular);
        sing.setText("单数");
        sing.setTextColor(getResources().getColor(R.color.gray));
		TextView comm = (TextView) nomalHeader.findViewById(R.id.txt_commission);
		comm.setText("提成");
		comm.setTextColor(getResources().getColor(R.color.gray));
		TextView achiev = (TextView) nomalHeader.findViewById(R.id.txt_achievement);
		achiev.setText("业绩");
		achiev.setTextColor(getResources().getColor(R.color.gray));
        list_contact.addHeaderView(nomalHeader);
        contactMoneyAdapter = new ContactMoneyAdapter(this);
        list_contact.setAdapter(contactMoneyAdapter);
    }

    /**
     * 获取全部的金额信息
     */
    public void getAllMoney() {
        goodsService.getAllMoneyAccount(new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                	hasFind = true;
                    moneys = (List<Money>) objects[0];
                    if (moneys != null && moneys.size() > 0) {
                        txt_no_one_warn.setVisibility(View.GONE);
                        String[] heads = (String[]) objects[1];
                        allMoneyAdapter.setList(moneys, heads);
                        allMoneyAdapter.notifyDataSetChanged();
                        initAllHeader(heads);
                        return;
                    }
                }
                allMoneyAdapter.setList(moneys, null);
                allMoneyAdapter.notifyDataSetChanged();
                txt_no_one_warn.setVisibility(View.VISIBLE);
                list_allMoney.setVisibility(View.GONE);
            }
        });
    }
    
    private void initAllHeader(String[] header_str) {
    	if(header_str != null && header_str.length > 0) {
    		int has_count = headAllContent.getChildCount();
    		int max = Math.max(header_str.length, has_count);
    		int min = Math.min(header_str.length, has_count);
    		LayoutParams params = new LayoutParams(150, LayoutParams.MATCH_PARENT, 1);
    		for (int i = 0; i < max; i++) {
				String head = header_str[i];
				if(StringUtil.isNullOrEmpty(head)) {
					head = "";
				}
				if(i < min) {
					((TextView) headAllContent.getChildAt(i)).setText(head);
				} else if(i >= has_count) {
					TextView view = new TextView(this);
					view.setLayoutParams(params);
					view.setBackgroundColor(getResources().getColor(R.color.light_gray_bg));
					view.setGravity(Gravity.CENTER);
					view.setTextColor(getResources().getColor(R.color.darkgray));
					view.setText(head);
					headAllContent.addView(view);
				} else if(i >= header_str.length) {
					headAllContent.removeViewAt(has_count + min - i - 1);
				}
			}
    	}
    }

    /**
     * 获取本公司所有用户
     */
    public void getContacts(){
    	if(arr_citys == null) {
    		arr_citys = new ArrayList<String>();
    	}
        User u = goodsService.getLoginUser();
        int companyId = 0;
        if(u != null) {
        	companyId = u.getCompanyId();
        }
        goodsService.getGoods(companyId, new HttpCallBack(this) {
            @SuppressWarnings("unchecked")
			@Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                	arr_citys.clear();
                    companyUsers = (ArrayList<User>) objects[0];
                    if(user != null && "Y".equals(user.getPower())) {
	                    User u = new User();
	                    u.setUid(0);
	                    u.setCname("全部");
	                    u.setAlias("全部");
	                    u.setCompanyId(1);
                    	companyUsers.add(0, u);
                    }
                    for (int i = 0; i < companyUsers.size(); i++) {
                        arr_citys.add(companyUsers.get(i).getCname());
                        if(userid == companyUsers.get(i).getUid()) {
                        	linePopuGridAdapter.setSelectId(i);
                        }
                    }
                    linePopuGridAdapter.setList(arr_citys);
                    linePopuGridAdapter.notifyDataSetChanged();
                }
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
        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                true);
        line_popu.getBackground().setAlpha(100);

        linePopuGridAdapter = new LinePopuGridAdapter(this);
        grid_popu.setAdapter(linePopuGridAdapter);

        getContacts();

        grid_popu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userid = companyUsers.get(position).getUid();
                if(userid == 0) {
                	getAllMoney();
                	list_allMoney.setVisibility(View.VISIBLE);
                    list_contact.setVisibility(View.GONE);
                } else {
                	findUserRate(userid);
                	list_allMoney.setVisibility(View.GONE);
                    list_contact.setVisibility(View.VISIBLE);
                }
                selectNum = position;
                baseTitleView.setContact(arr_citys.get(position));
                baseTitleView.changeCheck(false);
                popupWindow.dismiss();
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
     * 根据用户id获取rate
     */
    public void findUserRate(final int uid){
        goodsService.findUserRate(uid, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
            	hasFind = true;
                if (objects != null && objects.length > 0) {
                    double rate = (double) objects[0];
                    accountContacts(rate);
                }
            }
        });
    }

    /**
     * 获取公司某位成员的金额信息
     * @param uid
     * @param rate
     */
    public void accountContacts(final double rate){
        goodsService.accountContact(userid, rate, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0){
                    List<Money> moneyList = (List<Money>) objects[0];
                    if (moneyList != null && moneyList.size() > 0){
                        txt_no_one_warn.setVisibility(View.GONE);
                        contactMoneyAdapter.setList(moneyList);
                        contactMoneyAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                contactMoneyAdapter.setList(new ArrayList<Money>());
                contactMoneyAdapter.notifyDataSetChanged();
                txt_no_one_warn.setVisibility(View.VISIBLE);
                list_contact.setVisibility(View.GONE);
            }
        });
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	if(hasFocus && !hasFind) {
    		user = goodsService.getLoginUser();
    		userid = user == null ? -1 : user.getUid();
    		baseTitleView.setContact(user.getCname());
    		findUserRate(user.getUid());
    	}
    }
    
    class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            //当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
            HorizontalScrollView headSrcrollView = (HorizontalScrollView) headerAll
                    .findViewById(R.id.horizontalScrollView1);
            headSrcrollView.onTouchEvent(arg1);
            return false;
        }
    }
}

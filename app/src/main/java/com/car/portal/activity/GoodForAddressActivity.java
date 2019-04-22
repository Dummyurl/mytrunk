package com.car.portal.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.car.portal.R;
import com.car.portal.adapter.BottomDialogMenuAdapter;
import com.car.portal.adapter.ContactAdapter;
import com.car.portal.adapter.ContactsAdapter;
import com.car.portal.adapter.GoodForAddressAdapter;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.CarArrived;
import com.car.portal.entity.Contacts;
import com.car.portal.entity.GoodsContactData;
import com.car.portal.entity.Goods_For_Address;
import com.car.portal.entity.ParnerShipBase;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.SessionStore;
import com.car.portal.http.XUtil;
import com.car.portal.service.GoodsService;
import com.car.portal.service.UserService;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.PhoneCallUtils;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.DividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.internal.LinkedTreeMap;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

@SuppressLint("UseSparseArrays")
public class GoodForAddressActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener{
    @ViewInject(R.id.goods_list_view)
    private ListView goods_list_view;
    @ViewInject(R.id.swipe_layout_address)
    private SwipeRefreshLayout swipe_layout_address;
    private int userId=0;
    private String route = "", routeN = "";
    private Intent intent;
    private PopupWindow popupWindow,c_popupWindow,l_popupWindow,seedCalled_popupWindow;
    private View popupView,c_popupView,l_popupView,sc_popupView;
    private LinearLayout rela_popu;
    private RelativeLayout line_error,line_end,line_connect,line_call,line_view_call;
    private Goods_For_Address address;
    private GoodsService service;
    private GoodForAddressAdapter adapter;
    private LinearLayout linearLayout,c_line_call,line_choice,rela_close,line_drivers,line_goods,line_cencel,line_center;
    private ArrayList<Contacts> contats;
    private ListView list_contats;
    private ImageView img_check;
    private UserService userService;
    private TextView txt_contacts_name,txt_loading_date,txt_receipt_point,txt_driver_name,txt_arrived_date,txt_no_choice_goods,txt_no_choice_drivers;
    private RelativeLayout rela_link;
    private View view;
    private List<GoodsContactData.DataBean> contactList;
    private ContactAdapter contactAdapter;
    private MyHttpUtil util;
    private Dialog bottomDialog;
    private int type;//来电为1，查看为0
    private MaterialDialog dialog;
    private List<Goods_For_Address> foodlist;
    private  List listitem=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_for_address);
        x.view().inject(GoodForAddressActivity.this);
        if (intent==null)
            intent=getIntent();
        routeN = intent.getStringExtra("routeN");
        route = intent.getStringExtra("route");
        userService=new UserService(this);
        service = new GoodsService(this);
        contats=new ArrayList<Contacts>();
        foodlist = new ArrayList<>();
        view= LayoutInflater.from(this).inflate(R.layout.activity_good_for_address,null);
        onshow(true);
        initData();
        adapter = new GoodForAddressAdapter(GoodForAddressActivity.this);
        goods_list_view.setAdapter(adapter);
        util=new MyHttpUtil(this);
        initListen();
        initTitle();
    }

    /**
     * 初始化等待框
     */
    private void onshow(boolean b) {
        if(b) {
            dialog = new MaterialDialog.Builder(this)
                    .title("请稍后...")
                    .content("数据加载中，请稍后")
                    .progress(true, 0)
                    .show();
        }else if (dialog!=null){
            dialog.dismiss();
        }
    }

    /**
     * 初始化标题栏
     */
    public void initTitle(){

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.goods_info));
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab!=null;

        ab.setDisplayHomeAsUpEnabled(true);

        swipe_layout_address.setColorSchemeResources(R.color.view_blue);
        swipe_layout_address.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                swipe_layout_address.setRefreshing(false);
            }
        });
    }

    /**
     * listview的监听事件
     */
    public void initListen(){
        listitem.add("查看来电司机");
        listitem.add("货物作废");
        listitem.add("货物结束");
        listitem.add("货物关联");

        goods_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                address = (Goods_For_Address) adapter.getItem(position);
                delectcounts(address);
                showBottomDialog(listitem);
                //showPopupWindow();
            }
        });
    }

    private void delectcounts(final Goods_For_Address address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.resetViewCount(address.getId());
            }
        }).start();
    }

    /**
     * 初始化联系人的popupWindow
     */
    public void initContactsPopu(String num){
        if(c_popupWindow==null){
            c_popupView=LayoutInflater.from(this).inflate(R.layout.popu_contacts,null);
            linearLayout= (LinearLayout) c_popupView.findViewById(R.id.line_back);
            linearLayout.getBackground().setAlpha(100);
            c_popupWindow = new PopupWindow(c_popupView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            c_line_call= (LinearLayout) c_popupView.findViewById(R.id.line_call);
            list_contats= (ListView) c_popupView.findViewById(R.id.list_contacts);
            img_check= (ImageView) c_popupView.findViewById(R.id.img_check);

            c_popupView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    c_popupWindow.dismiss();
                }
            });
        }
        getContacts(num);
    }

    /**
     * 显示Contacts的PopuWindow
     */
    public void showContactsPopu(String num){
        initContactsPopu(num);
    }

    /**
     * 获取联系人
     */
    public void getContacts(String num){
        if(!StringUtil.isNullOrEmpty(num)) {
            final int nm = Integer.valueOf(num);
            HttpCallBack callBack = new HttpCallBack(this) {
                @SuppressWarnings("unchecked")
                @Override
                public void onSuccess(Object... objects) {
                    onshow(false);
                    if(objects != null && objects.length > 0) {
                        if(list_contats != null) {
                            ContactsAdapter adapter=new ContactsAdapter(GoodForAddressActivity.this);
                            contats=(ArrayList<Contacts>) objects[0];
                            adapter.setData((List<Contacts>) objects[0]);
                            list_contats.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Object... objects) {
                    onshow(false);
                }
            };
            userService.getBossContracts(nm, callBack);
        }
    }

    /**
     * 车货配对
     */
    public void carGoodsLink(){
        final Map<String,Object> map=new HashMap<String, Object>();
        map.put("goodsId",address.getId());
        map.put("t",System.currentTimeMillis());
        service.carGoodsLink(map, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                ParnerShipBase parnerShipReturn = (ParnerShipBase) objects[0];
                Goods_For_Address goods_for_address = parnerShipReturn.getGoodDto();
                CarArrived carArrived = parnerShipReturn.getDriver();
                txt_no_choice_goods.setVisibility(View.GONE);
                line_goods.setVisibility(View.VISIBLE);
                txt_contacts_name.setText(goods_for_address.getContractName());
                txt_loading_date.setText(goods_for_address.getEndDate());
                txt_receipt_point.setText(goods_for_address.getRoute());
                if (carArrived != null) {
                    txt_no_choice_drivers.setVisibility(View.GONE);
                    line_drivers.setVisibility(View.VISIBLE);
                    txt_arrived_date.setText(carArrived.getArriveDate());
                    txt_driver_name.setText(carArrived.getDriverName());
                    line_choice.setVisibility(View.VISIBLE);
                    linkCencel();
                    linkCenter();
                } else {
                    line_drivers.setVisibility(View.GONE);
                    txt_no_choice_drivers.setVisibility(View.VISIBLE);
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
                service.linkCencel(new HttpCallBack(GoodForAddressActivity.this) {
                    @Override
                    public void onSuccess(Object... objects) {
                        if (objects != null && objects.length > 0) {
                            Integer ret = (Integer) objects[0];
                            if (ret == 1) {
                                ToastUtil.show("取消订单配对成功", GoodForAddressActivity.this);
                                l_popupWindow.dismiss();
                            } else
                                ToastUtil.show("取消订单配对失败", GoodForAddressActivity.this);
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
                service.linkCenter(new HttpCallBack(GoodForAddressActivity.this) {
                    @Override
                    public void onSuccess(Object... objects) {
                        if(objects != null && objects.length > 0) {
                            Integer ret = (Integer) objects[0];
                            if (ret==1) {
                                ToastUtil.show("配对成功", GoodForAddressActivity.this);
                                Intent intent1=new Intent(GoodForAddressActivity.this,PairedListActivity.class);
                                startActivity(intent1);
                                finish();
                            }else
                                ToastUtil.show("配对失败失败", GoodForAddressActivity.this);
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
            l_popupWindow=new PopupWindow(l_popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,true);
            txt_contacts_name= l_popupView.findViewById(R.id.txt_contacts_name);
            txt_loading_date= l_popupView.findViewById(R.id.txt_loading_date);
            txt_receipt_point=  l_popupView.findViewById(R.id.txt_receipt_point);
            txt_driver_name=  l_popupView.findViewById(R.id.txt_driver_name);
            txt_arrived_date=  l_popupView.findViewById(R.id.txt_arrived_date);
            rela_link=  l_popupView.findViewById(R.id.rela_link);
            line_drivers=  l_popupView.findViewById(R.id.line_drivers);
            line_goods=  l_popupView.findViewById(R.id.line_goods);
            txt_no_choice_goods=  l_popupView.findViewById(R.id.txt_no_choice_goods);
            txt_no_choice_drivers=  l_popupView.findViewById(R.id.txt_no_choice_driver);
            rela_close=  l_popupView.findViewById(R.id.close);
            line_choice= l_popupView.findViewById(R.id.line_choice);
            line_cencel=  l_popupView.findViewById(R.id.line_cencel);
            line_center= l_popupView.findViewById(R.id.line_center);
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
     * 初始化查看&打过电话的司机的popupWindow
     */

    public void getContact(Map<String,Object> map,final HttpCallBack back) {
        String url = util.getUrl(R.string.findFocusForGoods);
        XUtil.Post(url, map, new MyCallBack<BaseEntity<ArrayList<LinkedTreeMap<String, Object>>>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                back.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<ArrayList<LinkedTreeMap<String, Object>>> arg0) {
                if(arg0.getResult() == 1) {
                    List<LinkedTreeMap<String, Object>> list = arg0.getData();
                    List<GoodsContactData.DataBean> goodsData  = new ArrayList<>();
                    if(list != null) {
                        for (LinkedTreeMap<String, Object> map : list) {
                            GoodsContactData.DataBean data = new GoodsContactData.DataBean();
                            LinkMapToObjectUtil.getObject(map, data);
                            goodsData.add(data);
                        }
                    }
                    back.onSuccess(goodsData);
                } else {
                    back.onFail(arg0.getResult(), arg0.getMessage(), false);
                }
            }
        });
    }

    private boolean clearFlag=false;
    public void ObtainContactData(){
        Map<String,Object> map=new HashMap<>();
        map.put("goodsId",address.getOutGoodsId());
        map.put("curPage",0);
        map.put("type",type);
        getContact(map,new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if (clearFlag) contactList.clear();
                contactList.addAll((ArrayList<? extends GoodsContactData.DataBean>) objects[0]);
                contactAdapter.notifyDataSetChanged();
                clearFlag=false;
            }

            @Override
            public void onError(Object... objects) {
                super.onError(objects);
                clearFlag=false;
            }
        });


    }

    public void initSeedCalledPopup(){

            sc_popupView=LayoutInflater.from(this).inflate(R.layout.popu_seedcalled,null);
            RadioGroup radioGroup=sc_popupView.findViewById(R.id.radio_group);
            RadioButton radioButton=sc_popupView.findViewById(R.id.radio_one);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    clearFlag=true;
                    switch (checkedId){
                        case R.id.radio_one:
                            type=0;
                            ObtainContactData();

                            break;
                        case R.id.radio_two:
                            type=1;
                            ObtainContactData();

                            break;
                    }
                }
            });
            RecyclerView recyclerView=sc_popupView.findViewById(R.id.recycler_view);

            contactList=new ArrayList<>();
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this,layoutManager));
            contactAdapter=new ContactAdapter(R.layout.item_contact,contactList);
            contactAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    switch (view.getId()){
                        case R.id.call:
                            List<String> list=new ArrayList<>();
                            list.add(contactList.get(position).getTel());
                            showBottomDialog(list);
                            break;

                        case R.id.local:
                            Intent intent = new Intent(GoodForAddressActivity.this,LocalMapActivity.class);
                            intent.putExtra("lat",contactList.get(position).getX());
                            intent.putExtra("lng",contactList.get(position).getY());
                            startActivity(intent);
                            //Toast.makeText(GoodForAddressActivity.this, "定位", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
            recyclerView.setAdapter(contactAdapter);
            View emptyView = LayoutInflater.from(this).inflate(R.layout.empyt_view_check_goods, null);
            contactAdapter.setEmptyView(emptyView);




            seedCalled_popupWindow=new PopupWindow(sc_popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,true);
            /*txt_contacts_name= (TextView) sc_popupView.findViewById(R.id.txt_contacts_name);
            txt_loading_date= (TextView) sc_popupView.findViewById(R.id.txt_loading_date);
            txt_receipt_point= (TextView) sc_popupView.findViewById(R.id.txt_receipt_point);*/
            txt_driver_name= (TextView) sc_popupView.findViewById(R.id.txt_driver_name);
            txt_arrived_date= (TextView) sc_popupView.findViewById(R.id.txt_arrived_date);
            rela_link= (RelativeLayout) sc_popupView.findViewById(R.id.rela_link);
            line_drivers= (LinearLayout) sc_popupView.findViewById(R.id.line_drivers);
            line_goods= (LinearLayout) sc_popupView.findViewById(R.id.line_goods);
            txt_no_choice_goods= (TextView) sc_popupView.findViewById(R.id.txt_no_choice_goods);
            txt_no_choice_drivers= (TextView) sc_popupView.findViewById(R.id.txt_no_choice_driver);
            rela_close= (LinearLayout) sc_popupView.findViewById(R.id.close);
            line_choice= (LinearLayout) sc_popupView.findViewById(R.id.line_choice);
            line_cencel= (LinearLayout) sc_popupView.findViewById(R.id.line_cencel);
            line_center= (LinearLayout) sc_popupView.findViewById(R.id.line_center);
            rela_link.getBackground().setAlpha(100);
            sc_popupView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seedCalled_popupWindow.dismiss();
                }
            });
            radioButton.performClick();

    }



    private void showBottomDialog(final List<String> menuList) {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        bottomDialog.setCanceledOnTouchOutside(true);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_menu, null);
        final RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view);
        BottomDialogMenuAdapter dialogMenuAdapter = new BottomDialogMenuAdapter(R.layout.item_bottom_dialog_menu, menuList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_dis_button, null);
        dialogMenuAdapter.setFooterView(view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager));
        if(menuList.get(0).equals("查看来电司机")){
            dialogMenuAdapter.setOnItemClickListener(this);
        }else {
            dialogMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    PhoneCallUtils.call(menuList.get(position));
                    if (bottomDialog!=null) bottomDialog.dismiss();
                }
            });
        }
        recyclerView.setAdapter(dialogMenuAdapter);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.cancel();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = this.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

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

    /**
     * 显示查看&打过电话的popupWindow
     */
    public void showSeedCalledPopu(){
        initSeedCalledPopup();
        //carGoodsLink();
        seedCalled_popupWindow.setTouchable(true);
        seedCalled_popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER, 0, 0);

    }

    /**
     * 货物作废
     */
    public void goodsError(){
    	if(address != null) {
    		service.goodsError(address.getBossId(), address.getId(), address.getOwer(), new HttpCallBack(this) {
    			@Override
    			public void onSuccess(Object... objects) {
    				if(objects != null && objects.length > 0) {
    					Integer ret = (Integer) objects[0];
    					if(ret == null || ret < 1) {
    						ToastUtil.show("操作失败", GoodForAddressActivity.this);
    					}else{
                            ToastUtil.show("货物作废完成", GoodForAddressActivity.this);
                        }
                        adapter.removeValue(address);
                        handler.sendEmptyMessage(HAND_MSG);
    				}
    			}
    		});
    	}
    }

    private static final int HAND_MSG = 0x1134;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == HAND_MSG) {
                adapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 货物结束
     */
    public void getEnd(){
    	if(address != null) {
    		service.getEnd(address.getBossId(), address.getId(), new HttpCallBack(this) {
    			@Override
    			public void onSuccess(Object... objects) {
    				if(objects != null && objects.length > 0) {
    					Integer ret = (Integer) objects[0];
    					if(ret == null || ret < 1) {
    						ToastUtil.show("操作失败", GoodForAddressActivity.this);
    					}else{
                            ToastUtil.show("货物已经成功结束", GoodForAddressActivity.this);
                        }
                        adapter.removeValue(address);
                        handler.sendEmptyMessage(HAND_MSG);
                    }
    			}
    		});
    	}
    }

    /**
     * 初始化数据
     */
    private void initData(){
		service.getGoodsByRoute(routeN, userId, new HttpCallBack(this) {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object... objects) {
                onshow(false);
				if(objects != null && objects.length > 0) {
                    foodlist.clear();
                    foodlist.addAll((List<Goods_For_Address>) objects[0]);
					adapter.setData(foodlist);
					adapter.notifyDataSetChanged();
				}
			}

            @Override
            public void onFail(int result, String message, boolean show) {
                super.onFail(result, message, show);
                onshow(false);
                //SessionStore.resetSessionId();
            }

            @Override
            public void onError(Object... objects) {
                onshow(false);
                ToastUtil.show("与服务器连接失败！",GoodForAddressActivity.this);
            }
        });
    }
    
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//    	super.onWindowFocusChanged(hasFocus);
//    	if(hasFocus && adapter.getCount() <= 0) {
//    		initData();
//    	}
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将Handler里面消息清空了。
        handler.removeCallbacksAndMessages(null);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return  true;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        bottomDialog.dismiss();
            switch (position){
                case 0:
                    showSeedCalledPopu();
                    break;
                case 1:
                    new MaterialDialog.Builder(this)
                            .title("货物作废")
                            .content("是否将货物作废？该记录将无效，不会进入统计数据")
                            .contentColor(getResources().getColor(R.color.title_black))
                            .positiveText("作废")
                            .negativeText("取消(不作废)")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    getEnd();
                                }
                            })
                            .show();
                    break;
                case 2:
                    new MaterialDialog.Builder(this)
                            .title("货物结束")
                            .content("是否将结束货物订单？该记录将有效，并进入统计数据")
                            .contentColor(getResources().getColor(R.color.title_black))
                            .positiveText("结束")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    goodsError();
                                }
                            })
                            .show();
                    break;
                case 3:
                    showLinkPopu();
                    break;
            }
    }


}

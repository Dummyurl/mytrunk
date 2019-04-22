package com.car.portal.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.car.portal.R;
import com.car.portal.adapter.BottomDialogMenuAdapter;
import com.car.portal.adapter.DriverForAddressAdapter;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.CarArrived;
import com.car.portal.entity.Goods_For_Address;
import com.car.portal.entity.ParnerShipBase;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.service.GoodsService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.PhoneCallUtils;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;
import com.car.portal.view.DividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class DriverForAddressActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,BaseQuickAdapter.OnItemClickListener{
    @ViewInject(R.id.goods_list_view)
    private ListView listView;
    @ViewInject(R.id.swipe_layout_address)
    SwipeRefreshLayout swipe_layout_address;
    private RelativeLayout line_error,line_end,line_connect,line_call;
    private DriverForAddressAdapter driverForAddressAdapter;
    private Intent intent;
    private PopupWindow popupWindow,l_popupWindow;
    private View popuView,l_popupView;
    private TextView txt_error;
    private DriverService driverService;
    private CarArrived carArrived;
    private GoodsService goodsService;
    private String col;
    private TextView txt_contacts_name,txt_loading_date,txt_receipt_point,txt_driver_name,txt_arrived_date,txt_no_choice_goods,txt_no_choice_drivers;
    private RelativeLayout rela_link;
    private View view;
    private Dialog bottomDialog;
    private LinearLayout rela_close,line_drivers,line_goods,line_choice,line_cencel,line_center;
    private  ArrayList<CarArrived> carlist;
    private  List listitem=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_for_address);
        carlist = new ArrayList<>();
        initTitle();
        intent=getIntent();
        driverService=new DriverService(this);
        goodsService=new GoodsService(this);
        carArrived=new CarArrived();
        view= LayoutInflater.from(this).inflate(R.layout.activity_good_for_address,null);

        driverForAddressAdapter=new DriverForAddressAdapter(this);
        listView.setAdapter(driverForAddressAdapter);
        listView.setOnItemClickListener(this);
        driverForAddressAdapter.setImageClick(new DriverForAddressAdapter.ImageClickListener() {
            @Override
            public void onClick(View v,CarArrived  b) {
                if(b.getTels().contains(",")){
                    String[] strings = b.getTels().split(",");
                    List<String> phonelist = new ArrayList<>();
                    for (int i =0;i<strings.length;i++){
                        phonelist.add(strings[i]);
                    }
                    showBottomDialog(phonelist);
                }else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + b.getTels()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 初始化标题栏
     */
    public void initTitle() {
        x.view().inject(DriverForAddressActivity.this);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.driver_for_address));
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab!=null;

        ab.setDisplayHomeAsUpEnabled(true);
        listitem.add("超期");
        listitem.add("货物关联");


        swipe_layout_address.setColorSchemeResources(R.color.view_blue);
        swipe_layout_address.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                swipe_layout_address.setRefreshing(false);
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        carArrived= (CarArrived) driverForAddressAdapter.getItem(position);
        showBottomDialog(listitem);
        //showPopupWindow();
    }
    /**
     * 车货配对
     */
    public void carGoodsLink(){
        final Map<String,Object> map=new HashMap<String, Object>();
        map.put("arriveId", carArrived.getId());
        map.put("t", System.currentTimeMillis());
        goodsService.carGoodsLink(map, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                ParnerShipBase parnerShipReturn = (ParnerShipBase) objects[0];
                Goods_For_Address goods_for_address = parnerShipReturn.getGoodDto();
                CarArrived carArrived1 = parnerShipReturn.getDriver();
                txt_no_choice_drivers.setVisibility(View.GONE);
                line_drivers.setVisibility(View.VISIBLE);
                txt_arrived_date.setText(carArrived1.getArriveDate());
                txt_driver_name.setText(carArrived1.getDriverName());
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
     * 初始化车货配对的ppupWindow
     */
    public void initLinkPopup(){
        if (l_popupWindow==null){
            l_popupView=LayoutInflater.from(this).inflate(R.layout.popu_link,null);
            l_popupWindow=new PopupWindow(l_popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,true);
            txt_contacts_name= (TextView) l_popupView.findViewById(R.id.txt_contacts_name);
            txt_loading_date= (TextView) l_popupView.findViewById(R.id.txt_loading_date);
            txt_receipt_point= (TextView) l_popupView.findViewById(R.id.txt_receipt_point);
            txt_driver_name= (TextView) l_popupView.findViewById(R.id.txt_driver_name);
            txt_arrived_date= (TextView) l_popupView.findViewById(R.id.txt_arrived_date);
            rela_link= (RelativeLayout) l_popupView.findViewById(R.id.rela_link);
            line_drivers= (LinearLayout) l_popupView.findViewById(R.id.line_drivers);
            line_goods= (LinearLayout) l_popupView.findViewById(R.id.line_goods);
            txt_no_choice_goods= (TextView) l_popupView.findViewById(R.id.txt_no_choice_goods);
            txt_no_choice_drivers= (TextView) l_popupView.findViewById(R.id.txt_no_choice_driver);
            line_choice= (LinearLayout) l_popupView.findViewById(R.id.line_choice);
            rela_close= (LinearLayout) l_popupView.findViewById(R.id.close);
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
     * 取消订单配对
     */
    public void linkCencel(){
        line_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsService.linkCencel(new HttpCallBack(DriverForAddressActivity.this) {
                    @Override
                    public void onSuccess(Object... objects) {
                        if (objects != null && objects.length > 0) {
                            Integer ret = (Integer) objects[0];
                            if (ret == 1) {
                                ToastUtil.show("取消订单配对成功", DriverForAddressActivity.this);
                                l_popupWindow.dismiss();
                            } else
                                ToastUtil.show("取消订单配对失败", DriverForAddressActivity.this);
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
                goodsService.linkCenter(new HttpCallBack(DriverForAddressActivity.this) {
                    @Override
                    public void onSuccess(Object... objects) {
                        if(objects != null && objects.length > 0) {
                            Integer ret = (Integer) objects[0];
                            if (ret==1) {
                                ToastUtil.show("订单配对成功", DriverForAddressActivity.this);
                                Intent intent1=new Intent(DriverForAddressActivity.this,PairedListActivity.class);
                                startActivity(intent1);
                                finish();
                            }else
                                ToastUtil.show("配对失败失败", DriverForAddressActivity.this);
                        }
                    }
                });
            }
        });
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
     * 显示popupWindow
     */
//    public void showPopupWindow(){
//        initPopupWindow();
//        View view=LayoutInflater.from(this).inflate(R.layout.activity_good_for_address,null);
//        popupWindow.setTouchable(true);
//        popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER, 0, 0);
//        popupWindowListen();
//    }

//    /**
//     * 初始化popupWindow
//     */
//    public void initPopupWindow(){
//        if (popupWindow==null){
//            //得到popupWindow的布局文件
//            popuView= LayoutInflater.from(this).inflate(R.layout.good_driver_item_popu,null);
//            //创建popuWindow并设置它的宽高
//            popupWindow=new PopupWindow(popuView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,true);
//            line_error= (RelativeLayout) popuView.findViewById(R.id.line_error);
//            line_end= (RelativeLayout) popuView.findViewById(R.id.line_end);
//            line_connect= (RelativeLayout) popuView.findViewById(R.id.line_link);
//            line_call= (RelativeLayout) popuView.findViewById(R.id.line_connect);
//            txt_error= (TextView) popuView.findViewById(R.id.txt_error);
//            popuView.getBackground().setAlpha(100);
//            line_end.setVisibility(View.GONE);
//            txt_error.setText("超期");
//
//            popuView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popupWindow.dismiss();
//                }
//            });
//        }
//    }

    private void expireDriver(){
        driverService.expireDriver(carArrived.getId(), carArrived.getDriverId(), carArrived.getOwen(), new HttpCallBack(DriverForAddressActivity.this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects!=null&&objects.length>0){
                    BaseEntity be=(BaseEntity) objects[0];
                    if (be==null||be.getResult()<=0){
                        ToastUtil.show("操作失败",DriverForAddressActivity.this);
                    }else{
                        ToastUtil.show("操作成功完成",DriverForAddressActivity.this);
                        driverForAddressAdapter.removeValue(carArrived);
                        driverForAddressAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


    /**
     * 初始化数据
     */
    public void initData(){
        col=intent.getStringExtra("id");
        LogUtils.e("DriverForAdress"," "+col);
        goodsService.getDriverByRoute(col, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                carlist.clear();
                carlist.addAll((ArrayList<CarArrived>) objects[0]);
                driverForAddressAdapter.setList(carlist);
                driverForAddressAdapter.notifyDataSetChanged();
                LogUtils.e("initListen", "     carArriveds:" + driverForAddressAdapter.getCount());
            }
        });
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	if(hasFocus && driverForAddressAdapter.getCount() <= 0) {
    		initData();
    	}
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
        if(menuList.get(0).equals("超期")){
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    bottomDialog.dismiss();
                    switch (position){
                    case 0:
                        new MaterialDialog.Builder(this)
                                .title("超期")
                                .content("是否确认司机已超期？")
                                .contentColor(getResources().getColor(R.color.title_black))
                                .positiveText("确认")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        expireDriver();
                                    }
                                })
                                .show();
                        break;
                }
    }
}

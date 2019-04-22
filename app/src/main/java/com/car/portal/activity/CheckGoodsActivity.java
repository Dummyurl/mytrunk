package com.car.portal.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.BottomDialogMenuAdapter;
import com.car.portal.adapter.CheckGoodsAdapter;
import com.car.portal.entity.Address;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.CheckGoodsData;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.XUtil;
import com.car.portal.service.UserService;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.PhoneCallUtils;
import com.car.portal.util.SharedPreferenceUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;
import com.car.portal.view.DividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.internal.LinkedTreeMap;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckGoodsActivity extends AppCompatActivity implements View.OnClickListener , BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {



    @ViewInject(R.id.start)TextView start;
    @ViewInject(R.id.end)TextView end;
    @ViewInject(R.id.custom)TextView custom;
    @ViewInject(R.id.recycler_view)RecyclerView recyclerView;
    @ViewInject(R.id.swipe_layout_goods)
    SwipeRefreshLayout swipe_layout_goods;
    private String startCode;
    private String endCode;
    private int userCarType;
    private int carL;
    private int carType;
    private MyHttpUtil util;
    private CheckGoodsAdapter checkGoodsAdapter;
    private Dialog bottomDialog;
    private View emptyView;
    private UserService userService = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_goods);
        x.view().inject(this);
        inittooler();

        list=new ArrayList<>();
        InitCityData();
        InitRecyclerView();
        util = new MyHttpUtil(this);
        start.setOnClickListener(this);
        end.setOnClickListener(this);
        custom.setOnClickListener(this);
        if(userService==null)
            userService = new UserService(getApplicationContext());
        ObtainFilterData();
    }

    private void inittooler() {

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("货源");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab!=null;

        ab.setDisplayHomeAsUpEnabled(true);
    }


    private void InitRecyclerView() {


        list=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,layoutManager));
        checkGoodsAdapter=new CheckGoodsAdapter(R.layout.item_check_goods,list);
        checkGoodsAdapter.setOnItemChildClickListener(this);
        checkGoodsAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(checkGoodsAdapter);
        emptyView = LayoutInflater.from(this).inflate(R.layout.empyt_view_check_goods, null);
        swipe_layout_goods.setColorSchemeResources(R.color.view_blue);
        swipe_layout_goods.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ObtainFilterData();
                swipe_layout_goods.setRefreshing(false);
            }
        });
    }



    private void InitCityData() {
        SharedPreferenceUtil shareUtil=new SharedPreferenceUtil();
        Address address = shareUtil.getModelValue(this);
        endCode="0";
        if(address!=null) {
            start.setText(address.getCity());
            startCode=address.getCityCode();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){
            case R.id.start:
                intent = new Intent(this,CitySelectActivity.class);
                intent.putExtra("title","出发城市");
                intent.putExtra("action","checkGoodsDepart");
                startActivityForResult(intent, 100);
                overridePendingTransition(0, 0);
                break;


            case R.id.end:
                intent = new Intent(this,CitySelectActivity.class);
                intent.putExtra("title","出发城市");
                intent.putExtra("action","checkGoodsArrive");
                startActivityForResult(intent, 101);
                overridePendingTransition(0, 0);
                break;

            case R.id.custom:
                intent = new Intent(this,CustomFilterActivity.class);
                startActivityForResult(intent,100);

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode==110&&data!=null){
            if (requestCode==100){
                userCarType=data.getIntExtra("useType",0);
                carL=data.getIntExtra("carLong",0);
                carType=data.getIntExtra("carType",0);
                ObtainFilterData();
                //Toast.makeText(this, useType+"  "+carLong+"  "+carType, Toast.LENGTH_SHORT).show();
            }
        }


        if (resultCode==0&&data!=null){
            String action=data.getStringExtra("action");
            if (requestCode==100){
                start.setText(ObtainContent(data,false));
                SharedPreferenceUtil.SaveCity(action,ObtainContent(data,true));
                startCode=data.getStringExtra("code");
                ObtainFilterData();
            }
            if (requestCode==101){
                end.setText(ObtainContent(data,false));
                SharedPreferenceUtil.SaveCity(action,ObtainContent(data,true));
                endCode=data.getStringExtra("code");
                ObtainFilterData();
            }

        }
    }


    public String ObtainContent(Intent data,boolean fullContent){
        String province=data.getStringExtra("province");
        String city=data.getStringExtra("city");
        String district=data.getStringExtra("district");
        String code=data.getStringExtra("code");

        String fullStr=province+" "+city+" "+district+" " +code;
        if (fullContent)
            return fullStr;

        String[] split = fullStr.split(" ");
        String address=split[2];
        String fristStr=address.substring(0);
        if (fristStr.startsWith("全"))
            return fristStr.substring(1,fristStr.length());
        else
            return address;
    }

    private ArrayList<CheckGoodsData.DataBean> list;
    private int currPage=0;
    private void ObtainFilterData() {
        Map<String,Object> map=new HashMap<>();
        map.put("startCode",startCode);
        map.put("endCode",endCode);
        map.put("userCarType",userCarType);
        map.put("carL",carL);
        map.put("carType ",carType);
        map.put("currPage ",currPage);


        getGoodsListData(map, new HttpCallBack(CheckGoodsActivity.this) {


            @Override
            public void onSuccess(Object... objects) {
                list.clear();
                list.addAll((ArrayList<? extends CheckGoodsData.DataBean>) objects[0]);
                checkGoodsAdapter.notifyDataSetChanged();
                if (list.size()==0&&checkGoodsAdapter.getEmptyView()==null) checkGoodsAdapter.setEmptyView(emptyView);
            }

            @Override
            public void onError(Object... objects) {
                list.clear();
                super.onError(objects);
            }
        });
    }

    public void getGoodsListData(Map<String,Object> map,final HttpCallBack back) {
        String url = util.getUrl(R.string.findAllGoods);
        XUtil.Post(url, map, new MyCallBack<BaseEntity<ArrayList<LinkedTreeMap<String, Object>>>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                back.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<ArrayList<LinkedTreeMap<String, Object>>> arg0) {
                if(arg0.getResult() == 1) {
                    List<LinkedTreeMap<String, Object>> list =  arg0.getData();
                    List<CheckGoodsData.DataBean> goodsData  = new ArrayList<>();
                    if(list != null) {
                        for (LinkedTreeMap<String, Object> map : list) {
                            CheckGoodsData.DataBean data = new CheckGoodsData.DataBean();
                            LinkMapToObjectUtil.getObject(map, data);
                            goodsData.add(data);
                        }
                    }
                    back.onSuccess(goodsData);
                } else {
                    ToastUtil.show(arg0.getMessage(),CheckGoodsActivity.this);
                    back.onFail(arg0.getResult(), arg0.getMessage(), false);
                }
            }
        });
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.call:
                User user = null;
                String str = null;
                if(userService!=null){
                    user = userService.getSavedUser();
                    if(user.getAuth()==1){
                        str=list.get(position).getTels();
                    }else{
                        str="您的资料重要，请完善资料并通过审核\n不能查看电话号码，我们需要您的支持";
                    }
                }else{
                    str="发生了错误，请退出重登录";
                }
                if (str!=null&&!str.trim().isEmpty()) {
                    String[] split = str.split(",");
                    showBottomDialog(Arrays.asList(split));
                }
            break;
            case R.id.local:
                /*Intent intent = new Intent(CheckGoodsActivity.this,LocalMapActivity.class);
                intent.putExtra("lat",contactList.get(position).getX());
                intent.putExtra("lng",contactList.get(position).getY());
                startActivity(intent);*/
                break;
        }
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
        dialogMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                PhoneCallUtils.call(menuList.get(position));
                if (bottomDialog!=null) bottomDialog.dismiss();

            }
        });
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
        Intent intent = new Intent(this,GoodsDetailActivity.class);
        CheckGoodsData.DataBean dataBean = list.get(position);
        intent.putExtra("data",dataBean);
        startActivity(intent); 
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

}

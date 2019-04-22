package com.car.portal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.car.portal.R;
import com.car.portal.adapter.BottomDialogMenuAdapter;
import com.car.portal.adapter.ReChangeAdapter;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.ApplicationConfig;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.ReChangeData;
import com.car.portal.entity.WxPayInfoData;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.XUtil;
import com.car.portal.util.KeyBoardUtils;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.view.DividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.orhanobut.hawk.Hawk;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverReChangeActivity extends AppCompatActivity implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;

    @ViewInject(R.id.focus_com_sub)
    Button subButton;

    private List<ReChangeData> list;
    protected MyHttpUtil util;
    private ReChangeAdapter reChangeAdapter;
    private Dialog bottomDialog;
    private IWXAPI api;
    private int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_rechange);
        x.view().inject(DriverReChangeActivity.this);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("会员充值");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab!=null;
        ab.setDisplayHomeAsUpEnabled(true);


        util = new MyHttpUtil(this);
        subButton.setOnClickListener(this);
        Object t = Hawk.get("uid");
        uid= t==null?0:Integer.parseInt(t.toString());
        InitRecyclerView();
        MyApplication.getContext().addActivity(this);
        getFindSetMeal(new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                    list.addAll((ArrayList<ReChangeData>) objects[0]);
                    reChangeAdapter.notifyDataSetChanged();
                }
            }
        });
        initWeChatAPi();
    }
    private void initWeChatAPi() {
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(ApplicationConfig.getIntance().getWxAppId());
    }
    private void InitRecyclerView() {
        list=new ArrayList<>();
        reChangeAdapter=new ReChangeAdapter(R.layout.item_rechange,list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,layoutManager));
        recyclerView.setAdapter(reChangeAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.focus_com_sub:
                List list=new ArrayList();
                list.add("微信支付");
                list.add("支付宝支付");
                list.add("银联支付");
                showBottomDialog(list);
                break;
        }
    }




    public void getFindSetMeal(final HttpCallBack back) {
        String url = util.getUrl(R.string.url_findSetMeal);
        XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList<LinkedTreeMap<String, Object>>>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                back.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<ArrayList<LinkedTreeMap<String, Object>>> arg0) {
                if(arg0.getResult() == 1) {
                    ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
                    ArrayList<ReChangeData> reChangeData  = new ArrayList<>();
                    if(list != null) {
                        for (LinkedTreeMap<String, Object> map : list) {
                            ReChangeData data = new ReChangeData();
                            LinkMapToObjectUtil.getObject(map, data);
                            reChangeData.add(data);
                        }
                    }
                    back.onSuccess(reChangeData);
                } else {
                    back.onFail(arg0.getResult(), arg0.getMessage(), false);
                }
            }
        });
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
        dialogMenuAdapter.setOnItemClickListener(this);
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
        switch (position){
            case 0:
                ReChangeData item = reChangeAdapter.getItem(reChangeAdapter.getPosition());
                int money=item.getMoney();
                int id=item.getId();
                if (money==0) money=item.getFlag();

                item.getId();
                Map<String,Object> map=new HashMap<>();
                map.put("id",id);
                map.put("money",money*100);
                map.put("uid",uid);

                getWxOrderInfo(map, new HttpCallBack(this) {
                    @Override
                    public void onSuccess(Object... objects) {


                        WxPayInfoData.DataBean wxpayBean = (WxPayInfoData.DataBean) objects[0];
                        //查看ApplicationConfig类 中的APP_ID配置是否正确
                        //生成bean类,把对应的数据赋值到微信支付提供的request类唤醒即可

                        PayReq request = new PayReq();
                        request.appId = wxpayBean.getAppid();
                        request.partnerId = wxpayBean.getPartnerid();
                        request.prepayId = wxpayBean.getPrepayid();
                        request.packageValue = wxpayBean.getPackageX();
                        request.nonceStr = wxpayBean.getNoncestr();
                        request.timeStamp =wxpayBean.getTimestamp();
                        request.sign = wxpayBean.getSign();
                        Hawk.put("nonceStr",wxpayBean.getNoncestr());
                        Hawk.put("out_trade_no",wxpayBean.getOut_trade_no());
                        api.sendReq(request);
                        bottomDialog.dismiss();
                        KeyBoardUtils.hideKeyboard(DriverReChangeActivity.this);
                    }
                });


                break;
            case 1:

                break;

        }
    }

    public void getWxOrderInfo(Map<String,Object> map,final HttpCallBack back) {
        String url = util.getUrl(R.string.prepayCompanyGenerateWX);
        XUtil.Post(url, map, new MyCallBack<WxPayInfoData>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                back.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(WxPayInfoData arg0) {
                if(arg0.getResult() == 1) {
                    WxPayInfoData.DataBean data = arg0.getData();

                    back.onSuccess(data);
                } else {
                    back.onFail(arg0.getResult(), arg0.getMessage(), false);
                }
            }



        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.record:
                Intent intent = new Intent(this,ReChangeRecordActivity.class);
                startActivity(intent);
                break;
        }
        return  true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rechang,menu);

        return true;
    }
}

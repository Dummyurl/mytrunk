package com.car.portal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.car.portal.R;
import com.car.portal.adapter.ReChangeRecordAdapter;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.RechangeRecordData;
import com.car.portal.entity.RechangeRecordTypeData;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.XUtil;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.view.BaseTitleView;
import com.car.portal.view.DividerItemDecoration;
import com.google.gson.internal.LinkedTreeMap;
import com.orhanobut.hawk.Hawk;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReChangeRecordActivity extends AppCompatActivity {
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;
    protected MyHttpUtil util;
    private int uid;
    private List<RechangeRecordData.DataBean> list;
    private List<RechangeRecordTypeData.DataBean> typeList;
    private ReChangeRecordAdapter recordAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechange_record);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("充值记录");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab!=null;
        ab.setDisplayHomeAsUpEnabled(true);



        uid=  Integer.parseInt(Hawk.get("uid").toString());
        util = new MyHttpUtil(this);
        x.view().inject(this);
        InitRecyclerView();



        getRechangeTypes( new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                typeList.addAll((ArrayList<? extends RechangeRecordTypeData.DataBean>) objects[0]);
                ObtainReChangeData();

            }
        });




    }

    private void ObtainReChangeData() {
        Map<String,String> map=new HashMap<>();
        map.put("userId",uid+"");
        getRecordData(map, new HttpCallBack(ReChangeRecordActivity.this) {
            @Override
            public void onSuccess(Object... objects) {

                list.addAll((ArrayList<? extends RechangeRecordData.DataBean>) objects[0]);
                recordAdapter.notifyDataSetChanged();
            }
        });
    }

    private void InitRecyclerView() {
        list=new ArrayList<>();
        typeList=new ArrayList<>();
        recordAdapter=new ReChangeRecordAdapter(R.layout.item_rechange_record,list,typeList);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empyt_view_check_goods, null);
        recordAdapter.setEmptyView(emptyView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,layoutManager));
        recyclerView.setAdapter(recordAdapter);
    }

    public void getRecordData(Map<String,String> map,final HttpCallBack back) {
        String url = util.getUrl(R.string.findRechangeForUser);
        XUtil.Post(url, map, new MyCallBack<BaseEntity<ArrayList<LinkedTreeMap<String, Object>>>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                back.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<ArrayList<LinkedTreeMap<String, Object>>> arg0) {
                if(arg0.getResult() == 1) {
                    List<LinkedTreeMap<String, Object>> list = arg0.getData();
                    List<RechangeRecordData.DataBean> recordData  = new ArrayList<>();
                    if(list != null) {
                        for (LinkedTreeMap<String, Object> map : list) {
                            RechangeRecordData.DataBean data = new RechangeRecordData.DataBean();
                            LinkMapToObjectUtil.getObject(map, data);
                            recordData.add(data);
                        }
                    }
                    back.onSuccess(recordData);
                } else {
                    back.onFail(arg0.getResult(), arg0.getMessage(), false);
                }
            }
        });
    }


    public void getRechangeTypes(final HttpCallBack back) {
        String url = util.getUrl(R.string.findRechangeOfTypes);
        XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList<LinkedTreeMap<String, Object>>>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                back.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<ArrayList<LinkedTreeMap<String, Object>>> arg0) {
                if(arg0.getResult() == 1) {
                    List<LinkedTreeMap<String, Object>> list = arg0.getData();
                    List<RechangeRecordTypeData.DataBean> recordData  = new ArrayList<>();
                    if(list != null) {
                        for (LinkedTreeMap<String, Object> map : list) {
                            RechangeRecordTypeData.DataBean data = new RechangeRecordTypeData.DataBean();
                            LinkMapToObjectUtil.getObject(map, data);
                            recordData.add(data);
                        }
                    }
                    back.onSuccess(recordData);
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
        }
        return  true;
    }

}

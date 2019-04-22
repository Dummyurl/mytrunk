package com.car.portal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import com.car.portal.R;
import com.car.portal.adapter.MyFindTabAdapter.MyFindTabAdapter;
import com.car.portal.entity.Qrcodebeen;
import com.car.portal.fragment.CustSumFragment;
import com.car.portal.fragment.ProcomRechargeFragment;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.XUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 2019年1月
 * 用户推广成果显示页面
 */
public class ProcommissActivty extends AppCompatActivity {
    @BindView(R.id.procommiss_tab)
    TabLayout mTab;
    //    @BindView(R.id.operation)
//    TextView operation;
    @BindView(R.id.Details_FinacialVp)
    ViewPager mVp;
    //    自定义Tab适配器
    private MyFindTabAdapter mFindTabAdapter;
    private List<Fragment> list_fragment;                                //定义要装fragment的列表
    private List<String> list_title;                                     //tab名称列表
    private CustSumFragment mCustSumFragment;
    private ProcomRechargeFragment mProcomRechargeFragment;
    private String type = "1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.procommiss);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("推广成果");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);


        mCustSumFragment = new CustSumFragment();
        mProcomRechargeFragment = new ProcomRechargeFragment();
        list_fragment = new ArrayList<>();
        list_fragment.add(mCustSumFragment);
        list_fragment.add(mProcomRechargeFragment);


        list_title = new ArrayList<>();
        list_title.add(getString(R.string.promot_fragment_one));
        list_title.add(getString(R.string.promot_fragment_two));


        //为TabLayout添加tab名称
        mTab.addTab(mTab.newTab().setText(list_title.get(0)));
        mTab.addTab(mTab.newTab().setText(list_title.get(1)));


        mFindTabAdapter = new MyFindTabAdapter(getSupportFragmentManager(), list_fragment, list_title);
        //viewpager加载adapter
        mVp.setAdapter(mFindTabAdapter);
        //tab_FindFragment_title.setViewPager(vp_FindFragment_pager);
        //TabLayout加载viewpager
        mTab.setupWithViewPager(mVp);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}


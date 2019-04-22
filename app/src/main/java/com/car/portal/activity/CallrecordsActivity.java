package com.car.portal.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.car.portal.R;
import com.car.portal.adapter.CallrecodesAdapter;
import com.car.portal.entity.CallLogBean;
import com.car.portal.entity.CallrecordEntity;
import com.car.portal.service.UserService;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallrecordsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recy_callcords)
    RecyclerView recyCallcords;
    List<CallrecordEntity> list = new ArrayList<>();
    CallrecodesAdapter adapter;
    UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callrecords);
        ButterKnife.bind(this);
        userService = new UserService(CallrecordsActivity.this);
        inittooler();
        initdata();
    }

    private void initdata() {
        try {
            List<CallLogBean> callLogBean =  userService.queryCallbeans();
        } catch (DbException e) {
            e.printStackTrace();
        }
        for (int i = 0;i<10;i++){
            CallrecordEntity callrecordEntity = new CallrecordEntity();
            callrecordEntity.setCompanyname("海南敏捷");
            callrecordEntity.setHeadimg(R.drawable.head_img);
            callrecordEntity.setTel("18569033041");
            callrecordEntity.setTime("2019-3-12 3:20");
            callrecordEntity.setUsername("树叶");
            list.add(callrecordEntity);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(CallrecordsActivity.this, 1);
        recyCallcords.setLayoutManager(layoutManager);
        adapter = new CallrecodesAdapter(list);
        recyCallcords.setAdapter(adapter);
    }

    private void inittooler() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("联系记录");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab!=null;
        ab.setDisplayHomeAsUpEnabled(true);

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

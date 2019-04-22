package com.car.portal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.car.portal.R;
import com.car.portal.adapter.MybankcardAdapter;
import com.car.portal.contract.MybankcardContract;
import com.car.portal.entity.Mybankcardbeen;
import com.car.portal.presenter.MybankcardPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MybankcardActivity extends AppCompatActivity implements MybankcardContract.View {
    @BindView(R.id.recycler_mybank)
    RecyclerView recyclerMybank;
    List<Mybankcardbeen> list = new ArrayList<>();
    MybankcardAdapter adapter;
    @BindView(R.id.btn_save)
    FloatingActionButton btnSave;
    private MybankcardContract.Presenter mpresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybankcard);
        ButterKnife.bind(this);
        mpresenter = new MybankcardPresenter(this);
        inittooler();
        initdata();
    }

    private void initdata() {
       // mpresenter.getdata();
    }


    private void inittooler() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("银行卡");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab != null;

        ab.setDisplayHomeAsUpEnabled(true);
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

    @Override
    public void setPresenter(MybankcardContract.Presenter presenter) {
        mpresenter = presenter;
    }

    @Override
    public void showdata(List<Mybankcardbeen> list) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerMybank.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerMybank.setLayoutManager(layoutManager);
        adapter = new MybankcardAdapter(list);
        recyclerMybank.setAdapter(adapter);
    }

    @OnClick(R.id.btn_save)
    public void onViewClicked() {
        Intent intent = new Intent(MybankcardActivity.this, PasswordcheckActivity.class);
        startActivity(intent);
    }
}

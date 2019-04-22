package com.car.portal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.car.portal.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener{
    @ViewInject(R.id.line_orders)
    private RelativeLayout line_orders;
    @ViewInject(R.id.line_money)
    private RelativeLayout line_money;
    @ViewInject(R.id.line_center_money)
    private RelativeLayout line_center_money;
    @ViewInject(R.id.line_phone)
    private RelativeLayout line_phone;
    @ViewInject(R.id.line_inter_phone)
    private RelativeLayout line_inter_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        x.view().inject(this);
        line_center_money.setOnClickListener(this);
        line_inter_phone.setOnClickListener(this);
        line_money.setOnClickListener(this);
        line_orders.setOnClickListener(this);
        line_phone.setOnClickListener(this);
        
        inittooler();
    }

    private void inittooler() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("财务管理");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab!=null;

        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.line_center_money:
                intent = new Intent(StatisticsActivity.this, CommissionActivity.class);	// 
                startActivity(intent);
                break;
            case R.id.line_inter_phone:
                intent = new Intent(StatisticsActivity.this, NetPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.line_orders:
                intent = new Intent(StatisticsActivity.this, OrdersActivity.class);
                startActivity(intent);
                break;
            case R.id.line_phone:
                intent = new Intent(StatisticsActivity.this, CallsActivity.class);
                startActivity(intent);
                break;
            case R.id.line_money:
                intent = new Intent(StatisticsActivity.this, MoneyActivity.class);
                startActivity(intent);
                break;
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
}

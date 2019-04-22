package com.car.portal.activity;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import com.car.portal.R;
import com.car.portal.entity.Company;
import com.car.portal.view.BaseTitleView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CompanyDetailActivity extends Activity {

    @ViewInject(R.id.com_detail_name)
    private TextView name;
    @ViewInject(R.id.com_detail_legal)
    private TextView legal;
    @ViewInject(R.id.com_detail_phone)
    private TextView phone;
    @ViewInject(R.id.com_detail_tel)
    private TextView tel;
    @ViewInject(R.id.com_detail_fax)
    private TextView fax;
    @ViewInject(R.id.com_detail_addr)
    private TextView address;
    @ViewInject(R.id.com_detail_intro)
    private TextView introduce;
    @ViewInject(R.id.com_detail_title)
    private BaseTitleView titleView;
    @ViewInject(R.id.com_detail_back)
    private Button back;
    @ViewInject(R.id.com_detail_goods)
    private Button goods;

    private Company company;
    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_detail);
        x.view().inject(CompanyDetailActivity.this);
        back.setOnClickListener(backListener);
        titleView.setTitle(getResources().getString(R.string.title_activity_company_detail));
        company = (Company) getIntent().getParcelableExtra("company");
        if (company == null) {
            finish();
        } else {
            init();
        }
    }

    private void init() {
        name.setText(company.getName());
        legal.setText(company.getLegaler());
        phone.setText(company.getTel());
        tel.setText(company.getOffice_tel());
        fax.setText(company.getFax());
        address.setText(company.getAddress());
        introduce.setText(company.getDecs());
        goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CompanyDetailActivity.this, GoodsListActivity.class);
//                intent.putExtra("company", company);
//                startActivity(intent);
            }
        });
    }
}

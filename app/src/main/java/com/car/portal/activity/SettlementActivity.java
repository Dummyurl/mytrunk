package com.car.portal.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.SettlementAdapter;
import com.car.portal.entity.CountConfirm;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.ContractService;
import com.car.portal.view.BaseTitleView;

@SuppressLint("UseSparseArrays")
public class SettlementActivity extends AppCompatActivity {
    @ViewInject(R.id.settlement_list)
    private ListView list_settlement;
    @ViewInject(R.id.txt_no_data)
    private TextView txt_no_data;
    private ContractService service;
    private SettlementAdapter pairedListAdapter;
    private PopupWindow popupWindow = null;
    private View popupView;
    private TextView btn_center, btn_cancel;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        view = LayoutInflater.from(this).inflate(R.layout.activity_settlement, null);
        service = new ContractService(this);
        initTitle();
    }

    /**
     * 初始化标题
     */
    public void initTitle() {
        x.view().inject(SettlementActivity.this);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("结算确认");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab!=null;

        ab.setDisplayHomeAsUpEnabled(true);


        pairedListAdapter = new SettlementAdapter(this);
        list_settlement.setAdapter(pairedListAdapter);
        list_settlement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final CountConfirm confirm = (CountConfirm) pairedListAdapter.getItem(position);
                showPopup(confirm);
//                service.endGood(confirm.getId(), new HttpCallBack(SettlementActivity.this) {
//                    @Override
//                    public void onSuccess(Object... objects) {
//                        pairedListAdapter.removeValue(confirm);
//                        pairedListAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void retry() {
//                        service.endGood(confirm.getId(), this);
//                    }
//                });
            }
        });
        params = new HashMap<String, Object>();
        params.put("t", System.currentTimeMillis());
        service.getCountConfirm(params, callBack);
    }

    /**
     * 初始化popupWindow
     */
    public void initPopup(){
        popupView = LayoutInflater.from(this).inflate(R.layout.popu_isadd_order, null);
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        TextView txt_message = (TextView) popupView.findViewById(R.id.txt_message);
        txt_message.setText("是否确认结算？");
        btn_cancel = (TextView) popupView.findViewById(R.id.txt_cencel);
        btn_center = (TextView) popupView.findViewById(R.id.txt_save);
        popupView.getBackground().setAlpha(100);
    }

    /**
     * 显示popupWindow
     * @param confirm
     */
    public void showPopup(final CountConfirm confirm){
        initPopup();
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btn_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.endGood(confirm.getId(), new HttpCallBack(SettlementActivity.this) {
                    @Override
                    public void onSuccess(Object... objects) {
                        pairedListAdapter.removeValue(confirm);
                        pairedListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER, 0, 0);
    }
    
    private Map<String, Object> params;

    private HttpCallBack callBack = new HttpCallBack(this) {
        @Override
        public void onSuccess(Object... objects) {
            if (objects != null && objects.length > 0) {
                @SuppressWarnings("unchecked")
                List<CountConfirm> list = (List<CountConfirm>) objects[0];
                if (list.size() > 0) {
                    pairedListAdapter.setData(list);
                    pairedListAdapter.notifyDataSetChanged();
                    txt_no_data.setVisibility(View.GONE);
                } else {
                txt_no_data.setVisibility(View.VISIBLE);
            }
            }
        }
    };

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

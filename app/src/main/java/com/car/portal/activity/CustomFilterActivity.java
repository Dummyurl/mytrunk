package com.car.portal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.adapter.FlowAdapter.FlowAdapter;
import com.car.portal.util.KeyBoardUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class CustomFilterActivity extends Activity implements View.OnClickListener, View.OnTouchListener {


    @ViewInject(R.id.use_type)
    TagFlowLayout useTypeFlow;
    @ViewInject(R.id.car_long)
    TagFlowLayout carLongFlow;
    @ViewInject(R.id.car_type)
    TagFlowLayout carTypeFlow;


    private int useType;
    private int carLong;
    private int carType;


    private FlowAdapter carLongAdapter;
    private FlowAdapter carTypeAdapter;

    @ViewInject(R.id.custom_long)
    EditText customLong;
    @ViewInject(R.id.car_long_parent)
    LinearLayout carLongParent;
    @ViewInject(R.id.confirm)
    Button confirm;
    private ArrayList<String> carLongList;
    private ArrayList<String> carTypeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_filter);
        x.view().inject(this);
        InitUseTypeData();
        InitCarLongData();
        InitCarTypeData();
        confirm.setOnClickListener(this);
        customLong.setOnTouchListener(this);

    }

    private void InitUseTypeData() {
        final List<String> userTypeList = new ArrayList<>();
        userTypeList.add("整车");
        userTypeList.add("零担");

        useTypeFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                ClearInputFocus();
                useType = position;
                return false;
            }
        });
        FlowAdapter useTypeAdapter = new FlowAdapter(userTypeList, useTypeFlow, this);
        useTypeFlow.setMaxSelectCount(1);
        useTypeFlow.setAdapter(useTypeAdapter);
    }

    private void InitCarLongData() {
        carLongList = new ArrayList<>();
        carLongList.add("1.8");
        carLongList.add("4.2");
        carLongList.add("5");
        carLongList.add("6.8");
        carLongList.add("7.7");
        carLongList.add("8.2");
        carLongList.add("9.6");
        carLongList.add("全部");


        carLongFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (carLongList.get(position).equals("全部")){
                    showAllCarLongData();
                    return false;
                }
                customLong.setText("");
                ClearInputFocus();
                KeyBoardUtils.hideKeyboard(CustomFilterActivity.this);
                carLong = position+1;
                flag = position;
                return false;
            }
        });
        carLongAdapter = new FlowAdapter(carLongList, carLongFlow, this);
        carLongFlow.setMaxSelectCount(1);
        carLongFlow.setAdapter(carLongAdapter);
    }


    private void InitCarTypeData() {
        carTypeList = new ArrayList<>();
        carTypeList.add("高低板");
        carTypeList.add("高栏");
        carTypeList.add("冷藏柜");
        carTypeList.add("平板");
        carTypeList.add("高低高");
        carTypeList.add("货柜");
        carTypeList.add("超低板");
        carTypeList.add("全部");


        carTypeFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (carTypeList.get(position).equals("全部")){
                    showAllCarTypeData();
                    return false;
                }
                ClearInputFocus();
                carType = position+1;
                return false;
            }
        });
        carTypeAdapter = new FlowAdapter(carTypeList, carTypeFlow, this);
        carTypeFlow.setMaxSelectCount(1);
        carTypeFlow.setAdapter(carTypeAdapter);
    }

    private void ClearInputFocus() {
        carLongParent.setFocusableInTouchMode(true);
        carLongParent.setFocusable(true);
        carLongParent.requestFocus();
    }

    private int flag = -1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                //CheckDataNull(carLong,carType,useType);

                String customLongStr = customLong.getText().toString();
                if (!customLongStr.isEmpty())
                    carLong = 11;


                Intent intent = getIntent();
                intent.putExtra("useType", useType);
                intent.putExtra("carLong", carLong);
                intent.putExtra("carType", carType);
                setResult(110, intent);
                finish();
                break;

        }
    }

    private void CheckDataNull(String... str) {
        for (int i = 0; i < str.length; i++) {
            if (str[i].isEmpty()){
                Toast.makeText(this, "请确保每个选项至少选中一个", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.custom_long:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(flag>=0){
                        carLongFlow.getChildAt(flag).performClick();
                        flag = -1;
                    }
                }

                break;
        }

        return false;
    }



    public void showAllCarLongData(){
        carLong=0;
        carLongList.clear();
        carLongList.add("1.8");
        carLongList.add("4.2");
        carLongList.add("5");
        carLongList.add("6.8");
        carLongList.add("7.7");
        carLongList.add("8.2");
        carLongList.add("9.6");
        carLongList.add("11.7");
        carLongList.add("13");
        carLongList.add("13.5");
        carLongList.add("17.5");
        carLongList.add("18");

        carLongAdapter.notifyDataChanged();

        carLongParent.setVisibility(View.VISIBLE);
    }

    public void showAllCarTypeData(){
        carType=0;
        carTypeList.clear();
        carTypeList.add("高低板");
        carTypeList.add("高栏");
        carTypeList.add("冷藏柜");
        carTypeList.add("平板");
        carTypeList.add("高低高");
        carTypeList.add("货柜");
        carTypeList.add("超低板");
        carTypeList.add("自卸车");
        carTypeList.add("高栏平板");
        carTypeList.add("高栏高低板");

        carTypeAdapter.notifyDataChanged();
    }


}

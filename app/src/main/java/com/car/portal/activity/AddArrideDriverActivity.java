package com.car.portal.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.car.portal.R;
import com.car.portal.datepicker.DatePickDialog;
import com.car.portal.entity.CarArrived;
import com.car.portal.entity.City;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;

public class AddArrideDriverActivity extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(R.id.base_title_view)
    private BaseTitleView titleView;
    @ViewInject(R.id.txt_arrive_date)
    private TextView txt_arrive_date;
    @ViewInject(R.id.txt_note_date)
    private TextView txt_note_date;
    @ViewInject(R.id.txt_unloading_point)
    private TextView txt_unloading_point;
    @ViewInject(R.id.txt_target_point)
    private TextView txt_target_point;
    @ViewInject(R.id.edit_remark)
    private EditText edit_remark;
    @ViewInject(R.id.line_ispark)
    private LinearLayout line_ispark;
    @ViewInject(R.id.line_isloading)
    private LinearLayout line_isloading;
    @ViewInject(R.id.txt_send)
    private TextView txt_send;
    @ViewInject(R.id.txt_save)
    private TextView txt_save;
    @ViewInject(R.id.img_ispark)
    private ImageView img_ispark;
    @ViewInject(R.id.img_unloading)
    private ImageView img_unloading;
    @ViewInject(R.id.img_delete)
    private ImageView img_delete;
    private Intent last_intent;
    private boolean isPark = false, isLoad = false;
    private DriverService driverService;
    private String inLoc, intLocN, outLocN;
    private int outLoc;
    private StringBuffer buffer;
    private DatePickDialog datePickDialog;
    private static final String DATESPLIT = "-";
    private String arriveTime="";
    private ArrayList<City> arriveList;
    TimePickerView pvTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_arride_driver);
        x.view().inject(this);
        last_intent = getIntent();
        buffer = new StringBuffer();
        driverService = new DriverService(this);
        initTitle();
        initListener();
        txt_note_date.setText(formatDate());
        txt_arrive_date.setText(arriveTime);
    }

    /**
     * 得到当前时间
     *
     * @return
     */
    public String formatDate() {
        String time = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(System.currentTimeMillis());
        time = simpleDateFormat.format(date);
        return time;
    }

    /**
     * 初始化事件监听
     */
    public void initListener() {
        txt_unloading_point.setOnClickListener(this);
        txt_target_point.setOnClickListener(this);
        line_ispark.setOnClickListener(this);
        line_isloading.setOnClickListener(this);
        txt_send.setOnClickListener(this);
        txt_save.setOnClickListener(this);
        txt_arrive_date.setOnClickListener(this);
        img_delete.setOnClickListener(this);
    }

    /**
     * 初始化标题
     */
    public void initTitle() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("添加司机到达");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab!=null;

        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_arrive_date:
                //新的时间选择
                Calendar selectedDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                startDate.set(2014, 1, 1);
                Calendar endDate = Calendar.getInstance();
                endDate.set(2050, 12, 31);
                pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        txt_arrive_date.setText(getTime(date));
                    }

                })       .setDate(selectedDate)
                        .setRangDate(startDate,endDate)
                        .build();
                pvTime.show();
                break;
            case R.id.txt_unloading_point:
                selectDepart();
                break;
            case R.id.txt_target_point:
                selectArrive();
                break;
            case R.id.line_isloading:
                isLoad = !isLoad;
                if (isLoad)
                    img_unloading.setBackgroundResource(R.drawable.checked);
                else
                    img_unloading.setBackgroundResource(R.drawable.unchecked);
                break;
            case R.id.line_ispark:
                isPark = !isPark;
                if (isPark)
                    img_ispark.setBackgroundResource(R.drawable.checked);
                else
                    img_ispark.setBackgroundResource(R.drawable.unchecked);
                break;
            case R.id.txt_send:
                if ((txt_unloading_point != null && !txt_unloading_point.getText().toString()
                        .equals("")) && (txt_note_date != null && !txt_note_date.getText()
                        .toString().equals("")) && (txt_arrive_date != null && !txt_arrive_date
                        .getText().toString().equals("")) && (txt_target_point != null &&
                        !txt_target_point.getText().toString().equals(""))) {
                    updataAdrrived();
                } else {
                    ToastUtil.show("请填写完整的信息", this);
                }
                break;
            case R.id.img_delete:
                    txt_target_point.setText("");
                break;
            case R.id.txt_save:
                break;
        }
    }

    private String getTime(Date date) {
        String format  = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }


    /**
     * 得到填写的数据
     *
     * @return
     */
    public CarArrived getData() {
        CarArrived carArrived = new CarArrived();
        carArrived.setDriverId(last_intent.getIntExtra("driverId", 0));
        carArrived.setDriverName(last_intent.getStringExtra("driverName"));
        carArrived.setDriver_owner(last_intent.getIntExtra("driver_owner", 0));
        carArrived.setArriveDate(txt_arrive_date.getText().toString());
        carArrived.setForce_add(0);
        carArrived.setRecordDate(txt_note_date.getText().toString());
        carArrived.setOwen(last_intent.getIntExtra("driver_owner", 0));
        carArrived.setNowLocation(outLoc + "");
        carArrived.setNowLocationC(outLocN + "");
        carArrived.setTargetLocation(inLoc);
        carArrived.setUserId(last_intent.getIntExtra("userId",0)+"");
        carArrived.setTargetLocationC(txt_target_point.getText().toString());
        if (edit_remark != null && !edit_remark.getText().toString().equals(""))
            carArrived.setMemo(edit_remark.getText().toString());
        else
            carArrived.setMemo("");
        if (isPark)
            carArrived.setInStop("on");
        else
            carArrived.setInStop("off");
        if (isLoad)
            carArrived.setGoodsOnCar("1");
        else
            carArrived.setGoodsOnCar("0");
        return carArrived;
    }

    /**
     * 保存并上传司机到达
     */
    public void updataAdrrived() {
        driverService.uploadAdrrivedDriver(getData(), new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null) {
                    ToastUtil.show("保存并上传司机到达成功！", AddArrideDriverActivity.this);
                    finish();
                } else
                    ToastUtil.show("保存并上传司机到达失败！", AddArrideDriverActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AddOrderActivity.SELECT_CITY && resultCode == SelectCityActivity.RESPONSE_MUTIPLE_CODE) {
            arriveList = data.getParcelableArrayListExtra("cities");
            if(arriveList != null && arriveList.size() > 0) {
                StringBuffer bf = new StringBuffer();
                StringBuffer in = new StringBuffer();
                for (City city : arriveList) {
                    bf.append(city.getShortName());
                    bf.append(",");
                    in.append(city.getCid());
                    in.append(",");
                }
                bf.deleteCharAt(bf.length() - 1);
                inLoc = in.deleteCharAt(in.length()-1).toString();
                if(bf.length() > 13) {
                    txt_target_point.setText(bf.subSequence(0, 12) + "…");
                } else {
                    txt_target_point.setText(bf.toString());
                }
                intLocN =txt_target_point.getText().toString();
            }
        } else if(requestCode == AddOrderActivity.SELECT_CITY && resultCode == SelectCityActivity.RESPONSE_SINGLE_CODE){
            City c = data.getParcelableExtra("city");
            if(c != null && c.getShortName() != null) {
                txt_unloading_point.setText(c.getShortName());
            }
            outLocN = txt_unloading_point.getText().toString();
            outLoc = c.getCid();
        }
    }

    protected void selectArrive() {
        Intent intent = new Intent(this, SelectCityActivity.class);
        intent.putExtra("minLevel", 0);
        intent.putExtra("maxLevel", 4);
        intent.putExtra("isMutiple", true);
        intent.putParcelableArrayListExtra("selectedCities", arriveList);
        startActivityForResult(intent, AddOrderActivity.SELECT_CITY);
    }

    protected void selectDepart() {
        Intent intent = new Intent(this, SelectCityActivity.class);
        intent.putExtra("minLevel", 0);
        intent.putExtra("maxLevel", 4);
        intent.putExtra("isMutiple", false);
        startActivityForResult(intent, AddOrderActivity.SELECT_CITY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}

package com.car.portal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.City;
import com.car.portal.entity.MobileList;
import com.car.portal.entity.PhoneExit;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddDriverActivity extends Activity implements View.OnClickListener{
    @ViewInject(R.id.edit_name)
    private EditText edit_name;
    @ViewInject(R.id.edit_id)
    private EditText edit_id;
    @ViewInject(R.id.edit_start_point)
    private TextView edit_start_point;
    @ViewInject(R.id.edit_target_point)
    private TextView edit_target_point;
    @ViewInject(R.id.edit_mobile_phone)
    private EditText edit_mobile_phone;
    @ViewInject(R.id.btn_center)
    private Button btn_center;
    @ViewInject(R.id.base_title_view)
    private BaseTitleView base_title_view;
    @ViewInject(R.id.edit_memo)
    private EditText edit_memo;
    @ViewInject(R.id.txt_provice)
    private TextView txt_provice;
    @ViewInject(R.id.txt_city)
    private TextView txt_city;
    private DriverService driverService = null;
    private ArrayList<City> arriveList, arrayList2;
    private String intLocN, inLoc, outLoc, outLocN, inCode, outCode;
    private final int START_CITY = 0x1020;
    private final int TARGET_CITY = 0x1021;
    private boolean phone_isExit = true;

    private CharSequence temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);
        x.view().inject(this);
        base_title_view.setTitle(getResources().getString(R.string.title_add_driver));
        driverService = new DriverService(this);
        edit_start_point.setOnClickListener(this);
        edit_target_point.setOnClickListener(this);
        btn_center.setOnClickListener(this);
        phoneListener();
    }

    /**
     * 选择出发城市
     */
    protected void selectDepart() {
        Intent intent = new Intent(this, SelectCityActivity.class);
        intent.putExtra("minLevel", 0);
        intent.putExtra("maxLevel", 2);
        intent.putExtra("isMutiple", true);
        intent.putParcelableArrayListExtra("selectedCities", arriveList);
        startActivityForResult(intent, START_CITY);
    }

    /**
     * 选择到达城市
     */
    protected void selectArrive() {
        Intent intent = new Intent(this, SelectCityActivity.class);
        intent.putExtra("minLevel", 0);
        intent.putExtra("maxLevel", 2);
        intent.putExtra("isMutiple", true);
        intent.putParcelableArrayListExtra("selectedCities", arrayList2);
        startActivityForResult(intent, TARGET_CITY);
    }

    public void getData(){
        String driverName = edit_name.getText().toString();
        String driverId = edit_id.getText().toString();
        String start_point = edit_start_point.getText().toString();
        String target_point = edit_target_point.getText().toString();
        String phoneNum = edit_mobile_phone.getText().toString();
        String memo = edit_memo.getText().toString();
        if (!StringUtil.isIDCardValidate(driverId).equals("")){
            ToastUtil.show("您的身份证号不正确", AddDriverActivity.this);
            edit_id.requestFocus();
            return;
        } else {
            if (!driverName.equals("") && !driverId.equals("") && !target_point.equals("") && !start_point.equals("") && !phoneNum.equals("") && !phone_isExit) {

                Intent intent = new Intent(AddDriverActivity.this, AddDriverInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("driver.meno", memo);
                bundle.putString("driver.province", txt_provice.getText().toString());//手机号归属地省份
                bundle.putString("driver.city_name", txt_city.getText().toString());//手机号归属地城市
                bundle.putInt("driver.phone_valid", 0);
                bundle.putString("driver.tels", phoneNum);
                bundle.putString("driver.aspect", start_point);
                bundle.putString("driver.aspectN", outLocN);//出发城市cid
                bundle.putString("driver.aspectCode", outCode);//出发点城市Code
                bundle.putString("driver.route", target_point);
                bundle.putString("driver.routeN", intLocN);
                bundle.putString("driver.routeCode", inCode);
                bundle.putString("driver.identityCard", driverId);
                bundle.putString("driver.name", driverName);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            } else {
                ToastUtil.show("请填写完整的信息！", this);
            }
        }
    }

    /**
     * 保存填写的数据并创建新的司机
     */
    public void saveData(){
        String driverName = edit_name.getText().toString();
        String driverId = edit_id.getText().toString();
        String start_point = edit_start_point.getText().toString();
        String target_point = edit_target_point.getText().toString();
        String phoneNum = edit_mobile_phone.getText().toString();
        String memo = edit_memo.getText().toString();
        if (!driverName.equals("") && !driverId.equals("")  && !target_point.equals("") && !start_point.equals("") && !phoneNum.equals("") && !phone_isExit ){
            final Map<String, Object> map = new HashMap<>();
            map.put("driver.meno", memo);
            map.put("driver.province", txt_provice.getText().toString());//手机号归属地省份
            map.put("driver.city_name", txt_city.getText().toString());//手机号归属地城市
            map.put("driver.phone_valid", 0);
            map.put("driver.tels", phoneNum);
            map.put("driver.aspect", start_point);
            map.put("driver.aspectN", outLocN);//出发城市cid
            map.put("driver.aspectCode", outCode);//出发点城市Code
            map.put("driver.route", target_point);
            map.put("driver.routeN", intLocN);
            map.put("driver.routeCode", inCode);
            map.put("driver.identityCard", driverId);
            map.put("driver.name", driverName);

            driverService.addDriver(map, new HttpCallBack(this) {
                @Override
                public void onSuccess(Object... objects) {
                    String numstr = (String) objects[0];
                    int num = Integer.parseInt(numstr.substring(numstr.length() - 2, numstr.length()-1));
                    if (num > 0){
                        ToastUtil.show("创建成功！", AddDriverActivity.this);
                    } else {
                        ToastUtil.show("创建失败！", AddDriverActivity.this);
                    }
                }
            });
        } else {
            ToastUtil.show("请填写完整的信息！", this);
        }
    }

    /**
     * 查询手机号码归属地
     * @param phone
     */
    public void queryPhoneHome(final String phone){
        driverService.queryPhone(phone, new HttpCallBack(this) {
            @SuppressWarnings("unchecked")
			@Override
            public void onSuccess(Object... objects) {
                if (objects != null) {
                    ArrayList<MobileList> mobileList = (ArrayList<MobileList>) objects[0];
                    if (mobileList != null && mobileList.size() > 0) {
                        txt_city.setText(mobileList.get(0).getCityname());
                    } else {
                        txt_city.setText("未知");
                    }
                    if (mobileList.get(0).getProvince() != null && !mobileList.get(0).getProvince
                            ().equals("")) {
                        txt_provice.setText(mobileList.get(0).getProvince());
                    } else {
                        txt_provice.setText("未知");
                    }

                } else {
                    txt_provice.setText("未知");
                    txt_city.setText("未知");
                }
            }
        });
    }

    /**
     * 手机号是否注册过
     * @param phone
     * @return
     */
    public boolean isPhoneExit(final String phone){
        driverService.isPhoneExit(phone, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null){
                    PhoneExit phoneExit = (PhoneExit) objects[0];
                    if (phoneExit.getDriver() != null){
                        ToastUtil.show("该手机号已被注册，请换其他手机号或直接登录", AddDriverActivity.this);
                    }else {
                        if (phone.length() == 11) {
                            queryPhoneHome(phone);
                            phone_isExit = false;
                        }
                    }
                }
            }
        });
        return phone_isExit;
    }

    /**
     * 监听phone的改变事件
     */
    public void phoneListener(){
        temp = null;
        edit_mobile_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11 || s.length() == 8) {
                    isPhoneExit(s.toString());
                } else if (s.length() > 11) {
//                    s = s.delete(10, s.length() - 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TARGET_CITY && resultCode == SelectCityActivity.RESPONSE_MUTIPLE_CODE) {
            arrayList2 = data.getParcelableArrayListExtra("cities");
            if(arrayList2 != null && arrayList2.size() > 0) {
                StringBuffer bf = new StringBuffer();
                StringBuffer in = new StringBuffer();
                StringBuffer cd = new StringBuffer();
                for (City city : arrayList2) {
                    bf.append(city.getShortName());
                    bf.append(",");
                    in.append(city.getCid());
                    in.append(",");
                    cd.append(city.getCode());
                    cd.append(",");
                }
                bf.deleteCharAt(bf.length() - 1);
                intLocN = in.deleteCharAt(in.length()-1).toString();
                inCode = cd.deleteCharAt(in.length() - 1).toString();
                if(bf.length() > 13) {
                    edit_target_point.setText(bf.subSequence(0, 12) + "…");
                } else {
                    edit_target_point.setText(bf.toString());
                }
                inLoc =edit_target_point.getText().toString();
            }
        } else if (requestCode == START_CITY && resultCode == SelectCityActivity.RESPONSE_MUTIPLE_CODE){
            arriveList = data.getParcelableArrayListExtra("cities");
            if(arriveList != null && arriveList.size() > 0) {
                StringBuffer bf = new StringBuffer();
                StringBuffer in = new StringBuffer();
                StringBuffer cd = new StringBuffer();
                for (City city : arriveList) {
                    bf.append(city.getShortName());
                    bf.append(",");
                    in.append(city.getCid());
                    in.append(",");
                    cd.append(city.getCode());
                    cd.append(",");
                }
                bf.deleteCharAt(bf.length() - 1);
                outLocN = in.deleteCharAt(in.length()-1).toString();
                outCode = cd.deleteCharAt(in.length() - 1).toString();
                if(bf.length() > 13) {
                    edit_start_point.setText(bf.subSequence(0, 12) + "…");
                } else {
                    edit_start_point.setText(bf.toString());
                }
                outLoc = edit_start_point.getText().toString();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_start_point:
                selectDepart();
                break;
            case R.id.edit_target_point:
                selectArrive();
                break;
            case R.id.btn_center:
//                saveData();
                getData();
                break;
        }
    }
}

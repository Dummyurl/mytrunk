package com.car.portal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.LinePopuGridAdapter;
import com.car.portal.adapter.RegisterPopuGridAdapter;
import com.car.portal.entity.BodyTypeList;
import com.car.portal.entity.CarType;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.service.GoodsService;
import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;

public class AddDriverInfoActivity extends Activity implements View.OnClickListener{
    @ViewInject(R.id.base_title_view)
    private BaseTitleView baseTitleView;
    @ViewInject(R.id.edit_mian_num)
    private EditText edit_mian_num;
    @ViewInject(R.id.edit_trailer_num)
    private EditText edit_trailer_num;
    @ViewInject(R.id.edit_car_length1)
    private EditText edit_car_length1;
    @ViewInject(R.id.edit_car_length2)
    private EditText edit_car_length2;
    @ViewInject(R.id.edit_car_width1)
    private EditText edit_car_width1;
    @ViewInject(R.id.edit_car_width2)
    private EditText edit_car_width2;
    @ViewInject(R.id.edit_load)
    private EditText edit_load;
    @ViewInject(R.id.txt_car_type)
    private TextView txt_car_type;
    @ViewInject(R.id.txt_operationor)
    private TextView txt_operationor;
    @ViewInject(R.id.txt_axis_count)
    private TextView txt_axis_count;
    @ViewInject(R.id.edit_height)
    private EditText edit_height;
    @ViewInject(R.id.btn_center)
    private Button btn_center;
    private PopupWindow popupWindow;
    private View popupView;
    private LinearLayout line_popu;
    private View view;
    private GridView grid_popu;
    private LinePopuGridAdapter linePopuGridAdapter = null;
    private RegisterPopuGridAdapter registerPopuGridAdapter = null;
    private int choose = 0;//1操作者，2出发地点，3目的地
    private GoodsService goodsService;
    private ArrayList<User> companyUsers = null;
    private int register_select = 0, type_select = 0, axit_select = 0;
    private ArrayList<String> arr_citys = null;
    private ArrayList<BodyTypeList> bodyTypeLists  = null;
    private int axis_id, type_id, register_id;
    private DriverService driverService = null;
    private List<CarType> types = null;
    private Map<String, Object> map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver_info);
        x.view().inject(this);
        view = LayoutInflater.from(this).inflate(R.layout.activity_add_driver, null);
        baseTitleView.setTitle(getResources().getString(R.string.title_add_carInfo));
        goodsService = new GoodsService(this);
        driverService = new DriverService(this);
        bodyTypeLists = new ArrayList<BodyTypeList>();
        companyUsers = new ArrayList<User>();
        map = new HashMap<>();
        initData();

        txt_operationor.setOnClickListener(this);
        txt_axis_count.setOnClickListener(this);
        txt_car_type.setOnClickListener(this);
        btn_center.setOnClickListener(this);
    }

    /**
     * 获取前一个页面传过来的数据
     */
    public void initData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        LogUtils.e("AddDriverInfo", "            memo:" + bundle.getString("driver.meno"));
        map.put("driver.meno", bundle.getString("driver.meno"));
        map.put("driver.province", bundle.getString("driver.province"));//手机号归属地省份
        map.put("driver.city_name", bundle.getString("driver.city_name"));//手机号归属地城市
        map.put("driver.phone_valid", bundle.getInt("driver.phone_valid"));
        map.put("driver.tels", bundle.getString("driver.tels"));
        map.put("driver.aspect", bundle.getString("driver.aspect"));
        map.put("driver.aspectN", bundle.getString("driver.aspectN"));//出发城市cid
        map.put("driver.aspectCode", bundle.getString("driver.aspectCode"));//出发点城市Code
        map.put("driver.route", bundle.getString("driver.route"));
        map.put("driver.routeN", bundle.getString("driver.routeN"));
        map.put("driver.routeCode", bundle.getString("driver.routeCode"));
        map.put("driver.identityCard", bundle.getString("driver.identityCard"));
        map.put("driver.name", bundle.getString("driver.name"));
    }

    /**
     * 保存数据
     */
    public void saveData(){
        String main_num = edit_mian_num.getText().toString();
        String trailer_num = edit_trailer_num.getText().toString();
        String length1 = edit_car_length1.getText().toString();
        String length2 = edit_car_length2.getText().toString();
        String width1 = edit_car_width1.getText().toString();
        String width2 = edit_car_width2.getText().toString();
        String load = edit_load.getText().toString();
        String car_type = txt_car_type.getText().toString();
        String axis_count = txt_axis_count.getText().toString();
        String operationor = txt_operationor.getText().toString();
        String height = edit_height.getText().toString();
        if ( !main_num.equals("") && !trailer_num.equals("") && !length1.equals("") && !length2.equals("") && !width1.equals("") && !width2.equals("")
                && !load.equals("") && !operationor.equals("") && !car_type.equals("") && !axis_count.equals("") && !height.equals("")) {
            //if (StringUtil.isCarNum(main_num) && StringUtil.isCarNum(trailer_num)) {
                map.put("driver.weight", load);
                map.put("driver.width", width1);
                map.put("driver.width2", width2);
                map.put("driver.length", length1);
                map.put("driver.length2", length2);
                map.put("driver.carId2", trailer_num);
                map.put("driver.carId", main_num);
                map.put("driver.types", axis_id);
                map.put("driver.bodyType", type_id);
                map.put("driver.hight", height);
                driverService.addDriver(map, new HttpCallBack(this) {
                    @Override
                    public void onSuccess(Object... objects) {
                        String numstr = (String) objects[0];
                        int num = Integer.parseInt(numstr.substring(numstr.length() - 2, numstr.length() - 1));

                        if (num > 0) {
                            ToastUtil.show("创建成功！", AddDriverInfoActivity.this);
                            startActivity(new Intent(AddDriverInfoActivity.this, DriverSearchActivity.class));
                            finish();
                        } else {
                            ToastUtil.show("创建失败！", AddDriverInfoActivity.this);
                        }
                    }
                });
           // } else {
           //     ToastUtil.show("您填写的车牌号不正确！", this);
           // }
        }else {
            ToastUtil.show("请填写完整的信息！", this);
        }
    }

    /* 展开路线分类/登记者
       */
    public void showPupoWindow(){
        initPopuWindow();

        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER, 0, 0);
    }

    /**
     * 初始化popupwindpw
     * choose  1操作员；2车厢类型；3轴数
     */
    public void initPopuWindow(){
        popupView= LayoutInflater.from(this).inflate(R.layout.popu_show_line,null);
        line_popu= (LinearLayout) popupView.findViewById(R.id.grid_bgLine_line);
        grid_popu= (GridView) popupView.findViewById(R.id.grid_popu);
        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        line_popu.getBackground().setAlpha(100);

        linePopuGridAdapter=new LinePopuGridAdapter(this);
        grid_popu.setAdapter(linePopuGridAdapter);
        registerPopuGridAdapter=new RegisterPopuGridAdapter(this);
        grid_popu.setAdapter(registerPopuGridAdapter);
        if(choose == 1){
            getCompanyUsers();
        } else if(choose == 2){
            getCarType();
        } else if (choose == 3){
            getAxitTypes();
        }

        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        grid_popu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (choose == 1) {
                    register_select = position;
                    register_id = companyUsers.get(position).getUid();
                    txt_operationor.setText(companyUsers.get(position).getCname());
                } else if (choose == 2) {
                    type_select = position;
                    txt_car_type.setText(bodyTypeLists.get(position).getName());
                    type_id = bodyTypeLists.get(position).getId();
                } else if (choose == 3) {
                    axit_select = position;
                    txt_axis_count.setText(types.get(position).getName());
                    axis_id = types.get(position).getId();
                } else
                    return;

                popupWindow.dismiss();
            }
        });
    }

    /**
     * 获取轴数信息
     */
    public void getAxitTypes(){
        types = new ArrayList<CarType>();
        arr_citys = new ArrayList<String>();
        driverService.getAxisType(new HttpCallBack(this) {
            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                    types = (List<CarType>) objects[0];
                    linePopuGridAdapter.setSelectId(axit_select);
                    for (int i = 0; i < types.size(); i++) {
                        arr_citys.add(types.get(i).getName());
                    }
                    linePopuGridAdapter.setList(arr_citys);
                    linePopuGridAdapter.notifyDataSetChanged();
                    grid_popu.setAdapter(linePopuGridAdapter);
                }
            }
        });
    }

    /**
     * 初始化选择车辆类型的popupWindow的数据
     */
    public void getCarType(){
        arr_citys=new ArrayList<String>();
        driverService.getCarType(new HttpCallBack(this) {
            @SuppressWarnings("unchecked")
			@Override
            public void onSuccess(Object... objects) {
                bodyTypeLists = (ArrayList<BodyTypeList>) objects[0];
//                carTypeAdapter.setList((ArrayList<BodyTypeList>) objects[0]);
                linePopuGridAdapter.setSelectId(type_select);
                for (int i = 0; i < bodyTypeLists.size(); i++) {
                    arr_citys.add(bodyTypeLists.get(i).getName());
                }
                linePopuGridAdapter.setList(arr_citys);
                linePopuGridAdapter.notifyDataSetChanged();
                grid_popu.setAdapter(linePopuGridAdapter);
            }
        });
    }

    /**
     * 获取公司成员信息
     */
    public void getCompanyUsers(){
        arr_citys=new ArrayList<String>();
        User u = goodsService.getLoginUser();
        int companyId = 0;
        if(u != null) {
        	companyId = u.getCompanyId();
        }
        goodsService.getGoods(companyId, new HttpCallBack(this) {
            @SuppressWarnings("unchecked")
			@Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                    companyUsers = (ArrayList<User>) objects[0];
                    linePopuGridAdapter.setSelectId(register_select);
                    for (int i = 0; i < companyUsers.size(); i++) {
                        arr_citys.add(companyUsers.get(i).getCname());
                    }
                    linePopuGridAdapter.setList(arr_citys);
                    linePopuGridAdapter.notifyDataSetChanged();
                    grid_popu.setAdapter(linePopuGridAdapter);

                    registerPopuGridAdapter.setList(companyUsers);
                    registerPopuGridAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_car_type:
                choose = 2;
                showPupoWindow();
                break;
            case R.id.txt_operationor:
                choose = 1;
                showPupoWindow();
                break;
            case R.id.txt_axis_count:
                choose = 3;
                showPupoWindow();
                break;
            case R.id.btn_center:
                saveData();
                break;
        }
    }
}

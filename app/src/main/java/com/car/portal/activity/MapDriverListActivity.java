package com.car.portal.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.BottomDialogMenuAdapter;
import com.car.portal.adapter.CarTypeAdapter;
import com.car.portal.adapter.MapDriverAdapter;
import com.car.portal.entity.BodyTypeList;
import com.car.portal.entity.City;
import com.car.portal.entity.Driver;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.service.UserService;
import com.car.portal.util.PhoneCallUtils;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;
import com.car.portal.view.DividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.internal.LinkedTreeMap;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapDriverListActivity extends Activity implements View.OnClickListener{
    @ViewInject(R.id.driver_list_listView)
    private ListView listView;
    @ViewInject(R.id.driver_slt_length)
    private EditText lengthEdit;
    @ViewInject(R.id.driver_slt_length2)
    private EditText length2Edit;
    @ViewInject(R.id.driver_target)
    private TextView targetEdit;
    @ViewInject(R.id.driver_type)
    private RelativeLayout typeText;
    @ViewInject(R.id.txt_type)
    private TextView txt_type;
    @ViewInject(R.id.img_day)
    private ImageView img_day;
    @ViewInject(R.id.txt_no_data)
    private TextView txt_no_data;
    @ViewInject(R.id.text_findaddress)
    private TextView text_findaddress;
    @ViewInject(R.id.img_found_return)
    private ImageView img_found_return;
    private PopupWindow popupWindow_filter;
    private View popup_filter;
    private GridView gridView;
    private CarTypeAdapter carTypeAdapter;
    private DriverService driverService;

    private MapDriverAdapter adapter;

    private boolean isCheck = false;//是否打开选择车辆类型的popupWindow
    private int SELECT_CITY = 0x1055;
    private int FIND_CITY = 0x1060;
    private double length;
    private double length2;
    private int typeId;
    private String target;  //当前城市
    private String startAddress,startAddressCode;
    private static final int RESPONSE_CODE = 0x1459;
    private boolean hasInit;
    private String searchX;
    private String searchY;
    private View footer;
    private int currentPage = 1;
    private UserService userService;
    private PopupWindow popupWindow;
    private TextView nameTxt, lengthTxt, weightTxt, aspectTxt, routeTxt, plateTxt;
    private Dialog bottomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_driver_list);
        x.view().inject(this);
        driverService = new DriverService(this);
        typeText.setOnClickListener(this);
        targetEdit.setOnClickListener(this);
        userService = new UserService(this);
        length = getIntent().getDoubleExtra("length", 0);
        length2 = getIntent().getDoubleExtra("length2", 0);
        typeId = getIntent().getIntExtra("typeId", 0);
        //target = getIntent().getStringExtra("target");

        if(startAddress==null||"".equals(startAddress)) {
            startAddress = userService.getAddress() == null ? "" : userService.getAddress().getCity();
            startAddressCode = userService.getAddress() == null ? "" : userService.getAddress().getCityCode();
        }
        searchX = getIntent().getStringExtra("searchX");
        searchY = getIntent().getStringExtra("searchY");
        //String city = getIntent().getStringExtra("city");
        adapter = new MapDriverAdapter(this);
        text_findaddress.setText("位置:"+startAddress);
        initListView();
        listView.setAdapter(adapter);
        img_found_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter.setImageClick(new MapDriverAdapter.ImageClickListener() {
            @Override
            public void onClick(View v, Object b) {
                User user = userService.getSavedUser();
                if(user.getAuth()==1) {
                    if(b==null){
                        ToastUtil.show("电话号不正确！", MapDriverListActivity.this);
                    }else {
                        if ( b.toString().contains(",")) {
                            String[] strings =  b.toString().split(",");
                            List<String> phonelist = new ArrayList<>();
                            for (int i = 0; i < strings.length; i++) {
                                phonelist.add(strings[i]);
                            }
                            showBottomDialog(phonelist);
                        } else {
                            PhoneCallUtils.call(b.toString());
                        }
                    }
                }else{
                    ToastUtil.show("您帐号没有经过审核或过期（充值延期）", MapDriverListActivity.this);
                }
            }
        });

        text_findaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapDriverListActivity.this, SelectCityActivity.class);
                intent.putExtra("minLevel", 0);
                intent.putExtra("maxLevel", 4);
                intent.putExtra("isMutiple", false);
                startActivityForResult(intent,FIND_CITY);
            }
        });

        lengthEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged (Editable s) {
                String val = lengthEdit.getText().toString();
                val = (StringUtil.isNullOrEmpty(val) ? "0" : val);
                double d = Double.valueOf(val);
                if(length - d > Double.MIN_VALUE) {
                    length = d;
                }
            }
        });
        length2Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged (Editable s) {
                String val = length2Edit.getText().toString();
                val = (StringUtil.isNullOrEmpty(val) ? "0" : val);
                double d = Double.valueOf(val);
                if(length2 - d > Double.MIN_VALUE) {
                    length2 = d;
                }
            }
        });
//        titleView.setTitle("线上司机列表");
//        titleView.setContact(city == null ? "" : city);
//        titleView.setRtnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View v) {
//                Intent intent = new Intent();
//                intent.putExtra("length", length);
//                intent.putExtra("length2", length2);
//                intent.putExtra("typeId", typeId);
//                intent.putExtra("target", target);
//                setResult(RESPONSE_CODE, intent);
//                finish();
//            }
//        });
    }

    /**
     * 初始化选择车辆类型的popupWindow
     */
    public void initFilterPopup(){
        if (popupWindow_filter==null){
            popup_filter= LayoutInflater.from(this).inflate(R.layout.popu_show_line,null);
            popupWindow_filter = new PopupWindow(popup_filter,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            gridView= (GridView) popup_filter.findViewById(R.id.grid_popu);
            LinearLayout line_popu= (LinearLayout) popup_filter.findViewById(R.id.grid_bgLine_line);
            line_popu.getBackground().setAlpha(100);

            carTypeAdapter=new CarTypeAdapter(this);
            gridView.setAdapter(carTypeAdapter);

            initFilterData();
        }
    }

    /**
     * 选择出发城市
     */
    protected void selectDepart() {
        Intent intent = new Intent(this, SelectCityActivity.class);
        intent.putExtra("minLevel", 0);
        intent.putExtra("maxLevel", 2);
        intent.putExtra("isMutiple", false);
        startActivityForResult(intent, SELECT_CITY);
    }

    /**
     * 显示选择车辆类型的popu
     */
    public void showFilterPopu(){
        initFilterPopup();
        popupWindow_filter.setTouchable(true);
        popupWindow_filter.showAsDropDown(typeText, 0, 0);
        initFilterListener();
    }
    /**
     * 初始化过滤popupWindow的监听事件
     */
    public void initFilterListener(){
        popup_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeChecked();
                popupWindow_filter.dismiss();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                carTypeAdapter.setSelectId(position);
                carTypeAdapter.notifyDataSetChanged();
                changeChecked();
                txt_type.setText(((BodyTypeList) carTypeAdapter.getItem(position)).getName());
                popupWindow_filter.dismiss();
                BodyTypeList type = (BodyTypeList) carTypeAdapter.getItem(position);
                if(type != null && typeId != type.getId()) {
                    typeId = type.getId();
                    getRange();
                }
            }
        });
    }

    /**
     * 初始化选择车辆类型的popupWindow的数据
     */
    public void initFilterData(){
        driverService.getCarType(new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                carTypeAdapter.setList((ArrayList<BodyTypeList>) objects[0]);
                carTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.driver_type:
                changeChecked();
                showFilterPopu();
                break;
            case R.id.driver_target:
                selectDepart();
                break;
        }
    }

    public void changeChecked(){
        isCheck =! isCheck;
        if (isCheck){
            img_day.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
        }else{
            img_day.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
            City c = data.getParcelableExtra("city");
            if (requestCode == SELECT_CITY && resultCode == SelectCityActivity.RESPONSE_SINGLE_CODE) {
                if (c != null && c.getShortName() != null) {
                    targetEdit.setText(c.getShortName());
                    if (!c.getShortName().equals(target)) {
                        target = c.getCode()+"";
                        getRange();
                    }
                }
            } else if (requestCode == FIND_CITY && resultCode == SelectCityActivity.RESPONSE_SINGLE_CODE) {
                if (c != null && c.getShortName() != null) {
                    text_findaddress.setText("位置:" + c.getShortName());
                    if(!c.getShortName().equals(startAddress)){
                        startAddress = c.getShortName();
                        getRange();
                    }
                }
            }
        }
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && !hasInit) {
            getRange();
        }
    }

    public void getRange(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("target", target);
        map.put("startAddress",startAddress);  //出发点城市名称，切换当前城市时
        map.put("x", searchX); //出发点的x坐标
        map.put("y", searchY);  //出发点的y坐标
        map.put("length", length);
        map.put("length2", length2);
        map.put("typeId", typeId);
        map.put("currentPage", currentPage);
        driverService.getMapDriver(map, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                hasInit = true;
                if (objects != null && objects.length > 0){
                    ArrayList<LinkedTreeMap<String, Object>> list = (ArrayList<LinkedTreeMap<String, Object>>) objects[0];
                    if(list != null) {
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        txt_no_data.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                txt_no_data.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }

            @Override
            public void onError (Object... objects) {
                super.onError(objects);
            }

            @Override
            public void onFail (int result, String message, boolean show) {
                super.onFail(result, message, show);
            }
        });
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("length", length);
            intent.putExtra("length2", length2);
            intent.putExtra("typeId", typeId);
            intent.putExtra("target", target);
            setResult(RESPONSE_CODE, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initListView () {
        footer = LayoutInflater.from(this).inflate(R.layout.listview_footer, null);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isFooter;

            @Override
            public void onScrollStateChanged (AbsListView view, int scrollState) {
                if (isFooter && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    currentPage++;
                    getRange();
                    isFooter = false;
                }
            }

            @Override
            public void onScroll (AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int count = adapter.getCount();
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0
                        && count >= currentPage * 10) {
                    isFooter = true;
                    if (listView.getFooterViewsCount() == 0) {
                        listView.addFooterView(footer);
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
               try {
                   Map<String, Object> map = (Map<String, Object>) adapter.getItem(position);
                   int driverId = ((Double) map.get("driverId")).intValue();
                   driverService.findById(driverId, new HttpCallBack(MapDriverListActivity.this) {
                       @Override
                       public void onSuccess(Object... objects) {

                       }
                   });
               }catch (Exception e){

               }
            }
        });
    }

    private void showBottomDialog(final List<String> menuList) {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        bottomDialog.setCanceledOnTouchOutside(true);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_menu, null);
        final RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view);
        BottomDialogMenuAdapter dialogMenuAdapter = new BottomDialogMenuAdapter(R.layout.item_bottom_dialog_menu, menuList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_dis_button, null);
        dialogMenuAdapter.setFooterView(view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager));
        dialogMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PhoneCallUtils.call(menuList.get(position));
            }
        });
        recyclerView.setAdapter(dialogMenuAdapter);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.cancel();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = this.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

    }

}

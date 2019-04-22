package com.car.portal.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bigkoo.pickerview.view.WheelTime;
import com.car.portal.R;
import com.car.portal.adapter.SoftKeyboardStateHelper;
import com.car.portal.datepicker.DatePickDialog;
import com.car.portal.entity.City;
import com.car.portal.entity.Goods_For_Address;
import com.car.portal.entity.User;
import com.car.portal.fragment.AlonecarlegnthFragment;
import com.car.portal.fragment.AlonecartypeFragment;
import com.car.portal.fragment.AlonegoodtypeFragment;
import com.car.portal.fragment.CarlegnthFragment;
import com.car.portal.fragment.DeliverFragment;
import com.car.portal.fragment.GoodTypeFragment;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.GoodsService;
import com.car.portal.service.UserService;
import com.car.portal.util.LogUtils;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("UseSparseArrays")
public class AddOrderActivity extends AppCompatActivity implements View.OnClickListener{
   /* @ViewInject(R.id.base_title_view)
    private BaseTitleView title;*/
    @ViewInject(R.id.txt_company_name)
    private TextView txt_company_name;//厂家名字
    @ViewInject(R.id.txt_boss_name)
    private TextView txt_boss_name;//厂家老板名字
    @ViewInject(R.id.txt_contacts_name)
    private TextView txt_contacts_name;//联系人名字
    @ViewInject(R.id.txt_starting_point)
    private TextView txt_send_goods;//发货地址
    @ViewInject(R.id.txt_receipt_point)
    private TextView txt_receive_goods;//收货地址
    @ViewInject(R.id.edit_goodType_list)//货物类型
    private TextView edit_goodType;
    @ViewInject(R.id.edit_car_length_list)//车长类型
    private  TextView edit_car_length_list;
    @ViewInject(R.id.edit_goods_weight)
    private TextView edit_goods_weight;
    @ViewInject(R.id.edit_car_type_list)
    private TextView edit_car_type_list;
    @ViewInject(R.id.edit_instructions)
    private EditText edit_instructions;//发布说明
    @ViewInject(R.id.Rel_carlength)
    private RelativeLayout Rel_carlength;
    @ViewInject(R.id.Rel_goodtype)
    private RelativeLayout Rel_goodtype;
    @ViewInject(R.id.Rel_cartype)
    private RelativeLayout Rel_cartype;
    @ViewInject(R.id.txt_registrant_name)//登记者
    private  TextView txt_registrant_name;
    @ViewInject(R.id.edit_remark)
    private EditText edit_remark;//备注
    @ViewInject(R.id.edit_loading_time)
    private TextView edit_loaing_time;
    @ViewInject(R.id.btn_cancel)
    private Button btn_cancel;//取消
    @ViewInject(R.id.btn_save)
    private Button btn_save;//保存
    @ViewInject(R.id.img_delete)
    private ImageView img_delete;
    @ViewInject(R.id.edit_transform)
    private EditText edit_transform;//运输方式
    @ViewInject(R.id.pre_pay)
    private TextView pre_pay;//预支付金额
    @ViewInject(R.id.scroLlview_addorder)
    private ScrollView scroLlview_addorder;
    @ViewInject(R.id.notSeeInCity)
    private CheckBox noSeeInCityButton;  //同城是否可见
    @ViewInject(R.id.isShare)
    private CheckBox isShareButton;    //是否发布给司机看
    private int isSee=0,isShare=1;
    private String company_name,boss_name,contacts_name;
    private int boss_id,uid;
    public static int SELECT_CITY=1080;
    private GoodsService goodsService;
    private String outLoc,routeId,route,inLoc,inLocN,inLocCode,car_long,car_type;
    private int outLocN,outLocCode,selectnum;
    private PopupWindow popupWindow, popup_transform;
    private View popupView,popup_trans_view;
    private TextView txt_saveOrder,txt_centel;
    private LinearLayout line_popu;
    private DatePickDialog datePickDialog;
    private static final String DATESPLIT = "-";
    private String arriveTime="";
    private ArrayList<City> arriveList;
    private TextView txt_land, txt_ocean, txt_air;//运输的三种方式
    private LinearLayout line_trans;
    private GoodTypeFragment goodInfoFragment;//货物类型弹窗
    private OptionsPickerView pvNoLinkOptions;
    private AlonecartypeFragment alonecartypeFragment;
    private UserService service;
    private User user;
//    private List goods = new ArrayList<>();
//    private List trucks = new ArrayList<>();
    private List transport = new ArrayList();
//    String[] goodslist = {"普货","重货","泡货","设备","配件","百货","建材","食品","饮料","化工","水果","蔬菜","木材","煤炭","石材","家具","树苗","化肥","钢材","粮食"};
//    String[] truckslist = {"请选择","1.8米","4.2米","5米","6.8米","7.7米","8.2米","9.6米","11.7米","13米","13.5米","17.5米","18米"};
    String[] transportlist = {"陆运","海运","空运"};
    private InputMethodManager imm;
    private AlonecarlegnthFragment alonecarlegnthFragment;
    private int options=0,mioptions=4,mioptions2=0,publicoption = 0;
    public static final int CARTYPE_CODE = 1;     // 车长类型返回码
    public static final int REMARKTYPE_CODE = 2;  // 备注类型返回码
    public static final int GOODSTYPE_CODE = 3;   // 货物类型返回码
    public static final int CONTACTS_CODE = 4;     // 联系人电话号码
    TimePickerView pvTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        x.view().inject(AddOrderActivity.this);
        goodsService=new GoodsService(this);
        initView();
        initData();
    }

    /**
     * 初始化view控件及其绑定监听时间事件
     */
    public void initView(){
        imm = (InputMethodManager) getSystemService(AddOrderActivity.INPUT_METHOD_SERVICE);
        txt_receive_goods.setOnClickListener(this);
        txt_send_goods.setOnClickListener(this);
        edit_loaing_time.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        Rel_cartype.setOnClickListener(this);
        img_delete.setOnClickListener(this);
        edit_transform.setOnClickListener(this);
        edit_remark.setOnClickListener(this);
        Rel_goodtype.setOnClickListener(this);
        Rel_carlength.setOnClickListener(this);
        noSeeInCityButton.setOnClickListener(this);
        isShareButton.setOnClickListener(this);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("登记货物");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab!=null;

        ab.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 初始化数据
     */
    public void initData(){

        Intent intent=getIntent();
        company_name=intent.getStringExtra("company");
        contacts_name=intent.getStringExtra("contactsName");
        boss_name=intent.getStringExtra("bossName");
        boss_id=Integer.parseInt(intent.getStringExtra("bossId"));
        uid=intent.getIntExtra("uid", 0);
        route=intent.getStringExtra("route");
        routeId=intent.getStringExtra("routeId");
        selectnum=intent.getIntExtra("selectnum",0);
        outLocN = intent.getIntExtra("startCity",0);
        outLocCode = Integer.parseInt(intent.getStringExtra("startCityCode"));
        txt_send_goods.setText(intent.getStringExtra("startCityName"));
        outLoc=intent.getStringExtra("startCityName");

        edit_loaing_time.setText(getTime(new Date(System.currentTimeMillis())));
        txt_company_name.setText(company_name);
        txt_contacts_name.setText("联系人：" + contacts_name);
        txt_boss_name.setText("老板：" + boss_name);


        if (service == null) {
            service = new UserService(AddOrderActivity.this);
        }

        user = service.getLoginUser();

        txt_registrant_name.setText(user.getCname() + "");

//        goods = java.util.Arrays.asList(goodslist);
//        trucks = java.util.Arrays.asList(truckslist);
        transport = java.util.Arrays.asList(transportlist);



        edit_remark.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scroLlview_addorder.fullScroll(ScrollView.FOCUS_DOWN);
                                edit_remark.setFocusable(true);
                                edit_remark.setFocusableInTouchMode(true);
                                edit_remark.requestFocus();
                            }
                        }, 500);//0.5秒后执行Runnable中的run方法
                        break;
                }
                return false;
            }
        });

        edit_instructions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scroLlview_addorder.fullScroll(ScrollView.FOCUS_DOWN);
                                edit_instructions.setFocusable(true);
                                edit_instructions.setFocusableInTouchMode(true);
                                edit_instructions.requestFocus();
                            }
                        }, 500);//0.5秒后执行Runnable中的run方法
                        break;
                }
                return false;
            }
        });

    }


    private String getTime(Date date) {
        String format  = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    private void option(final List<String> list, final TextView ed, final int type){
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        pvNoLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                ed.setText("");
                ed.setText(list.get(options1));
                publicoption = options1;
                if(type==1) {
                    options = options1;
                }else if(type==2){
                    mioptions = options1;
                }else if(type==3){
                    mioptions2 = options1;
                }

            }
        })
                .setSelectOptions(type==1?options:(type==2?mioptions:mioptions2),0)
                .build();
        pvNoLinkOptions.setNPicker(list,null,null);
        pvNoLinkOptions.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_starting_point:
                selectDepart();
                break;
            case R.id.txt_receipt_point:
                selectArrive();
                break;
            case R.id.edit_loading_time:
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                Calendar selectedDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                startDate.set(2014, 1, 1);
                Calendar endDate = Calendar.getInstance();
                endDate.set(2050, 12, 31);
                 pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        edit_loaing_time.setText(getTime(date));
                    }


                })       .setDate(selectedDate)
                        .setRangDate(startDate,endDate)
                        .setLayoutRes(R.layout.item_checkbox, new CustomListener() {
                            @Override
                            public void customLayout(View v) {
                                TextView tv_finish = v.findViewById(R.id.tv_finish);
                                tv_finish.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        pvTime.returnData();
                                        pvTime.dismiss();
                                    }
                                });
                            }
                        })
                        .build();
                pvTime.show();
                break;
//                货物类型
            case R.id.txt_registrant_name://登记者名字

                break;
            case R.id.img_delete:
                txt_receive_goods.setText("");
                break;
            case R.id.btn_save:
                if ((txt_send_goods!=null&&!txt_send_goods.getText().toString().equals(""))
                        &&(txt_receive_goods!=null&&!txt_receive_goods.getText().toString().equals(""))
                        &&(edit_loaing_time!=null&&!edit_loaing_time.getText().toString().equals(""))
                        &&edit_goodType!=null && !"".equals(edit_goodType.getText().toString())
                        &&edit_car_length_list!=null && !"".equals(edit_car_length_list.getText().toString())
                        &&edit_goods_weight!=null && !"".equals(edit_goods_weight.getText().toString())) {
                    addOrder();
                }else {
                    if(txt_send_goods==null || "".equals(txt_send_goods)){
                        ToastUtil.show("出发地点：不能为空", this);
                    }else if(txt_receive_goods==null || "".equals(txt_receive_goods)){
                        ToastUtil.show("收货地点：不能为空", this);
                    }else if(edit_loaing_time==null || "".equals(edit_loaing_time)) {
                        ToastUtil.show("装车时间：不能为空", this);
                    }else if(edit_goodType==null || "".equals(edit_goodType.getText().toString())){
                        ToastUtil.show("货物类型：不能为空", this);
                    }else if(edit_car_length_list==null || "".equals(edit_car_length_list.getText().toString())){
                        ToastUtil.show("车长：不能为空", this);
                    }else if(edit_goods_weight==null || "".equals(edit_goods_weight.getText().toString())){
                        ToastUtil.show("货物重量：不能为空", this);
                    }else {
                        ToastUtil.show("请填写完整的货单信息", this);
                    }
                }
                break;
            case R.id.btn_cancel:
                new MaterialDialog.Builder(AddOrderActivity.this)
                        .title("提示")
                        .content("是否要取消登记货物？")
                        .positiveText("确认")
                        .negativeText("手滑了")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                finish();
                            }
                        })
                        .show();
                break;
            case R.id.edit_transform://运输方式
                option(transport,edit_transform,3);
                break;
            case R.id.Rel_goodtype:
                showGoodtypeWindow();
                break;

            case R.id.Rel_carlength:
                showCarLengthWindow();
                break;
            case  R.id.notSeeInCity:
                if(noSeeInCityButton.isChecked()){
                    isSee = 1;
                }else{
                    isSee = 0;
                }
                break;
            case R.id.isShare:
                if(isShareButton.isChecked()){
                    isShare=1;
                }else{
                    isShare=0;
                }
                break;
            case R.id.Rel_cartype:
                showCartypeWindow();
                break;
        }
    }

    private void showGoodtypeWindow() {
        AlonegoodtypeFragment alonegoodtypeFragment = new AlonegoodtypeFragment();
        alonegoodtypeFragment.show(getSupportFragmentManager(),"fragment_bottom_dialog");
        alonegoodtypeFragment.setOnDialogListener(new AlonegoodtypeFragment.OnDialogListener() {
            @Override
            public void onDialogClick(String a, String b) {
                edit_goodType.setText(a);
                selectnum = Integer.parseInt(b);
            }
        });
    }


    /**
     * 检查并保存订单
     */
    public void addOrder(){
        //检查此条获取信息是否存在,存在则提示是否继续添加，不存在则直接保存
        goodsService.inspectGoodsOrder(boss_id, routeId, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if(objects != null && objects.length > 0) {
                    Integer ret = (Integer) objects[0];
                    if(ret == null || ret < 1) {
                        saveOrder();
                    }else{
                        showPopupWindow();
                    }
                }
            }

            @Override
            public void onError(Object... objects) {
                super.onError(objects);
            }
        });
    }




    /**
     * 初始化交运输方式的popupwindow
     */
//    public void initPopupTransWindow(){
//        if (popup_transform == null){
//            popup_trans_view = LayoutInflater.from(this).inflate(R.layout.transform_popu,null);
//            popup_transform = new PopupWindow(popup_trans_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
//            txt_land = (TextView) popup_trans_view.findViewById(R.id.txt_land);
//            txt_ocean = (TextView) popup_trans_view.findViewById(R.id.txt_ocean);
//            txt_air = (TextView) popup_trans_view.findViewById(R.id.txt_air);
//            line_trans = (LinearLayout) popup_trans_view.findViewById(R.id.line_trans);
//            line_trans.getBackground().setAlpha(0);
//            popup_trans_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popup_transform.dismiss();
//                }
//            });
//        }
//    }
//
//    /**
//     * 显示
//     */
//    public void showTransWindow(){
//        initPopupTransWindow();
//        popup_transform.setTouchable(true);
//        popup_transform.showAsDropDown(txt_transform);
//    }

    /**
     * 初始化popupWindow
     */
    public void initPopupWindow(){
        if (popupWindow==null) {
            popupView = LayoutInflater.from(this).inflate(R.layout.popu_isadd_order, null);
            popupWindow = new PopupWindow(popupView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            txt_saveOrder = (TextView) popupView.findViewById(R.id.txt_save);
            txt_centel = (TextView) popupView.findViewById(R.id.txt_cencel);
            line_popu= (LinearLayout) popupView.findViewById(R.id.line_popu);
            line_popu.getBackground().setAlpha(100);
            popupView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
    }

    /**
     * 显示Popupwindow
     */
    @SuppressLint("InflateParams")
    public void showPopupWindow() {
        initPopupWindow();
        View view = LayoutInflater.from(this).inflate(R.layout.activity_add_order,null);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER, 0, 0);
        popupOnClick();
    }

    /**
     * popupWindow的点击事件
     */
    public void popupOnClick(){
        txt_saveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                saveOrder();
            }
        });
        txt_centel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 保存出货单的方法
     */
    public void saveOrder(){
        final Goods_For_Address goods_for_address=new Goods_For_Address();
        //获取备注文本框中的信息
        String remark="";
        if (edit_remark!=null&&!edit_remark.getText().toString().equals(""))
            remark = edit_remark.getText().toString();
        goods_for_address.setBoss_owner(0);
        goods_for_address.setBossId(boss_id);
        goods_for_address.setBossName(boss_name);
        goods_for_address.setCompany(company_name);
        goods_for_address.setContractName(contacts_name);
        goods_for_address.setConvey(0);
        goods_for_address.setEnd(0 + "");
        goods_for_address.setEndDate(edit_loaing_time.getText().toString());
        goods_for_address.setForce_add(0);
        goods_for_address.setMemo(remark==null?"(无)":remark);
        goods_for_address.setOnlyRecord(0 + "");
        goods_for_address.setOperator(uid + "");
        goods_for_address.setOutLoc(outLoc);
        goods_for_address.setOutLocN(outLocN + "");
        goods_for_address.setOutLocCode(Integer.toString(outLocCode));
        goods_for_address.setOverTime(0 + "");
        goods_for_address.setOwer(uid + "");
        goods_for_address.setPrepareMoney(0);
        goods_for_address.setRoute(inLoc);
        goods_for_address.setRouteN(inLocN);
        goods_for_address.setRouteCode(inLocCode);
        goods_for_address.setStartDate(formatDate());
        goods_for_address.setType(selectnum +1+ "");  //编号由1开始的，所以+1
        goods_for_address.setIsSee(isSee);
        goods_for_address.setIsShare(isShare);
        goods_for_address.setCarLength(car_long);
        goods_for_address.setMemo2(edit_instructions.getText().toString());
        goods_for_address.setBabyType(Integer.parseInt(car_type)); //车箱类型，
        goodsService.addGoodsOrder(goods_for_address, new HttpCallBack
                (AddOrderActivity.this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                    Integer ret = (Integer) objects[0];
                    if (ret == null || ret < 1)
                        ToastUtil.show("保存失败", AddOrderActivity.this);
                    else {
                        ToastUtil.show("出货单保存成功！", AddOrderActivity.this);
                        finish();
                    }
                }
            }
        });
    }

    /**
     * 得到当前时间
     * @return
     */
    public String formatDate(){
        String time="";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date=new Date(System.currentTimeMillis());
        time=simpleDateFormat.format(date);
        return time;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AddOrderActivity.SELECT_CITY && resultCode == SelectCityActivity.RESPONSE_MUTIPLE_CODE) {
            arriveList = data.getParcelableArrayListExtra("cities");
            if(arriveList != null && arriveList.size() > 0) {
                StringBuffer bf = new StringBuffer();
                StringBuffer in = new StringBuffer();
                StringBuffer inN = new StringBuffer();
                for (City city : arriveList) {
                    bf.append(city.getShortName());
                    bf.append(",");
                    in.append(city.getCode());
                    in.append(",");
                    inN.append(city.getCid());
                    inN.append(",");
                }
                bf.deleteCharAt(bf.length() - 1);
                inLocCode = in.deleteCharAt(in.length()-1).toString();
                inLocN = inN.deleteCharAt(inN.length()-1).toString();
                if(bf.length() > 13) {
                    txt_receive_goods.setText(bf.subSequence(0, 12) + "…");
                } else {
                    txt_receive_goods.setText(bf.toString());
                }
                inLoc =txt_receive_goods.getText().toString();
            }
        } else if (requestCode == AddOrderActivity.SELECT_CITY && resultCode == SelectCityActivity.RESPONSE_SINGLE_CODE){
            City c = data.getParcelableExtra("city");
            outLocN = c.getCid();
            outLocCode = c.getCode();
            if(c != null && c.getShortName() != null) {
                txt_send_goods.setText(c.getShortName());
            }
            outLoc = txt_send_goods.getText().toString();
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
    public void showCarLengthWindow()
    {
        alonecarlegnthFragment = new AlonecarlegnthFragment();
        alonecarlegnthFragment.show(getSupportFragmentManager(),"fragment_bottom_dialog");
        alonecarlegnthFragment.setOnDialogListener(new AlonecarlegnthFragment.OnDialogListener() {
            @Override
            public void onDialogClick(String a, String b, String c) {
                edit_car_length_list.setText(a);
                car_long = b;
            }
        });
    }

    public void showCartypeWindow()
    {
        alonecartypeFragment = new AlonecartypeFragment();
        alonecartypeFragment.show(getSupportFragmentManager(),"fragment_bottom_dialog");
        alonecartypeFragment.setOnDialogListener(new AlonecarlegnthFragment.OnDialogListener() {
            @Override
            public void onDialogClick(String a, String b, String c) {
                edit_car_type_list.setText(a);
                car_type = b;
            }
        });
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

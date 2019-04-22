package com.car.portal.fragment;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.car.portal.R;
import com.car.portal.activity.AddOrderActivity;
import com.car.portal.activity.CitySelectActivity;
import com.car.portal.activity.DriverAuthActivity;
import com.car.portal.activity.LoginActivity;
import com.car.portal.activity.MainActivity;
import com.car.portal.activity.SelectCityActivity;
import com.car.portal.datepicker.DatePickDialog;
import com.car.portal.entity.Address;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.City;
import com.car.portal.entity.MyJsonReturn;
import com.car.portal.entity.PortalDriver;
import com.car.portal.entity.User;
import com.car.portal.entity.UserDetail;
import com.car.portal.entity.commonlyusedbean;
import com.car.portal.entity.newOrder;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.SessionStore;
import com.car.portal.service.DriverService;
import com.car.portal.service.GoodsService;
import com.car.portal.service.UserService;
import com.car.portal.util.SharedPreferenceUtil;
import com.car.portal.util.ToastUtil;
import com.orhanobut.hawk.Hawk;

import org.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/6/30.
 */

public class DeliverFragment extends MainBaseFragment implements CompoundButton.OnCheckedChangeListener {

    private TextView txt_send_goods;//发货地址
    private TextView txt_receive_goods;//收货地址
    private TextView edit_remark;//备注
    private TextView edit_loaing_time;
    private Button btn_cancel;//取消
    private FloatingActionButton btn_save;//保存
    private EditText edit_volume; //货物体积
    private EditText edit_square; //货物方
    private EditText edit_freight; //运费
    private TextView edit_car_length;// 车长类型
    private TextView edit_goodType;
    private TextView txt_contacts_name;
    private CheckBox condition1;
    private CheckBox condition2;

    public static final int CARTYPE_CODE = 1;     // 车长类型返回码
    public static final int REMARKTYPE_CODE = 2;  // 备注类型返回码
    public static final int GOODSTYPE_CODE = 3;   // 货物类型返回码
    public static final int CONTACTS_CODE = 4;     // 联系人电话号码

    private CarlegnthFragment carlegnthFragment; // 车长类型弹窗
    private RemarksFragment remarksFragment; // 备注弹窗
    private GoodTypeFragment goodInfoFragment;//货物类型弹窗
    private ContactsNameFragment contactsNameFragment;//联系人弹窗
    private View popupView,popup_trans_view ,view;
    private TextView txt_saveOrder,txt_centel;
    private LinearLayout line_popu;
    private LinearLayout line_ViewAdd;
    private ArrayList<City> arriveList; //收货地点信息
    private GoodsService goodsService;
    private DatePickDialog datePickDialog;
    private static final String DATESPLIT = "-";
    private String arriveTime="";

    private int outLocN,selectnum,boss_id,uid;
    private String outLoc,inLoc,intLocN,startCode,endCode;
    private String userCarType,carLenght,bodyTypeOption,bodyTypeOptionV,carLenghtV,carLengthResult; //车长选择
    private String goodsType,goodsTypeV;// 货物类型
    private String memo;// 备注信息
    private String contactsName; // 联系人选择
    private String tels ;        //联系电话选择
    private String uidStr;   //联系人编号
    private PopupWindow popupWindow;
    private LocationManager locationManager;
    //货物用户信息
    private UserService userService;
    private User user;
    boolean isshow = false;
    public commonlyusedbean commonlyusedbean;
    HashMap<String, String[]> returnJson = new HashMap<>();
    private String[] contactNameList,telsList; //后台获取的联系人电话号码跟名字数组
    private int uids[];
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    //唯一怕的就是获取完的时候但是txt_contacts_name 没有findViewById 报错
                    if(userService==null) userService=new UserService(getContext());
                    if(user==null) user = userService.getLoginUser();
                    int i = 0,x=0;
                    if(uids!=null && user!=null){
                        try {
                            for(int n:uids){
                                if(n==user.getUid()) {
                                    i=x;
                                    break;
                                }
                                x++;
                            }
                        }catch (Exception e){

                        }
                    }
                    if(i>=uids.length){
                        contactsName = contactNameList[0];// 联系人选择(默认是第一个联系人)
                        tels = telsList[0];//联系电话选择
                        uidStr = Integer.toString(uids[0]);
                    }else {
                        contactsName = contactNameList[i];
                        tels = telsList[i];
                        uidStr = Integer.toString(uids[i]);
                    }
                    txt_contacts_name.setText(contactsName+","+tels);
                    break;
                case 2:
                    commonlyusedbean com = (com.car.portal.entity.commonlyusedbean) msg.obj;
                    txt_send_goods.setText(com.getOutLoc());
                    txt_receive_goods.setText(com.getRoute());
                    startCode = com.getOutLocCode();
                    endCode = com.getRouteCode();
                    outLoc = com.getOutLoc();
                    inLoc = com.getRoute();
                    break;
                default:
                    break;
            }
        }
    };

    // 继承的类需要实现的方法
    @Override
    public void search(String condition) {
    }

    @Override
    public void onWindowFoucusChanged(boolean hasFocus) {
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            requestData();
        }
    }

    public void requestData() {
        if(isshow) {
            final DeliverTabFragment deliverTabFragment = (DeliverTabFragment) this.getParentFragment();
            commonlyusedbean = deliverTabFragment.getDataex();
            if (commonlyusedbean != null) {
                Message message = new Message();
                message.what = 2;
                message.obj = commonlyusedbean;
                handler.sendMessage(message);
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.from(getActivity()).inflate(
                R.layout.activity_add_deliver, null);
        userService = new UserService(getActivity());
        goodsService=new GoodsService(getActivity());
        user = userService.getLoginUser();
        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        String locationProvider = null;

        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }else{
            if (providers.contains(LocationManager.GPS_PROVIDER)) {
                //如果是GPS
                locationProvider = LocationManager.GPS_PROVIDER;
            } else {
                Intent i = new Intent();
                i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getContext().startActivity(i);
                return null;
            }
        }
        //TODO 确认usr是动态向服务器提取的，应是动态请求服务器，并保存最新状态
        DriverService driverService = new DriverService(getActivity());
        driverService.checkUserAuthentication(getUserAuthBack);
        if(SessionStore.getSessionId()==null){
            Intent intent = new Intent(getActivity(),LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//关掉所要到的界面中间的activity
            startActivity(intent);
        }else {
            getContactPhone();
            initListener();
        }
        isshow = true;

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initListener()
   {
       txt_send_goods = (TextView) view.findViewById(R.id.txt_starting_point);
       txt_receive_goods = (TextView) view.findViewById(R.id.txt_receipt_point);
       edit_remark = (TextView) view.findViewById(R.id.edit_remark);
       edit_loaing_time =  (TextView) view.findViewById(R.id.edit_loading_time);
       btn_cancel =  (Button) view.findViewById(R.id.btn_cancel);
       btn_save =   view.findViewById(R.id.btn_save);
       edit_volume =  (EditText) view.findViewById(R.id.edit_volume);
       edit_square =  (EditText) view.findViewById(R.id.edit_square);
       edit_freight =  (EditText) view.findViewById(R.id.edit_freight);
       edit_car_length =  (TextView) view.findViewById(R.id.edit_car_length);
       edit_goodType =  (TextView) view.findViewById(R.id.edit_goodType);
       txt_contacts_name =  (TextView) view.findViewById(R.id.txt_contacts_name);
       condition1 =   view.findViewById(R.id.condition1);
       condition2 =    view.findViewById(R.id.condition2);
      line_ViewAdd = view.findViewById(R.id.line_ViewAdd);
       edit_loaing_time.setText(formatDate());
       Address address = userService.getAddress();
       if(address!=null) {
           txt_send_goods.setText(address.getCity());
           startCode = address.getCityCode();
           outLoc = address.getCity();
           outLocN = address.getCid();
       }
       condition1.setOnCheckedChangeListener(this);
       condition2.setOnCheckedChangeListener(this);
       txt_send_goods.setOnClickListener(listener);
       txt_receive_goods.setOnClickListener(listener);
       edit_loaing_time.setOnClickListener(listener);
       btn_save.setOnClickListener(listener);
       btn_cancel.setOnClickListener(listener);
       edit_square.setInputType(EditorInfo.TYPE_CLASS_PHONE);
       edit_volume.setInputType(EditorInfo.TYPE_CLASS_PHONE);
       edit_car_length.setOnClickListener(listener);
       edit_remark.setOnClickListener(listener);
       edit_goodType.setOnClickListener(listener);
       line_ViewAdd.setOnClickListener(listener);


   }

    //请求司机信息
    private HttpCallBack getInfoBack = new HttpCallBack(getActivity()) {
        @Override
        public void onSuccess(Object... objects) {
            if (objects != null && objects.length > 0) {
                PortalDriver driver = (PortalDriver) objects[0];
                UserDetail userDetail = (UserDetail)objects[1];
                Intent intent = new Intent(getActivity(), DriverAuthActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("driver", driver);
                bundle.putSerializable("userDetail",userDetail);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };
    //请求用户审核状态信息
    private HttpCallBack getUserAuthBack = new HttpCallBack(getActivity()) {
        @Override
        public void onSuccess(Object... objects) {
            if (objects != null && objects.length > 0) {
                BaseEntity b = (BaseEntity)objects[0];
                int auth = b.getData()==null?0:Integer.parseInt((String )b.getData());
                user.setAuth(auth);
            }
        }
    };

    private Intent intent;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txt_starting_point:
                    //selectDepart();
                    intent = new Intent(getActivity(), CitySelectActivity.class);
                    intent.putExtra("action","depart");
                    intent.putExtra("title","出发城市");
                    startActivityForResult(intent, 100);
                    getActivity().overridePendingTransition(0, 0);
                    break;
                case R.id.txt_receipt_point:
                    //selectArrive();


                    /*
                    * 调用CitySelectActivity只选用传入两个参数
                    * action:作为数据保存和取出时候唯一的的key
                    * title:该Activity控件的标题名称
                    * */
                    intent = new Intent(getActivity(), CitySelectActivity.class);
                    intent.putExtra("action","arrive");
                    intent.putExtra("title","到达城市");
                    startActivityForResult(intent, 101);
                    getActivity().overridePendingTransition(0, 0);
                    break;
                case R.id.edit_loading_time:
                    Calendar selectedDate = Calendar.getInstance();
                    Calendar startDate = Calendar.getInstance();
                    startDate.set(2014, 1, 1);
                    Calendar endDate = Calendar.getInstance();
                    endDate.set(2050, 12, 31);

                    TimePickerView pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            edit_loaing_time.setText(getTime(date));
                        }


                    })       .setDate(selectedDate)
                            .setRangDate(startDate,endDate)
                            .build();
                    pvTime.show();
                    break;
                case R.id.btn_save:

                    // 备注可以不填 运费金额可以不填 体积跟输入默认第一个
                    if ((txt_send_goods.getText()!=null&&!"".equals(txt_send_goods.getText().toString()))
                            &&(txt_receive_goods.getText()!=null&&!"".equals(txt_receive_goods.getText().toString()))
                            &&(edit_loaing_time.getText()!=null&&!"".equals(edit_loaing_time.getText().toString()))
                            &&(txt_contacts_name.getText()!=null&&!"".equals(txt_contacts_name.getText().toString()))
                            &&(edit_goodType.getText()!=null&&!"".equals(edit_goodType.getText().toString()))
                            &&((edit_volume.getText()!=null&&!"".equals(edit_volume.getText().toString())) ||(edit_square.getText()!=null&&!"".equals(edit_square.getText().toString())))
                            //&&((edit_volume.getText()!=null&&!"".equals(edit_volume.getText().toString())) ||(edit_square.getText()!=null&&!"".equals(edit_square.getText().toString())))
                            &&(edit_car_length.getText()!=null&&!"".equals(edit_car_length.getText().toString()))
                            &&(user.getAuth() == 1))
                        sendOrder();
                    else
                        // 用户认证信息auth==1 才是认证成功
                        if (user.getAuth()!= 1) {
                            ToastUtil.show("请先完成信息认证",getActivity());
                            DriverService driverService = new DriverService(getActivity());
                            driverService.getDriverInfo(getInfoBack);
                        }else {
                            if(txt_send_goods.getText()==null || "".equals(txt_send_goods.getText().toString())){
                                ToastUtil.show("出发地点：不能为空", getActivity());
                            }else if(txt_receive_goods.getText()==null || "".equals(txt_receive_goods.getText())){
                                ToastUtil.show("收货地点：不能为空", getActivity());
                            }else if(edit_loaing_time.getText()==null || "".equals(edit_loaing_time.getText())) {
                                ToastUtil.show("装车时间：不能为空", getActivity());
                            }else if(edit_car_length.getText()==null||"".equals(edit_car_length.getText().toString())) {
                                ToastUtil.show("车长和类型：不能为空", getActivity());
                            }else if(edit_goodType.getText()==null|| "".equals(edit_goodType.getText().toString())) {
                                ToastUtil.show("货物类型：不能为空", getActivity());
                            }else if((edit_volume.getText()==null||"".equals(edit_volume.getText().toString())) && (edit_square.getText()==null||"".equals(edit_square.getText().toString()))){
                                ToastUtil.show("重量或体积必须有一项需要填写", getActivity());
                            }else {
                                ToastUtil.show("请填写完整的货单信息", getActivity());
                            }
                        }

                    break;
                case R.id.btn_cancel:
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.edit_car_length:
                    // 展开车长选择的弹窗
                    showCarLengthWindow(view);
                    break;
                case R.id.edit_remark:
                    //展开备注信息弹窗
                    showRemarksWindow(view);
                    break;
                case R.id.edit_goodType:
                    // 货物类型选择
                    showGoodTypeWindow(view);
                    break;
                case R.id.line_ViewAdd:
                    // 联系人列表
                    showContactsNameWindow(view);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        /*
        * 这里取从选择器回来的数据,resultCode是从选择器返回的code==0必须data!=null
        * 的情况下再进行取值,排除了选择器没有操作就回调当前Activity时为空指针的错误
        * 从选择器回调的了四个参数,分别是:省,市,区,邮编code
        * ObtainContent()方法已经封装了,第一个参数是intent,第二个是是否取出完成的数据:
        * 例如:false 得到的是 "宝安区" true 得到的是 "广东 深圳市 宝安区 518100"
        * */
        if (resultCode==0&&data!=null){

            if (requestCode==100){
                txt_send_goods.setText(ObtainContent(data,false));
                SharedPreferenceUtil.SaveCity("depart",ObtainContent(data,true));
                startCode=data.getStringExtra("code");
                outLoc=txt_send_goods.getText().toString();
            }
            if (requestCode==101){
                txt_receive_goods.setText(ObtainContent(data,false));
                SharedPreferenceUtil.SaveCity("arrive",ObtainContent(data,true));
                endCode=data.getStringExtra("code");
                inLoc=txt_receive_goods.getText().toString();
            }

        }

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
                    endCode = String.valueOf(city.getCode());
                }
                bf.deleteCharAt(bf.length() - 1);
                intLocN = in.deleteCharAt(in.length()-1).toString();
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
            if(c != null && c.getShortName() != null) {
                txt_send_goods.setText(c.getShortName());
            }
            startCode = String.valueOf(c.getCode());
            outLoc = txt_send_goods.getText().toString();
        }else if (requestCode == CARTYPE_CODE){ // 车长类型选择返回
            userCarType = data.getStringExtra("usecarType");
            carLenght = data.getStringExtra("carLength");
            bodyTypeOption = data.getStringExtra("carType");
            bodyTypeOptionV = data.getStringExtra("bodyTypeOptionV");
            carLenghtV = data.getStringExtra("carLenghtV");
            String carLengthResult = data.getStringExtra("carLengthResult");
            edit_car_length.setText(userCarType+" "+carLengthResult+" "+bodyTypeOption);
            carlegnthFragment.dismiss();
        }
        else if (requestCode == REMARKTYPE_CODE){ // 备注选择返回
            memo = new String();
            int count = 0;
            Bundle bundle = data.getExtras();
            SerializableHashMap serializableHashMap = (SerializableHashMap) bundle.get("object");
            HashMap<String,String> orderMap =serializableHashMap.getMap();
            for (String key : orderMap.keySet()) {
                if (orderMap.get(key) != null )
                {
                    count ++;
                    memo = (memo==null) ? orderMap.get(key) : memo+orderMap.get(key);
                    if(count < orderMap.size()){
                        memo = memo +"，";
                    }
                }
            }
            edit_remark.setText(memo);
            remarksFragment.dismiss();
        }
        else if (requestCode == GOODSTYPE_CODE){ // 货物类型选择返回
            goodsType = data.getStringExtra("result");
            goodsTypeV  =data.getStringExtra("resultV");
            edit_goodType.setText(goodsType);
            goodInfoFragment.dismiss();
        }
        else if (requestCode == CONTACTS_CODE){ // 联系人选择
            contactsName = data.getStringExtra("contactsName");// 联系人选择(默认是第一个联系人)
            tels = data.getStringExtra("tels");//联系电话选择
            uidStr = data.getStringExtra("uidStr");//联系电话选择
            //显示类型不同表现
            txt_contacts_name.setText(data.getStringExtra("results"));
            contactsNameFragment.dismiss();
        }
    }



    /**
     * 选择时间
     */

    private String getTime(Date date) {
        String format  = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /*
     * Created By  20180704
     * 发送发货信息
     */
    public void sendOrder(){
        btn_save.setEnabled(false);//设置按钮不能再次点击
        final newOrder newOrderRecord =new newOrder ();

        String volume = null;//货物体积
        String quare = null; //重量
        if (edit_volume !=null && !edit_volume.getText().toString().equals(""))
        {
            volume = edit_volume.getText().toString();
        }
        if (edit_square!=null && !edit_square.getText().toString().equals(""))
        {
            quare = edit_square.getText().toString();
        }
        int freight = 0;// 运费
        if (edit_freight !=null&&!edit_freight.getText().toString().equals(""))
        {
            freight = Integer.parseInt(edit_freight.getText().toString());
        }
        // 编辑发货信息
        newOrderRecord.setStart_address(outLoc);
        newOrderRecord.setEnd_address(inLoc);
        newOrderRecord.setUserCarType(userCarType);
        newOrderRecord.setCarLenght(carLenght);
        newOrderRecord.setBodyTypeOption(bodyTypeOption);
        newOrderRecord.setGoodsType(goodsType);
        //体积跟顿数二选一 如果没选的话就是0
        if (volume!=null)
        {
            newOrderRecord.setVolume(Integer.parseInt(volume.toString()));
        }else
        {
            newOrderRecord.setVolume(0);
        }

        if (quare!=null)
        {
            newOrderRecord.setQuare(Integer.parseInt(quare.toString()));
        }else
        {
            newOrderRecord.setQuare(0);
        }
        newOrderRecord.setMoney(freight);
        newOrderRecord.setLoadingDate(edit_loaing_time.getText().toString());
        newOrderRecord.setMemo(memo==null?"(无)":memo);
        newOrderRecord.setUserId(user.getUid());
        newOrderRecord.setCompanyId(user.getCompanyId());
        newOrderRecord.setContactsName(contactsName);
        newOrderRecord.setTels(tels);
        newOrderRecord.setUserId(Integer.parseInt(uidStr.split(",")[0]));
        newOrderRecord.setStartCode(startCode);
        newOrderRecord.setEndCode(endCode);
        newOrderRecord.setBodyTypeOptionV(bodyTypeOptionV);
        newOrderRecord.setIsSaveTo(isSaveTo);
        newOrderRecord.setIsSee(isSee);
        newOrderRecord.setCarLengthV(carLenghtV);
        newOrderRecord.setGoodsTypeV(goodsTypeV);
        //保存发货
        goodsService.sendGoodsOrder(newOrderRecord, new HttpCallBack(getActivity()) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                    Integer ret = (Integer) objects[0];
                    if (ret == null || ret < 1)
                        ToastUtil.show("保存失败", getActivity());
                    else {
                        ToastUtil.show("出货单保存成功！", getActivity());
                        btn_save.setEnabled(true);
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            }

            @Override
            public void onFail(int result, String message, boolean show) {
                super.onFail(result, message, show);
                btn_save.setEnabled(true);
            }

            @Override
            public void onError(Object... objects) {
                super.onError(objects);
                btn_save.setEnabled(true);
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


    /**
     * 初始化popupWindow
     */
    public void initPopupWindow(){
        if (popupWindow==null) {
            popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popu_isadd_order, null);
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_add_deliver,null);
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
                sendOrder();
            }
        });
        txt_centel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    // 车长
    public void showCarLengthWindow(View v)
    {
        FragmentManager fm = getFragmentManager();
        carlegnthFragment = new CarlegnthFragment();
        carlegnthFragment.setTargetFragment(DeliverFragment.this, CARTYPE_CODE);
        carlegnthFragment.show(fm, "fragment_bottom_dialog");
    }

    // 备注
    public void showRemarksWindow(View v)
    {
        FragmentManager fm = getFragmentManager();
        remarksFragment= new RemarksFragment();
        remarksFragment.setTargetFragment(DeliverFragment.this,REMARKTYPE_CODE);
        remarksFragment.show(fm, "fragment_bottom_dialog");
    }

    //货物类型
    public void showGoodTypeWindow(View v){
        FragmentManager fm = getFragmentManager();
        goodInfoFragment= new GoodTypeFragment();
        goodInfoFragment.setTargetFragment(DeliverFragment.this, GOODSTYPE_CODE);
        goodInfoFragment.show(fm, "fragment_bottom_dialog");
    }

    //联系人跟电话列表
    public void  showContactsNameWindow(View v){
        FragmentManager fm = getFragmentManager();
        contactsNameFragment= new ContactsNameFragment();
        contactsNameFragment.setTargetFragment(DeliverFragment.this, CONTACTS_CODE);
        contactsNameFragment.show(fm, "fragment_bottom_dialog");
    }

    // 获取用户信息之后,选择默认的第一个用户信息
    public void getContactPhone(){
        //contactTelsList等一般情况下此列表都是不会变的，在第一次登录时可以进行下载更新本地
        if(Hawk.get("contactTelsList")==null) {
            goodsService.findContactPhone(null, new HttpCallBack(getActivity()) {
                @Override
                public void onSuccess(Object... objects) {
                    MyJsonReturn<String> returnJson = (MyJsonReturn<String>) objects[0];
                    contactNameList = returnJson.getContactsNames();
                    telsList = returnJson.getTels();
                    uids = returnJson.getUids();
                    if (contactNameList.length > 0 && telsList.length > 0 && uids.length > 0) {
                        Hawk.put("contactTelsList", telsList);
                        Hawk.put("contactUids", uids);
                        Hawk.put("contactNameList", contactNameList);
                        Message msgMessage = Message.obtain();
                        msgMessage.what = 1;
                        //SessionStore.setMyToken(returnJson.getToken());
                        handler.sendMessage(msgMessage);
                    } else {
                        ToastUtil.show("用户信息获取为空", getActivity());
                        //SessionStore.resetSessionId();
                        //SharedPreferenceUtil util = SharedPreferenceUtil.getIntence();
                        //util.saveSessionId(null, getContext());

                        reLogin();
                    }
                }

                @Override
                public void onError(Object... objects) {
                    super.onError(objects);
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            telsList = Hawk.get("contactTelsList");
            uids = Hawk.get("contactUids");
            contactNameList= Hawk.get("contactNameList");
            Message msgMessage = Message.obtain();
            msgMessage.what = 1;
            handler.sendMessage(msgMessage);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    public String ObtainContent(Intent data,boolean fullContent){
        String province=data.getStringExtra("province");
        String city=data.getStringExtra("city");
        String district=data.getStringExtra("district");
        String code=data.getStringExtra("code");

        String fullStr=province+" "+city+" "+district+" " +code;
        if (fullContent)
            return fullStr;

        String[] split = fullStr.split(" ");
        String address=split[2];
        String fristStr=address.substring(0);
        if (fristStr.startsWith("全"))
            return fristStr.substring(1,fristStr.length());
        else
            return address;
    }

    private int isSaveTo=0;
    private int isSee=1;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.condition1:
                if (isChecked){
                    condition1.setTextColor(getResources().getColor(R.color.white));
                    isSaveTo=1;
                }else {
                    condition1.setTextColor(getResources().getColor(R.color.gray));
                    isSaveTo=0;
                }
                break;

            case R.id.condition2:
                if (isChecked){
                    condition2.setTextColor(getResources().getColor(R.color.white));
                    isSee=0;
                }else {
                    condition2.setTextColor(getResources().getColor(R.color.gray));
                    isSee=1;
                }
                break;
        }
    }
}

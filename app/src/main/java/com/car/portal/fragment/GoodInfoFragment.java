package com.car.portal.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.GoodInfoAdapter;
import com.car.portal.adapter.LinePopuGridAdapter;
import com.car.portal.entity.GoodsCityBaseEntity;
import com.car.portal.entity.GoodsInfo_City;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.service.GoodsService;
import com.car.portal.service.UserService;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

@SuppressLint("UseSparseArrays")
public class GoodInfoFragment extends MainBaseFragment {
    @ViewInject(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private View view;
    @ViewInject(R.id.gridview_goodsinfo)
    private GridView gridView;                //内容显示块
    private GoodInfoAdapter goodInfoAdapter = null;
    private ArrayList<GoodsInfo_City> goodsInfo_cities;

    @ViewInject(R.id.line_line)
    private LinearLayout line_line;            //线路分类
    @ViewInject(R.id.line_register)
    private LinearLayout line_register;        //登记者
    @ViewInject(R.id.line_date)
    private LinearLayout line_date;            //时间选择

    private LinearLayout line_popu;       //???????

    @ViewInject(R.id.img_line)
    private ImageView img_line;
    @ViewInject(R.id.img_register)
    private ImageView img_register;
    @ViewInject(R.id.img_date)
    private ImageView img_date;
    @ViewInject(R.id.txt_line)
    private TextView txt_line;
    @ViewInject(R.id.txt_register)
    private TextView txt_register;
    @ViewInject(R.id.txt_date)
    private TextView txt_date;


    private ArrayList<String> arr_citys = new ArrayList<String>();    //路线
    private ArrayList<String> str_date = new ArrayList<String>();    //日期
    private ArrayList<User> companyUsers = new ArrayList<User>();    //当前公司员工
    private ArrayList<String> userList = new ArrayList<String>();    //当前公司员工名字

    private boolean ischeck_line, ischeck_register, ischeck_date;

    private PopupWindow popupWindow = null;

    private View popupView;
    private GridView grid_popu;
    private int route_select = 0, register_select = 0, date_select = 0;
    private String route_select_txt = "", register_select_txt = "", date_select_txt = "";
    private int choose;
    private int pathValue = 0, userId = 0, date = 0;//路线，登记者,日期
    private String condition = "";
    private boolean hasHelloFlag = false;
    private GoodsService goodsService;

    private LinePopuGridAdapter linePopuGridAdapter;


    private boolean hasStarted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.good_info_fragment, null);
        ischeck_line = ischeck_register = ischeck_date = false;
        str_date.clear();
        str_date.add("当天");
        str_date.add("昨天");
        str_date.add("三天");
        str_date.add("五天");
        arr_citys.clear();
        arr_citys.add("全部");
        arr_citys.add("江浙/福建");
        arr_citys.add("加长");
        arr_citys.add("西北/河南");
        arr_citys.add("短程");
        arr_citys.add("重庆/成都");
        arr_citys.add("河北/山西");
        arr_citys.add("山东");
        arr_citys.add("贵州/云南");
        arr_citys.add("湖南/湖北");

        x.view().inject(this, view);

        initListen();
        goodsService = new GoodsService(getActivity());
        if (goodInfoAdapter == null) {
            goodInfoAdapter = new GoodInfoAdapter(getActivity());
        }
        gridView.setEmptyView(view.findViewById(R.id.hind_text));
        gridView.setAdapter(goodInfoAdapter);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if(refreshLayout!=null) {
                refreshLayout.autoRefresh();
            }
        }
    }


    /**
     * 初始化监听事件
     * showPopuWindow(?) 1选择展开路线分类；2展开登记者;3展开时间
     */
    public void initListen() {
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                getCitys();
                goodInfoAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
            }
        });
        getCitys();



        line_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ischeck_line = !ischeck_line;
                if (ischeck_line) {
                    img_line.setBackgroundResource(R.drawable.arrow_down);
                    choose = 1;
                    showPupoWindow();
                } else {
                    img_line.setBackgroundResource(R.drawable.arrow_up);
                    popupWindow.dismiss();
                }
            }
        });
        line_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ischeck_register = !ischeck_register;
                if (ischeck_register) {
                    img_register.setBackgroundResource(R.drawable.arrow_down);
                    choose = 2;
                    showPupoWindow();
                } else {
                    img_register.setBackgroundResource(R.drawable.arrow_up);
                    popupWindow.dismiss();
                }
            }
        });
        line_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ischeck_date = !ischeck_date;
                if (ischeck_date) {
                    img_date.setBackgroundResource(R.drawable.arrow_down);
                    choose = 3;
                    showPupoWindow();
                } else {
                    img_date.setBackgroundResource(R.drawable.arrow_up);
                    popupWindow.dismiss();
                }
            }
        });
    }

    /**
     * 展开路线分类/登记者
     */
    public void showPupoWindow() {
        initPopuWindow();
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(line_line);
    }

    /**
     * 初始化popupwindpw
     * choose  1路线分类；2登记者；3时间
     */
    public void initPopuWindow() {
        popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popu_show_line, null);
        line_popu = (LinearLayout) popupView.findViewById(R.id.grid_bgLine_line);
        grid_popu = (GridView) popupView.findViewById(R.id.grid_popu);
        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                true);
        line_popu.getBackground().setAlpha(100);

        linePopuGridAdapter = new LinePopuGridAdapter(getActivity());
        grid_popu.setAdapter(linePopuGridAdapter);
        if (choose == 1) {
            initLineList();
        } else if (choose == 2) {
            getCompanyUsers();
        } else if (choose == 3) {
            initDate();
        }
        //设置箭头
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("initPopuWindow", "                         " + choose);
                if (choose == 1) {
                    ischeck_line = false;
                    img_line.setBackgroundResource(R.drawable.arrow_up);
                    popupWindow.dismiss();
                } else if (choose == 2) {
                    ischeck_register = false;
                    img_register.setBackgroundResource(R.drawable.arrow_up);
                    popupWindow.dismiss();
                } else if (choose == 3) {
                    ischeck_date = false;
                    img_date.setBackgroundResource(R.drawable.arrow_up);
                    popupWindow.dismiss();
                } else {
                    return;
                }
            }
        });
        grid_popu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (choose == 1) {
                    route_select = position;
                    pathValue = position;
                    getCitys();
                    ischeck_line = false;
                    txt_line.setText(arr_citys.get(position));
                    route_select_txt = arr_citys.get(position);
                    popupWindow.dismiss();
                    img_line.setBackgroundResource(R.drawable.arrow_up);
                } else if (choose == 2) {
                    register_select = position;
                    userId = companyUsers.get(position).getUid();
                    LogUtils.e("GoodsInfoFrag", "    chooseId:" + userId);
                    getCitys();
                    ischeck_register = false;
                    txt_register.setText(companyUsers.get(position).getCname());
                    register_select_txt =companyUsers.get(position).getCname();
                    popupWindow.dismiss();
                    img_register.setBackgroundResource(R.drawable.arrow_up);
                } else if (choose == 3) {
                    date_select = position;
                    date = position;
                    hasHelloFlag = false;
                    getCitysByDate();
                    ischeck_date = false;
                    txt_date.setText(str_date.get(position));
                    date_select_txt = str_date.get(position);
                    popupWindow.dismiss();
                    img_date.setBackgroundResource(R.drawable.arrow_up);
                } else
                    return;
            }
        });
    }

    /**
     * 初始化路线数据
     */
    public void initLineList() {
        linePopuGridAdapter.setSelectId(route_select);
        linePopuGridAdapter.setList(arr_citys);
        linePopuGridAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化时间数据
     */
    public void initDate() {
        linePopuGridAdapter.setSelectId(date_select);
        linePopuGridAdapter.setList(str_date);
        linePopuGridAdapter.notifyDataSetChanged();
    }

    /**
     * 获取内容信息
     */
    public void getCitys() {
        LogUtils.e("GoodsInfoFragment", "getCitys condition:" + condition);
        if (goodsService == null) {
            goodsService = new GoodsService(getActivity());
        }

        goodsService.findGoodsOfCitys(hasHelloFlag, userId, pathValue, condition, new MyCallBack<GoodsCityBaseEntity<ArrayList>>() {
            @Override
            public void onError(Throwable arg0, boolean b) {

            }

            @Override
            public void onSuccess(GoodsCityBaseEntity<ArrayList> arg0) {
                if (!arg0.isOvertime()) {
                    ArrayList<LinkedTreeMap<String, Object>> list = arg0.getTableList();
                    if (list == null) {
                        ToastUtil.show("数据获取出错，请退出再次进入", getActivity());
                    } else {
                        ArrayList<GoodsInfo_City> goodsInfo_cities = new
                                ArrayList<GoodsInfo_City>();
                        for (int i = 0; i < list.size(); i++) {
                            GoodsInfo_City goodsInfo_city = new GoodsInfo_City();
                            LinkMapToObjectUtil.getObject(list.get(i), goodsInfo_city);
                            goodsInfo_cities.add(goodsInfo_city);
                        }
                        if (goodInfoAdapter != null) {
                            goodInfoAdapter.setList(goodsInfo_cities);
                            goodInfoAdapter.notifyDataSetChanged();
                        }
                        if(arg0.getToken()!=null && !"".equals(arg0.getToken())){
                            UserService userService = new UserService(getContext());
                            User us = userService.getSavedUser();
                            us.setToken(arg0.getToken());
                            userService.saveLoginUser(us);
                        }
                    }
                    //SessionStore.setMyToken(arg0.getToken());
                }
            }
        });

    }

    /**
     * 根据日期查找城市信息
     */
    public void getCitysByDate() {
        goodsService.getCityForDate(hasHelloFlag, userId, pathValue, date, new HttpCallBack
                (getActivity()) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                    goodsInfo_cities = (ArrayList<GoodsInfo_City>) objects[0];
                    if (goodInfoAdapter != null) {
                        goodInfoAdapter.setList(goodsInfo_cities);
                        goodInfoAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * 获取公司成员信息
     */
    public void getCompanyUsers() {
        if (userList.size() <= 0) {
            User u = goodsService.getLoginUser();
            int companyId = 0;
            if (u != null) {
                companyId = u.getCompanyId();
            }
            goodsService.getGoods(companyId, new HttpCallBack(getActivity()) {
                @Override
                public void onSuccess(Object... objects) {
                    if (objects != null && objects.length > 0) {
                        companyUsers = (ArrayList<User>) objects[0];
                        User total = new User();
                        total.setUid(0);
                        total.setCname("全部");
                        total.setAlias("全部");
                        total.setCompanyId(1);
                        companyUsers.add(0, total);
                        for (int i = 0; i < companyUsers.size(); i++) {
                            userList.add(companyUsers.get(i).getCname());
                        }
                        linePopuGridAdapter.setSelectId(register_select);
                        linePopuGridAdapter.setList(userList);
                        linePopuGridAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFail(int result, String message, boolean show) {
                    super.onFail(result, message, show);
                }

                @Override
                public void onError(Object... objects) {
                    super.onError(objects);
                    ((Exception) objects[0]).printStackTrace();
                }
            });
        } else {
            linePopuGridAdapter.setSelectId(register_select);
            linePopuGridAdapter.setList(userList);
            linePopuGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void search(String condition) {
        this.condition = condition;
        getCitys();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!StringUtil.isNullOrEmpty(route_select_txt)) {
            txt_line.setText(route_select_txt);
        }
        if(!StringUtil.isNullOrEmpty(register_select_txt)) {
            txt_register.setText(register_select_txt);
        }
        if(!StringUtil.isNullOrEmpty(date_select_txt)) {
            txt_date.setText(date_select_txt);
        }
    }

    @Override
    public void onWindowFoucusChanged(boolean hasFocus) {
        // 都要重新获取城市信息获取
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

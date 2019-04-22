package com.car.portal.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.adapter.AllotResultAdapter;
import com.car.portal.entity.AllotRechargeBean;
import com.car.portal.entity.Qrcodebeen;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.XUtil;
import com.car.portal.service.UserService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 2019年1月
 */
public class ProcomRechargeFragment extends Fragment {
    @BindView(R.id.procommiss_rl_layout)
    RecyclerView mRlLayout;
    @BindView(R.id.procommiss_smalayout)
    SmartRefreshLayout mSmalayout;
    Unbinder unbinder;
    @BindView(R.id.Null_Message_Tips)
    LinearLayout mNullMessageTips;
    @BindView(R.id.commission_statistics)
    TextView commissionStatistics;
    private AllotResultAdapter allotResultAdapter;
    private List<AllotRechargeBean> allotRechargeList = new ArrayList<>();
    //    点击列表数据Id号
    List<Integer> listItemID = new ArrayList<Integer>();
    //    刷新状态
    boolean isRefresh = true;
    //    当前页
    private int currentPage = 1;
    //   每页十行
    private static int pageSize = 10;
    private List mProList = new ArrayList();
    private int id;//数据库唯一ID
    private int uid;//用户编号
    private int money;//金额，以分为单位，显示要除100
    private int pay_uid;//支付者编号
    private int checkout;//是否已提现，0：没提现，1：已提现
    private String ctime;//发生时间
    //用于存储条目数据
    private List<String> list = new ArrayList<>();
    private UserService service;
    private AllotRechargeBean allotRechargeBean;
    private Activity activity;
    private MyHttpUtil util;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.promot_recharge_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        service = new UserService(getActivity());
        initReclyView();
        initView();
        initData();
        initLoadMore();
        return view;
    }

    private void initData() {
        util = new MyHttpUtil(getActivity());
        String url = util.getUrl(R.string.url_allotrechangeAmount);
        XUtil.Post(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Qrcodebeen qrcodebeen = new Gson().fromJson(s, Qrcodebeen.class);
                if (qrcodebeen.getResult() == 1) {
                    commissionStatistics.setText("");
                    commissionStatistics.setText("提成合计:"+qrcodebeen.getData());
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
            }
        });
    }

    private void initView() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageSize", pageSize);
        map.put("currentPage", currentPage);
        User user = service.getSavedUser();
        map.put("uid", user.getUid());
//        allotResultAdapter.notifyDataSetChanged();
        service.findAllotRechange(map, new HttpCallBack(getActivity()) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects == null) {
                    mNullMessageTips.setVisibility(View.VISIBLE);
                } else {
                    mNullMessageTips.setVisibility(View.GONE);
                    allotRechargeList = (ArrayList<AllotRechargeBean>) objects[0];
                    for (AllotRechargeBean bean : allotRechargeList) {
                        allotResultAdapter.addData(bean);
                    }
                    allotResultAdapter.notifyDataSetChanged();
                }


            }
        });
    }


    private void initLoadMore() {
        //刷新
        mSmalayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                allotRechargeList.clear();
                initRefresh();
                refreshlayout.finishRefresh();
            }
        });
        //加载更多
        mSmalayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initLoad();
                refreshlayout.finishLoadmore();
            }
        });
    }

    //加载更多
    private void initLoad() {
        currentPage++;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageSize", pageSize);
        map.put("currentPage", currentPage);
        User user = service.getSavedUser();
        map.put("uid", user.getUid());
        allotResultAdapter.notifyDataSetChanged();
        service.findAllotRechange(map, new HttpCallBack(getActivity()) {
            @Override
            public void onSuccess(Object... objects) {

                allotRechargeList = (ArrayList<AllotRechargeBean>) objects[0];
                for (AllotRechargeBean bean : allotRechargeList) {
                    allotResultAdapter.addData(bean);
                }
                allotResultAdapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 刷新之后在这里请求数据
     */
    private void initRefresh() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageSize", pageSize);
        map.put("currentPage", currentPage);
        User user = service.getSavedUser();
        map.put("uid", user.getUid());
        service.findAllotRechange(map, new HttpCallBack(getActivity()) {
            @Override
            public void onSuccess(Object... objects) {
                allotRechargeList.clear();
                allotResultAdapter.getData().clear();
                allotRechargeList = (ArrayList<AllotRechargeBean>) objects[0];
                for (AllotRechargeBean bean : allotRechargeList) {
                    allotResultAdapter.addData(bean);
                }
                allotResultAdapter.notifyDataSetChanged();

            }
        });
    }


    private void initReclyView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRlLayout.setLayoutManager(linearLayoutManager);
        allotResultAdapter = new AllotResultAdapter(R.layout.item_allot_rechange, allotRechargeList);
        mRlLayout.setAdapter(allotResultAdapter);
// TODO: 2019/1/24 0024 预留功能
        allotResultAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Toast.makeText(getActivity(), "点击触发子控件主布局", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

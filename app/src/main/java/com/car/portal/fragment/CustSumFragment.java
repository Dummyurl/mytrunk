package com.car.portal.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.adapter.ProcomMissAdapter;
import com.car.portal.entity.ProcomMissBean;
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
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 2019年1月
 */
public class CustSumFragment extends Fragment {
    @BindView(R.id.procommiss_rl_layout)
    RecyclerView mRlLayout;
    @BindView(R.id.procommiss_smalayout)
    SmartRefreshLayout mSmalayout;
    Unbinder unbinder;
    @BindView(R.id.add_all)
    TextView mAddAll;
    @BindView(R.id.close_all)
    TextView mCloseAll;
    @BindView(R.id.notice_recharge)
    TextView mRecharge;
    @BindView(R.id.procommiss_bottom_window)
    LinearLayout mBottomWindow;
    @BindView(R.id.edit_text)
    LinearLayout editText;
    @BindView(R.id.close_windows)
    LinearLayout mCloseWindows;
    @BindView(R.id.Null_Message_Tips)
    LinearLayout mNullMessageTips;
    @BindView(R.id.Procom_statistics)
    TextView ProcomStatistics;
    private ProcomMissAdapter mProcomMissAdapter;
    private Activity activity;
    private List<ProcomMissBean> missBeanList = new ArrayList<>();

    //    点击列表数据Id号
    List<Integer> listItemID = new ArrayList<Integer>();
    //    刷新状态
    boolean isRefresh = true;
    //    每页10行
    private static int pageSize = 10;
    //    当前页
    private int currentPage = 1;
    private List mProList = new ArrayList();
    //照片
    private int[] mImages;
    // 名字
    private String[] mName;
    //  公司
    private String[] mCompany;
    //  用户类型（司机/货主）
    private int[] mUserType;
    //    省市
    private String[] mCity;
    //    电话号码
    private String[] mTel;
    // 审核状态
    private int[] mAuth;
    //    充值金额
    private int[] money;
    //    注册日期
    private String[] regDate;
    //    checkBox状态
    private boolean isCheck = false;

    private String type;

    //用于存储条目数据
    private List<String> list = new ArrayList<>();
    //用户存储条目的选择状态
    private Map<Integer, Boolean> isCheckd = new HashMap<>();
    public Map<Integer, Boolean> isChecks;
    //用于存放已选择的条目
    private List<Integer> selectList = new ArrayList<>();

    private UserService service;
    private MyHttpUtil util;
    private ProcomMissBean mProcomMissBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.promot_summary_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        inittype();
        initReclyView();
        service = new UserService(getActivity());

        initView();
        //initTestMessage();
        initData();
        initLoadMore();
        return view;
    }

    private void initData() {
        util = new MyHttpUtil(getActivity());
        String url = util.getUrl(R.string.url_saleRechangeAmount);
        XUtil.Post(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Qrcodebeen qrcodebeen = new Gson().fromJson(s, Qrcodebeen.class);
                if (qrcodebeen.getResult() == 1) {
                    ProcomStatistics.setText("");
                    ProcomStatistics.setText("充值合计:"+qrcodebeen.getData());
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
        service.findSalesRechange(map, new HttpCallBack(getActivity()) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects == null) {
//                    mSmalayout.setVisibility(View.GONE);

                } else {
                    missBeanList = (ArrayList<ProcomMissBean>) objects[0];
                    if(missBeanList.size()==0){
                        Toast.makeText(getActivity(), "您还未推广用户", Toast.LENGTH_SHORT).show();
                    }else {
                        mNullMessageTips.setVisibility(View.GONE);
                        for (ProcomMissBean bean : missBeanList) {
                            mProcomMissAdapter.addData(bean);
                        }
                        mProcomMissAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
    }

    private void inittype() {
        if (isCheck) {
            mBottomWindow.setVisibility(View.VISIBLE);
//            editText.setVisibility(View.GONE);
        } else {
            mBottomWindow.setVisibility(View.GONE);
//            editText.setVisibility(View.VISIBLE);
        }
    }


    private void initLoadMore() {
        //刷新
        mSmalayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

//             for (int i = 0; i<20; i++) {
//            ProcomMissBean procomMissBean = new ProcomMissBean(mImages[i], mName[i], mTel[i], mCity[i], mCompany[i], mUserType[i], regDate[i], mAuth[i], money[i],false);
//            missBeanList.add(procomMissBean);
//        }
//        mProcomMissAdapter.notifyDataSetChanged();
//                mProcomMissAdapter.removeAllHeaderView();
//                mProcomMissAdapter.removeAllFooterView();
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

    //下拉加载
    private void initLoad() {
        currentPage++;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageSize", pageSize);
        map.put("currentPage", currentPage);
        User user = service.getSavedUser();
        map.put("uid", user.getUid());
        service.findSalesRechange(map, new HttpCallBack(getActivity()) {
            @Override
            public void onSuccess(Object... objects) {
                missBeanList = (ArrayList<ProcomMissBean>) objects[0];
                for (ProcomMissBean bean : missBeanList) {
                    mProcomMissAdapter.addData(bean);
                }
                mProcomMissAdapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 刷新之后在这里请求数据
     */
    private void initRefresh() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageSize", pageSize);
        map.put("currentPage", 1);
        User user = service.getSavedUser();
        map.put("uid", user.getUid());
        service.findSalesRechange(map, new HttpCallBack(getActivity()) {
            @Override
            public void onSuccess(Object... objects) {
                missBeanList.clear();
                mProcomMissAdapter.getData().clear();
                missBeanList = (ArrayList<ProcomMissBean>) objects[0];
                for (ProcomMissBean bean : missBeanList) {
                    mProcomMissAdapter.addData(bean);
                }
                mProcomMissAdapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 测试数据
     */
    private void initTestMessage() {
        mImages = new int[]{R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
                R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
                R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
                R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
                R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};

        mName = new String[]{"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨",
                "朱", "秦", "尤", "许"};

        mCompany = new String[]{"货满车测试公司", "浩发有限公司", "义浩有限公司", "长圣有限公司 ", " 金庆有限公司", "汇中有限公司",
                "成同有限公司", "信长有限公司", "晶安有限公司", " 庆合有限公司", "汇恒有限公司", " 国宏有限公司 ", "多辉有限公司",
                "永多有限公司 ", "盈凯有限公司", "安巨有限公司", "发宝有限公司", "欣鼎有限公司 ", "满安有限公司", "宏康有限公司"};

        mUserType = new int[]{1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0};

        mAuth = new int[]{1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0};


        mTel = new String[]{" 1928737772", " 1928737772", "18122604989", " 1928737772", "18122604989", " 1928737772", "18122604989", " 1928737772", "18122604989", "18122604989", "18122604989", " 1928737772",
                "18122604989", " 1928737772", "18122604989", " 1928737772", "18122604989", " 1928737772", " 1928737772", " 1928737772"};
        regDate = new String[]{"2018年3月3日", "2018年8月9日", "2018年9月9日", "2017年7月7日", "2018年3月3日", "2018年8月9日", "2018年9月9日", "2017年7月7日",
                "2018年3月3日", "2018年8月9日", "2018年9月9日", "2017年7月7日", "2018年3月3日", "2018年8月9日", "2018年9月9日", "2017年7月7日",
                "2018年3月3日", "2018年8月9日", "2018年9月9日", "2017年7月7日"};
        money = new int[]{60, 66, 99, 120, 999, 666, 55, 88, 888, 100, 60, 66, 99, 120, 999, 666, 55, 88, 888, 100};

        mCity = new String[]{"广州", "佛山", "惠州", "湛江", "台湾省", "广州", "佛山", "惠州", "湛江", "台湾省", "广州", "佛山", "惠州", "湛江", "台湾省", "广州", "佛山", "惠州", "湛江", "台湾省"};
//        Random random=new Random();
//       mUserType=random.nextInt(2);
//初始化测试数据
//        for (int i = 0; i < 20; i++) {
//            ProcomMissBean procomMissBean = new ProcomMissBean(mImages[i], mName[i], mTel[i], mCity[i], mCompany[i], mUserType[i], regDate[i], mAuth[i], money[i],false);
//            missBeanList.add(procomMissBean);
//           final boolean ss=true;
//        }
    }

    private void initReclyView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRlLayout.setLayoutManager(linearLayoutManager);
        mProcomMissAdapter = new ProcomMissAdapter(R.layout.item_procoming_miss, missBeanList);
        mRlLayout.setAdapter(mProcomMissAdapter);
//    主布局点击回调
        mProcomMissAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

//                Toast.makeText(ProcommissActivty.this, "点击触发子控件主布局", Toast.LENGTH_SHORT).show();
            }
        });

        //初始化子控件点击checkbox回调
        mProcomMissAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            private int currentNum = -1;

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
////                默认未选中
                for (ProcomMissBean bean : missBeanList) {
                    bean.setCheck(false);
                }
                if (currentNum == -1) { // 选中
                    missBeanList.get(position).setCheck(true);
                    currentNum = position;
                } else if (currentNum == position) { // 同一个item选中变未选中
                    for (ProcomMissBean bean : missBeanList) {
                        bean.setCheck(false);
                    }
                    currentNum = -1;
                } else if (currentNum != position) { // 不是同一个item选中当前的，去除上一个选中的
                    for (ProcomMissBean bean : missBeanList) {
                        bean.setCheck(false);
                    }
                    missBeanList.get(position).setCheck(true);
                    currentNum = position;
                }
//                if (mProcomMissBean.isCheck()) {
//                    mProcomMissBean.setCheck(false);
//                    for (int i = 0; i < missBeanList.size(); i++) {
//                        if (missBeanList.get(i).getId() == mProcomMissBean.getId()) {
//                            missBeanList.remove(i);
//                            mProcomMissAdapter.notifyDataSetChanged();
                Log.d("单条数据被点击", "onItemChildClick: " + position);
//                Toast.makeText(getActivity(), "这个是checkBox的点击事件", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
            }
        });
    }


    /**
     * 编辑、取消编辑
     *
     * @param view
     */
    public void btnEditList(View view) {

        mProcomMissAdapter.flage = !mProcomMissAdapter.flage;

        if (mProcomMissAdapter.flage) {
            mBottomWindow.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
            mProcomMissAdapter.notifyDataSetChanged();
        } else {
            mBottomWindow.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
        }

        mProcomMissAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.add_all, R.id.close_all, R.id.notice_recharge, R.id.edit_text, R.id.close_windows})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_all:
                addall();
                break;
            case R.id.close_all:
                closeall();
                break;
            case R.id.notice_recharge:
                // TODO: 2019/1/21 0021通知充值
                break;
            case R.id.edit_text:
                btnEditList(view);
//                isCheck = false;
//                mBottomWindow.setVisibility(View.VISIBLE);
//                editText.setVisibility(View.GONE);
                break;
            case R.id.close_windows:
//                isCheck = true;
//                mBottomWindow.setVisibility(View.GONE);
//                editText.setVisibility(View.VISIBLE);
                btnEditList(view);
                break;

        }
    }

    private void addall() {
        int curNum = -1;
        if (curNum == -1) {
            for (ProcomMissBean bean : missBeanList) {
                bean.isCheck = true;
            }
            curNum = 0;
        } else if (curNum == 0) {
            for (ProcomMissBean bean : missBeanList) {
                bean.isCheck = false;
            }
        }
        curNum = -1;
        mProcomMissAdapter.notifyDataSetChanged();
    }

    private void closeall() {
        int curNum = -1;
        if (curNum == -1) {
            for (ProcomMissBean bean : missBeanList) {
                bean.isCheck = false;
            }
            curNum = 0;
        } else if (curNum == 0) {
            for (ProcomMissBean bean : missBeanList) {
                bean.isCheck = true;
            }
        }
        curNum = -1;
        mProcomMissAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

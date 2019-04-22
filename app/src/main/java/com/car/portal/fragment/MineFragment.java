package com.car.portal.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.car.portal.R;
import com.car.portal.activity.AgreementActivity;
import com.car.portal.activity.AuthorActivity;
import com.car.portal.activity.CheckGoodsActivity;
import com.car.portal.activity.MapDriverActivity;
import com.car.portal.activity.PairedListActivity;
import com.car.portal.activity.SettlementActivity;
import com.car.portal.activity.StatisticsActivity;
import com.car.portal.adapter.MyPagerAdapter;

import java.util.ArrayList;

public class MineFragment extends MainBaseFragment {
    //    @ViewInject(R.id.line_statistics)
    private LinearLayout line_statistics;
    //    @ViewInject(R.id.line_map)
    private LinearLayout line_map;
    //    @ViewInject(R.id.line_link)
    private LinearLayout line_link;
    //    @ViewInject(R.id.line_calculations)
    private LinearLayout line_calculations;
    //    @ViewInject(R.id.line_agreement)
    private LinearLayout line_agreement;
    //    @ViewInject(R.id.viewpager)
    private ViewPager mPager;
    private LinearLayout line_author;
    private LinearLayout line_checkGoods;
    private View view;
    /**
     * 推荐墙的点布局
     */

    private ArrayList<ImageView> listViews;
    /**
     * 更新消息
     */
    private static final int PROGRESS = 1;
    /**
     * 推荐墙页面切换速度
     */
    private static final int PAGEALTSPEED = 5000;
    /**
     * 上一次显示页面的位置
     */
    private int mLastIndex;
    //    private LinearLayout line_link,line_map,line_calculations,line_agreement;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.mine_frag, null);
        initView();
        initLisner();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 初始化控件
     */
    public void initView() {
        // 推荐墙
        mPager = (ViewPager) view.findViewById(R.id.viewpager);
        line_link = (LinearLayout) view.findViewById(R.id.line_link);
        line_map = (LinearLayout) view.findViewById(R.id.line_map);
        line_calculations = (LinearLayout) view.findViewById(R.id.line_calculations);
        line_agreement = (LinearLayout) view.findViewById(R.id.line_agreement);
        line_statistics = (LinearLayout) view.findViewById(R.id.line_statistics);
        line_author = (LinearLayout) view.findViewById(R.id.line_author);
        line_checkGoods=view.findViewById(R.id.checkGoods);
    }

    /**
     * 控件监听事件
     */
    public void initLisner() {
        line_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), PairedListActivity.class);
                startActivity(intent);
            }
        });
        line_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), MapDriverActivity.class);
                startActivity(intent);
            }
        });
        line_calculations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), SettlementActivity.class);
                startActivity(intent);
            }
        });
        line_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), AgreementActivity.class);
                startActivity(intent);
            }
        });
        line_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), StatisticsActivity.class);
                startActivity(intent);
            }
        });
        line_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), AuthorActivity.class);
                startActivity(intent);
            }
        });
        line_checkGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), CheckGoodsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化ViewPager
     */

    @SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PROGRESS) { // 自动滑动页面
                // 滚动页面
            	mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            	mHandler.sendEmptyMessageDelayed(PROGRESS, PAGEALTSPEED);
            }
        }
    };

    @Override
    public void search(String condition) {

    }

    @Override
    public void onWindowFoucusChanged(boolean hasFocus) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}

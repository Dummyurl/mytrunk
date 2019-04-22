package com.car.portal.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.car.portal.R;
import com.car.portal.contract.CommonlyusedContract;
import com.car.portal.entity.Goods_For_Address;
import com.car.portal.entity.ParnerShip;
import com.car.portal.entity.commonlyusedbean;
import com.car.portal.entity.newOrder;
import com.car.portal.model.CommonlyusedModel;
import com.car.portal.presenter.CommonlyusedPresenter;
import com.car.portal.view.ChildViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CommonlyusedFragment extends Fragment implements CommonlyusedContract.View {
    View view;
    @BindView(R.id.refresh_commonly)
    SwipeRefreshLayout refreshCommonly;
    @BindView(R.id.recy_commonly_used)
    RecyclerView recyclerView;
    private CommonlyusedContract.Presenter mpresenter;
    private List<commonlyusedbean> datalist = new ArrayList<>();
    CommonlyusedModel.CommonlyAdapter adapter;
    TimePickerView pvTime;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.from(getActivity()).inflate(
                    R.layout.activity_commonly_used, null);
            new CommonlyusedPresenter(this,getContext());
            initView();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    private void initView() {
        ButterKnife.bind(this, view);
        refreshCommonly.setColorSchemeResources(R.color.view_blue,R.color.colorAcceet);
        refreshCommonly.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mpresenter.getGoodsNormal(getActivity());
                refreshCommonly.setRefreshing(false);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommonlyusedModel.CommonlyAdapter(datalist,getContext());
        recyclerView.setAdapter(adapter);
        mpresenter.getGoodsNormal(getContext());
        final DeliverTabFragment deliverTabFragment = (DeliverTabFragment)this.getParentFragment();
        adapter.setOnItemClickListener(new CommonlyusedModel.CommonlyAdapter.OnItemOnClickListener() {
            @Override
            public void onItemOnClick(View view, final commonlyusedbean pos) {
                assert deliverTabFragment != null;
                deliverTabFragment.setFragment(new DeliverTabFragment.GotoFragment() {
                    @Override
                    public void switchFragment(ChildViewPager viewPager) {
                        deliverTabFragment.setDataex(pos);
                        viewPager.setCurrentItem(0);
                    }
                });
                deliverTabFragment.forSkip();
            }

            @Override
            public void ondelItemclick(View view, final int id, final int position) {
                   new MaterialDialog.Builder(getActivity())
                           .title("删除")
                           .content("是否删除该条常用货源？该操作不可逆，删除后无法恢复")
                           .contentColor(getResources().getColor(R.color.title_black))
                           .positiveText("删除")
                           .negativeText("再考虑")
                           .onPositive(new MaterialDialog.SingleButtonCallback() {
                               @Override
                               public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                   adapter.removeItem(position);
                                   mpresenter.deleteGoodsNormal(getContext(),id);
                               }
                           }).show();
            }

            @Override
            public void onsendItemclick(View view, final int id) {
                Toast toast = Toast.makeText(getContext(), "请选择装车时间", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Calendar selectedDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                startDate.set(2014, 1, 1);
                Calendar endDate = Calendar.getInstance();
                endDate.set(2050, 12, 31);

                TimePickerView pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                       mpresenter.onkeysendGoods(getActivity(),id,getTime(date));
                    }


                })       .setDate(selectedDate)
                        .setRangDate(startDate,endDate)
                        .build();
                pvTime.show();
            }
        });
    }

    @Override
    public void setPresenter(CommonlyusedContract.Presenter presenter) {
        mpresenter = presenter;
    }


    private String getTime(Date date) {
        String format  = "yyyy-MM-dd ";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
/**
 *
 *
 *@作者：舒椰
 *是否每次打开页面都刷新数据？
 *@时间： 2019/4/17 0017 上午 11:27
 */


//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (getUserVisibleHint()) {
//            requestData();
//        }
//    }
//
//    public void requestData() {
//        refreshCommonly.post(new Runnable() {
//            @Override
//            public void run() {
//                refreshCommonly.setRefreshing(true);
//                mpresenter.getGoodsNormal(getContext());
//            }
//        });
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void setGoodsNormal(List<commonlyusedbean> list) {
        if(refreshCommonly.isRefreshing()){
            refreshCommonly.setRefreshing(false);
        }
        datalist.clear();
        datalist.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setdeleteNormal(String d) {

    }

    @Override
    public void setonkeysend(String d) {
        Toast.makeText(getActivity(), d, Toast.LENGTH_SHORT).show();
    }
}

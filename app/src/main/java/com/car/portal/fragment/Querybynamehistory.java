package com.car.portal.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.car.portal.R;
import com.car.portal.activity.QuerybynameActivity;
import com.car.portal.entity.Historyquerylinkman;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Querybynamehistory extends Fragment {

    @BindView(R.id.chip_query_group)
    ChipGroup chipQueryGroup;
    Unbinder unbinder;
    @BindView(R.id.img_delect_history)
    ImageView imgDelectHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, null);
        unbinder = ButterKnife.bind(this, view);
        initview();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initview() {
        final List<Historyquerylinkman> list = LitePal.findAll(Historyquerylinkman.class);
        if(list.size()==0){
            imgDelectHistory.setVisibility(View.GONE);
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            final Chip tabChip = new Chip(getActivity());
            tabChip.setChipText(list.get(i).getTerms());
            tabChip.setCloseIcon(getResources().getDrawable(R.drawable.ic_close));
            tabChip.setCloseIconEnabled(true);
            final int finalI = i;
            tabChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QuerybynameActivity activity = (QuerybynameActivity) getActivity();
                    activity.setLinkmanname(list.get(finalI).getTerms());
                }
            });
            tabChip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(finalI).delete();
                    tabChip.setVisibility(View.GONE);
                }
            });
            chipQueryGroup.addView(tabChip);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.img_delect_history)
    public void onViewClicked() {
        new MaterialDialog.Builder(getActivity()).title("清除")
                .contentColor(getResources().getColor(R.color.title_black))
                .content("是否清空历史记录？")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        chipQueryGroup.setVisibility(View.GONE);
                        LitePal.deleteAll(Historyquerylinkman.class);
                    }
                }).show();
    }
}

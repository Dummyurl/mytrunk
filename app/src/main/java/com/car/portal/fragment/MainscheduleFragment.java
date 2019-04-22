package com.car.portal.fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.car.portal.R;
import com.car.portal.entity.MainscheduleEntity;


public class MainscheduleFragment extends DialogFragment {

    private View view;
    private ImageView driverHeadImg,driverInfoImg,driverCheckImg,companyInfoImg;


    public static MainscheduleFragment newInstance(MainscheduleEntity data) {
        MainscheduleFragment fragment = new MainscheduleFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data",data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_dialogs_main, null);
        Button closs_btn = view.findViewById(R.id.closs_btn);
        MainscheduleEntity data = getArguments().getParcelable("data");
        driverCheckImg = view.findViewById(R.id.driver_check_img);
        driverHeadImg = view.findViewById(R.id.driver_head_img);
        driverInfoImg = view.findViewById(R.id.driver_info_img);
        companyInfoImg = view.findViewById(R.id.company_info_img);
        showDialog(data);
        closs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void showDialog(MainscheduleEntity mainscheduleEntity) {
        if (mainscheduleEntity.isHeadImg() == true) {
            Glide.with(view.getContext()).load(R.drawable.ture).into(driverHeadImg);
        } else {
            Glide.with(view.getContext()).load(R.drawable.img_false).into(driverHeadImg);
        }

        if (mainscheduleEntity.isDriver_info() == true) {
            Glide.with(view.getContext()).load(R.drawable.ture).into(driverInfoImg);
        } else {
            Glide.with(view.getContext()).load(R.drawable.img_false).into(driverInfoImg);
        }

        if (mainscheduleEntity.isDriver_check() == true) {
            Glide.with(view.getContext()).load(R.drawable.ture).into(driverCheckImg);
        } else {
            Glide.with(view.getContext()).load(R.drawable.img_false).into(driverCheckImg);
        }

        if (mainscheduleEntity.isCompany_info() == true) {
            Glide.with(view.getContext()).load(R.drawable.ture).into(companyInfoImg);
        } else {
            Glide.with(view.getContext()).load(R.drawable.img_false).into(companyInfoImg);
        }
    }

}

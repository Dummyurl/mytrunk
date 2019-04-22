package com.car.portal.adapter;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.Boss;
import com.car.portal.entity.CarArrived;
import com.car.portal.util.LogUtils;

public class DriverForAddressAdapter extends BaseAdapter {
    private List<CarArrived> carArriveds;
    private Context context;
    private ImageClickListener imageClick;
    public DriverForAddressAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return carArriveds == null ? 0 : carArriveds.size();
    }

    @Override
    public Object getItem(int position) {
        if (carArriveds != null && carArriveds.size() > 0)
            return carArriveds.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_driver_for_addr, null);
            viewHolder = new ViewHolder();
            viewHolder.txt_driver_name = (TextView) convertView.findViewById(R.id.txt_contacts_name);
            viewHolder.txt_target_point = (TextView) convertView.findViewById(R.id.txt_target_point);
            viewHolder.txt_unloading_point = (TextView) convertView.findViewById(R.id.txt_unloading_point);
            viewHolder.txt_arrive_date = (TextView) convertView.findViewById(R.id.txt_arrive_date);
            viewHolder.txt_car_length = (TextView) convertView.findViewById(R.id.txt_car_length);
            viewHolder.txt_remark = (TextView) convertView.findViewById(R.id.txt_remark);
            viewHolder.txt_instop = (TextView) convertView.findViewById(R.id.txt_instop);
            viewHolder.img_call = convertView.findViewById(R.id.img_call);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        final CarArrived carArrived = carArriveds.get(position);
        viewHolder.txt_driver_name.setText(carArrived.getDriverName());
        viewHolder.txt_arrive_date.setText("到达日期 " + carArrived.getArriveDate());

        viewHolder.img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageClick != null) {
                    imageClick.onClick(view,    carArrived );
                }
            }
        });

        if (carArrived.getLength2()!=null)
            viewHolder.txt_car_length.setText(carArrived.getLength()+"~"+carArrived.getLength2() + "米");
        else
            viewHolder.txt_car_length.setText(carArrived.getLength() + "米");

        if (carArrived.getMemo() != null && !carArrived.getMemo().equals(""))
            viewHolder.txt_remark.setText(carArrived.getMemo());
        else
            viewHolder.txt_remark.setText("暂无");
        viewHolder.txt_target_point.setText( carArrived.getTargetLocationC());
        viewHolder.txt_unloading_point.setText(carArrived.getNowLocationC());
        if(carArrived.getInStop() == null)
            viewHolder.txt_instop.setVisibility(View.GONE);
        else if (carArrived.getInStop().equals("off"))
            viewHolder.txt_instop.setText("未进入停车场");
        else
            viewHolder.txt_instop.setText("停车场内");
        LogUtils.e("DriverAddre", "    isStop:" + carArrived.getInStop());


        return convertView;
    }

    public void setList(List<CarArrived> carArriveds){
        if (carArriveds!=null)
            this.carArriveds=carArriveds;
        else
            this.carArriveds=new ArrayList<CarArrived>();
    }

    public class ViewHolder{
        TextView txt_driver_name,txt_target_point,txt_unloading_point,txt_arrive_date,txt_car_length,txt_remark, txt_instop;
        ImageView img_call;
    }
    
    public void addValue(CarArrived arrived) {
    	if(arrived != null && carArriveds != null && !carArriveds.contains(arrived)) {
    		carArriveds.add(arrived);
    	}
    }
    
    public void removeValue(CarArrived arrived) {
    	if(arrived != null && carArriveds != null && carArriveds.contains(arrived)) {
    		carArriveds.remove(arrived);
    	}
    }
    public void setImageClick(ImageClickListener clickListener) {
        imageClick = clickListener;
    }

    public static interface ImageClickListener {
        void onClick(View v, CarArrived b);
    }
}

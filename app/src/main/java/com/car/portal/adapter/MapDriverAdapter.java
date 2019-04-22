package com.car.portal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.car.portal.R;
import com.car.portal.activity.MapDriverListActivity;
import com.car.portal.entity.CarArrived;
import com.car.portal.service.DriverService;
import com.car.portal.util.PhoneCallUtils;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by User on 2016/7/7.
 */
public class MapDriverAdapter extends BaseAdapter {

    private Context context;
    private List<? extends Map<String, Object>> datas;
    private DriverService driverService;
    private ImageClickListener imageClick;
    public MapDriverAdapter(Context context) {
        this.context = context;
        driverService = new DriverService(context);
    }

    @Override
    public int getCount () {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem (int position) {
        int i = datas==null?0:datas.size();
        if(position>i || position <i)
            return datas.get(0);
        else
            return datas.get(position-1);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.map_driver_item, null);
            holder.driverName =  convertView.findViewById(R.id.driverName);
            holder.driverRoute =  convertView.findViewById(R.id.driverRoute);
            holder.carLength =  convertView.findViewById(R.id.carLength);
            holder.targetLocationC = convertView.findViewById(R.id.targetLocationC);
            holder.driver_img = convertView.findViewById(R.id.driver_img);
            holder.timeDiff = convertView.findViewById(R.id.timeDiff);
            holder.img_callphone = convertView.findViewById(R.id.img_callphone);
            holder.car_number = convertView.findViewById(R.id.car_number);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final Map<String, Object> map = datas.get(position);
        if(map != null) {
            if(1 == ((Double) map.get("types")).intValue()) {
                holder.driverName.setText(StringUtil.createImageString(map.get("driverName").toString(), R.drawable.phone_icon, context));
            } else {
                holder.driverName.setText(map.get("driverName").toString());
            }
            String len = (map.get("length") == null ? "" : ((Double)map.get("length")).intValue() + (
                    ((Double) map.get("length2")).intValue() <= 0 ? "" : "-" + ((Double)map.get("length2")).intValue()));
            holder.carLength.setText("车长："+len);
            holder.driverRoute.setText("起点："+map.get("route").toString());
            holder.targetLocationC.setText("前往："+map.get("targetLocationC").toString());
            holder.timeDiff.setText(map.get("timeDiff").toString());
            if(map.get("carId")!=null) {
                holder.car_number.setText(map.get("carId").toString());
            }else{
                holder.car_number.setText("");
            }
            if(map.get("personImage")!=null&& !"".equals(map.get("personImage"))) {
                Glide.with(context).load(driverService.getServer() + map.get("personImage").toString()).into(holder.driver_img);
            }
            holder.img_callphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imageClick != null) {
                        imageClick.onClick(v, map.get("tels"));
                    }
                }
            });
        }
        return convertView;
    }

    class Holder{
        TextView driverName;
        TextView carLength,car_number;
        TextView driverRoute,targetLocationC,timeDiff;
        RoundedImageView driver_img;
        ImageView img_callphone;
    }

    public void setList(List<? extends  Map<String, Object>> list) {
        datas = list;
    }


    public void setImageClick(MapDriverAdapter.ImageClickListener clickListener) {
        imageClick = clickListener;
    }

    public static interface ImageClickListener {
        void onClick(View v, Object b);
    }
}

package com.car.portal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.CommissionList;

import java.util.List;

/**
 * Created by Admin on 2016/6/16.
 */
public class CommistionAdapter extends BaseAdapter{
    private Context context;
    private List<CommissionList> commissionLists;

    public CommistionAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return commissionLists == null ? 0 : commissionLists.size();
    }

    @Override
    public Object getItem(int position) {
        if (commissionLists != null && commissionLists.size() > 0){
            return commissionLists.get(position);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.commission_list_item, null);
            viewHolder.txt_boss_name = (TextView) convertView.findViewById(R.id.txt_boss_name);
            viewHolder.txt_contacts_name = (TextView) convertView.findViewById(R.id.txt_contacts_name);
            viewHolder.txt_target_point = (TextView) convertView.findViewById(R.id.txt_target_point);
            viewHolder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);
            viewHolder.txt_money = (TextView) convertView.findViewById(R.id.txt_money);
            viewHolder.img_isinvaild = (ImageView) convertView.findViewById(R.id.img_isinvaild);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (commissionLists != null && commissionLists.size() >0){
            CommissionList commissionList = commissionLists.get(position);
            viewHolder.txt_boss_name.setText(commissionList.getBossName());
            viewHolder.txt_contacts_name.setText(commissionList.getDriverName());
            viewHolder.txt_date.setText(commissionList.getContractDate());
            viewHolder.txt_money.setText(commissionList.getMoney() + "");
            viewHolder.txt_target_point.setText(commissionList.getRoute());
            if (commissionList.getIsV().equals("1")){
                viewHolder.img_isinvaild.setVisibility(View.VISIBLE);
            } else {
                viewHolder.img_isinvaild.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    public void setList(List<CommissionList> list){
        if (list != null && list.size() > 0){
            this.commissionLists = list;
        }
    }

    private class ViewHolder{
        TextView txt_contacts_name, txt_boss_name, txt_target_point, txt_date, txt_money;
        ImageView img_isinvaild;
    }
}

package com.car.portal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.Money;

import java.util.List;

/**
 * Created by Admin on 2016/6/22.
 */
public class ContactMoneyAdapter extends BaseAdapter {
    private Context context;
    private List<Money> moneys;

    public ContactMoneyAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return moneys == null ? 0 : moneys.size();
    }

    @Override
    public Object getItem(int position) {
        if (moneys != null && moneys.size() > 0){
            return moneys.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_money_item, null);
            viewHolder = new ViewHolder();
//            viewHolder.txt_month = (TextView) convertView.findViewById(R.id.txt_month);
            viewHolder.txt_singular = (TextView) convertView.findViewById(R.id.txt_singular);
            viewHolder.txt_commission = (TextView) convertView.findViewById(R.id.txt_commission);
            viewHolder.txt_year = (TextView) convertView.findViewById(R.id.txt_year);
            viewHolder.txt_achievement = (TextView) convertView.findViewById(R.id.txt_achievement);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Money money = moneys.get(position);
        StringBuffer year = new StringBuffer(7);
        year.append(money.getYear());
        year.append("-");
        if(money.getMonth().length() < 2) {
        	year.append("0");
        }
        year.append(money.getMonth());
        viewHolder.txt_year.setText(year.toString());
//        viewHolder.txt_month.setText(money.getMonth());
        viewHolder.txt_singular.setText(money.getDs());
        viewHolder.txt_commission.setText(money.getTc());
        viewHolder.txt_achievement.setText(money.getYi());
        return convertView;
    }

    public void setList(List<Money> list){
        if (list != null){
            this.moneys = list;
        }
    }

    private class ViewHolder{
        TextView txt_singular, txt_commission, txt_year, txt_achievement;
    }
}

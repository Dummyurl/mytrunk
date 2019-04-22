package com.car.portal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.car.portal.R;
import com.car.portal.entity.ContractList;
import com.car.portal.entity.PairedList;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AgreementListAdapter extends BaseAdapter {
    private Context context;
    private List<ContractList> agreementLists;

    public AgreementListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return agreementLists == null ? 0 : agreementLists.size();
    }

    @Override
    public Object getItem(int position) {
        return agreementLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.agreement_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txt_contacts_name = (TextView) convertView.findViewById(R.id.txt_contacts_name);
            viewHolder.txt_target_point = (TextView) convertView.findViewById(R.id.txt_target_point);
            viewHolder.txt_agreement_data = (TextView) convertView.findViewById(R.id.txt_agreement_data);
            viewHolder.txt_boss_name = (TextView) convertView.findViewById(R.id.txt_boss_name);
            viewHolder.txt_card = (TextView) convertView.findViewById(R.id.txt_card);
            viewHolder.img_isinvaild = (ImageView) convertView.findViewById(R.id.img_isinvaild);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ContractList con = agreementLists.get(position);
        viewHolder.txt_contacts_name.setText(con.getDriverName() == null ? "" : con.getDriverName());
        viewHolder.txt_target_point.setText(con.getTargetAddress() == null ? "" : con.getTargetAddress());
        viewHolder.txt_agreement_data.setText(con.getContractDate() == null ? "" : con.getContractDate());
        viewHolder.txt_card.setText(con.getDriverLicense() == null ? "" : con.getDriverLicense());
        viewHolder.txt_boss_name.setText(con.getBossName() == null ? "" : con.getBossName());
        if (con.getInvalid()){
            viewHolder.img_isinvaild.setVisibility(View.VISIBLE);
        } else {
            viewHolder.img_isinvaild.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView txt_contacts_name, txt_target_point, txt_agreement_data, txt_boss_name, txt_card;
        ImageView img_isinvaild;
    }

    public void setData(List<ContractList> list) {
        if (list != null) {
            agreementLists = list;
        } else {
            agreementLists = new ArrayList<ContractList>();
        }
    }

    public void addValue(ContractList value) {
        if(agreementLists == null) {
            agreementLists = new ArrayList<ContractList>();
        }
        if(value != null && !agreementLists.contains(value)) {
            agreementLists.add(value);
        }
    }

    public void removeValue(ContractList value) {
        if(agreementLists != null && value != null && agreementLists.contains(value)) {
            agreementLists.remove(value);
        }
    }
}

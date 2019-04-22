package com.car.portal.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.GoodsTypes;
import com.car.portal.entity.Goods_For_Address;
import com.car.portal.view.drop.WaterDrop;

import java.util.ArrayList;
import java.util.List;

public class GoodForAddressAdapter extends BaseAdapter{
    private Context context;
    private List<Goods_For_Address> goods_for_addresses;

    public GoodForAddressAdapter(){

    }
    public GoodForAddressAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return goods_for_addresses == null ? 0 : goods_for_addresses.size();
    }

    @Override
    public Object getItem(int position) {
        if (goods_for_addresses != null && goods_for_addresses.size()>0)
            return goods_for_addresses.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_good_address,null);
            viewHolder=new ViewHolder();
            viewHolder.txt_contacts_name= (TextView) convertView.findViewById(R.id.txt_contacts_name);
            viewHolder.txt_loading_date= (TextView) convertView.findViewById(R.id.txt_loading_date);
            viewHolder.txt_note_date= (TextView) convertView.findViewById(R.id.txt_note_date);
            viewHolder.txt_starting_point= (TextView) convertView.findViewById(R.id.txt_starting_point);
            viewHolder.txt_goods_type= (TextView) convertView.findViewById(R.id.txt_goods_type);
            viewHolder.txt_remark= (TextView) convertView.findViewById(R.id.txt_remark);
            viewHolder.txt_target_point = (TextView) convertView.findViewById(R.id.txt_target_point);
            viewHolder.txt_company_name = (TextView) convertView.findViewById(R.id.txt_company_name);
            viewHolder.drop = convertView.findViewById(R.id.waterdrop);
            convertView.setTag(viewHolder);
        }else
            viewHolder= (ViewHolder) convertView.getTag();
        Goods_For_Address goods_for_address=goods_for_addresses.get(position);
        viewHolder.txt_contacts_name.setText(goods_for_address.getContractName());
        viewHolder.txt_loading_date.setText(goods_for_address.getEndDate());
        viewHolder.txt_note_date.setText(goods_for_address.getStartDate());
        viewHolder.txt_starting_point.setText(goods_for_address.getOutLoc());
        viewHolder.txt_goods_type.setText(GoodsTypes.getTypeName(Integer.parseInt(goods_for_address
                .getType())));
        viewHolder.txt_target_point.setText(goods_for_address.getRoute());

        if(goods_for_address.getViewCount()==0||
                "".equals(goods_for_address.getViewCount())
                ){
            viewHolder.drop.setVisibility(View.GONE);
        }else {
            viewHolder.drop.setVisibility(View.VISIBLE);
            viewHolder.drop.setText(goods_for_address.getViewCount()+"");
        }
        if (goods_for_address.getMemo() != null && !goods_for_address.getMemo().equals(""))
            viewHolder.txt_remark.setText(goods_for_address.getMemo());
        else
            viewHolder.txt_remark.setText("暂无");
        String company_name = goods_for_address.getCompany();
        if(company_name!="") {
            viewHolder.txt_company_name.setText(company_name);
        }else {
            viewHolder.txt_company_name.setVisibility(View.GONE);
        }
//        viewHolder.txt_goods_name.setText(goods_for_address.get);
        return convertView;
    }

    private class ViewHolder{
        TextView txt_contacts_name,txt_loading_date,txt_note_date,txt_starting_point,txt_goods_type,txt_remark,txt_target_point, txt_company_name;
        WaterDrop drop;
    }
    
    public void setData(List<Goods_For_Address> list) {
    	if(list != null) {
    		goods_for_addresses = list;
    	} else {
    		goods_for_addresses = new ArrayList<Goods_For_Address>();
    	}
    }
    
    public void setValue(Goods_For_Address address) {
    	if(address != null && !goods_for_addresses.contains(address)) {
    		goods_for_addresses.add(address);
    	}
    }
    
    public void removeValue(Goods_For_Address address) {
    	if(address != null && goods_for_addresses.contains(address)) {
    		goods_for_addresses.remove(address);
    	}
    }
}
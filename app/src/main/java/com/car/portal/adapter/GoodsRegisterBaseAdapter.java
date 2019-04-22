package com.car.portal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.MyTypes;

import java.util.ArrayList;
import java.util.List;

public class GoodsRegisterBaseAdapter extends BaseAdapter{
    private List<MyTypes> mlist;
    private Context mcontext;
    private int mselect_item;

    public GoodsRegisterBaseAdapter(Context context,int select_item){
        this.mcontext=context;
        this.mselect_item=select_item;
    }

    @Override
    public int getCount() {
        return mlist==null?0:mlist.size();
    }

    @Override
    public Object getItem(int position) {
        if (mlist!=null&&mlist.size()>0)
            return mlist.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;
        if(convertView==null){
            viewholder=new ViewHolder();
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.goods_type_list_item,null);
            viewholder.txt_type= (TextView) convertView.findViewById(R.id.txt_typename);
            convertView.setTag(viewholder);
        }else
            viewholder= (ViewHolder) convertView.getTag();
        if (position==mselect_item) {
            viewholder.txt_type.setTextColor(convertView.getResources().getColor(R.color.black));
            viewholder.txt_type.setBackgroundResource(R.color.white);
        }
        else {
//            convertView.setBackgroundColor(mcontext.getResources().getColor(R.color.littleGray));
            viewholder.txt_type.setTextColor(convertView.getResources().getColor(R.color.gray));
            viewholder.txt_type.setBackgroundResource(R.drawable.item_noradius_white);
        }
        viewholder.txt_type.setText(mlist.get(position).getName());
        return convertView;
    }

    public class ViewHolder{
        TextView txt_type;
    }
    
    public void setData(ArrayList<MyTypes> list){
    	if(list == null) {
    		mlist = new ArrayList<MyTypes>();
    	} else {
    		mlist = list;
    	}
    }

    public void setSelectItem(int select_num){
        this.mselect_item=select_num;
    }

    public ArrayList<MyTypes> getMlist(){
        return (ArrayList<MyTypes>) mlist;
    }
}

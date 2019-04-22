package com.car.portal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016/6/17.
 */
public class AuthorAdapter extends BaseAdapter {
    private Context context;
    private List<User> users;
    private int select_num = -1;
    private boolean isCheck = false;

    public AuthorAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public Object getItem(int position) {
        if (users != null && users.size() > 0){
            return users.get(position);
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
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.author_list_item, null);
            viewHolder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.txt_author = (TextView) convertView.findViewById(R.id.txt_author);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txt_name.setText(users.get(position).getCname());
        if (position == select_num){
            viewHolder.txt_author.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.txt_name.setBackgroundResource(R.drawable.check_button_white);
            viewHolder.txt_author.setBackgroundResource(R.drawable.check_button_blue);
        } else {
            viewHolder.txt_author.setTextColor(context.getResources().getColor(R.color.darkgray));
            viewHolder.txt_name.setBackgroundResource(R.drawable.uncheck_button_white);
            viewHolder.txt_author.setBackgroundResource(R.drawable.uncheck_button_blue);
        }
        return convertView;
    }

    public void setList(List<User> userArrayList){
        if (userArrayList != null && userArrayList.size() > 0){
            this.users = userArrayList;
        }
    }

    public void selectOne(int position){
        this.select_num = position;
    }

    public class ViewHolder{
        TextView txt_name, txt_author;
    }
}

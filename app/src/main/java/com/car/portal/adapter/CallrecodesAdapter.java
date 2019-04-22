package com.car.portal.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.CallrecordEntity;
import com.car.portal.entity.Findcpybeen;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CallrecodesAdapter extends RecyclerView.Adapter<CallrecodesAdapter.Viewholder> {


    private Context context;
    private List<CallrecordEntity> Datalist;
    SharedPreferences sp;
        public CallrecodesAdapter(List<CallrecordEntity> list){
                this.Datalist = list;
        }
       ArrayList<Integer> ischeck = new ArrayList<>();

    @NonNull
    @Override
    public CallrecodesAdapter.Viewholder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        if(context==null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_phone_record, parent, false);
        final CallrecodesAdapter.Viewholder holder = new CallrecodesAdapter.Viewholder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        CallrecordEntity callrecordEntity =   Datalist.get(position);
        holder.tv_record_name.setText(callrecordEntity.getUsername());
        holder.tv_record_phone.setText(callrecordEntity.getTel()+"  "+callrecordEntity.getTime());
    }
    public void removeData(int position) {
        ischeck.clear();
        Datalist.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return Datalist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView tv_record_name,tv_record_phone;
        ImageView img_driver_head;
        CardView item_layth;
        public Viewholder(View itemView) {
            super(itemView);
            item_layth = (CardView) itemView;
            tv_record_name = itemView.findViewById(R.id.tv_record_name);
            tv_record_phone = itemView.findViewById(R.id.tv_record_phone);
            img_driver_head = itemView.findViewById(R.id.img_driver_head);
        }
    }
}

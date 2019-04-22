package com.car.portal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.car.portal.R;
import com.car.portal.entity.Mybankcardbeen;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MybankcardAdapter extends RecyclerView.Adapter<MybankcardAdapter.Viewholder> {

    private Context context;
    private List<Mybankcardbeen> Datalist;

    public  MybankcardAdapter(List<Mybankcardbeen> list){
        this.Datalist = list;
    }

    @NonNull
    @Override
    public MybankcardAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context==null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_bankcard_list, parent, false);
        final MybankcardAdapter.Viewholder holder = new MybankcardAdapter.Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MybankcardAdapter.Viewholder holder, int position) {
                Mybankcardbeen mybankcardbeen =  Datalist.get(position);
                 Glide.with(context).load(mybankcardbeen.getBankcardimg()).into(holder.img_bankcard);
                 holder.text_banknum.setText(mybankcardbeen.getBankcard_num());
                 holder.text_banktype.setText(mybankcardbeen.getBankcard_type());
                 holder.text_bankname.setText(mybankcardbeen.getBankcard_name());
                 Glide.with(context).load(mybankcardbeen.getBg_card()).into(holder.img_bank_bg);
    }

    @Override
    public int getItemCount() {
        return Datalist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        RoundedImageView img_bankcard;
        ImageView img_bank_bg;
        TextView text_bankname,text_banktype,text_banknum;
        RelativeLayout item_bank;
        public Viewholder(View itemView) {
            super(itemView);
            item_bank = (RelativeLayout) itemView;
            img_bank_bg = itemView.findViewById(R.id.img_bank_bg);
            img_bankcard = itemView.findViewById(R.id.img_bankcard);
            text_bankname = itemView.findViewById(R.id.text_bankname);
            text_banktype = itemView.findViewById(R.id.text_banktype);
            text_banknum = itemView.findViewById(R.id.text_banknum);
        }
    }
}

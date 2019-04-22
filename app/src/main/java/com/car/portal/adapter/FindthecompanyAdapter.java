package com.car.portal.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.Findcpybeen;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FindthecompanyAdapter extends RecyclerView.Adapter<FindthecompanyAdapter.Viewholder> {


    private Context context;
    private List<Findcpybeen> Datalist;
    SharedPreferences sp;
        public  FindthecompanyAdapter(List<Findcpybeen> list){
                this.Datalist = list;
        }
       ArrayList<Integer> ischeck = new ArrayList<>();

    @NonNull
    @Override
    public FindthecompanyAdapter.Viewholder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        if(context==null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_foundcpy, parent, false);
        final FindthecompanyAdapter.Viewholder holder = new FindthecompanyAdapter.Viewholder(view);
        holder.Rel_found_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Findcpybeen findcpybeen =   Datalist.get(position);
                if(ischeck.contains(position)){
                    holder.cbox_found_ischeck.setVisibility(View.GONE);
                }else {
                    ischeck.clear();
                    ischeck.add(position);
                    holder.cbox_found_ischeck.setVisibility(View.VISIBLE);
                }
                sp = context.getSharedPreferences("SP", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("cpyid",findcpybeen.getId());
                editor.putInt("position",position);
                editor.commit();
                notifyDataSetChanged();
            }
        });
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        Findcpybeen findcpybeen =   Datalist.get(position);
        if(findcpybeen.getTimer().contains("]")){
            holder.txt_found_timer.setText(findcpybeen.getTimer().replace("]",""));
        }else {
            holder.txt_found_timer.setText(findcpybeen.getTimer());
        }
        holder.txt_found_cpyname.setText(findcpybeen.getProposer());
        if(ischeck.contains(position)){
            holder.cbox_found_ischeck.setVisibility(View.VISIBLE);
        }else {
            holder.cbox_found_ischeck.setVisibility(View.GONE);
        }
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
        TextView txt_found_cpyname,txt_found_timer;
        ImageView cbox_found_ischeck;
        RelativeLayout Rel_found_view;
        public Viewholder(View itemView) {
            super(itemView);
            Rel_found_view = (RelativeLayout) itemView;
            txt_found_timer = itemView.findViewById(R.id.txt_found_timer);
            txt_found_cpyname = itemView.findViewById(R.id.txt_found_cpyname);
            cbox_found_ischeck = itemView.findViewById(R.id.cbox_found_ischeck);
        }
    }
}

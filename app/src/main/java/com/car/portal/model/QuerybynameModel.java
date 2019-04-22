package com.car.portal.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.contract.QuerybynameContract;
import com.car.portal.entity.Boss;
import com.car.portal.http.HttpCallBack;
import com.car.portal.presenter.QuerybynamePresenter;
import com.car.portal.service.GoodsService;
import com.car.portal.service.UserService;
import com.car.portal.view.ICallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuerybynameModel implements QuerybynameContract.Model {

    @Override
    public void getqueryname(String name, Context context, final ICallback<List<Boss>> callback) {
        GoodsService userService = new GoodsService(context);
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("condition",name);
        map.put("typeId", -1);
        map.put("currentPage", 1);
        userService.getBossInfo(map, new HttpCallBack(context) {
            @Override
            public void onSuccess(Object... objects) {
                if(objects != null && objects.length > 0) {
                 List<Boss>  mbosslist = (ArrayList<Boss>) objects[0];
                 callback.onSucceed(mbosslist);
                }
            }
        });
    }

    public static class QuerybynameAdapter extends RecyclerView.Adapter<QuerybynameAdapter.Viewholder> {
        private List<Boss> list;
        private Context context;
        private static OnItemClickListener onItemClick;
        public QuerybynameAdapter(List<Boss> list, Context context) {
            this.list = list;
            this.context = context;
        }

        public static interface OnItemClickListener {
            void onItemClick(View view,Boss boss);
        }

        public static void setOnItem(OnItemClickListener myListener) {
            onItemClick = myListener;
        }

        @NonNull
        @Override
        public QuerybynameAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(context==null){
                context = parent.getContext();
            }
            View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
            final QuerybynameAdapter.Viewholder holder = new QuerybynameAdapter.Viewholder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull QuerybynameAdapter.Viewholder holder, int position) {
                final Boss boss = list.get(position);
                holder.text_serach_name.setText(boss.getName());
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClick.onItemClick(v,boss);
                    }
                });
        }

        @Override
        public int getItemCount() {
            return list.size()>10?10:list.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            TextView text_serach_name;
            LinearLayout linearLayout;
            public Viewholder(@NonNull View itemView) {
                super(itemView);
                linearLayout = (LinearLayout) itemView;
                text_serach_name = itemView.findViewById(R.id.text_serach_name);
            }
        }
    }

}

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

import com.car.portal.R;
import com.car.portal.contract.CommonlyusedContract;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.Goods_For_Address;
import com.car.portal.entity.ParnerShip;
import com.car.portal.entity.commonlyusedbean;
import com.car.portal.entity.newOrder;
import com.car.portal.fragment.DeliverTabFragment;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.GoodsService;
import com.car.portal.view.ChildViewPager;
import com.car.portal.view.ICallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CommonlyusedModel implements CommonlyusedContract.Model {
    GoodsService goodsService;

    public CommonlyusedModel(Context context) {
       goodsService = new GoodsService(context);
    }

    @Override
    public void getGoodsNormal(Context context, final ICallback<List<commonlyusedbean>> callback) {
        goodsService.getGoodsNormallist(new HttpCallBack(context) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects[0] != null) {
                    List<commonlyusedbean> orderslist = (List<commonlyusedbean>) objects[0];
                    callback.onSucceed(orderslist);
                }
            }

            @Override
            public void onError(Object... objects) {
                super.onError(objects);
                callback.onError("获取数据失败");
            }
        });
    }


    @Override
    public void deleteGoodsNormal(Context context, int goodsId, final ICallback<String> callback) {
        goodsService.delGoodsnor(goodsId, new HttpCallBack(context) {
            @Override
            public void onSuccess(Object... objects) {
                callback.onSucceed("删除成功");
            }
            @Override
            public void onError(Object... objects) {
                super.onError(objects);
                callback.onError("删除失败");
            }
        });
    }

    @Override
    public void onkeysendGoods(Context context, int goodsNormatId, String loadingDate, final ICallback<String> callback) {
        goodsService.onkeysend(goodsNormatId, loadingDate, new HttpCallBack(context) {
            @Override
            public void onSuccess(Object... objects) {
                    callback.onSucceed("发送成功");
            }

            @Override
            public void onError(Object... objects) {
                super.onError(objects);
                callback.onError("发送失败");
            }
        });
    }


    public static class CommonlyAdapter extends RecyclerView.Adapter<CommonlyAdapter.Viewholder> {
        private List<commonlyusedbean> list;
        private Context context;
        public CommonlyAdapter(List<commonlyusedbean> list, Context context) {
            this.list = list;
            this.context = context;
        }

        public interface OnItemOnClickListener{
            void onItemOnClick(View view ,commonlyusedbean pos);
            void ondelItemclick(View view,int id,int position);
            void onsendItemclick(View view,int id);
        }

        private OnItemOnClickListener mOnItemOnClickListener;

        public void setOnItemClickListener(OnItemOnClickListener listener){
            this.mOnItemOnClickListener = listener;
        }

        @NonNull
        @Override
        public CommonlyAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(context==null){
                context = parent.getContext();
            }
            View view = LayoutInflater.from(context).inflate(R.layout.item_commonlygood, parent, false);
            final CommonlyAdapter.Viewholder holder = new CommonlyAdapter.Viewholder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final CommonlyAdapter.Viewholder holder, final int position) {
            final commonlyusedbean order =  list.get(position);
            holder.txt_remark.setText(order.getMemo());
            holder.txt_starting_point.setText(order.getOutLoc());
            holder.txt_target_point.setText(order.getRoute());
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemOnClickListener.onItemOnClick(v,order);
                }
            });
            holder.text_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    mOnItemOnClickListener.ondelItemclick(v,order.getId(),position);
                }
            });
            holder.text_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemOnClickListener.onsendItemclick(v,order.getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            TextView txt_starting_point,txt_target_point,txt_remark,text_del,text_send;
            LinearLayout linearLayout;
            CardView cardview;
            public Viewholder(@NonNull View itemView) {
                super(itemView);
                linearLayout = (LinearLayout) itemView;
                txt_starting_point =  itemView.findViewById(R.id.txt_starting_point);
                txt_remark =  itemView.findViewById(R.id.txt_remark);
                txt_target_point =  itemView.findViewById(R.id.txt_target_point);
                text_del = itemView.findViewById(R.id.text_del);
                cardview = itemView.findViewById(R.id.cardview);
                text_send = itemView.findViewById(R.id.text_send);
            }
        }


        public void removeItem(int pos){
            list.remove(pos);
            notifyItemRemoved(pos);
        }
    }
}

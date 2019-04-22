package com.car.portal.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.OrderList;
import com.car.portal.view.CallsHeadView;
import com.car.portal.view.MyHScrollView;

/**
 * Created by Admin on 2016/6/23.
 */
public class CallsPhoneAdapter extends BaseAdapter {
    private Context context;
    private CallsHeadView mHead;

    private List<Contract> contracts;
    private List<String> dates;
    private Map<String,Map<String, String>> content_datas;
    private boolean isByYear;
    private LayoutParams params = new LayoutParams(80, LayoutParams.MATCH_PARENT);
    private LayoutParams widthParams = new LayoutParams(160, LayoutParams.MATCH_PARENT);

    public CallsPhoneAdapter(Context context, CallsHeadView head){
        this.context = context;
        this.mHead = head;
    }

    @Override
    public int getCount() {
        return contracts == null ? 0 : contracts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            synchronized (context) {
                convertView = LayoutInflater.from(context).inflate(R.layout.calls_title_item, null);
                holder = new ViewHolder();
                MyHScrollView scrollView1 = (MyHScrollView) convertView
                        .findViewById(R.id.horizontalScrollView1);
                holder.scrollView = scrollView1;
                holder.txt_name = (TextView) convertView.findViewById(R.id.textView1);
                holder.line_title = (LinearLayout) convertView.findViewById(R.id.line_title);
                MyHScrollView headSrcrollView = (MyHScrollView) mHead.findViewById(R.id.horizontalScrollView1);
                headSrcrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(scrollView1));
                for (int i = 0; i < dates.size(); i++){
                    TextView textView = new TextView(context);
                    holder.line_title.addView(textView);
                    textView.setTextColor(context.getResources().getColor(R.color.black));
                    textView.setTextSize(16);
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(params);
                    textView.setBackgroundResource(R.drawable.text_cell);
                }
                convertView.setTag(holder);
            }
        } else {
        	holder = (ViewHolder) convertView.getTag();
        	if(holder.line_title.getChildCount() < dates.size()) {
        		synchronized (context) {
        			for (int i = holder.line_title.getChildCount(); i < dates.size(); i++){
                        TextView textView = new TextView(context);
                        holder.line_title.addView(textView);
                        textView.setTextColor(context.getResources().getColor(R.color.black));
                        textView.setTextSize(16);
                        textView.setGravity(Gravity.CENTER);
                        textView.setLayoutParams(params);
                        textView.setBackgroundResource(R.drawable.text_cell);
                    }
				}
        	} else if(holder.line_title.getChildCount() > dates.size()) {
        		synchronized (context) {
        			for (int i = holder.line_title.getChildCount() - 1; i >= dates.size(); i--){
    					holder.line_title.removeViewAt(i);
                    }
				}
			}
        }

        String uid = contracts.get(position).uid;
        String name = contracts.get(position).name;
        holder.txt_name.setText(name == null ? "12" : name);
        for (int i = 0; i < dates.size(); i++){
            String value = getValue(uid, dates.get(i));
            TextView v = (TextView) holder.line_title.getChildAt(i);
            v.setText(value);
            if(isByYear) {
            	v.setLayoutParams(widthParams);
            } else {
            	v.setLayoutParams(params);
            }
        }
        return convertView;
    }

    class OnScrollChangedListenerImp implements MyHScrollView.OnScrollChangedListener {
        MyHScrollView mScrollViewArg;

        public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
            mScrollViewArg = scrollViewar;
        }

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            mScrollViewArg.smoothScrollTo(l, t);
        }
    };

    private class ViewHolder{
        TextView txt_name;//行头
        LinearLayout line_title;//列头
        HorizontalScrollView scrollView;
    }

    public void initDates(Map<String, String> contract, List<String> dates, List<OrderList> orderLists, boolean isByYear) {
        try {
        	this.isByYear = isByYear;
            contracts = new ArrayList<Contract>(contract.size());
            for (String key : contract.keySet()) {
                Contract con = new Contract(key, contract.get(key));
                contracts.add(con);
            }
            this.dates = dates;
            Collections.sort(this.dates, new Comparator<String>() {
                @SuppressLint("NewApi")
                @Override
                public int compare(String lhs, String rhs) {
                    int l = Integer.valueOf(lhs.replace("-", ""));
                    int r = Integer.valueOf(rhs.replace("-", ""));
                    return Integer.compare(l, r);
                }
            });
            content_datas = new HashMap<String, Map<String, String>>(contracts.size());
            for (int index = 0; index < orderLists.size(); index++) {
                OrderList order = orderLists.get(index);
                String uid_key = order.getUid();
                Map<String, String> values = content_datas.get(uid_key);
                if (values == null) {
                    values = new HashMap<String, String>();
                    content_datas.put(uid_key, values);
                }
                values.put(order.getDay(), order.getCounts());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getValue(String uid, String day) {
    	if (content_datas != null) {
    		Map<String, String> values = content_datas.get(uid);
	        if(values != null) {
	            String value = values.get(day);
	            return value == null ? "" : value;
	        }
		}
        return "";
    }

    class Contract{
        String uid;
        String name;
        public Contract(String uid, String name) {
            this.uid = uid;
            this.name = name;
        }
    }
}

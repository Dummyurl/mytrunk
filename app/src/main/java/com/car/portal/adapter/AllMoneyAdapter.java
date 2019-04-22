package com.car.portal.adapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.car.portal.entity.Money;
import com.car.portal.view.MyHScrollView;

/**
 * Created by Admin on 2016/6/22.
 */
public class AllMoneyAdapter extends BaseAdapter {
    private Context context;
    private Map<String, Map<String, String>> datas = new HashMap<String, Map<String,String>>();
    private String[] header_str;
    private LinearLayout header;
    private final String[] month_str = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};

    public AllMoneyAdapter(Context context, LinearLayout header){
        this.context = context;
        this.header = header;
    }

    @Override
    public int getCount() {
        return month_str.length;
    }

    @Override
    public Object getItem(int position) {
        return month_str[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutParams params = new LayoutParams(150, LayoutParams.MATCH_PARENT, 1);
        if (convertView == null) {
        	synchronized (context) {
	            convertView = LayoutInflater.from(context).inflate(R.layout.money_contract_item, null);
	            viewHolder = new ViewHolder();
	            MyHScrollView scrollView1 = (MyHScrollView) convertView.findViewById(R.id.horizontalScrollView1);
	            viewHolder.scrollView = scrollView1;
	            viewHolder.txt_month = (TextView) convertView.findViewById(R.id.txt_month);
	            viewHolder.content = (LinearLayout) convertView.findViewById(R.id.lay_content);
	            if(header_str != null) {
		            for (int i = 0; i < header_str.length; i++) {
		            	TextView textView = new TextView(context);
	                    viewHolder.content.addView(textView);
	                    textView.setTextColor(context.getResources().getColor(R.color.black));
	                    textView.setTextSize(16);
	                    textView.setGravity(Gravity.CENTER);
	                    textView.setLayoutParams(params);
	                    textView.setBackgroundResource(R.drawable.text_cell);
					}
	            }
	            MyHScrollView headSrcrollView = (MyHScrollView) header.findViewById(R.id.horizontalScrollView1);
	            headSrcrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(scrollView1));
	            convertView.setTag(viewHolder);
        	}
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            if(header_str != null) {
	            if(viewHolder.content.getChildCount() < header_str.length) {
	        		synchronized (context) {
	        			for (int i = viewHolder.content.getChildCount(); i < header_str.length; i++){
	                        TextView textView = new TextView(context);
	                        viewHolder.content.addView(textView);
	                        textView.setTextColor(context.getResources().getColor(R.color.black));
	                        textView.setTextSize(16);
	                        textView.setGravity(Gravity.CENTER);
	                        textView.setLayoutParams(params);
	                        textView.setBackgroundResource(R.drawable.text_cell);
	                    }
					}
	        	} else if(viewHolder.content.getChildCount() > header_str.length) {
	        		synchronized (context) {
	        			for (int i = viewHolder.content.getChildCount() - 1; i >= header_str.length; i--){
	        				viewHolder.content.removeViewAt(i);
	                    }
					}
				}
            }
        }
        
        viewHolder.txt_month.setText(month_str[position]);
        viewHolder.txt_month.setTextColor(context.getResources().getColor(R.color.gray));
        if(header_str != null) {
	        for (int i = 0; i < header_str.length; i++) {
	        	String value = getData(position, i);
	        	((TextView) viewHolder.content.getChildAt(i)).setText(value);
			}
        }
        return convertView;
    }

    /**
     * 放置查询到的数据
     * @param position
     * @param viewHolder
     */
    public String getData(int position, int index){
        String month = position + 1 + "";
        String year = header_str[index];
        Map<String, String> ymap = datas.get(year);
        String value = null;
        if(ymap != null) {
        	value = ymap.get(month);
        }
        if(value != null) {
        	return value;
        } else {
        	return "0";
        }
    }

    public void setList(List<Money> list, String[] headers){
        if (list != null && headers != null){
        	for (Money money : list) {
        		Map<String, String> yearMap = datas.get(money.getYear());
        		if(yearMap == null) {
        			yearMap = new HashMap<String, String>();
        			datas.put(money.getYear(), yearMap);
        		}
        		yearMap.put(money.getMonth(), money.getYi());
			}
        }
        this.header_str = headers;
        if(headers == null) {
        	this.header_str = new String[]{"" + Calendar.getInstance().get(Calendar.YEAR)};
        }
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
        TextView txt_month;
        LinearLayout content;
        HorizontalScrollView scrollView;
    }
}

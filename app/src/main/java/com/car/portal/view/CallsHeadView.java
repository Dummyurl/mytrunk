package com.car.portal.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.car.portal.R;

/**
 * Created by Admin on 2016/6/24.
 */
public class CallsHeadView extends RelativeLayout{
    private TextView textView1;
    private MyHScrollView horizontalScrollView1;
    private LinearLayout line_title;

    public CallsHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calls_title_item, this);
        textView1 = (TextView) findViewById(R.id.textView1);
        horizontalScrollView1 = (MyHScrollView) findViewById(R.id.horizontalScrollView1);
        line_title = (LinearLayout) findViewById(R.id.line_title);
    }

    public void addTitle(List<String> str_title, Context context, boolean isByYear){
    	line_title.removeAllViews();
        if (str_title != null && str_title.size() > 0){
            for (int i = 0; i < str_title.size(); i++){
                TextView textView = new TextView(context);
                line_title.addView(textView);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
                if(isByYear) {
                	layoutParams.width = 160;
                } else {
                	layoutParams.width = 80;
                }
                layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
                textView.setTextColor(context.getResources().getColor(R.color.black));
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(16);
                textView.setText(str_title.get(i));
                textView.setLayoutParams(layoutParams);
                textView.setBackgroundColor(context.getResources().getColor(R.color.gray_est));
            }
        }
    }

    public void setTitle(String title){
        textView1.setText(title);
    }
}

package com.car.portal.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.util.LogUtils;

public class QueryTitleView extends RelativeLayout{
    private ImageView img_back,img_more,img_query;
    private EditText edit_search;
    private QueryClickListener listener;
    private Context context;
    private TextView txt_title;
    private RelativeLayout rela_query;
    private View.OnClickListener rtnClickListener;

    public QueryTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.title_query, this);
        img_back= (ImageView) findViewById(R.id.img_back);
        img_query= (ImageView) findViewById(R.id.search_confirm);
        img_more= (ImageView) findViewById(R.id.img_date_query);
        edit_search= (EditText) findViewById(R.id.search_text);
        txt_title= (TextView) findViewById(R.id.txt_title);
        rela_query= (RelativeLayout) findViewById(R.id.rela_query);
        init();
    }

    private void init() {
    	img_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);
                String condition = edit_search.getText().toString();
                if (listener != null) {
                    listener.onQuery(condition);
                    LogUtils.e("Query","    点击到了");
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                if(rtnClickListener != null) {
                    rtnClickListener.onClick(v);
                } else if(context instanceof Activity){
                    ((Activity) context).finish();
                }
            }
        });
	}

	public void setBackClickListener(View.OnClickListener clickListener) {
        rtnClickListener = clickListener;
    }

    public void setMoreListener(View.OnClickListener listener){
        img_more.setOnClickListener(listener);
    }

    public void setMoreIsVisible(){
        img_more.setVisibility(GONE);
    }

    public void setQueryIsVisible(boolean isVisible,String title){
        if (isVisible) {
            rela_query.setVisibility(View.VISIBLE);
            txt_title.setVisibility(INVISIBLE);
        }else {
            rela_query.setVisibility(INVISIBLE);
            txt_title.setVisibility(VISIBLE);
            txt_title.setText(title);
        }
    }

    public void setOnQueryListener(QueryClickListener listener) {
    	this.listener = listener;
    }
    
    public static interface QueryClickListener{
    	void onQuery(String condition);
    }
}

package com.car.portal.adapter;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.ReChangeData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.xutils.common.util.DensityUtil;

import java.util.List;

public class ReChangeAdapter extends BaseQuickAdapter<ReChangeData,BaseViewHolder> {


    private int position=-1;
    private List<ReChangeData> data;
    public ReChangeAdapter(int layoutResId, @Nullable List<ReChangeData> data) {
        super(layoutResId, data);
        this.data=data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ReChangeData item) {
        String typeContent="元/"+item.getMonths()+"个月";
        TextView detail=helper.getView(R.id.detail);
        TextView type=helper.getView(R.id.type);
        final EditText money=helper.getView(R.id.money);
        final LinearLayout parent=helper.getView(R.id.parent);
        final CheckBox state=helper.getView(R.id.state);
        type.setText(typeContent);
        detail.setText(item.getDetail());
        money.setText(item.getMoney()+"");


        state.setChecked(position==helper.getAdapterPosition());
        if (item.getMoney()<=0){
            money.setText("");
            type.setText("/元");
            ViewGroup.LayoutParams layoutParams = money.getLayoutParams();
            layoutParams.width= DensityUtil.dip2px(80);
            money.setLayoutParams(layoutParams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                money.setBackgroundResource(R.drawable.stroke_bottom_only2);
            }else {
                money.setBackgroundColor(Color.parseColor("#FFE2E2E2"));
            }
        }else {
            money.setEnabled(false);
            ViewGroup.LayoutParams layoutParams = money.getLayoutParams();
            layoutParams.width= LinearLayout.LayoutParams.WRAP_CONTENT;
            money.setLayoutParams(layoutParams);
            money.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        money.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) parent.performClick();
            }
        });

        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    int money = Integer.parseInt(s.toString());
                    item.setFlag(money);
                }else {
                    item.setFlag(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(position!=helper.getAdapterPosition()){
                    state.setChecked(true);
                    if(position!=-1){
                        notifyItemChanged(position,0);
                    }
                    position=helper.getAdapterPosition();
                    money.setSelection(money.getText().length());
                    money.setFocusable(true);
                    money.setFocusableInTouchMode(true);
                    money.requestFocus();

                }
            }
        });

    }

    protected void setTextStyle(TextView tvPrice, String tv,String money) {
        String time = tv;
        SpannableStringBuilder mSpannable = new SpannableStringBuilder(time);
        mSpannable.setSpan(new RelativeSizeSpan(0.8f), money.length(), tv.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPrice.setText(mSpannable);
    }

    public int getPosition() {
        return position;
    }
}

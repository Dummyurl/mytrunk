package com.car.portal.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.xutils.common.util.DensityUtil;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private int wight;
    private int height;
    private int item_height;
    private Paint paint;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    public DividerItemDecoration(Context context, RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        this.context=context;
        wight = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(Color.parseColor("#e0e0e0"));
        item_height = DensityUtil.dip2px( 0.6f);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            int top = view.getBottom();
            int bottom = top + item_height;
            //这里把left和right的值分别增加item_padding,和减去item_padding.
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            if(params.getViewAdapterPosition()==count-1){
                continue;
            }
            c.drawRect(DensityUtil.dip2px( 14), top, wight- DensityUtil.dip2px( 14), bottom, paint);

        }
    }
}
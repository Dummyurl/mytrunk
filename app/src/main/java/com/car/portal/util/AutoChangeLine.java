package com.car.portal.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.car.portal.entity.City;
import com.car.portal.view.CitySelectedItemView;

public class AutoChangeLine extends ViewGroup {

	private static final int PADDING_HOR = 5;// 水平方向padding
	private static final int PADDING_VERTICAL = 5;// 垂直方向padding
	private static final int SIDE_MARGIN = 5;// 左右间距
	private static final int TEXT_MARGIN = 5;

	public AutoChangeLine(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public AutoChangeLine(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
		setPadding(5, 5, 5, 5);
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		int autualWidth = r - l;
		int x = SIDE_MARGIN;// 横坐标开始
		int y = PADDING_VERTICAL;// 纵坐标开始
		int rows = 1;
		for (int i = 0; i < childCount; i++) {
			View view = getChildAt(i);
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();
			x += width + TEXT_MARGIN + PADDING_HOR;
			if (x > autualWidth) {
				x = width + SIDE_MARGIN + PADDING_HOR;
				rows++;
			}
			y = rows * (height + TEXT_MARGIN) + PADDING_VERTICAL;
			if (i == 0) {
				view.layout(x - width , y - height, x - TEXT_MARGIN, y - 10);
			} else {
				view.layout(x - width + TEXT_MARGIN, y - height, x - 5, y - 10);
			}
		}
	};

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int x = 0;// 横坐标
		int y = 0;// 纵坐标
		int rows = 1;// 总行数
		int specWidth = MeasureSpec.getSize(widthMeasureSpec);
		int actualWidth = specWidth - SIDE_MARGIN * 2 - 2 * PADDING_HOR;// 实际宽度
		int childCount = getChildCount();
		for (int index = 0; index < childCount; index++) {
			View child = getChildAt(index);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			x += width + TEXT_MARGIN + PADDING_HOR;
			if (x > actualWidth) {// 换行
				x = width + PADDING_HOR;
				rows++;
			}
			y = rows * (height + TEXT_MARGIN) + 2 * PADDING_VERTICAL;
		}
		setMeasuredDimension(actualWidth, y);
	}

	public void deleteByCid(City c) {
		if(c == null) {
			return;
		}
		int childCount = getChildCount();
		int i = -1;
		for (int index = 0; index < childCount; index++) {
			View v =  getChildAt(index);
			if(v instanceof CitySelectedItemView) {
				CitySelectedItemView sv = (CitySelectedItemView) v;
				if (sv.getCity().equals(c)) {
					i = index;
					break;
				}
			}
		}
		if(i >= 0 && i < childCount) {
			removeViewAt(i);
		}
	}


}

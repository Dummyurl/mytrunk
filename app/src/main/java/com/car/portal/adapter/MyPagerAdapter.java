package com.car.portal.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyPagerAdapter extends PagerAdapter {

	ArrayList<ImageView> listViews;

	public MyPagerAdapter(ArrayList<ImageView> listViews) {
		this.listViews = listViews;
	}

	/**
	 * 返回总条数
	 * */
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	/**
	 * 实例化每个页面的视图，相当于getView() container：其实就是ViewPager 中文意思容器
	 * position：当前实例化页面的位置
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int pos = position % listViews.size();
//		LogUtils.e("PagerAdapter", "Image position:" + pos);
		ImageView imageView = listViews.get(pos);
		try{
			// 把View添加到容器中
			if(imageView.getParent() == null) {
				container.addView(imageView);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageView;
	}

	/**
	 * 比较当前页面和instantiateItem是否是同一个对象 view:当前的页面 object:是由instantiateItem返回的对象
	 */
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	/**
	 * 控件的销毁
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}

package com.car.portal.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.activity.GoodsRegisterAcriviry;
import com.car.portal.util.LogUtils;

public class HomeTitleView extends LinearLayout {
	
	private TextView title;
	private ImageView search;
	private EditText edit_condition;
	private Context context;
	private ImageView more,addgoods;
	private PopupWindow popupWindow;
	private LinearLayout line_message,line_addgoods;
	private LinearLayout linearLayout;
	private TitleQueryListener titleQueryListener;

	@SuppressLint("InflateParams")
	public HomeTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.title_home, this);

		init();
	}
	
	private void init() {
		title = (TextView) findViewById(R.id.titleText);
		search = (ImageView) findViewById(R.id.title_search);
		more= (ImageView) findViewById(R.id.more);
		edit_condition = (EditText) findViewById(R.id.edit_condition);
		more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopuwindow(v);
			}
		});
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (edit_condition.isShown()) {
					edit_condition.setVisibility(View.INVISIBLE);
					imm.hideSoftInputFromWindow(edit_condition.getWindowToken(), 0);
					String condition = edit_condition.getText().toString();
					if (titleQueryListener != null) {
						titleQueryListener.onQuery(condition);
						LogUtils.e("Query", "    点击到了");
					}
				} else {
					edit_condition.setVisibility(View.VISIBLE);
					edit_condition.findFocus();
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		});
		addgoods = (ImageView)findViewById(R.id.title_addgoods);
		addgoods.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, GoodsRegisterAcriviry.class);
				context.startActivity(intent);
			}
		});

	}

	/**
	 * 隐藏控件只显示标题
	 */

	public void inVisisble(){
		search.setVisibility(View.INVISIBLE);
		more.setVisibility(View.INVISIBLE);
		addgoods.setVisibility(View.INVISIBLE);
	}

	/**
	 * 显示popuwindow更多内容
	 */
	public void showPopuwindow(View parent){
		View view=null;
		if(popupWindow==null){
			view=LayoutInflater.from(context).inflate(R.layout.popu_more,null);
			popupWindow = new PopupWindow(view,
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
			linearLayout= (LinearLayout) view.findViewById(R.id.line_popu_more);
			//line_addgoods= (LinearLayout) view.findViewById(R.id.line_addgoodsinfo);
			line_message= (LinearLayout) view.findViewById(R.id.line_message);
			linearLayout.getBackground().setAlpha(0);

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					popupWindow.dismiss();
				}
			});
		}
		popupWindow.setTouchable(true);
		popupWindow.showAsDropDown(parent);
		popupListener();
	}

	/**
	 * popuwindow的点击事件
	 */
	public void popupListener(){
		popupWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});

//		line_addgoods.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, GoodsRegisterAcriviry.class);
//				context.startActivity(intent);
//				popupWindow.dismiss();
//			}
//		});
		line_message.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(context, MessageActivity.class);
//				context.startActivity(intent);
//				popupWindow.dismiss();
			}
		});
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}
	
	public void setSearchVisible(boolean visible) {
		if(visible) {
			search.setVisibility(View.VISIBLE);
		} else {
			search.setVisibility(View.GONE);
		}
	}

	public void setEditVisible(boolean visible){
		if(visible) {
			edit_condition.setVisibility(View.VISIBLE);
		} else {
			edit_condition.setVisibility(View.GONE);
		}
	}
	
	public void setSearchListener(TitleQueryListener listener) {
		this.titleQueryListener = listener;
	}
	
	public void setMessageClickListener(View.OnClickListener clickListener) {
//		message.setOnClickListener(clickListener);
	}

	public class GetPopup extends PopupWindow{

	}

	public static interface TitleQueryListener{
		public void onQuery(String condition);
	}
}



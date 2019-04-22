package com.car.portal.view;

import com.car.portal.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class FocusItemView extends LinearLayout {
	
	private TextView content;
	private ImageView delete;
	private int itemId;
	private String itemName;
	private boolean isDelete;
	private LinearLayout mainLay;
	private int position;
	private View diliver;
	private int sltColor = getResources().getColor(R.color.green);;
	private int unsltColor = getResources().getColor(R.color.gray);
	private View.OnClickListener deleteListener;
	
	@SuppressLint("InflateParams")
	public FocusItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.focus_item, this);
		content = (TextView) findViewById(R.id.focus_content);
		delete = (ImageView) findViewById(R.id.focus_delete);
		mainLay = (LinearLayout) findViewById(R.id.focus_item_lay);
		diliver = findViewById(R.id.focus_diliver);
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	public void setShowDetailListener(View.OnClickListener clickListener) {
		content.setOnClickListener(clickListener);
	}
	
	public void setContent(String content, int maxlenght) {
		if(content == null) {
			content = "";
		} else if(content.length() > maxlenght){
			content = content.substring(0,maxlenght) + "...";
		}
		this.content.setText(content);
	}
	
	public boolean isDeleted() {
		return isDelete;
	}
	
	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
		if(!isDelete) {
			isDelete = true;
			delete.setImageResource(R.drawable.refresh);
			mainLay.setBackground(getResources().getDrawable(R.drawable.border_focus_gray));
			diliver.setBackgroundColor(unsltColor);
			content.setTextColor(unsltColor);
		} else {
			isDelete = false;
			delete.setImageResource(R.drawable.delete);
			mainLay.setBackground(getResources().getDrawable(R.drawable.border_focus_green));
			diliver.setBackgroundColor(sltColor);
			content.setTextColor(sltColor);
		}
		setVisibility(View.VISIBLE);
	}
	
	public void setDeleteLinstenUnDispare() {
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isDelete) {
					isDelete = true;
					delete.setImageResource(R.drawable.refresh);
					mainLay.setBackground(getResources().getDrawable(R.drawable.border_focus_gray));
					diliver.setBackgroundColor(unsltColor);
					content.setTextColor(unsltColor);
				} else {
					isDelete = false;
					delete.setImageResource(R.drawable.delete);
					mainLay.setBackground(getResources().getDrawable(R.drawable.border_focus_green));
					diliver.setBackgroundColor(sltColor);
					content.setTextColor(sltColor);
				}
				if(deleteListener != null) {
					deleteListener.onClick(v);
				}
			}
		});
	}
	
	public void setDeleteLinstenDispare() {
		delete.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				isDelete = true;
				setVisibility(View.GONE);
				if(deleteListener != null) {
					deleteListener.onClick(v);
				}
			}
		});
	}
	
	public void setDeleteCityLinstenDispare() {
		delete.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if(deleteListener != null) {
					deleteListener.onClick(v);
				}
			}
		});
	}
	
	public void setDeleteListener(View.OnClickListener clickListener) {
		deleteListener = clickListener;
	}
}

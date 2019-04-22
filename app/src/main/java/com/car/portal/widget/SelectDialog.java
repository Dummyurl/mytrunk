package com.car.portal.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class SelectDialog extends Dialog
{

	private Context context;
	private View view;

	public SelectDialog(Context context)
	{
		super(context);
		this.context = context;
	}

	public SelectDialog(Context context, int theme, View view)
	{
		super(context, theme);
		this.context = context;
		this.view = view;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(view);
	}

	public View getView()
	{
		return view;
	}
}

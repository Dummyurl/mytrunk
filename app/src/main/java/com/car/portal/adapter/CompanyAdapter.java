package com.car.portal.adapter;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.image.ImageOptions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.entity.Company;
import com.car.portal.holder.CompanyHolder;
import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;

public class CompanyAdapter extends BaseAdapter {

	private List<Company> data;
	private LayoutInflater mInflater;
	private static ImageOptions options;
	private static String host;
	private static int focus_drawable = R.drawable.heart_red;
	private static int unfocus_drawable = R.drawable.heart_gray;
	private FocusClickListener focusClickListener;
	private boolean showFocus = false;
	private static List<Company> focus;

	public CompanyAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		if (options == null) {
			ImageOptions.Builder builder = new ImageOptions.Builder();
			builder.setFailureDrawable(context.getResources().getDrawable(R.drawable.picture_error));
			builder.setImageScaleType(ImageView.ScaleType.CENTER_INSIDE);
			builder.setUseMemCache(true);
			options = builder.build();
		}
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CompanyHolder holder;
		if (convertView == null) {
			holder = new CompanyHolder();
			convertView = mInflater.inflate(R.layout.company_list_item, null);
			holder.logo = (ImageView) convertView.findViewById(R.id.company_logo);
			holder.name = (TextView) convertView.findViewById(R.id.company_name);
			holder.address = (TextView) convertView.findViewById(R.id.company_address);
			holder.detail = (ImageView) convertView.findViewById(R.id.company_detail);
			convertView.setTag(holder);
		} else {
			holder = (CompanyHolder) convertView.getTag();
		}
		Company com = data.get(position);
		if (com != null) {
			if (!StringUtil.isNullOrEmpty(com.getLogo()) && !StringUtil.isNullOrEmpty(host)) {
				x.image().bind(holder.logo, host + com.getLogo(), options);
			}
			holder.name.setText(com.getName());
			holder.address.setText(com.getAddress());
			if(showFocus) {
				holder.detail.setTag(position);
				if(focus.contains(com)) {
					holder.detail.setImageResource(focus_drawable);
				} else {
					holder.detail.setImageResource(unfocus_drawable);
				}
				holder.detail.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(focusClickListener !=  null) {
							int position = (Integer) v.getTag();
							Company com = (Company) getItem(position);
							boolean isFocus = false;
							if(!focus.contains(com)) {
								isFocus = true;
							}
							focusClickListener.onclick(v, isFocus, com);
						}
					}
				});
			} else {
				holder.detail.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	public void setData(List<Company> data) {
		this.data = data;
	}

	public void addCompany(Company company) {
		if (company == null)
			return;
		if (data == null) {
			data = new ArrayList<Company>();
		}
		data.add(company);
	}

	public boolean deleteCompany(Company company) {
		if (company != null) {
			return data.remove(company);
		}
		return false;
	}

	public boolean deleteCompany(int position) {
		if (position >= 0 && position < data.size()) {
			return data.remove(position) != null;
		}
		return false;
	}
	
	public void setFocusClickListener(FocusClickListener focusClickListener) {
		this.focusClickListener = focusClickListener;
	}
	
	public void setFocus(List<Company> focus) {
		if(focus == null) {
			focus = new ArrayList<Company>();
			LogUtils.e("companyAdapter", "focus is null");
		}
		CompanyAdapter.focus = focus;
		showFocus = true;
	}

	public void setHost(String host) {
		CompanyAdapter.host = host;
	}

	public static interface FocusClickListener {
		void onclick(View view, boolean isFocus, Company com);
	}
	
	public static class HttpBackChange {
		
		private ImageView view;
		private boolean isFocus;

		public HttpBackChange(ImageView view, boolean isFocus) {
			this.view = view;
			this.isFocus = isFocus;
		}
		
		public void change(boolean callResult, Company com) {
			if(view != null) {
				if(callResult) {
					if (isFocus) {
						view.setImageResource(focus_drawable);
					} else {
						view.setImageResource(unfocus_drawable);
					}
				} else {
					if(isFocus && focus !=  null && focus.contains(com)) {
						focus.remove(com);
					} else if(!isFocus && com != null && focus != null && !focus.contains(com)) {
						focus.add(com);
					}
				}
			}
		}
	}
	
	public List<Company> getFocus() {
		return focus;
	}
}

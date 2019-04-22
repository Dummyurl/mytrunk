package com.car.portal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import com.car.portal.R;
import com.car.portal.adapter.CompanyAdapter;
import com.car.portal.entity.Company;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.DriverService;
import com.car.portal.service.UserService;
import com.car.portal.util.StringUtil;
import com.car.portal.util.ToastUtil;
import com.car.portal.view.BaseTitleView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CompanyListActivity extends Activity {

	@ViewInject(R.id.company_title)
	private BaseTitleView title;
	@ViewInject(R.id.company_listview)
	private ListView listView;
	@ViewInject(R.id.txt_no_data)
	private TextView txt_no_data;
	private CompanyAdapter adapter;
	private DriverService service;
	private String city;
	private String keyword;
	private int currentPage;
	private static final int CURRENT_PAGER_SIZE = 10;
	public static final int RESULT_CODE = 0x1023;
	private View footer;
	private User user;
	private CompanyAdapter.FocusClickListener focusClickListener = new CompanyAdapter.FocusClickListener() {
		@Override
		public void onclick(View view, boolean isFocus, final Company com) {
			ImageView img = (ImageView) view;
			final CompanyAdapter.HttpBackChange backChange = new CompanyAdapter.HttpBackChange(img, isFocus);
			List<Company> focuses = adapter.getFocus();
			if (user.getFocusmax() <= focuses.size() && isFocus) {
				ToastUtil.show("您最多只能关注" + user.getFocusmax() + "个公司。现在已关注" + focuses.size() + "个",
						CompanyListActivity.this);
				backChange.change(false, com);
			} else {
				StringBuffer ids = new StringBuffer();
				if (isFocus) {
					focuses.add(com);
				} else {
					focuses.remove(com);
				}
				for (Company company : focuses) {
					ids.append(company.getId());
					ids.append(",");
				}
				if (ids.length() > 0) {
					ids.deleteCharAt(ids.length() - 1);
				}
				params = new HashMap<String, Object>();
				params.put("comIds", ids.toString());
				HttpCallBack focusBack = new HttpCallBack(CompanyListActivity.this) {
					@Override
					public void onSuccess(Object... objects) {
						backChange.change(true, com);
					}

					public void onFail(int result, String message, boolean show) {
						backChange.change(false, com);
						super.onFail(result, message, show);
					}

					@Override
					public void onError(Object... objects) {
						backChange.change(false, com);
						super.onError(objects);
					}
				};
				service.saveDriverFocusCom(params, focusBack);
			}
		}
	};

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_list);
		x.view().inject(CompanyListActivity.this);
		service = new DriverService(CompanyListActivity.this);
		adapter = new CompanyAdapter(CompanyListActivity.this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(CompanyListActivity.this, CompanyDetailActivity.class);
				intent.putExtra("company", (Company) adapter.getItem(position));
				startActivity(intent);
			}
		});
		LayoutInflater inflater = LayoutInflater.from(this);
		footer = inflater.inflate(R.layout.listview_footer, null);
		List<Company> companies = getIntent().getParcelableArrayListExtra("companies");
		city = getIntent().getStringExtra("cityName");
		keyword = getIntent().getStringExtra("searchKey");
		currentPage = 1;
		adapter.setData(companies);
		List<Company> focus = getIntent().getParcelableArrayListExtra("focus");
		adapter.setFocus(focus);
		adapter.setFocusClickListener(focusClickListener);
		listView.setAdapter(adapter);
		title.setRtnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.putParcelableArrayListExtra("focus", (ArrayList<Company>) adapter.getFocus());
				setResult(RESULT_CODE, intent);
				finish();
			}
		});
		title.setTitle(getResources().getString(R.string.title_activity_company_list));
		user = new UserService(CompanyListActivity.this).getLoginUser();
		listView.setOnScrollListener(scrollListener);
	}

	private OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
		private boolean isLastRow;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
				loadMore();
				isLastRow = false;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			Adapter adapter = view.getAdapter();
			int count = adapter.getCount();
			if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0
					&& count >= CURRENT_PAGER_SIZE * currentPage) {
				isLastRow = true;
				if (listView.getFooterViewsCount() == 0) {
					listView.addFooterView(footer);
				}
			}
		}
	};

	private  Map<String, Object> params;
	
	private void loadMore() {
		currentPage++;
		params = new HashMap<String, Object>();
		if (!StringUtil.isNullOrEmpty(city)) {
			params.put("cityName", city);
		}
		if (!StringUtil.isNullOrEmpty(keyword)) {
			params.put("searchKey", keyword);
		}
		params.put("currPage", currentPage);
		service.searchCom(params, callBack);
	}

	private HttpCallBack callBack = new HttpCallBack(this) {
		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(Object... objects) {
			if (objects != null && objects.length > 0) {
				List<Company> list = (List<Company>) objects[0];
				if (list.size() > 0) {
					for (Company company : list) {
						adapter.addCompany(company);
					}
					listView.removeFooterView(footer);
					adapter.notifyDataSetChanged();
					txt_no_data.setVisibility(View.GONE);
				} else {
					txt_no_data.setVisibility(View.VISIBLE);
				}
			}
		}
	};
	
}

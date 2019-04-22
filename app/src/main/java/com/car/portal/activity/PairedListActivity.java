package com.car.portal.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ListView;

import com.car.portal.R;
import com.car.portal.adapter.PairedListAdapter;
import com.car.portal.entity.ParnerShip;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.ContractService;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint({ "UseSparseArrays", "InflateParams" })
public class PairedListActivity extends AppCompatActivity {
	@ViewInject(R.id.paired_list)
	private ListView list_paried;
	private ContractService service;
	private Map<String, Object> params = new HashMap<String, Object>();
	private PairedListAdapter pairedListAdapter;
	private static final int CURRENT_PAGER_SIZE = 10;
	private int currentPage = 0;
	private View footer;
	private boolean hasFind = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paired_list);
		x.view().inject(PairedListActivity.this);
		service = new ContractService(this);
		params.put("pageRows", CURRENT_PAGER_SIZE);
		params.put("currPage", currentPage);
		LayoutInflater inflater = LayoutInflater.from(this);
		footer = inflater.inflate(R.layout.listview_footer, null);
		initTitle();
		initData();
	}


	/**
	 * 初始化标题
	 */
	public void initTitle() {
		android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("订单");
		setSupportActionBar(toolbar);

		ActionBar ab = getSupportActionBar();

		assert ab!=null;

		ab.setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		pairedListAdapter = new PairedListAdapter(this, null);
		list_paried.setAdapter(pairedListAdapter);
		list_paried.setOnScrollListener(scrollListener);
		loadMore();
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
				if (list_paried.getFooterViewsCount() == 0) {
					list_paried.addFooterView(footer);
				}
			}
		}
	};
	
	private void loadMore() {
		currentPage ++;
		params.put("currPage", currentPage);
		service.getParnerList(params, new HttpCallBack(this) {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object... objects) {
				if(objects != null && objects.length > 0) {
					List<ParnerShip> list = (List<ParnerShip>) objects[0];
					if(list != null) {
						if(currentPage==1){
							pairedListAdapter.setData(list);
						}else {
							for (ParnerShip ship : list) {
								pairedListAdapter.addValue(ship);
							}
						}
						pairedListAdapter.notifyDataSetChanged();
					}
				}
				list_paried.removeFooterView(footer);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:
				finish();
				break;
		}
		return  true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		currentPage=0;
	}
}

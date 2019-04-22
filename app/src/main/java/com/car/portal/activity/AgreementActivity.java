package com.car.portal.activity;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.AgreementListAdapter;
import com.car.portal.entity.ContractList;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.ContractService;
import com.car.portal.service.GoodsService;
import com.car.portal.util.LogUtils;
import com.car.portal.view.QueryTitleView;

public class AgreementActivity extends Activity implements AdapterView.OnItemClickListener {
    @ViewInject(R.id.title_query)
    private QueryTitleView queryTitleView;
    @ViewInject(R.id.agreement_list)
    private ListView list_agreement;
    @ViewInject(R.id.search_text)
    private EditText searchView;
    @ViewInject(R.id.search_confirm)
    private ImageView searchBtn;
    @ViewInject(R.id.txt_no_data)
    private TextView txt_no_data;
    private ContractService service;
    private int currentPage = 1;
    private String condition;
    private AgreementListAdapter pairedListAdapter;
    private View footer;
    private ContractList contractList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        x.view().inject(this);
        service = new ContractService(this);
        contractList=new ContractList();
        list_agreement.setOnItemClickListener(this);
        initView();
        queryTitleView.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        queryTitleView.setMoreIsVisible();//隐藏标题栏最右边的图标
    }

    private HttpCallBack callBack = new HttpCallBack(this) {
        @SuppressWarnings("unchecked")
		@Override
        public void onSuccess(Object... objects) {
            if (objects != null && objects.length > 0) {
                List<ContractList> list = (List<ContractList>) objects[0];
                if(list.size() > 0) {
                    if (currentPage <= 1) {
                        pairedListAdapter.setData(list);
                    } else {
                        for (ContractList con : list) {
                            pairedListAdapter.addValue(con);
                        }
                    }
                    pairedListAdapter.notifyDataSetChanged();
                    if (list_agreement.getFooterViewsCount() > 0) {
                        list_agreement.removeFooterView(footer);
                    }
                    txt_no_data.setVisibility(View.GONE);
                } else {
                    txt_no_data.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onFail(int result, String message, boolean show) {
            super.onFail(result, message, show);
            if (list_agreement.getFooterViewsCount() > 0) {
                list_agreement.removeFooterView(footer);
            }
        }

        @Override
        public void onError(Object... objects) {
            super.onError(objects);
            if (list_agreement.getFooterViewsCount() > 0) {
                list_agreement.removeFooterView(footer);
            }
        }
    };

    /**
     * 初始化数据
     */
    @SuppressLint("InflateParams")
    public void initView() {
        pairedListAdapter = new AgreementListAdapter(this);
        list_agreement.setAdapter(pairedListAdapter);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                condition = searchView.getText().toString();
                currentPage = 1;
                service.getContractList(condition, currentPage, callBack);
            }
        });
        LayoutInflater inflater = LayoutInflater.from(this);
        footer = inflater.inflate(R.layout.listview_footer, null);
        list_agreement.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isFooter;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isFooter && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    currentPage++;
                    service.getContractList(condition, currentPage, callBack);
                    isFooter = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Adapter adapter = view.getAdapter();
                int count = adapter.getCount();
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0
                        && count >= currentPage * 10) {
                    isFooter = true;
                    if (list_agreement.getFooterViewsCount() == 0) {
                        list_agreement.addFooterView(footer);
                    }
                }
            }
        });


        final GoodsService goodsService=new GoodsService(this);
        User u = goodsService.getLoginUser();
        int companyId = 0;
        if(u != null) {
            companyId = u.getCompanyId();
        }
        goodsService.getGoods(companyId, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                    ArrayList<User> users = (ArrayList<User>) objects[0];
                    for (int i = 0; i < users.size(); i++)
                        goodsService.addUser(users.get(i));
                } else
                    LogUtils.e("AgreementActivity", "没有数据");
            }
        });
        service.getContractList(condition, currentPage, callBack);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this, AgreementItemActivity.class);
        contractList= (ContractList) pairedListAdapter.getItem(position);
        intent.putExtra("id",contractList.getId() + "");
        LogUtils.e("AgreementAct", "      id:" + contractList.getId());
        startActivity(intent);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);

    }
}

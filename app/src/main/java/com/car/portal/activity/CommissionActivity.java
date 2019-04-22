package com.car.portal.activity;

import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.CommistionAdapter;
import com.car.portal.entity.CommissionList;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.ContractService;
import com.car.portal.view.BaseTitleView;

public class CommissionActivity extends Activity implements AdapterView.OnItemClickListener{
    @ViewInject(R.id.base_title_view)
    private BaseTitleView baseTitleView;
    @ViewInject(R.id.commission_list)
    private ListView commission_list;
    @ViewInject(R.id.txt_no_one_warn)
    private TextView txt_no_one_warn;
    private CommistionAdapter commistionAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_list);
        x.view().inject(this);
        baseTitleView.setTitle(getResources().getString(R.string.title_commission));
        commistionAdapter = new CommistionAdapter(this);
        commission_list.setAdapter(commistionAdapter);
        commission_list.setOnItemClickListener(this);
    }

    /**
     * 获取数据
     */
    public void getData(){
        final ContractService service = new ContractService(this);
        service.getCommentList(new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0) {
                    List<CommissionList> contractLists = (List<CommissionList>) objects[0];
                    commistionAdapter.setList(contractLists);
                    commistionAdapter.notifyDataSetChanged();
                    txt_no_one_warn.setVisibility(View.GONE);
                } else {
                    txt_no_one_warn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this, AgreementItemActivity.class);
        CommissionList commissionList= (CommissionList) commistionAdapter.getItem(position);
        intent.putExtra("id", commissionList.getContractId());
        startActivity(intent);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	if(hasFocus && commistionAdapter.getCount() <= 0) {
    		getData();
    	}
    }
}

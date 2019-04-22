package com.car.portal.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.car.portal.R;
import com.car.portal.adapter.AuthorAdapter;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.GoodsService;
import com.car.portal.util.ToastUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class AuthorActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    @ViewInject(R.id.list_author)
    private ListView list_author;
    private AuthorAdapter authorAdapter;
    private GoodsService service;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        x.view().inject(this);
        service = new GoodsService(this);
        authorAdapter = new AuthorAdapter(this);
        inittooler();
        getData();
        list_author.setAdapter(authorAdapter);
        list_author.setOnItemClickListener(this);
    }

    private void inittooler() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("临时授权");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab!=null;

        ab.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 或去去公司全部成员
     */
    public void getData(){
    	User u = service.getSavedUser();
        int companyId = 0;
        if(u != null) {
        	companyId = u.getCompanyId();
        }
        service.getGoods(companyId, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0){
                    List<User> users = (ArrayList<User>) objects[0];
                    authorAdapter.setList(users.subList(1, users.size()));
                    authorAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 授权方法
     * @param uid
     */
    public void assistAuthor(final String uid){
        service.assistAuthor(uid, new HttpCallBack(this) {
            @Override
            public void onSuccess(Object... objects) {
                if (objects != null && objects.length > 0){
                    int result = (int) objects[0];
                    if (result == 1){
                        ToastUtil.show("授权成功！", AuthorActivity.this);
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(user!=null) {
            if ("Y".equals(user.getPower())) {
                authorAdapter.selectOne(position);
                authorAdapter.notifyDataSetChanged();
                User u = (User) authorAdapter.getItem(position);
                assistAuthor(u.getUid() + "");
            } else {
                ToastUtil.show("你没有权限进行该操作", AuthorActivity.this);
            }
        }
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	if(hasFocus && authorAdapter.getCount() <= 0) {
//    		user = service.getLoginUser();
//    		getData();
    	}
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
}

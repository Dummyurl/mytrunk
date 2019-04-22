package com.car.portal.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.adapter.FindthecompanyAdapter;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.Findcpybeen;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.MyHttpUtil;
import com.car.portal.http.XUtil;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoincompanylistActivity extends AppCompatActivity {
    @BindView(R.id.img_found_return)
    ImageView imgFoundReturn;
    @BindView(R.id.ryv_found_item)
    RecyclerView ryvFoundItem;
    @BindView(R.id.btn_join_agree)
    Button btnFoundApply;
    @BindView(R.id.btn_join_decline)
    Button btnJoinDecline;
    private FindthecompanyAdapter adapter;
    SharedPreferences sp, spuser;
    String cpyid;
    private List<Findcpybeen> list = new ArrayList<>();
    private MyHttpUtil util;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findapply);
        ButterKnife.bind(this);
        initView();
        initdata();
    }

    private void initdata() {
        findApplyJoinCompany();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        ryvFoundItem.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ryvFoundItem.setLayoutManager(layoutManager);

    }

    private void findApplyJoinCompany() {
        spuser = getSharedPreferences("users", MODE_PRIVATE);
        String uid = spuser.getInt("uid", 0) + "";
        util = new MyHttpUtil(JoincompanylistActivity.this);
        String url = util.getUrl(R.string.url_findApplyJoinCompany) + "?toUid=" + uid;
        Log.d("url", url);
        XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList>>() {


            @Override
            public void onSuccess(BaseEntity<ArrayList> arg0) {
                ArrayList<LinkedTreeMap<String, Object>> data = arg0.getData();
                ArrayList<Findcpybeen> pmbList = new ArrayList<Findcpybeen>();
                String[] arr;
                String datalist;
                if (data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        Findcpybeen fdp = new Findcpybeen();
                        datalist = data.get(i) + "";
                        arr = datalist.split(",");
                        fdp.setId(arr[0]);
                        fdp.setUid(arr[1]);
                        fdp.setProposer(arr[2]);
                        fdp.setCopmanyid(arr[3]);
                        fdp.setTouid(arr[4]);
                        fdp.setTelphone(arr[5]);
                        fdp.setTimer(arr[6]);
                        pmbList.add(fdp);
                    }
                    list.addAll(pmbList);
                    adapter = new FindthecompanyAdapter(list);
                    ryvFoundItem.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
            }
        });
    }

    private void initView() {
    }


    public static void gotofindcop(Context context) {
        Intent intent = new Intent(context, JoincompanylistActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.img_found_return, R.id.btn_join_agree,R.id.btn_join_decline})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_found_return:
                finish();
                break;
            case R.id.btn_join_agree:
                sp = getSharedPreferences("SP", MODE_PRIVATE);
                cpyid = sp.getString("cpyid", "-1");
                int position = sp.getInt("position", 0);
                if (cpyid.equals("-1")) {
                    Toast.makeText(this, "您还未选中申请", Toast.LENGTH_SHORT).show();
                } else {
                    if (cpyid.contains("[")) {
                        cpyid = cpyid.replace("[", "").substring(0, cpyid.length() - 3);
                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                    setapply(cpyid, 1);
                    adapter.removeData(position);
                }
            case R.id.btn_join_decline:
                sp = getSharedPreferences("SP", MODE_PRIVATE);
                cpyid = sp.getString("cpyid", "-1");
                int positiondel = sp.getInt("position", 0);
                if (cpyid.equals("-1")) {
                    Toast.makeText(this, "您还未选中申请", Toast.LENGTH_SHORT).show();
                } else {
                    if (cpyid.contains("[")) {
                        cpyid = cpyid.replace("[", "").substring(0, cpyid.length() - 3);
                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                    setapply(cpyid, 0);
                    adapter.removeData(positiondel);
                }

                break;
        }
    }

    private void setapply(String id, int b) {
        util = new MyHttpUtil(JoincompanylistActivity.this);
        String url = util.getUrl(R.string.url_processApplyJoinCompany) + "?id=" + id + "&flag=" + b;
        XUtil.Post(url, null, new MyCallBack<BaseEntity>() {
            @Override
            public void onSuccess(BaseEntity baseEntity) {
                Log.d("onsucc", baseEntity.getMessage());
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Log.d("onsucc", throwable.toString());
            }
        });
    }

}

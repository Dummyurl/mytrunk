package com.car.portal.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.car.portal.R;
import com.car.portal.contract.NotificationContract;
import com.car.portal.entity.NotificationEntity;
import com.car.portal.model.NotificationModel;
import com.car.portal.presenter.NotificationPresenter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends AppCompatActivity implements NotificationContract.View {

    NotificationContract.Presenter mpresenter;
    @BindView(R.id.recy_not_list)
    RecyclerView recyNotList;
    @BindView(R.id.text_not_compile)
    TextView textNotCompile;
    @BindView(R.id.checkbox_not_checkall)
    CheckBox checkboxNotCheckall;
    @BindView(R.id.Rel_notbottom)
    RelativeLayout RelNotbottom;
    @BindView(R.id.line_not_compile)
    LinearLayout lineNotCompile;
    @BindView(R.id.text_not_delete)
    TextView textNotDelete;
    @BindView(R.id.swf_not_getdata)
    SwipeRefreshLayout swfNotGetdata;
    private NotificationModel.NotificationAdapter adapter;
    private List<NotificationEntity> datalist;
    boolean isshow = false;
    BottomSheetBehavior bottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        new NotificationPresenter(this);
        inittooler();
        initdata();
    }

    private void initdata() {
        datalist = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
//        recyNotList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyNotList.setLayoutManager(layoutManager);
        adapter = new NotificationModel.NotificationAdapter(NotificationActivity.this, datalist);
        recyNotList.setAdapter(adapter);
        mpresenter.getnotificationlist(this, isshow);
        checkboxNotCheckall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.chechall(true);
                } else {
                    adapter.chechall(false);
                }
            }
        });
        swfNotGetdata.setColorSchemeResources(R.color.view_blue);
        swfNotGetdata.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mpresenter.getnotificationlist(NotificationActivity.this, isshow);
                swfNotGetdata.setRefreshing(false);
            }
        });
    }

    private void inittooler() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("系统通知");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab != null;

        ab.setDisplayHomeAsUpEnabled(true);

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.Rel_notbottom));
        bottomSheetBehavior.setSkipCollapsed(true);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void setPresenter(NotificationContract.Presenter presenter) {
        mpresenter = presenter;
    }

    @Override
    public void shownotificationlist(List<NotificationEntity> list) {
        datalist.clear();
        datalist.addAll(list);
        adapter.notifyDataSetChanged();
    }


    @OnClick({R.id.line_not_compile, R.id.text_not_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.line_not_compile:
                if (isshow) {
                    isshow = false;
                    adapter.isshowcheck(false);
                    textNotCompile.setText("编辑");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                } else {
                    isshow = true;
                    adapter.isshowcheck(true);
                    textNotCompile.setText("取消");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }


                break;
            case R.id.text_not_delete:
                new MaterialDialog.Builder(this)
                        .content("是否清除已选中的消息通知？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                adapter.deleteItem();
                                checkboxNotCheckall.setChecked(false);
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }


    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}

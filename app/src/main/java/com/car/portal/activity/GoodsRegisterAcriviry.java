package com.car.portal.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.adapter.BottomDialogMenuAdapter;
import com.car.portal.adapter.GoodsRegisterBaseAdapter;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.Boss;
import com.car.portal.entity.MyTypes;
import com.car.portal.fragment.GoodsListFragment;
import com.car.portal.http.MyCallBack;
import com.car.portal.service.GoodsService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.view.DividerItemDecoration;
import com.google.gson.internal.LinkedTreeMap;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("UseSparseArrays")
public class GoodsRegisterAcriviry extends AppCompatActivity implements
        AdapterView.OnItemClickListener {
    @BindView(R.id.tv_goodtype)
    TextView tvGoodtype;
    @BindView(R.id.list_typename_act)
    LinearLayout listTypenameAct;
    @BindView(R.id.Drawergoods)
    DrawerLayout Drawergoods;
    @ViewInject(R.id.list_typename)
    private ListView list_goodstype;
    /* @ViewInject(R.id.query_title_view)
     private QueryTitleView queryTitleView;*/
    //    @ViewInject(R.id.frame)
//    private FrameLayout frameLayout;
    private GoodsRegisterBaseAdapter registerBaseAdapter;
    private ArrayList<MyTypes> typelist;
    private int selectnum = -1;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private GoodsListFragment goodsListFragment;
    private int select_item = 0;
    private GoodsService service;
    private PopupWindow popupWindow;
    private View popupView;
    private TextView txt_today, txt_tomorrow, txt_three, txt_five;
    private LinearLayout linearLayout;
    private String m_condition = "";
    private boolean hasSearch = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_register);
        ButterKnife.bind(this);
        x.view().inject(this);
        initTitle();
        service = new GoodsService(this);
        list_goodstype.setOnItemClickListener(this);

        typelist = new ArrayList<MyTypes>();
        registerBaseAdapter = new GoodsRegisterBaseAdapter(GoodsRegisterAcriviry.this, select_item);
        list_goodstype.setAdapter(registerBaseAdapter);
        initFragment();
    }

    /**
     * 初始化标题
     */
    public void initTitle() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("货物联系");
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.search:
                Intent intent = new Intent(this,QuerybynameActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_linkman, menu);
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


    /**
     * 初始化Fragment
     */
    public void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        goodsListFragment = GoodsListFragment.newInstance(selectnum, m_condition);
        fragmentTransaction.replace(R.id.frame, goodsListFragment);
        fragmentTransaction.addToBackStack(null);
        //提交修改
        fragmentTransaction.commit();

    }

    /**
     * 显示选择时间的popupWindow
     */
   /* public void showePopupWindow() {
        initPopupWindow();
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(queryTitleView);
        popupListener();
    }*/

    /**
     * 初始化popupWindow
     */
    public void initPopupWindow() {
        if (popupView == null) {
            popupView = LayoutInflater.from(this).inflate(R.layout.popu_date, null);
            popupWindow = new PopupWindow(popupView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams
                    .MATCH_PARENT, true);
            linearLayout = (LinearLayout) popupView.findViewById(R.id.line_popu_date);
            txt_today = (TextView) popupView.findViewById(R.id.txt_today);
            txt_tomorrow = (TextView) popupView.findViewById(R.id.txt_tomorrow);
            txt_three = (TextView) popupView.findViewById(R.id.txt_three);
            txt_five = (TextView) popupView.findViewById(R.id.txt_five);
            linearLayout.getBackground().setAlpha(0);
        }
    }

    /**
     * popup监听事件
     */
    public void popupListener() {
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        txt_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypes();
            }
        });
        txt_tomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txt_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txt_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 添加货物类型数据
     *
     * @return
     */
    public void getTypes() {
        service.getGoodsTypes(new MyCallBack<BaseEntity<ArrayList>>() {
            public void onError(Throwable throwable, boolean b) {
                BaseUtil.writeFile("GoodsRegester", throwable);
            }

            public void onSuccess(BaseEntity<ArrayList> arg0) {
                if (arg0.getResult() == 1) {
                    hasSearch = true;
                    ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
                    ArrayList<MyTypes> types = new ArrayList<MyTypes>();
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            MyTypes goodtypes = new MyTypes();
                            LinkMapToObjectUtil.getObject(list.get(i), goodtypes);
                            types.add(goodtypes);
                        }
                    }
                    registerBaseAdapter.setData(types);
                    registerBaseAdapter.notifyDataSetChanged();
                    typelist = types;
                }
            }
        });
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (typelist.size() > 0) {
            selectnum = typelist.get(position).getId();
            select_item = position;
        } else {
            selectnum = 0;
            select_item = 0;
        }
        initFragment();
        registerBaseAdapter.setSelectItem(select_item);
        registerBaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !hasSearch) {
            getTypes();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //按下返回鍵退出當前activity
        finish();
    }

    @OnClick({R.id.tv_goodtype})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goodtype:
                Drawergoods.openDrawer(listTypenameAct);
                break;
        }
    }
}

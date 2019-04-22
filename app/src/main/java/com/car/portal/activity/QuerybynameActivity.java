package com.car.portal.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;


import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.contract.QuerybynameContract;
import com.car.portal.entity.Boss;
import com.car.portal.entity.Historyquerylinkman;
import com.car.portal.fragment.GoodsListFragment;
import com.car.portal.fragment.QueryBynameresult;
import com.car.portal.fragment.Querybynamehistory;
import com.car.portal.model.QuerybynameModel;
import com.car.portal.presenter.QuerybynamePresenter;
import com.nostra13.universalimageloader.utils.L;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.Gravity.BOTTOM;

public class QuerybynameActivity extends AppCompatActivity implements QuerybynameContract.View {

    @BindView(R.id.search_query_byname)
    SearchView searchQueryByname;
    private QuerybynameContract.Presenter mpresenter;
    private Querybynamehistory querybynamehistory;
    PopupWindow window;
    RecyclerView recyclerView;
    QuerybynameModel.QuerybynameAdapter adapter;
    List<Boss> list = new ArrayList<>();
    ArrayList<Boss> resultlist = new ArrayList<>();
    String str;
    FragmentTransaction transaction;
    FragmentManager manager;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_querybyname);
        ButterKnife.bind(this);
        new QuerybynamePresenter(this);
        initview();
    }

    private void initview() {
//        searchQueryByname.setIconified(false);
        searchQueryByname.setIconifiedByDefault(false);
        searchQueryByname.setSubmitButtonEnabled(true);
        ImageView iv_submit = searchQueryByname.findViewById(R.id.search_go_btn);
        iv_submit.setImageResource(R.drawable.ic_check_24dp);
        searchQueryByname.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mpresenter.getquerylist(s,getApplicationContext());
                searchQueryByname.clearFocus();
                window.dismiss();
                insertTerms(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                str =s;
                if (!"".equals(s)){
                    mpresenter.getqueryname(s, getApplicationContext());
                }else {
                    window.dismiss();
                }
                return false;
            }
        });
        querybynamehistory = new Querybynamehistory();
         manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_query,querybynamehistory).commit();
        initpop();
        adapter.setOnItem(new QuerybynameModel.QuerybynameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Boss boss) {
                searchQueryByname.clearFocus();
                resultlist.clear();
                resultlist.add(boss);
                initFragment(resultlist);
                window.dismiss();
                insertTerms(boss.getName());
            }
        });
    }

    /**
     *
     *@作者 舒椰
     *@时间 2019/4/4 0004 下午 3:33
     * 将查询的词插入数据库保存起来，先做一次查询，判断数据库内有无相同的词语
     * 并且数量不能超过10，一旦超过10，执行查询删除
     */
   protected void insertTerms(String s){
       if(s.length()>8){
           s = s.substring(0,7)+"...";
       }

       List<Historyquerylinkman> alllist = LitePal.findAll(Historyquerylinkman.class);
       List<Historyquerylinkman> list = LitePal.where("terms = ?", s).find(Historyquerylinkman.class);
       if(alllist.size()<10&&list.size() == 0) {
               Historyquerylinkman historyquerylinkman = new Historyquerylinkman();
               historyquerylinkman.setTerms(s);
               historyquerylinkman.save();
       }else if(list.size()==0){
         LitePal.delete(Historyquerylinkman.class,1);
            Historyquerylinkman linkman = LitePal.findFirst(Historyquerylinkman.class);
            linkman.delete();
           Historyquerylinkman historyquerylinkman = new Historyquerylinkman();
           historyquerylinkman.setTerms(s);
           historyquerylinkman.save();
       }
    }

    /**
     *
     *@作者 舒椰
     *@时间 2019/4/4 0004 下午 4:54
     * 初始化底部搜索帮助的布局
     */

    private void initpop() {
        View contentView=getLayoutInflater().inflate(R.layout.item_query_userlist, null);
        window=new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setOutsideTouchable(false);
        window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        recyclerView = contentView.findViewById(R.id.recy_content_link);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new QuerybynameModel.QuerybynameAdapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    /**
     *
     *@作者 舒椰
     *@时间 2019/4/4 0004 下午 4:52
     * 点击历史记录，子页面将值传过来，执行搜索方法
     */
    
    public void setLinkmanname(String name){
        mpresenter.getquerylist(name,getApplicationContext());
        searchQueryByname.clearFocus();
        window.dismiss();
        insertTerms(name);
    }

    /**
     *
     *@作者 舒椰
     *@时间 2019/4/4 0004 下午 12:03
     *  创建新的Fragment来展示搜索结果
     */

    public void initFragment(ArrayList<Boss> boss) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        QueryBynameresult queryBynameresult = QueryBynameresult.newInstance(boss);
        fragmentTransaction.replace(R.id.frame_query, queryBynameresult);
        //提交修改
        fragmentTransaction.commit();
    }


    /**
     *
     *@作者 舒椰
     *@时间 2019/4/4 0004 下午 4:53
     *  得到服务器返回来的模糊搜索的值。
     */
    @Override
    public void setqueryname(List<Boss> d) {
        list.clear();
        if (!searchQueryByname.hasFocus()){
           return;
        }
        if (d.size()>0&&!"".equals(str)) {
            list.addAll(d);
            if (!window.isShowing()) {
                window.showAsDropDown(searchQueryByname, BOTTOM, 10, 0);
            }
        }else {
            window.dismiss();
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void setquerylist(List<Boss> d) {
        resultlist.clear();
        resultlist.addAll(d);
        initFragment(resultlist);
    }


    @Override
    public void setPresenter(QuerybynameContract.Presenter presenter) {
        mpresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

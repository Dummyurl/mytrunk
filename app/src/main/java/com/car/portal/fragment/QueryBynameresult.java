package com.car.portal.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.car.portal.R;
import com.car.portal.activity.AddOrderActivity;
import com.car.portal.activity.AddlinkmanActivity;
import com.car.portal.activity.CallrecordsActivity;
import com.car.portal.activity.ContactsInfoActivity;
import com.car.portal.activity.LoginActivity;
import com.car.portal.adapter.BottomDialogMenuAdapter;
import com.car.portal.adapter.ContactsAdapter;
import com.car.portal.adapter.GoodsListBaseAdapter;
import com.car.portal.entity.Boss;
import com.car.portal.entity.Contacts;
import com.car.portal.entity.Goods;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.UserService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;
import com.car.portal.view.DividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBynameresult extends Fragment implements BaseQuickAdapter.OnItemClickListener {

    private ArrayList<Boss> mbosslist= new ArrayList<Boss>();
    private GoodsListBaseAdapter goodsListBaseAdapter;
    private ListView list_goos;
    private TextView text_empty_hint;
    private View view;
    private static QueryBynameresult frag;
    private static boolean first =false;
    private int pos;
    private PopupWindow popupWindow;
    private View popupView;
    private Dialog bottomDialog;
    private ListView list_contats;
    private LinearLayout line_call;
    private ImageView img_check;
    private UserService userService;
    private ContactsAdapter adapter;
    private int bossId,uid;
    private String phone;
    private LinearLayout linearLayout,emptylinear;
    private ArrayList<Contacts> conlist;
    public static QueryBynameresult newInstance(ArrayList<Boss> d) {
        if(!first) {
            frag = new QueryBynameresult();
            Bundle b = new Bundle();
            b.putParcelableArrayList("data", d);
            frag.setArguments(b);
            first = true;
        } else {
            Bundle b = new Bundle();
            b.putParcelableArrayList("data",d);
            Message msg = new Message();
            msg.what = INIT_FRAG;
            msg.setData(b);
            frag.handler.handleMessage(msg);
        }
        return frag;

    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            mbosslist.clear();
            ArrayList<Boss> boss  = msg.getData().getParcelableArrayList("data");
            if(boss.size()==0){
                text_empty_hint.setText("未找到符合的联系人");
            }
                mbosslist.addAll(boss);
                goodsListBaseAdapter.setData(mbosslist);
                goodsListBaseAdapter.notifyDataSetChanged();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.goods_list_fragment,null);
        initView();
        return view;
    }

    private void initView() {
        userService = new UserService(getContext());
        ArrayList<Boss> boss = getArguments().getParcelableArrayList("data");
        text_empty_hint = view.findViewById(R.id.text_empty_hint);
        if (boss.size()==0){
            text_empty_hint.setText("未找到符合的联系人");
        }else {
            mbosslist.addAll(boss);
        }
        list_goos = (ListView) view.findViewById(R.id.list_goods);
        list_goos.setEmptyView(view.findViewById(R.id.empty));
        goodsListBaseAdapter = new GoodsListBaseAdapter(getActivity());
        goodsListBaseAdapter.setData(mbosslist);
        list_goos.setAdapter(goodsListBaseAdapter);
        list_goos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                List list=new ArrayList();
                pos = position;
                list.add("查看客户");
                list.add("登记货物");
                list.add("联系记录");
                list.add("查看货物登记");
                list.add("添加联系人");
                showBottomDialog(list);

            }
        });
        goodsListBaseAdapter.setImageClick(new GoodsListBaseAdapter.ImageClickListener() {
            @Override
            public void onClick(View v, Boss b) {
                if(b != null) {
                    showPopupWindow(b.getId());
                }
            }
        });
    }


    public void showPopupWindow(String num) {
        initPopupWindow(num);
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.goods_register,null);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER, 0, 0);
        popupOnClick();
    }



    public void setListen(int num){
        uid=userService.getLoginUser().getUid();
        phone = conlist.get(num).getTel();
        //bossId = conlist.get(num).getBossId();
    }

    public void popupOnClick(){
        list_contats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setListen(position);
                adapter.seletNum(position);
                adapter.notifyDataSetChanged();
            }
        });
        /**
         * 点击拔号，通过获取列表中的电话号码进行拔号操作
         */
        line_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("phone", phone);
                map.put("bossId", bossId);
                map.put("uid", uid);
                //向后台写数据通话记录
                HttpCallBack callBack = new HttpCallBack(getActivity()) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(Object... objects) {
                        //popupWindow.dismiss();
                        BaseUtil.diallPhone(getContext(),phone);
                    }
                };
                userService.saveOfflineCall(bossId,uid,phone,callBack );
            }
        });
    }


    /**
     * 显示popupWindow
     */
    public void initPopupWindow(String num){
        if(popupWindow==null){
            popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popu_contacts,null);
            linearLayout= (LinearLayout) popupView.findViewById(R.id.line_back);
            linearLayout.getBackground().setAlpha(100);
            popupWindow = new PopupWindow(popupView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            line_call= (LinearLayout) popupView.findViewById(R.id.line_call);
            list_contats= (ListView) popupView.findViewById(R.id.list_contacts);
            img_check= (ImageView) popupView.findViewById(R.id.img_check);
            adapter = new ContactsAdapter(getActivity());
            list_contats.setAdapter(adapter);

            popupView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
        bossId = Integer.parseInt(num);
        getContacts(num);
    }


    /**
     * 获取联系人信息
     *
     * @param num
     */
    public void getContacts(String num) {
        if(!StringUtil.isNullOrEmpty(num)) {
            final int nm = Integer.valueOf(num);
            HttpCallBack callBack = new HttpCallBack(getActivity()) {
                @SuppressWarnings("unchecked")
                @Override
                public void onSuccess(Object... objects) {
                    if(objects != null && objects.length > 0) {
                        if(list_contats != null) {
                            conlist = (ArrayList<Contacts>) objects[0];
                            if(conlist!=null && conlist.size()>0) {
                                phone = conlist.get(0).getTel();
                            }
                            adapter.setData((List<Contacts>) objects[0]);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            };
            userService.getBossContracts(nm, callBack);
        }
    }



    /**
     *
     *@作者 Administrator
     *@时间 2019/3/11 0011 下午 3:59
     *弹出底部选择框
     */
    private void showBottomDialog(final List<String> menuList) {
        bottomDialog = new Dialog(getActivity(), R.style.BottomDialog);
        bottomDialog.setCanceledOnTouchOutside(true);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_bottom_menu, null);
        final RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view);
        BottomDialogMenuAdapter dialogMenuAdapter = new BottomDialogMenuAdapter(R.layout.item_bottom_dialog_menu, menuList, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_dis_button, null);
        dialogMenuAdapter.setFooterView(view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), linearLayoutManager));
        dialogMenuAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(dialogMenuAdapter);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.cancel();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = this.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

    }


    private static final int INIT_FRAG = 0x1025;

    @Override
    public void onDestroy() {
        super.onDestroy();
        first = false;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Boss boss = null;
        switch (position){
            case 0:
                boss = mbosslist.get(pos);
                if(boss != null) {
                    Intent intent = new Intent(getActivity(), ContactsInfoActivity.class);
                    intent.putExtra("contacts_name", boss.getName());
                    intent.putExtra("boss_name", boss.getBossName());
                    intent.putExtra("company", boss.getCompany());
                    intent.putExtra("connect_data", boss.getConnectDate());
                    intent.putExtra("other", boss.getOtherInfo());
                    intent.putExtra("route", boss.getRoute());
                    intent.putExtra("type", boss.getTypess());
                    startActivity(intent);
                }
                bottomDialog.dismiss();
                break;
            case 1:
                boss = mbosslist.get(pos);
                if(boss!=null) {
                    Intent intent = new Intent(getActivity(), AddOrderActivity.class);
                    intent.putExtra("bossId", boss.getId());
                    intent.putExtra("contactsName", boss.getName());
                    intent.putExtra("bossName", boss.getBossName());
                    intent.putExtra("company", boss.getCompany());
                    intent.putExtra("uid", userService.getLoginUser().getUid());
                    intent.putExtra("routeId", boss.getRouteN());
                    intent.putExtra("route", boss.getRoute());
                    intent.putExtra("selectnum", -1);
                    intent.putExtra("startCity", boss.getCity());
                    intent.putExtra("startCityName", boss.getCityName());
                    LogUtils.e("GoodslistFragemnt", "    routeId:" + boss.getRouteN());
                    startActivity(intent);
                }
                bottomDialog.dismiss();
                break;
            case 2:
                Intent itent = new Intent(getActivity(), CallrecordsActivity.class);
                startActivity(itent);
                bottomDialog.dismiss();
                break;

            case 3:
                bottomDialog.dismiss();
                break;
            case 4:
                Intent intent = new Intent(getActivity(),AddlinkmanActivity.class);
                startActivity(intent);
                bottomDialog.dismiss();
                break;
        }
    }



}

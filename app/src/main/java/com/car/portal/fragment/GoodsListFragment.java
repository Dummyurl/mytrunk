package com.car.portal.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.car.portal.R;
import com.car.portal.activity.AddOrderActivity;
import com.car.portal.activity.AddlinkmanActivity;
import com.car.portal.activity.CallrecordsActivity;
import com.car.portal.activity.ContactsInfoActivity;
import com.car.portal.activity.GoodsRegisterAcriviry;
import com.car.portal.adapter.BottomDialogMenuAdapter;
import com.car.portal.adapter.ContactsAdapter;
import com.car.portal.adapter.GoodsListBaseAdapter;
import com.car.portal.entity.Boss;
import com.car.portal.entity.Contacts;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.GoodsService;
import com.car.portal.service.UserService;
import com.car.portal.util.BaseUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;
import com.car.portal.view.DividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;

public class GoodsListFragment extends Fragment implements BaseQuickAdapter.OnItemClickListener{
    private ArrayList<Boss> mbosslist;
    private GoodsListBaseAdapter goodsListBaseAdapter;
    private ListView list_goos;
    private View view;
    private int selectnum = -1;
    private GoodsService service;
    private int currentPage = 1;
    private View footer;
    private UserService userService;
    private String m_condition = "";
    private int pos;
    private PopupWindow popupWindow;
    private View popupView;
    private ListView list_contats;
    private LinearLayout line_call;
    private ImageView img_check;
    private boolean ischeck=false;
    private LinearLayout linearLayout,emptylinear;
    private int bossId;
    private String phone,bid;//电话号码。sting类型的bossid,为了底部传值而设置
    private int uid;
    private Dialog bottomDialog;
   // private ContactsAdapter adapter;
    private ArrayList<Contacts> conlist;
    private Contacts selectContact;
    private OptionsPickerView pvNoLinkOptions;
    private List<String> phonelist = new ArrayList<>();//电话列表
    private List<String> linklist = new ArrayList<>();//用户名加电话列表
    private MaterialDialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.goods_list_fragment, null);
        x.view().inject(this, view);
        selectnum = getArguments().getInt(Boss.ARG_POSITION);
        m_condition = getArguments().getString("condition");
        initView();
        LogUtils.e("GoodsListFragement", "   onCreateView:      selectId:" + selectnum + "    confition:" + m_condition);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
    public GoodsListFragment() {}


    //登陆弹框
    private void onshow(boolean b) {
        if(b) {
            dialog = new MaterialDialog.Builder(getContext())
                    .content("请稍后...")
                    .progress(true, 0)
                    .show();
        }else if (dialog!=null){
            dialog.dismiss();
        }
    }


    /**
     * 初始化控件并绑定监听
     */
    @SuppressLint("InflateParams")
	public void initView() {
        userService=new UserService(getActivity());
        mbosslist=new ArrayList<Boss>();
        list_goos = (ListView) view.findViewById(R.id.list_goods);
        list_goos.setEmptyView(view.findViewById(R.id.empty));
        emptylinear = view.findViewById(R.id.empty);
        footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
        service = new GoodsService(getActivity());
        goodsListBaseAdapter = new GoodsListBaseAdapter(getActivity());
        goodsListBaseAdapter.setImageClick(new GoodsListBaseAdapter.ImageClickListener() {
			@Override
			public void onClick(View v, Boss b) {
				if(b != null) {
                    onshow(true);
					showPopupWindow(b.getId());
				}
			}
		});
        list_goos.setAdapter(goodsListBaseAdapter);
        list_goos.setOnScrollListener(l);
        list_goos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                List list=new ArrayList();
                pos = position;
                bid = mbosslist.get(position).getId();
                list.add("查看客户");
                list.add("登记货物");
                list.add("呼叫客户");
                list.add("联系记录");
                list.add("查看货物登记");
                list.add("添加联系人");
                showBottomDialog(list);

            }
        });
        emptylinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(getActivity(),AddlinkmanActivity.class);
              startActivity(intent);
            }
        });
        getBossInfo(selectnum, currentPage, m_condition);
    }

    /**
     * 上拉加载数据
     */
    private AbsListView.OnScrollListener l = new AbsListView.OnScrollListener() {
        private boolean isFooter;
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (isFooter && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                currentPage++;
                getBossInfo(selectnum, currentPage, m_condition);
                isFooter = false;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            int count = goodsListBaseAdapter.getCount();
            if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0
                    && count >= currentPage * 10) {
                isFooter = true;
                if (list_goos.getFooterViewsCount() == 0) {
                    list_goos.addFooterView(footer);
                }
            }
        }
    };

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
                    intent.putExtra("selectnum", selectnum);
                    intent.putExtra("startCity", boss.getCity());
                    intent.putExtra("startCityName", boss.getCityName());
                    intent.putExtra("startCityCode",boss.getCityCode());
                    LogUtils.e("GoodslistFragemnt", "    routeId:" + boss.getRouteN());
                    startActivity(intent);
                }
                bottomDialog.dismiss();
                break;
            case 2:
                onshow(true);
                showPopupWindow(bid);
                bottomDialog.dismiss();
                break;

            case 3:
                Intent itent = new Intent(getActivity(), CallrecordsActivity.class);
                startActivity(itent);
                bottomDialog.dismiss();
                break;

            case 4:
                bottomDialog.dismiss();
                break;
            case 5:
                Intent intent = new Intent(getActivity(),AddlinkmanActivity.class);
                startActivity(intent);
                bottomDialog.dismiss();
                break;
        }
    }


    /**
     * 获取客户信息
     */
    public void getBossInfo(final int num, final int page, final String condition) {
        final Map<String, Object> map = new HashMap<String, Object>();
        LogUtils.e("GoodsListFragment", "      condition" + condition);
        map.put("condition", condition == null ? "" : condition);
        map.put("typeId", num);
        map.put("currentPage", page);
    	service.getBossInfo(map, new HttpCallBack(getActivity()) {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object... objects) {
				if(objects != null && objects.length > 0) {
                    if(currentPage == 1) {
                        mbosslist = (ArrayList<Boss>) objects[0];
                            goodsListBaseAdapter.setData(mbosslist);

                    }else{
                        List<Boss> list = (ArrayList<Boss>) objects[0];
                        if(list != null) {
                            for (Boss boss : list) {
                                goodsListBaseAdapter.addValue(boss);
                            }
                        }
                    }
                    goodsListBaseAdapter.notifyDataSetChanged();
                    list_goos.removeFooterView(footer);
				}
			}
		});
    }

    /**
     * 获取listview传递的selectnum
     *
     * @param position
     * @return
     */
    public static GoodsListFragment newInstance(int position, String condition) {
    	if(frag == null) {
			if(frag == null) {
    			frag = new GoodsListFragment();
    			Bundle b = new Bundle();
    			b.putInt(Boss.ARG_POSITION, position);
    			b.putString("condition", condition);
    			frag.setArguments(b);
			}
    	} else {
    		Bundle b = new Bundle();
    		b.putInt("position", position);
    		b.putString("condition", condition);
    		Message msg = new Message();
    		msg.what = INIT_FRAG;
    		msg.setData(b);
    		frag.handler.handleMessage(msg);
    	}
        return frag;
    }
    
    private static GoodsListFragment frag;
    
    @SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		mbosslist.remove(mbosslist);
    		goodsListBaseAdapter.setData(mbosslist);
    		goodsListBaseAdapter.notifyDataSetChanged();
    		LogUtils.e("Tag", goodsListBaseAdapter.getCount() + "");
    		String condition = msg.getData().getString("condition");
    		int position = msg.getData().getInt("position");
    		m_condition = condition;
    		selectnum = position;
    		currentPage = 1;
    		getBossInfo(position, frag.currentPage, condition);
    	}
    };
    
    /**
     * 初始化Popupwindow
     */
    public void showPopupWindow(String num) {
        //initPopupWindow(num);
//        View view=LayoutInflater.from(getActivity()).inflate(R.layout.goods_register,null);
//        popupWindow.setTouchable(true);
//        if(view!=null) {
//            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//        }
//        popupOnClick();
        bossId = Integer.parseInt(num);
        getContacts(num);
    }
    /**
     *
     *@作者：舒椰
     * 弹出底部电话选择
     *@时间： 2019/4/22 0022 下午 5:29
     */
    
    
    public void showPickWindow(){
        onshow(false);
        pvNoLinkOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                phone = phonelist.get(options1);
                popupOnClick(phone);
            }
        })
                .setContentTextSize(20)//滚轮文字大小
                .setLineSpacingMultiplier(2.5f)
                .setLayoutRes(R.layout.item_optionspicker, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tv_submit = v.findViewById(R.id.tv_submit);
                        tv_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvNoLinkOptions.returnData();
                            }
                        });
                    }
                })
                .build();

        pvNoLinkOptions.setNPicker(linklist,null,null);
        pvNoLinkOptions.show();
    }

    /**
     * 显示popupWindow
     */
//    public void initPopupWindow(String num){
//        if(popupWindow==null){
//            popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popu_contacts,null);
//            linearLayout= (LinearLayout) popupView.findViewById(R.id.line_back);
//            linearLayout.getBackground().setAlpha(100);
//            popupWindow = new PopupWindow(popupView,
//                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
//            line_call= (LinearLayout) popupView.findViewById(R.id.line_call);
//            list_contats= (ListView) popupView.findViewById(R.id.list_contacts);
//            img_check= (ImageView) popupView.findViewById(R.id.img_check);
//            adapter = new ContactsAdapter(getActivity());
//            list_contats.setAdapter(adapter);
//
//            popupView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popupWindow.dismiss();
//                }
//            });
//        }
//        bossId = Integer.parseInt(num);
//        getContacts(num);
//    }
    

    /**
     * 选择一个联系人电话
     */
    public void setListen(int num){
        uid=userService.getLoginUser().getUid();
        phone = conlist.get(num).getTel();
        //bossId = conlist.get(num).getBossId();
    }

    /**
     * 厂家联系人列表的点击事件，通过点击操作，提取其电话号码，为拔号操作作准备
     * 拔号操作之后，应在调用后台的记录操作，记录其已经进行拔号操作
     * 在这里不管它是否真正联系上，都默认认为你成功联系
//     */
    public void popupOnClick(final String p){
        /**
         * 点击拔号，通过获取列表中的电话号码进行拔号操作
         */
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("phone", p);
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
                        if (objects != null && objects.length > 0) {
                            conlist = (ArrayList<Contacts>) objects[0];
                            if (conlist != null && conlist.size() > 0) {
                                phone = conlist.get(0).getTel();
                            }
//							adapter.setData((List<Contacts>) objects[0]);
//                            adapter.notifyDataSetChanged();
                            phonelist.clear();
                            linklist.clear();
                            for (int i = 0; i < conlist.size(); i++) {
                                phonelist.add(conlist.get(i).getTel());
                                linklist.add(conlist.get(i).getName() + "  " + conlist.get(i).getTel());
                            }
                            showPickWindow();
                        }
                    }
                };
                userService.getBossContracts(nm, callBack);
            }
    }
    
    private static final int INIT_FRAG = 0x1025;

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}

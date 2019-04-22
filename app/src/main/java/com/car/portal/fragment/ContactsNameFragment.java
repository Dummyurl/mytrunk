package com.car.portal.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.car.portal.R;
import com.car.portal.activity.LoginActivity;
import com.car.portal.entity.MyJsonReturn;
import com.car.portal.entity.User;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.SessionStore;
import com.car.portal.http.XUtil;
import com.car.portal.service.GoodsService;
import com.car.portal.service.UserService;
import com.car.portal.util.SharedPreferenceUtil;
import com.car.portal.util.ToastUtil;
import com.orhanobut.hawk.Hawk;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;


/**
 * Created by Administrator on 2018/7/4.
 */

public class ContactsNameFragment extends DialogFragment {
    Dialog dialog;
    private GoodsService goodsService;
    private String[] contactNameList,telsList; //后台获取的联系人电话号码跟名字数组
    private int[] uids;
    private ListViewAdapter contactsNameAdapter,telsListAdapter;
    private String contactName,tels,results,uidStr;//选择的用户名字列表

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    initView();
                    break;
                default:
                    break;
            }
        }
    };

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.setContentView(R.layout.dialog_contactsname);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        goodsService = new GoodsService(getActivity());

        getContactPhone();
        return dialog;
    }

    public void getContactPhone(){
        UserService userService = new UserService(getActivity());
        User user = userService.getLoginUser();
        if(user==null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }else{
            if(Hawk.get("contactTelsList")==null) {
                String pass = new String(Base64.decode(user.getPassword().getBytes(), Base64.NO_PADDING));
                String uid = Integer.toString(user.getUid());
                SortedMap<Object, Object> params = XUtil.generateTokenMapParam(uid, pass);
                goodsService.findContactPhone(params, new HttpCallBack(getActivity()) {
                    @Override
                    public void onSuccess(Object... objects) {
                        MyJsonReturn<String> returnJson = (MyJsonReturn<String>) objects[0];
                        contactNameList = returnJson.getContactsNames();
                        telsList = returnJson.getTels();
                        uids = returnJson.getUids();
                        if (telsList != null) {
                            if (contactNameList.length > 0 && telsList.length > 0 && uids.length > 0) {
                                Message msgMessage = Message.obtain();
                                msgMessage.what = 1;
                                handler.sendMessage(msgMessage);
                            }
                        } else {
                            ToastUtil.show("用户信息获取为空,请重试", getActivity());
                            /*SessionStore.resetSessionId();
                            SharedPreferenceUtil util = SharedPreferenceUtil.getIntence();
                            util.saveSessionId(null, getContext());*/
                            reLogin();
                        }
                    }

                    @Override
                    public void onError(Object... objects) {

                        super.onError(objects);
                    }
                });
            }else{
                telsList = Hawk.get("contactTelsList");
                uids = Hawk.get("contactUids");
                contactNameList= Hawk.get("contactNameList");
                Message msgMessage = Message.obtain();
                msgMessage.what = 1;
                handler.sendMessage(msgMessage);
            }
        }
    }

    // 获取数据之后生成listView
    public void initView()
    {

        ListView contactsNamelistView = (ListView)dialog.findViewById(R.id.contactsNamesList);
        contactsNameAdapter = new ListViewAdapter(getActivity(),contactNameList);
        contactsNamelistView.setAdapter(contactsNameAdapter);

        // 自适应高度
        setListViewHeightBasedOnChildren(contactsNamelistView);

        Button choose_contants = (Button) dialog.findViewById(R.id.choose_contants);
        choose_contants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                if (getTargetFragment()== null){
                    return;
                }

                HashMap<Integer, Boolean> contactsNameSelected = contactsNameAdapter.getIsSelected();
                for (Map.Entry<Integer, Boolean> item : contactsNameSelected.entrySet()){
                    if (item.getValue() == true)
                    {
                        String nameValue = contactNameList[item.getKey()];
                        contactName = (contactName==null) ? nameValue : contactName+","+nameValue;
                        String telsValue = telsList[item.getKey()];
                        tels = (tels==null) ? telsValue : tels+ "," +   telsValue;
                        String uidValue = Integer.toString(uids[item.getKey()]);
                        uidStr =(uidStr==null)?uidValue:uidStr+","+uidValue;
                        String resultsValue = nameValue + ":"+telsValue;//+":"+uidValue;
                        results = (results==null) ? resultsValue : results+ "," + resultsValue;
                    }

                }

                //没有点击的话默认选择第一个
                if (contactName == null) {contactName = contactNameList[0];}
                if (tels == null) {tels = telsList[0];}
                if(uidStr==null){uidStr=Integer.toString(uids[0]);}
                if (results == null){results = contactName+":"+tels+":"+uidStr;}
                intent.putExtra("contactsName",contactName); //联系人姓名
                intent.putExtra("tels",tels); //联系人电话
                intent.putExtra("uidStr",uidStr); //联系人编号
                intent.putExtra("results",results); //车长类型
                getTargetFragment().onActivityResult(DeliverFragment.CONTACTS_CODE, Activity.RESULT_OK, intent);
            }
        });
    }


    public class ListViewAdapter extends BaseAdapter {

        private Context context;
        private String[] contactList;
        /**
         *
         */
        // 用来控制CheckBox的选中状况
        public HashMap<Integer, Boolean> isSelected;

        class ViewHolder {
            CheckBox cb;
        }

        public ListViewAdapter(Context context, String[] contactNameList) {
            // TODO Auto-generated constructor stub
            this.contactList = contactNameList;
            this.context = context;
            isSelected = new HashMap<Integer, Boolean>();
            // 初始化数据
            initDate();
        }

        // 初始化isSelected的数据
        private void initDate() {
            for (int i = 0; i < contactList.length; i++) {
                getIsSelected().put(i, false);
            }
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return contactList.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return contactList[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            // 页面
            ViewHolder holder;
            String bean = contactList[position]+":" +telsList[position];
            LayoutInflater inflater = LayoutInflater.from(context);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.dialog_contactsname_item, null);
                holder = new ViewHolder();
                holder.cb = (CheckBox) convertView.findViewById(R.id.CheckBox);

                convertView.setTag(holder);
            } else {
                // 取出holder
                holder = (ViewHolder) convertView.getTag();
            }
            holder.cb.setText(bean);
            // 监听checkBox并根据原来的状态来设置新的状态
            holder.cb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (isSelected.get(position)) {
                        isSelected.put(position, false);
                        setIsSelected(isSelected);
                    } else {
                        isSelected.put(position, true);
                        setIsSelected(isSelected);
                    }

                }
            });
            // 根据isSelected来设置checkbox的选中状况
            holder.cb.setChecked(getIsSelected().get(position));
            return convertView;
        }

        public HashMap<Integer, Boolean> getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
            this.isSelected = isSelected;
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}

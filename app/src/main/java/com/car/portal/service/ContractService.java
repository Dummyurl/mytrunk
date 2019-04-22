package com.car.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.car.portal.R;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.CommissionEntity;
import com.car.portal.entity.CommissionList;
import com.car.portal.entity.ContactDTOEntity;
import com.car.portal.entity.ContractList;
import com.car.portal.entity.CountConfirm;
import com.car.portal.entity.MyJsonReturn;
import com.car.portal.entity.OrderList;
import com.car.portal.entity.ParnerShip;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.XUtil;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.ToastUtil;
import com.google.gson.internal.LinkedTreeMap;

public class ContractService extends BaseService{

    public ContractService(Context context) {
    	super(context);
    }

    public void getCountConfirm(Map<String, Object> params, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_countConfirm);
        XUtil.Post(url, params, new MyCallBack<MyJsonReturn<LinkedTreeMap<String, Object>>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(MyJsonReturn<LinkedTreeMap<String, Object>> arg0) {
                if (arg0.getResult() == null || arg0.getResult() != 0) {
                    callBack.onFail(-2, "列表获取失败", false);
                } else {
                    List<CountConfirm> list = new ArrayList<CountConfirm>();
                    for (LinkedTreeMap<String, Object> map : arg0.getContractList()) {
                        CountConfirm cc = new CountConfirm();
                        LinkMapToObjectUtil.getObject(map, cc);
                        list.add(cc);
                    }
                    callBack.onSuccess(list, arg0.getCount());
                }
            }
        });
    }

    public void getParnerList(Map<String, Object> params, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_panerShipList);
        XUtil.Post(url, params, new MyCallBack<MyJsonReturn<LinkedTreeMap<String, Object>>>() {

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(MyJsonReturn<LinkedTreeMap<String, Object>> arg0) {
                if (arg0.getOvertime() == null || arg0.getOvertime()) {
                    callBack.onFail(-2, "", false);
                } else {
                    List<ParnerShip> list = new ArrayList<ParnerShip>();
                    for (LinkedTreeMap<String, Object> map : arg0.getList()) {
                        ParnerShip parnerShip = new ParnerShip();
                        LinkMapToObjectUtil.getObject(map, parnerShip);
                        list.add(parnerShip);
                    }
                    callBack.onSuccess(list, arg0.getCount());
                }
            }
        });
    }

    public void endGood(int goodsId, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_endGoods);
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("id", goodsId);
        params.put("t", System.currentTimeMillis());
        XUtil.Post(url, params, new MyCallBack<MyJsonReturn<String>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(MyJsonReturn<String> arg0) {
                if (arg0.getCount() != null && arg0.getCount() == 1) {
                    callBack.onSuccess();
                } else {
                    callBack.onFail(-1, "操作失败,刷新后重试", false);
                }
            }
        });
    }



    /**
     * 获取提成金额确认的合同列标
     * @param callBack
     */
    public void getCommentList(final HttpCallBack callBack){
        String url = util.getUrl(R.string.url_comments) + "?confirm=0&t=" + System.currentTimeMillis();
        XUtil.Post(url, null, new MyCallBack<CommissionEntity>() {
            @Override
            public void onSuccess(CommissionEntity arg0) {
                if (!arg0.isOvertime()) {
                    List<CommissionList> list = new ArrayList<CommissionList>();
                    if (arg0.getQdo() != null) {
                        CommissionEntity.Qdo<ArrayList<LinkedTreeMap>> arrayListQdo = arg0.getQdo();
                        for (LinkedTreeMap map : arrayListQdo.getResultList()) {
                            CommissionList con = new CommissionList();
                            LinkMapToObjectUtil.getObject(map, con);
                            list.add(con);
                        }
                    }
                    callBack.onSuccess(list);
                } else {
                    callBack.onFail(-2, "", false);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                callBack.onError(throwable, b);
            }
        });
    }

    /**
     * 获取合同列表
     * @param condition
     * @param currentPage
     * @param callBack
     */
    public void getContractList(String condition, int currentPage, final HttpCallBack callBack) {
        String url = util.getUrl(R.string.url_getContractList);
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("condition",condition == null ? "" : condition);
        params.put("currentPage", currentPage);
        XUtil.Post(url, params, new MyCallBack<BaseEntity<ArrayList<LinkedTreeMap>>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
            }

            @Override
            public void onSuccess(BaseEntity<ArrayList<LinkedTreeMap>> arg0) {
                if (arg0.getResult() != null && arg0.getResult() == 1) {
                    List<ContractList> list = new ArrayList<ContractList>();
                    if(arg0.getData() != null) {
                        for (LinkedTreeMap map : arg0.getData()) {
                            ContractList con = new ContractList();
                            LinkMapToObjectUtil.getObject(map, con);
                            list.add(con);
                        }
                    }
                    callBack.onSuccess(list, arg0.getMessage());
                } else {
                    callBack.onFail(arg0.getResult(), "获取列表失败", false);
                }
            }
        });
    }

    /**
     * 获取id为id的合同的具体信息
     * @param id
     * @param callBack
     */
    public void getOneAgreementInfo(String id,final HttpCallBack callBack){
        String url=util.getUrl(R.string.url_agreement_item_info);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("id",id);
        map.put("t",System.currentTimeMillis());
        XUtil.Post(url, map, new MyCallBack<ContactDTOEntity>() {
            @Override
            public void onSuccess(ContactDTOEntity arrayListBaseEntity) {
                if (!arrayListBaseEntity.isOvertime()){
                    if (arrayListBaseEntity.getContractDto()!=null){
                       callBack.onSuccess(arrayListBaseEntity.getContractDto());
                    }
                }else
                    callBack.onFail(-2,"",false);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                callBack.onError(throwable,b);
            }
        });
    }

    /**
     * 获取网络电话统计数据
     * @param year
     * @param month
     * @param callBack
     */
    public void getNetPhone(String year, String month, final HttpCallBack callBack){
        String url = util.getUrl(R.string.url_netPhoneInfo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("year", year);
        map.put("month", month);
        XUtil.Post(url, map, new MyCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }
        });
    }

    /**
     * 根据月份获取该月份的电话统计
     * @param previous
     * @param callBack
     */
    public void getCallsByMonths(int previous, final HttpCallBack callBack){
        String url = util.getUrl(R.string.url_findCalls_byMonths);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("previous", previous);
        map.put("t", System.currentTimeMillis());
        XUtil.Post(url, map, new MyCallBack<CommissionEntity>() {
            @Override
            public void onSuccess(CommissionEntity commissionEntity) {
                if (!commissionEntity.isOvertime()){
                    List<String> str_list = new ArrayList<String>();//行 日期
                    List<OrderList> orderLists = new ArrayList<OrderList>();//内部数据列表
                    Map<String, String> maps = new HashMap<String, String>();//列~人名
                    if (commissionEntity.getQdo() != null ){
                        CommissionEntity.Qdo<ArrayList<LinkedTreeMap>> arrayListQdo = commissionEntity.getQdo();
                        for (LinkedTreeMap map1 : arrayListQdo.getResultList()) {
                            OrderList order = new OrderList();
                            LinkMapToObjectUtil.getObject(map1, order);
                            orderLists.add(order);
                        }
                        maps = arrayListQdo.getMap3();
                        str_list = arrayListQdo.getResultList2();
                        LogUtils.e("contactServuice", " getCallsByMonths      maps:" + maps.size() + "      str.size:" + str_list.size() + "       orderList:" + orderLists.size());
                        callBack.onSuccess(maps, orderLists, str_list);
                    } else {
                        ToastUtil.show("未查询到相关数据！", context);
                    }
                } else {
                    callBack.onFail(-2, "", false);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                callBack.onError(throwable, b);
            }
        });
    }

    /**
     * 根据年份查询电话统计数据
     * @param year
     * @param callBack
     */
    public void getCallsByYears(int year, final HttpCallBack callBack){
        String url = util.getUrl(R.string.url_findCalls_byYears);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("year", year);
        map.put("t", System.currentTimeMillis());
        XUtil.Post(url, map, new MyCallBack<CommissionEntity>() {
           @Override
           public void onSuccess(CommissionEntity commissionEntity) {
               if (!commissionEntity.isOvertime()){
                   List<String> str_list = new ArrayList<String>();
                   List<OrderList> orderLists = new ArrayList<OrderList>();
                   Map<String, String> maps = new HashMap<String, String>();
                   if (commissionEntity.getQdo() != null ){
                       CommissionEntity.Qdo<ArrayList<LinkedTreeMap>> arrayListQdo = commissionEntity.getQdo();
                       for (LinkedTreeMap map1 : arrayListQdo.getResultList()) {
                           OrderList order = new OrderList();
                           LinkMapToObjectUtil.getObject(map1, order);
                           orderLists.add(order);
                       }
                       maps = arrayListQdo.getMap3();
                       str_list = arrayListQdo.getResultList2();
                       LogUtils.e("contactServuice", "getCallsByYears       maps:" + maps.size() + "      str.size:" + str_list.size() + "       orderList:" + orderLists.size());
                       callBack.onSuccess(maps, orderLists, str_list);
                   } else {
                       ToastUtil.show("未查询到相关数据！", context);
                   }
               }else{
                   callBack.onFail(-2, "", false);
               }
           }

           @Override
           public void onError(Throwable throwable, boolean b) {
                callBack.onError(throwable, b);
           }
         });
    }
}

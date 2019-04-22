package com.car.portal.service;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.car.portal.R;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.Boss;
import com.car.portal.entity.CarArrived;
import com.car.portal.entity.CommissionEntity;
import com.car.portal.entity.Goods;
import com.car.portal.entity.GoodsCityByDate;
import com.car.portal.entity.GoodsInfo_City;
import com.car.portal.entity.Goods_For_Address;
import com.car.portal.entity.Money;
import com.car.portal.entity.MyJsonReturn;
import com.car.portal.entity.NetPhoneRecord;
import com.car.portal.entity.OrderList;
import com.car.portal.entity.OrdersEntity;
import com.car.portal.entity.ParnerShip;
import com.car.portal.entity.ParnerShipReturn;
import com.car.portal.entity.User;
import com.car.portal.entity.commonlyusedbean;
import com.car.portal.entity.newOrder;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.SessionStore;
import com.car.portal.http.XUtil;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.MyDBUtil;
import com.car.portal.util.StringUtil;
import com.google.gson.internal.LinkedTreeMap;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class GoodsService extends BaseService {

	private ArrayList<CarArrived> arrList;
	public static final String FIND_USER_BYUID ="SELECT * FROM user c WHERE c.uid = ?";

	public GoodsService(Context context) {
		super(context);
	}

	/**
	 * 临时授权
	 * @param uid
	 * @param callBack
	 */
	public void assistAuthor(String uid, final HttpCallBack callBack){
		String url = util.getUrl(R.string.url_assistAuthor) +"?uid=" + uid + "&t=" + System.currentTimeMillis();
		XUtil.Post(url, null, new MyCallBack<BaseEntity<String>>() {
			@Override
			public void onSuccess(BaseEntity<String> stringBaseEntity) {
				if (stringBaseEntity != null) {
					callBack.onSuccess(stringBaseEntity.getResult());
				} else {
					callBack.onFail(stringBaseEntity.getResult(), stringBaseEntity.getMessage(),
							true);
				}
			}

			@Override
			public void onError(Throwable throwable, boolean b) {
				onError(throwable, b);
			}
		});
	}

	/**
	 * 获取订单统计的数据
	 * @param uid
	 * @param callBack
	 */
	public void getOrders(int uid, final HttpCallBack callBack){
		String url = util.getUrl(R.string.url_orders) + "?uid=" + uid;
		XUtil.Post(url, null, new MyCallBack<OrdersEntity<ArrayList>>() {
			@Override
			public void onSuccess(OrdersEntity<ArrayList> arrayListOrdersEntity) {
				if(!arrayListOrdersEntity.isOvertime()){
					ArrayList<LinkedTreeMap<String, Object>> lsit = arrayListOrdersEntity.getCountList();
					List<OrderList> orderLists = new ArrayList<OrderList>();
					if (lsit != null){
						for (LinkedTreeMap<String, Object> map : lsit){
							OrderList order = new OrderList();
							LinkMapToObjectUtil.getObject(map, order);
							if(!order.getCounts().equals("0")) {
								orderLists.add(order);
							}
						}
					}
					String title = arrayListOrdersEntity.getYearString();
					callBack.onSuccess(orderLists, title);
				} else {
					callBack.onFail(-2, "", true);
				}
			}

			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable, b);
			}
		});
	}
	/**
	 *
	 *@作者：舒椰
	 *获取常用货物的list
	 *@时间： 2019/4/15 下午 3:10
	 */
	public void getGoodsNormallist(final HttpCallBack callBack){
			String url = util.getUrl(R.string.url_findMyGoodsNormal);
			XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList>>() {
				@Override
				public void onSuccess(BaseEntity<ArrayList> arrayListBaseEntity) {
                    if (arrayListBaseEntity.getResult() == 1) {
                        ArrayList<LinkedTreeMap<String, Object>> list = arrayListBaseEntity.getData();
                        List<commonlyusedbean> gds = new ArrayList<commonlyusedbean>();
                        if (list != null) {
                            for (LinkedTreeMap<String, Object> map : list) {
								commonlyusedbean goods = new commonlyusedbean();
                                LinkMapToObjectUtil.getObject(map, goods);
                                gds.add(goods);
                            }
                            callBack.onSuccess(gds);
                        }
                    }
				}

				@Override
				public void onError(Throwable throwable, boolean b) {
					callBack.onError(throwable);
				}
			});
	}
    
	/**
	 *
	 *@作者：舒椰
	 *  一键发货功能
	 *@时间： 2019/4/17 0017 下午 2:17
	 */

	public void onkeysend(int goodsId,String data,final HttpCallBack back){
	    String url =  util.getUrl(R.string.url_releaseGoodsOneKey)+ "?goodsNormatId=" + goodsId+"&loadingDate="+data;
	    XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList>>() {
            @Override
            public void onSuccess(BaseEntity<ArrayList> arrayListBaseEntity) {
                if (arrayListBaseEntity.getResult() == 1) {
                    back.onSuccess(arrayListBaseEntity);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }
        });
    }
	
	
	

	public void delGoodsnor(int goodsId,final HttpCallBack back){
		String url = util.getUrl(R.string.url_deleteGoodsNormal)+ "?goodsId=" + goodsId;
		XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onSuccess(BaseEntity<ArrayList> arrayListBaseEntity) {
				if (arrayListBaseEntity.getResult() == 1) {
					back.onSuccess(arrayListBaseEntity);
				}
			}

			@Override
			public void onError(Throwable throwable, boolean b) {

			}
		});
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getGoodsList(Map<String, Object> params, final HttpCallBack back) {
		String url = util.getUrl(R.string.url_getGoodsInfo);
		XUtil.Post(url, params, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				back.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
					List<Goods> gds = new ArrayList<Goods>();
					if (list != null) {
						for (LinkedTreeMap<String, Object> map : list) {
							Goods goods = new Goods();
							LinkMapToObjectUtil.getObject(map, goods);
							gds.add(goods);
						}
					}
					back.onSuccess(gds);
				} else {
					back.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * 货物信息  按城市进行分类获取
	 * @param hasHelloFlag
	 * @param userId
	 * @param pathValue
	 * @param callBack
	 */
	public void findGoodsOfCitys(boolean hasHelloFlag, int userId, int pathValue, String condition, MyCallBack callBack) {
		String path = util.getUrl(R.string.url_getgoods_city);
		LogUtils.e("GoodsService", "       getCitys   condition:" + condition);
		try {
			condition = URLEncoder.encode(condition, "utf-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		String url = path + "?hasHelloFlag=" + hasHelloFlag + "&userId=" + userId + "&pathValue=" + pathValue
				+ "&route=0&t=" + System.currentTimeMillis() + "&condition=" + condition;
		LogUtils.e("GoodsService.getCitys 参数",url);
		XUtil.Get(url, null, callBack);

	}

	/**
	 * 根据时间查找城市
	 * @param hasHelloFlag
	 * @param userId
	 * @param pathValue
	 * @param date
	 * @param callBack
	 */
	public void getCityForDate(boolean hasHelloFlag, int userId, int pathValue, int date, final HttpCallBack callBack){
		String path = util.getUrl(R.string.url_getcity_by_date);
		String url = path + "?hasHelloFlag=" + hasHelloFlag + "&userId=" + userId + "&pathValue=" + pathValue
				+ "&day="+date+"&route=0&t=" + System.currentTimeMillis();
		XUtil.Post(url, null, new MyCallBack<GoodsCityByDate<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean b) {
				callBack.onError(arg0, b);
			}

			@Override
			public void onSuccess(GoodsCityByDate<ArrayList> arg0) {
				if (!arg0.isOvertime()) {
					ArrayList<LinkedTreeMap<String, Object>> list = arg0.getTableListDay();
					if (list != null) {
						ArrayList<GoodsInfo_City> goodsInfo_cities = new
								ArrayList<GoodsInfo_City>();
						for (int i = 0; i < list.size(); i++) {
							GoodsInfo_City goodsInfo_city = new GoodsInfo_City();
							LinkMapToObjectUtil.getObject(list.get(i), goodsInfo_city);
							goodsInfo_cities.add(goodsInfo_city);
						}
						callBack.onSuccess(goodsInfo_cities);
					}
				} else {
					callBack.onFail(-2, "", false);
				}
			}
		});
	}

	/**
	 * 获取本公司所有联系人
	 * @param callBack
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getGoods(final int companyId, final HttpCallBack callBack){
		List<User> list = null;
		final DbManager manager = MyDBUtil.getManager();  //本地数据库查询
		String time = shareUtil.getNomalValue("loadCompanyUser", MyApplication.getContext());
		long t = 0;
		if(!StringUtil.isNullOrEmpty(time)) {
			t = Long.valueOf(time);
		}
		long dt = System.currentTimeMillis() - t;
		if(dt < 7200000L) {//允许两小时查询一次最新的统计数据，时间差小于2小时的，直接在本地数据库中查询，并不进行后台统计更新
			try {
				manager.delete(User.class, WhereBuilder.b("companyId", "!=", companyId));
				list = manager.findAll(User.class);
			} catch (DbException e1) {
				e1.printStackTrace();
			}
		}
		if(list == null || list.size() <= 0 || (list.size() == 1 && list.get(0).getUid() == 0)) {
			String path=util.getUrl(R.string.url_company_user);
			XUtil.Post(path, null, new MyCallBack<BaseEntity<ArrayList>>() {
				@Override
				public void onError(Throwable throwable, boolean b) {
					callBack.onError(throwable, b);
				}
				@Override
				public void onSuccess(BaseEntity<ArrayList> stringBaseEntity) {
					if(stringBaseEntity.getResult()==1){
						shareUtil.setNomalValue("loadCompanyUser", String.valueOf(System.currentTimeMillis()), MyApplication.getContext());
						ArrayList<LinkedTreeMap<String,Object>> list=stringBaseEntity.getData();
						if (list!=null){
							ArrayList<User> companyUsers=new ArrayList<User>();
							try {
								manager.delete(User.class, WhereBuilder.b("uid", ">=", 0));
								for(int i=0;i<list.size();i++){
									User user=new User();
									LinkMapToObjectUtil.getObject(list.get(i), user);
									companyUsers.add(user);
									User user2 = manager.findById(User.class, user.getUid());
									if (user2 != null) {
										manager.deleteById(User.class, user2.getUid());
									}
									user.setCompanyId(companyId);
									manager.save(user);
								}
							} catch (DbException e1) {
								e1.printStackTrace();
							}
							callBack.onSuccess(companyUsers);
						} else {
							callBack.onFail(-1, "查找失败", false);
						}
					} else {
						callBack.onFail(stringBaseEntity.getResult(), stringBaseEntity.getMessage(), true);
					}
				}
			});
		} else {
			callBack.onSuccess(list);
		}
	}

	/**
	 * 添加公司成员至数据库
	 * @param user
	 */
	public void addUser(User user){
		DbManager dbManager = MyDBUtil.getManager();
		try {
			User u = dbManager.findById(User.class, user.getUid());
			if(u == null) {
				dbManager.save(user);
			} else {
				dbManager.delete(u);
				u.setUid(user.getUid());
				u.setAlias(user.getAlias());
				u.setCname(user.getCname());
				u.setCompanyId(user.getCompanyId());
				u.setUserType(user.getUserType());
				dbManager.save(u);
			}
		} catch (DbException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 根据uid查询公司成员信息
	 * @param id
	 */
	public User selectUserById(int id){
		DbManager dbManager = MyDBUtil.getManager();
		Cursor cursor = null;
		User users = new User();
		try {
			String sql = FIND_USER_BYUID.replace("?", id + "");
			cursor = dbManager.execQuery(sql);
			int idColumn = cursor.getColumnIndex("uid");
			int nameColumn=cursor.getColumnIndex("cname");
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				users.setUid(cursor.getInt(idColumn));
				users.setCname(cursor.getString(nameColumn));
			}
//			LogUtils.e("GoodsService","   "+users.getUid()+":"+users.getCname());
		}catch (Exception e){
			System.out.println(e.getMessage());
		}finally {
			if (dbManager != null) {
				try {
					dbManager.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				dbManager = null;
			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return users;
	}

	/**
	 * 获取所有司机
	 * @param route
	 * @param callBack
	 * @return
	 */
	public ArrayList<CarArrived> getDriverByRoute(String route, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_getDriverByRoute);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("route", route);
		XUtil.Post(url, params, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
					arrList = new ArrayList<CarArrived>();
					if (list != null) {
						for (LinkedTreeMap<String, Object> map : list) {
							CarArrived arr = new CarArrived();
							LinkMapToObjectUtil.getObject(map, arr);
							arrList.add(arr);
						}
					}
					callBack.onSuccess(arrList);
				} else {
					callBack.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
		return arrList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getBossInfo(Map<String, Object> map, final HttpCallBack callBack) {
//		String path = util.getUrl(R.string.url_getBoss) + "?typeId=" + typeId + "&currentPage="+currentPage;
		String path = util.getUrl(R.string.url_getBoss);
		LogUtils.e("GoodsService", "         getBossInfo      typeId:" + map.get("typeId"));
		LogUtils.e("GoodsService", "      condition" + map.get("condition"));
		XUtil.Post(path, map, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable throwable, boolean b) {
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
					List<Boss> mbosslist = new ArrayList<Boss>();
					if (list != null) {
						for (int i = 0; i < list.size(); i++) {
							Boss boss = new Boss();
							LinkMapToObjectUtil.getObject(list.get(i), boss);
							mbosslist.add(boss);
						}
					}
					callBack.onSuccess(mbosslist);
				} else {
					callBack.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}

	/**
	 * 货物作废
	 * @param bossId
	 * @param goodsId
	 * @param owner
	 * @param back
	 */
	public void goodsError(int bossId, int goodsId, String owner, final HttpCallBack back) {
		String path = util.getUrl(R.string.url_save_good_error);
		long t = System.currentTimeMillis();
		String url = path + "?bossId=" + bossId + "&goodsId=" + goodsId + "&owner=" + owner + "&t=" + t;
		
		XUtil.Get(url, null, new MyCallBack<MyJsonReturn<String>>() {
			@Override
			public void onError(Throwable throwable, boolean b) {
				back.onError(throwable, b);
			}

			@Override
			public void onSuccess(MyJsonReturn<String> arg0) {
				if (arg0.getOvertime() != null && !arg0.getOvertime()) {
					back.onSuccess(arg0.getCount());
				} else {
					back.onFail(-2, "", false);
				}
			}
		});
	}

	/**
	 * 货物结束
	 */
	public void getEnd(int bossId, int goodsId, final HttpCallBack callBack) {
		String path = util.getUrl(R.string.url_save_good_end);
		String url = path + "?bossId=" + bossId + "&goodsId=" + goodsId
				+ "&status=1";
		XUtil.Post(url, null, new MyCallBack<MyJsonReturn<String>>() {
			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable, b);
			}

			@Override
			public void onSuccess(MyJsonReturn<String> arg0) {
				if (arg0.getOvertime() != null && !arg0.getOvertime()) {
					callBack.onSuccess(arg0.getCount());
				} else {
					callBack.onFail(-2, "", false);
				}
			}
		});
	}

	/**
	 * 检查出货单
	 */
	public void inspectGoodsOrder(int bossId,String routeId,final HttpCallBack callBack){
		String path=util.getUrl(R.string.url_inspect_order);
		String url=path+"?bossId="+bossId+"&route="+routeId;
		XUtil.Post(url, null, new MyCallBack<MyJsonReturn<String>>() {
			@Override
			public void onSuccess(MyJsonReturn<String> stringMyJsonReturn) {
				if (stringMyJsonReturn.getOvertime() != null && !stringMyJsonReturn.getOvertime
						()) {
					callBack.onSuccess(stringMyJsonReturn.getCount());
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
	 * 车货配对
	 * @param map
	 * @param callBack
	 */
	public void carGoodsLink(Map<String,Object> map,final HttpCallBack callBack){
		String path=util.getUrl(R.string.url_car_goods_link);
		XUtil.Post(path, map, new MyCallBack<ParnerShipReturn>() {
			@Override
			public void onSuccess(ParnerShipReturn stringLinkEntity) {
				if (stringLinkEntity.getResult()==1){
					callBack.onSuccess(stringLinkEntity.getParnership());
				}else{
					callBack.onFail(-2,"",false);
				}
			}

			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable,b);
			}
		});
	}

	/**
	 * 确认车货配对
	 * @param callBack
	 */
	public void linkCenter(final HttpCallBack callBack){
		String path=util.getUrl(R.string.url_linl_confirm);
		XUtil.Post(path, null, new MyCallBack<BaseEntity<String>>() {
			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable, b);
			}

			@Override
			public void onSuccess(BaseEntity<String> stringBaseEntity) {
				if (stringBaseEntity.getResult()>=0)
					callBack.onSuccess(stringBaseEntity.getResult());
				else
					callBack.onFail(-2,"",false);
			}
		});
	}

	/**
	 * 取消车货配对
	 * @param callBack
	 */
	public void linkCencel(final HttpCallBack callBack){
		String path=util.getUrl(R.string.url_linl_cencel);
		XUtil.Post(path, null, new MyCallBack<BaseEntity<String>>() {
			@Override
			public void onSuccess(BaseEntity<String> stringBaseEntity) {
				if (stringBaseEntity.getResult()==1)
					callBack.onSuccess(stringBaseEntity.getResult());
				else
					callBack.onFail(-2,"",false);
			}

			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable, b);
			}
		});
	}

	/**
	 * 保存出货单
	 */
	public void addGoodsOrder(Goods_For_Address goods_for_address,final HttpCallBack callBack){
		String path;
		if(goods_for_address.getIsShare()==1){
			path = util.getUrl(R.string.url_add_orderShare);
		}else {
			path=util.getUrl(R.string.url_add_order);
		}
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("goodsDto.bossId",goods_for_address.getBossId());
		map.put("goodsDto.type",goods_for_address.getType());  //货物类型
		map.put("goodsDto.body_type",goods_for_address.getBabyType());  //车箱类型
		map.put("goodsDto.boss_owner",goods_for_address.getBoss_owner());
		map.put("goodsDto.prepareMoney",goods_for_address.getPrepareMoney());
		map.put("goodsDto.operator",goods_for_address.getOperator());
		map.put("goodsDto.ower",goods_for_address.getOwer());
		map.put("goodsDto.onlyRecord",goods_for_address.getOnlyRecord());
		map.put("goodsDto.convey",goods_for_address.getConvey());
		map.put("goodsDto.overTime",goods_for_address.getOverTime());
		map.put("goodsDto.end",goods_for_address.getEnd());
		map.put("goodsDto.startDate",goods_for_address.getStartDate());
		map.put("goodsDto.company",goods_for_address.getCompany());
		map.put("goodsDto.bossName",goods_for_address.getBossName());
		map.put("goodsDto.contractName",goods_for_address.getContractName());
		map.put("goodsDto.force_add",goods_for_address.getForce_add());
		map.put("goodsDto.outLoc",goods_for_address.getOutLoc());
		map.put("goodsDto.outLocN",goods_for_address.getOutLocN());
		map.put("goodsDto.outLocCode",goods_for_address.getOutLocCode());
		map.put("goodsDto.route",goods_for_address.getRoute());
		map.put("goodsDto.routeN",goods_for_address.getRouteN());
		map.put("goodsDto.routeCode",goods_for_address.getRouteCode());
		map.put("goodsDto.endDate",goods_for_address.getEndDate());
		map.put("goodsDto.memo",goods_for_address.getMemo());
		map.put("goodsDto.memoOut",goods_for_address.getMemo2());
		map.put("goodsDto.car_long",goods_for_address.getCarLength());

		XUtil.Post(path, map, new MyCallBack<MyJsonReturn<String>>() {
			@Override
			public void onSuccess(MyJsonReturn<String> stringMyJsonReturn) {
				if (stringMyJsonReturn.getOvertime() != null && !stringMyJsonReturn.getOvertime()) {
					callBack.onSuccess(stringMyJsonReturn.getCount());
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getGoodsByRoute(String route, int userId,
			final HttpCallBack callBack) {
		String path = util.getUrl(R.string.url_getgoods_one_address);
		String url = path + "?routeN=" + route + "&userId=" + userId;
		XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					ArrayList<LinkedTreeMap> goods = (ArrayList<LinkedTreeMap>) arg0.getData();
					List<Goods_For_Address> list = new ArrayList<Goods_For_Address>();
					if (goods != null) {
						for (LinkedTreeMap map : goods) {
							Goods_For_Address good = new Goods_For_Address();
							LinkMapToObjectUtil.getObject(map, good);
							list.add(good);
						}
					}
					callBack.onSuccess(list);
				} else {
					//SessionStore.resetSessionId();
					callBack.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}

			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable, b);
			}
		});
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getGoodsTypes(MyCallBack callBack) {
		String path =util.getUrl(R.string.url_getGoodTypes);
        XUtil.Post(path, null, callBack);
	}

	/**
	 * 获取金额全部信息
	 * @param callBack
	 */
	public void getAllMoneyAccount(final HttpCallBack callBack){
		String url = util.getUrl(R.string.url_findallmoney) + "?t=" + System.currentTimeMillis();
		XUtil.Post(url, null, new MyCallBack<CommissionEntity>() {
			@Override
			public void onSuccess(CommissionEntity arg0) {
				if (!arg0.isOvertime()) {
					List<Money> list = new ArrayList<Money>();
					String[] head = null;
					if (arg0.getQdo() != null) {
						CommissionEntity.Qdo<ArrayList<LinkedTreeMap>> arrayListQdo = arg0.getQdo();
						String hearders = arrayListQdo.getHtml_str();
						hearders = hearders.replace("'", "");
						head = hearders.split(",");
						for (LinkedTreeMap map : arrayListQdo.getResultList()) {
							Money money = new Money();
							LinkMapToObjectUtil.getObject(map, money);
							list.add(money);
						}
					}
					callBack.onSuccess(list, head);
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
	 * 金额     根据uid查找A
	 * @param uid
	 * @param callBack
	 */
	public void findUserRate(int uid, final HttpCallBack callBack){
		String url = util.getUrl(R.string.url_findmoneyrate);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", uid);
		map.put("t", System.currentTimeMillis());
		XUtil.Post(url, map, new MyCallBack<CommissionEntity>() {
			@Override
			public void onSuccess(CommissionEntity arg0) {
				if (!arg0.isOvertime()){
					callBack.onSuccess(arg0.getRate());
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

	public void resetViewCount(int goodid){
		String url = util.getUrl(R.string.url_resetViewCount);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("goodsId", goodid);
		XUtil.Post(url, map, new MyCallBack<BaseEntity>() {
			@Override
			public void onSuccess(BaseEntity baseEntity) {

			}

			@Override
			public void onError(Throwable throwable, boolean b) {

			}
		});

	}



	/**
	 * 根据rate查找金额具体信息
	 * @param uid
	 * @param rate
	 * @param callBack
	 */
	public void accountContact(int uid, double rate, final HttpCallBack callBack){
		String url = util.getUrl(R.string.url_findcontactmoney);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", uid);
		map.put("rate", rate);
		map.put("t", System.currentTimeMillis());
		map.put("base", 0);
		XUtil.Post(url, map, new MyCallBack<CommissionEntity>() {
			@Override
			public void onSuccess(CommissionEntity commissionEntity) {
				if (!commissionEntity.isOvertime()){
					List<Money> list = new ArrayList<Money>();
					if (commissionEntity.getQdo() != null) {
						CommissionEntity.Qdo<ArrayList<LinkedTreeMap>> arrayListQdo = commissionEntity.getQdo();
						for (LinkedTreeMap map : arrayListQdo.getResultList()) {
							Money money = new Money();
							LinkMapToObjectUtil.getObject(map, money);
							list.add(money);
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
	
	public void getNetPhoneCursor(int year, int month, final HttpCallBack back) {
		String url = util.getUrl(R.string.url_getNetWork);
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("year", year);
		params.put("month", month);
		XUtil.Post(url, params, new MyCallBack<BaseEntity>() {

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				back.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity arg0) {
				if (arg0.getResult() == 1) {
					ArrayList<LinkedTreeMap<String, Object>> datas =
							(ArrayList<LinkedTreeMap<String, Object>>) arg0.getData();
					List<NetPhoneRecord> phones = new ArrayList<NetPhoneRecord>();
					if(datas != null) {
						for (LinkedTreeMap<String,Object> linkedTreeMap : datas) {
							NetPhoneRecord netP = new NetPhoneRecord();
							LinkMapToObjectUtil.getObject(linkedTreeMap, netP);
							phones.add(netP);
						}
					}
					back.onSuccess(phones);
				} else {
					back.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}



	/**
	 * 出货信息
	 * createBy 20180704 update
	 */
	public void sendGoodsOrder(newOrder newOrder, final HttpCallBack callBack){
		String path=util.getUrl(R.string.url_sendGoodsOrder);
		Log.e("path" ,path);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("newOrder.userId", newOrder.getUserId());
		map.put("newOrder.companyId", newOrder.getCompanyId());
		map.put("newOrder.start_address", newOrder.getStart_address());
		map.put("newOrder.end_address", newOrder.getEnd_address());
		map.put("newOrder.userCarType", newOrder.getUserCarType());
		map.put("newOrder.carLenght", newOrder.getCarLenght());
		map.put("newOrder.bodyTypeOption", newOrder.getBodyTypeOption());
		map.put("newOrder.goodsType", newOrder.getGoodsType());
		map.put("newOrder.quare", newOrder.getQuare());
		map.put("newOrder.volume", newOrder.getVolume());
		map.put("newOrder.money", newOrder.getMoney());
		map.put("newOrder.loadingDate", newOrder.getLoadingDate());
		map.put("newOrder.memo", newOrder.getMemo());
		map.put("newOrder.contactsNames", newOrder.getContactsName());
		map.put("newOrder.tels", newOrder.getTels());
		map.put("newOrder.startCode", newOrder.getStartCode());

		map.put("newOrder.isSee",newOrder.getIsSee());
		map.put("newOrder.endCode", newOrder.getEndCode());
		map.put("newOrder.bodyTypeOptionV", newOrder.getBodyTypeOptionV());
		map.put("newOrder.carLenghtV", newOrder.getCarLengthV());
		map.put("newOrder.goodsTypeV", newOrder.getGoodsTypeV());
		map.put("newOrder.isSaveTo",newOrder.getIsSaveTo());

		XUtil.Post(path, map, new MyCallBack<MyJsonReturn<String>>() {
			@Override
			public void onSuccess(MyJsonReturn<String> stringMyJsonReturn) {
				if (stringMyJsonReturn.getOvertime()!=null&&!stringMyJsonReturn.getOvertime()){
					callBack.onSuccess(stringMyJsonReturn.getCount());
				}else{
					callBack.onFail(-2,"",false);
				}
			}

			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable,b);
			}
		});
	}


	/**
	 * 出货信息
	 * createBy 20180704 update
	 */
	public void findContactPhone(SortedMap<Object, Object> params,final HttpCallBack callBack){
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", getSavedUser().getUsername());
		String path=util.getUrl(R.string.url_findContactPhone);
		XUtil.Post(path, map, new MyCallBack<MyJsonReturn<String>>() {
			@Override
			public void onSuccess(MyJsonReturn<String> stringMyJsonReturn) {
				callBack.onSuccess(stringMyJsonReturn);
			}

			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable,b);
			}
		});
	}
}

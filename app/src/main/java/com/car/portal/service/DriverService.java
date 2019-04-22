package com.car.portal.service;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.car.portal.R;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.BodyTypeList;
import com.car.portal.entity.CarArrived;
import com.car.portal.entity.CarType;
import com.car.portal.entity.CarTypeEntity;
import com.car.portal.entity.City;
import com.car.portal.entity.Company;
import com.car.portal.entity.Driver;
import com.car.portal.entity.MobileEntity;
import com.car.portal.entity.MobileList;
import com.car.portal.entity.MyJsonReturn;
import com.car.portal.entity.PhoneExit;
import com.car.portal.entity.PortalDriver;
import com.car.portal.entity.UNPauseReturn;
import com.car.portal.entity.UserDetail;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.XUtil;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.ToastUtil;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONObject;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverService extends BaseService {

	public DriverService(Context context) {
		super(context);
	}

	/**
	 * 查询是否存在电话
	 * @param phone
	 * @param callBack
	 */
	public void isPhoneExit(String phone, final HttpCallBack callBack){
		String url = util.getUrl(R.string.url_isphone_iexit) + "?tels=" + phone + "&driverId=0";
		XUtil.Post(url, null, new MyCallBack<PhoneExit>() {
			@Override
			public void onSuccess(PhoneExit phoneExit) {
				if (phoneExit != null) {
					callBack.onSuccess(phoneExit);
				}
				LogUtils.e("UserService", "   isPhoneExit:" + phoneExit.getDriverId());
			}

			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable, b);
			}
		});
	}

	/**
	 * 查询归属地
	 * @param phone
	 * @param callBack
	 */
	public void queryPhone(String phone, final HttpCallBack callBack){
		String url = util.getUrl(R.string.url_phone_home) + "?tels=" + phone;
		XUtil.Post(url, null, new MyCallBack<MobileEntity<ArrayList>>() {
			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable, b);
			}

			@Override
			public void onSuccess(MobileEntity<ArrayList> mobileListMobileEntity) {
				if (mobileListMobileEntity.getMobileList() != null && mobileListMobileEntity.getMobileList().size() > 0){
				ArrayList<LinkedTreeMap<String, Object>> list =
						(ArrayList<LinkedTreeMap<String, Object>>) mobileListMobileEntity
								.getMobileList();
					ArrayList<MobileList> mobiles = new ArrayList<MobileList>();
					if (list != null) {
						for (LinkedTreeMap map : list) {
							MobileList mobile = new MobileList();
							LinkMapToObjectUtil.getObject(map, mobile);
							mobiles.add(mobile);
						}
						LogUtils.e("DriverService", "     size:" + list.size());
					}
					callBack.onSuccess(mobiles);
				}
			}
		});
	}

	/**
	 * 获取当前屏幕范围内的司机
	 * @param map
	 * @param callBack
	 */
	public void getMapDriver(Map<String,Object> map, final HttpCallBack callBack){
		String url = util.getUrl(R.string.url_getDriverForMap);
		XUtil.Post(url, map, new MyCallBack<BaseEntity<ArrayList<Map<String, Object>>>>() {
			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable, b);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList<Map<String, Object>>> argo) {
				LogUtils.e("DriverService", "    stringBaseEntity：" + argo.getData());
				if(argo.getResult() == 1) {
					callBack.onSuccess(argo.getData());
				} else {
					callBack.onFail(argo.getResult(), argo.getMessage(), true);
				}
			}
		});
	}

	/**
	 * 添加新司机
	 * @param callBack
	 * @param
	 */
	public void addDriver(Map<String, Object> map, final HttpCallBack callBack){
		final String url = util.getUrl(R.string.url_adddriver);
		XUtil.Post(url, map, new MyCallBack<String>() {
			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable, b);
			}

			@Override
			public void onSuccess(String arrayListBaseEntity) {
				LogUtils.e("DriverService", "   addDriver: " + arrayListBaseEntity);
				if (arrayListBaseEntity != null){
					callBack.onSuccess(arrayListBaseEntity);
				}
			}
		});
	}

	/**
	 * 得到车辆类型
	 * @param callBack
	 */
	public void getCarType(final HttpCallBack callBack){
		String url = util.getUrl(R.string.url_getcartype);
		XUtil.Post(url, null, new MyCallBack<CarTypeEntity<ArrayList>>() {
			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable, b);
			}

			@Override
			public void onSuccess(CarTypeEntity<ArrayList> arg0) {
				if (arg0 != null) {
					ArrayList<LinkedTreeMap<String, Object>> types =
							(ArrayList<LinkedTreeMap<String, Object>>) arg0
									.getBodyTypeList();
					ArrayList<BodyTypeList> list = new ArrayList<BodyTypeList>();
					if (types != null) {
						for (LinkedTreeMap map : types) {
							BodyTypeList type = new BodyTypeList();
							LinkMapToObjectUtil.getObject(map, type);
							list.add(type);
						}
						LogUtils.e("DriverService", "     size:" + list.size());
					}
					callBack.onSuccess(list);
				}
			}
		});
	}

	/**
	 * 获取车辆轴数
	 * @param back
	 */
	public void getAxisType(final HttpCallBack back) {
		final String url = util.getUrl(R.string.url_getCarType);
		XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList<LinkedTreeMap<String, Object>>>>() {
			@Override
			public void onSuccess(BaseEntity<ArrayList<LinkedTreeMap<String, Object>>> result) {
				if (result.getResult() == 1) {
					ArrayList<CarType> types = new ArrayList<CarType>();
					if(result.getData() != null) {
						for (LinkedTreeMap<String, Object> map : result.getData()) {
							CarType carType = new CarType();
							LinkMapToObjectUtil.getObject(map, carType);
							types.add(carType);
						}
						back.onSuccess(types);
					}
					LogUtils.e("getAxisType", url + "    result:  " + result.getResult());
				} else {
					back.onFail(result.getResult(), result.getMessage(), true);
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				back.onError(ex, isOnCallback);
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getDriverFCity(final HttpCallBack back) {
		String url = util.getUrl(R.string.url_getDFocusCity);
		XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				back.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					ArrayList<LinkedTreeMap<String, Object>> cities =
							(ArrayList<LinkedTreeMap<String, Object>>) arg0
							.getData();
					ArrayList<City> list = new ArrayList<City>();
					if (cities != null) {
						for (LinkedTreeMap map : cities) {
							City city = new City();
							LinkMapToObjectUtil.getObject(map, city);
							list.add(city);
						}
					}
					back.onSuccess(list);
				} else {
					back.onFail(arg0.getResult(), arg0.getMessage(), false);
				}
			}
		});
	}

	public void saveDriverFocusCity(Map<String, Object> params, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_saveDFocusCity);
		XUtil.Post(url, params, new MyCallBack<BaseEntity<LinkedTreeMap<String, Object>>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<LinkedTreeMap<String, Object>> arg0) {
				if (arg0.getResult() == 1) {
					callBack.onSuccess(arg0.getMessage());
				} else {
					callBack.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}

	public void getDriverFCom(final HttpCallBack back) {
		String url = util.getUrl(R.string.url_getDFocusCom);
		XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList<LinkedTreeMap<String, Object>>>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				back.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList<LinkedTreeMap<String, Object>>> arg0) {
				if (arg0.getResult() == 1) {
					ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
					ArrayList<Company> companies = new ArrayList<Company>();
					if (list != null) {
						for (LinkedTreeMap<String, Object> map : list) {
							Company com = new Company();
							LinkMapToObjectUtil.getObject(map, com);
							companies.add(com);
						}
					}
					back.onSuccess(companies);
				} else {
					back.onFail(arg0.getResult(), arg0.getMessage(), false);
				}
			}
		});
	}

	public void saveDriverFocusCom(Map<String, Object> params, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_saveDfocusCom);
		XUtil.Post(url, params, new MyCallBack<BaseEntity<LinkedTreeMap<String, Object>>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<LinkedTreeMap<String, Object>> arg0) {
				if (arg0.getResult() == 1) {
					callBack.onSuccess(arg0.getMessage());
				} else {
					callBack.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}

    /**
     * 个人头像
     * @param params
     * @param callBack
     */
	public void savePersonImgs(Map<String,File>params,final HttpCallBack callBack){
	    String url=util.getUrl(R.string.url_savePersonImg);
	    XUtil.UpLoadFile(url, params, new MyCallBack<BaseEntity<String>>() {
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                callBack.onError(arg0, arg1);
				Log.d("失败", "onError: "+arg0.getMessage()+arg1 );
            }

            @Override
            public void onSuccess(BaseEntity<String> arg0) {
				Log.d("savePersonImgs 成功", "onSuccess: "+arg0.getMessage()+arg0.getResult() );
                //if (arg0.getResult() == 1) {
                    callBack.onSuccess(arg0.getMessage());
               // } else {
               //     callBack.onFail(arg0.getResult(), arg0.getMessage(), true);
               // }
            }
        });
    }







	public void saveDriverImgs(Map<String, File> params, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_saveDriverImg);
		XUtil.UpLoadFile(url, params, new MyCallBack<BaseEntity<String>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}
			@Override
			public void onSuccess(BaseEntity<String> arg0) {
				if (arg0.getResult() == 1) {
					callBack.onSuccess(arg0.getMessage());
				} else {
					callBack.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}

	// 保存公司logo
		public void saveCompanyLogoImg(Map<String, File> params, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_uploadLogo);
		XUtil.UpLoadFile(url, params, new MyCallBack<BaseEntity<String>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<String> arg0) {
				if (arg0.getResult() == 1) {
					callBack.onSuccess(arg0.getMessage());
				} else {
					callBack.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}


	public void getDriverInfo(final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_getDriverInfo);
		XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList<LinkedTreeMap<String, Object>>>>
				() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				LogUtils.e("getDiverInfo Error", "" + arg0.toString());
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList<LinkedTreeMap<String, Object>>> arg0) {
				if (arg0.getResult() == 1) {
					PortalDriver driver = new PortalDriver();
					//Log.e("ddddd", "ddd" + driver.getIdentImage());
					LinkMapToObjectUtil.getObject(arg0.getData().get(0), driver);
					UserDetail detail = new UserDetail();
					LinkMapToObjectUtil.getObject(arg0.getData().get(1), detail);
					callBack.onSuccess(driver, detail);
				} else {
					callBack.onFail(arg0.getResult(), arg0.getMessage(), false);
				}
			}
		});
	}

	public void loadDriverImg(ImageView view, String url,Context context) {
		if (!url.contains("?")) {
			url += "?time=" + System.currentTimeMillis();
		} else {
			url += "&time=" + System.currentTimeMillis();
		}
		Glide.with(context).load(url).override(500,500).into(view);
	}

	public void departPlan(Map<String, Object> params, final HttpCallBack back) {
		String url = util.getUrl(R.string.url_departPlan);
		XUtil.Post(url, params, new MyCallBack<BaseEntity<String>>() {
			@Override
			public void onSuccess(BaseEntity<String> result) {
				if (result.getResult() == 1) {
					back.onSuccess(result.getMessage());
				} else {
					back.onFail(result.getResult(), result.getMessage(), true);
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				back.onError(ex, isOnCallback);
			}
		});
	}

	@SuppressWarnings("rawtypes")
	public void searchCom(Map<String, Object> params, final HttpCallBack back) {
		String url = util.getUrl(R.string.url_searchCompany);
		XUtil.Post(url, params, new MyCallBack<BaseEntity<ArrayList>>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(BaseEntity<ArrayList> result) {
				if (result.getResult() >= 1) {
					ArrayList<LinkedTreeMap<String, Object>> list = result
							.getData();
					List<Company> coms = new ArrayList<Company>();
					if (list != null) {
						for (LinkedTreeMap<String, Object> map : list) {
							Company company = new Company();
							LinkMapToObjectUtil.getObject(map, company);
							coms.add(company);
						}
					}
					back.onSuccess(coms);
				} else {
					back.onFail(result.getResult(), result.getMessage(), true);
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				back.onError(ex, isOnCallback);
			}
		});
	}

	public String getServer() {
		return util.getHost();
	}

	public void expireDriver(int carArriveId, int driverId, int uid, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_expiredDriver);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("arriveId", carArriveId);
		params.put("userId", uid);
		params.put("driverId", driverId);
		params.put("t", System.currentTimeMillis());
		XUtil.Post(url, params, new MyCallBack<BaseEntity<Double>>() {
			@Override
			public void onSuccess(BaseEntity<Double> result) {
				callBack.onSuccess(result);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				callBack.onError(ex, isOnCallback);
			}
		});
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getArrivedDriver(String condition, int currentPage, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_getArriveDriver);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentPage", currentPage);
		map.put("target", condition);
		XUtil.Post(url, map, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					List<LinkedTreeMap<String, Object>> list = arg0.getData();
					List<CarArrived> arrivedlist = new ArrayList<CarArrived>();
					if (list != null) {
						for (LinkedTreeMap<String, Object> linkedTreeMap : list) {
							CarArrived arrived = new CarArrived();
							LinkMapToObjectUtil.getObject(linkedTreeMap,
									arrived);
							arrivedlist.add(arrived);
						}
					}
					callBack.onSuccess(arrivedlist);
				} else {
					ToastUtil.show(arg0.getMessage()+"",context);
				}
			}
		});
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void searchDriver(double length, double length2, double width,
			int dayOff, String condition, int currentPage,String publicDriver, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_searchDriver);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentPage", currentPage);
		map.put("length", length);
		map.put("length2", length2);
		map.put("width", width);
		map.put("dayOff", dayOff);
		map.put("condition", condition);
		map.put("publicDriver",publicDriver);
		XUtil.Post(url, map, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					List<LinkedTreeMap<String, Object>> list = arg0.getData();
					List<Driver> dlist = new ArrayList<Driver>();
					if (list != null) {
						for (LinkedTreeMap<String, Object> linkedTreeMap : list) {
							Driver d = new Driver();
							LinkMapToObjectUtil.getObject(linkedTreeMap, d);
							dlist.add(d);
						}
					}
					callBack.onSuccess(dlist);
				} else {
					callBack.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getParkDriver(int currentPage, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_getParkDriver);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentPage", currentPage);
		XUtil.Post(url, map, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					List<LinkedTreeMap<String, Object>> list = arg0.getData();
					List<CarArrived> arrivedlist = new ArrayList<CarArrived>();
					if (list != null) {
						for (LinkedTreeMap<String, Object> linkedTreeMap : list) {
							CarArrived arrived = new CarArrived();
							LinkMapToObjectUtil.getObject(linkedTreeMap,
									arrived);
							arrivedlist.add(arrived);
						}
					}
					callBack.onSuccess(arrivedlist);
				} else {
					callBack.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getPauseDriver(String condition, int currentPage, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_getPauseDriver);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentPage", currentPage);
		map.put("condition", condition);
		XUtil.Post(url, map, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					List<LinkedTreeMap<String, Object>> list = arg0.getData();
					List<Driver> dlist = new ArrayList<Driver>();
					if (list != null) {
						for (LinkedTreeMap<String, Object> linkedTreeMap : list) {
							Driver d = new Driver();
							LinkMapToObjectUtil.getObject(linkedTreeMap, d);
							dlist.add(d);
						}
					}
					callBack.onSuccess(dlist);
				} else {
					callBack.onFail(-1, "查找失败", true);
				}
			}
		});
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getDeletedDriver(String condition, int currentPage, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_getDeletedDriver);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentPage", currentPage);
		map.put("condition", condition);
		XUtil.Post(url, map, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					List<LinkedTreeMap<String, Object>> list = arg0.getData();
					List<Driver> dlist = new ArrayList<Driver>();
					if (list != null) {
						for (LinkedTreeMap<String, Object> linkedTreeMap : list) {
							Driver d = new Driver();
							LinkMapToObjectUtil.getObject(linkedTreeMap, d);
							dlist.add(d);
						}
					}
					callBack.onSuccess(dlist);
				} else {
					callBack.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getNewDriver(final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_getNewDriver);
		XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					List<LinkedTreeMap<String, Object>> list = arg0.getData();
					List<Driver> dlist = new ArrayList<Driver>();
					if (list != null) {
						for (LinkedTreeMap<String, Object> linkedTreeMap : list) {
							Driver d = new Driver();
							LinkMapToObjectUtil.getObject(linkedTreeMap, d);
							dlist.add(d);
						}
					}
					callBack.onSuccess(dlist);
				} else {
					callBack.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}
	
	public void removeNewDriver(int driverId, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_removeNewDriver);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", driverId);
		XUtil.Post(url, params, new MyCallBack<Driver>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(Driver arg0) {
				callBack.onSuccess(arg0);
			}
		});
	}
	
	/**
	 * 解除暂停
	 * @param driverId
	 * @param callBack
	 */
	public void removePause(int driverId, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_unPauseDriver);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("driverId", driverId);
		XUtil.Post(url, map, new MyCallBack<UNPauseReturn>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(UNPauseReturn arg0) {
				callBack.onSuccess();
			}
		});
	}
	
	/**
	 * 删除司机恢复
	 * @param driverId
	 * @param callBack
	 */
	public void recoverDriver(int driverId, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_recoverDelDriver);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("driverId", driverId);
		XUtil.Post(url, map, new MyCallBack<MyJsonReturn<String>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(MyJsonReturn<String> arg0) {
				if(arg0 .getResult() != null && arg0.getResult() == 1) {
					callBack.onSuccess();
				} else {
					callBack.onFail(-1, "操作失败", true);
				}
			}
		});
	}
	
	/**
	 * 彻底删除司机
	 * @param driverId
	 * @param callBack
	 */
	public void removeDelDriver(int driverId, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_removeDelDriver);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("driverId", driverId);
		XUtil.Post(url, map, new MyCallBack<MyJsonReturn<String>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(MyJsonReturn<String> arg0) {
				if(arg0 .getResult() != null && arg0.getResult() == 1) {
					callBack.onSuccess();
				} else {
					callBack.onFail(-1, "操作失败", true);
				}
			}
		});
	}

	/**
	 * 保存并发布已到达司机
	 * @param carArrived
	 * @param callBack
	 */
	public void uploadAdrrivedDriver(CarArrived carArrived,final HttpCallBack callBack){
		String path=util.getUrl(R.string.url_updata_arrived_driver);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("carArriveDTO.driverId",carArrived.getDriverId());
		map.put("carArriveDTO.driverName",carArrived.getDriverName());
		map.put("carArriveDTO.driver_owner",carArrived.getDriver_owner());
		map.put("carArriveDTO.userId",carArrived.getUserId());
		map.put("carArriveDTO.force_add",carArrived.getForce_add());
		map.put("carArriveDTO.arriveDate",carArrived.getArriveDate());
		map.put("carArriveDTO.recordDate",carArrived.getRecordDate());
		map.put("carArriveDTO.owen",carArrived.getOwen());
		map.put("carArriveDTO.nowLocation",carArrived.getNowLocation());
		map.put("carArriveDTO.nowLocationC",carArrived.getNowLocationC());
		map.put("carArriveDTO.targetLocation",carArrived.getTargetLocation());
		map.put("carArriveDTO.targetLocationC",carArrived.getTargetLocationC());
		map.put("carArriveDTO.memo",carArrived.getMemo());
		map.put("carArriveDTO.inStop",carArrived.getInStop());
		map.put("carArriveDTO.goodsOnCar",carArrived.getGoodsOnCar());

		XUtil.Post(path, map, new MyCallBack<MyJsonReturn<String>>() {
			@Override
			public void onError(Throwable throwable, boolean b) {
				callBack.onError(throwable, b);
			}

			@Override
			public void onSuccess(MyJsonReturn<String> stringMyJsonReturn) {
				if (stringMyJsonReturn.getCount() != null && stringMyJsonReturn.getCount() >= 0) {
					callBack.onSuccess();
				} else {
					callBack.onFail(-1, "操作失败！", true);
				}
			}
		});
	}

	public void findById(int id, final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_getDriver) + "?id=" + id;
		XUtil.Get(url, null, new MyCallBack<JSONObject>() {
			@Override
			public void onError (Throwable throwable, boolean b) {
				callBack.onError(throwable, b);
			}

			@Override
			public void onSuccess (JSONObject jsonObject) {
				callBack.onSuccess(jsonObject);
			}
		});
	}

	//获取用户提交"资料认证"状态
	public void getAuthentication(final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_getAuthentication);
		XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
                callBack.onSuccess(arg0);
            }

		});
	}

	//获取用户认证信息
	public void checkUserAuthentication(final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_checkUserAuthentication);
		XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				callBack.onSuccess(arg0);
			}

		});
	}


}
package com.car.portal.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import android.content.Context;
import android.database.Cursor;

import com.car.portal.R;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.City;
import com.car.portal.entity.CityLoadStateEnum;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.XUtil;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.MyDBUtil;
import com.car.portal.util.SharedPreferenceUtil;
import com.car.portal.util.StringUtil;
import com.google.gson.internal.LinkedTreeMap;

public class CitysService extends BaseService{

	public static int PAGER_SIZE = 100;

	public static final String FIND_PROVINCE = "SELECT * FROM city c where c.newLevel = 1 GROUP BY (c.provice_id)";
	public static final String FIND_CITY_BYPROVICE = "SELECT * FROM city c WHERE c.provice_id = ? AND c.newLevel=2 ORDER BY c.id";
	public static final String FIND_COUNTY_BYCITY="SELECT * FROM city c WHERE c.newLevel = NEWLEVEL_VALUE and parentId = PARENTID_VALUE ORDER BY c.id";

	//SELECT * FROM citys c WHERE c.provice_id = 16 AND c.level=20 ORDER BY c.id

	public CitysService(Context context) {
		super(context);
	}

	@SuppressWarnings("rawtypes")
	public void getProvince(final HttpCallBack back) {
		CityLoadStateEnum stateEnum = new SharedPreferenceUtil().getCityLoadStatue(context);
		if (stateEnum != CityLoadStateEnum.FINISH) {
			String url = util.getUrl(R.string.url_findProvince);
			XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList>>() {
				@Override
				public void onError(Throwable arg0, boolean arg1) {
					back.onError(arg0, arg1);
				}

				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(BaseEntity<ArrayList> arg0) {
					if (arg0.getResult() == 1) {
						ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
						List<City> citys = new ArrayList<City>();
						if (list != null) {
							for (LinkedTreeMap<String, Object> map : list) {
								City city = new City();
								LinkMapToObjectUtil.getObject(map, city);
								citys.add(city);
							}
						}
						back.onSuccess(citys);
					} else {
						back.onFail(arg0.getResult(), arg0.getMessage(), true);
					}
				}
			});
		} else {
			DbManager dbManager = MyDBUtil.getManager();
			Cursor cursor = null;
			try {
				cursor = dbManager.execQuery(FIND_PROVINCE);
				List<City> list = getFromCursor(cursor);
				back.onSuccess(list);
			} catch (DbException e) {
				System.out.println(e.getMessage());
			} finally {
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
		}
	}

	@SuppressWarnings("rawtypes")
	public void getCityByProvince(int provinceId, final HttpCallBack back) {
		CityLoadStateEnum stateEnum = new SharedPreferenceUtil().getCityLoadStatue(context);
		if (stateEnum != CityLoadStateEnum.FINISH) {
			String url = util.getUrl(R.string.url_findByProvince);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("provinceId", provinceId);
			XUtil.Post(url, params, new MyCallBack<BaseEntity<ArrayList>>() {
				@Override
				public void onError(Throwable arg0, boolean arg1) {
					back.onError(arg0, arg1);
				}

				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(BaseEntity<ArrayList> arg0) {
					if (arg0.getResult() == 1) {
						ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
						List<City> citys = new ArrayList<City>();
						if (list != null) {
							for (LinkedTreeMap<String, Object> map : list) {
								City city = new City();
								LinkMapToObjectUtil.getObject(map, city);
								citys.add(city);
							}
						}
						back.onSuccess(citys);
					} else {
						back.onFail(arg0.getResult(), arg0.getMessage(), true);
					}
				}
			});
		} else {
			DbManager dbManager = MyDBUtil.getManager();
			Cursor cursor = null;
			try {
				String sql = FIND_CITY_BYPROVICE.replace("?", provinceId + "");
				cursor = dbManager.execQuery(sql);
				List<City> list = getFromCursor(cursor);
				back.onSuccess(list);
			} catch (DbException e) {
				System.out.println(e.getMessage());
			} finally {
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
		}
	}

	/**
	 * 获取区县
	 * @param
	 */
	public void getChildByCity(City city, final HttpCallBack back){
		if(city == null) {
			return;
		}
		CityLoadStateEnum stateEnum = new SharedPreferenceUtil().getCityLoadStatue(context);
		if (city.getNewLevel() >= 3 || stateEnum != CityLoadStateEnum.FINISH) {
			String url = util.getUrl(R.string.url_getChildCity);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cityId", city.getCid());
			XUtil.Post(url, params, new MyCallBack<BaseEntity<ArrayList>>() {
				@Override
				public void onError(Throwable arg0, boolean arg1) {
					back.onError(arg0, arg1);
				}

				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(BaseEntity<ArrayList> arg0) {
					if (arg0.getResult() == 1) {
						ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
						List<City> citys = new ArrayList<City>();
						if (list != null) {
							for (LinkedTreeMap<String, Object> map : list) {
								City city = new City();
								LinkMapToObjectUtil.getObject(map, city);
								citys.add(city);
							}
						}
						back.onSuccess(citys);
					} else {
						back.onFail(arg0.getResult(), arg0.getMessage(), true);
					}
				}
			});
		}else {
			DbManager dbManager = MyDBUtil.getManager();
			Cursor cursor = null;
			try {
				String sql = FIND_COUNTY_BYCITY.replace("NEWLEVEL_VALUE", (city.getNewLevel() + 1) + "").replace("PARENTID_VALUE", city.getCode() + "");
				cursor = dbManager.execQuery(sql);
				List<City> list = getFromCursor(cursor);
				back.onSuccess(list);
			} catch (DbException e) {
				System.out.println(e.getMessage());
			} finally {
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
		}
	}

	public void findCity(int cid, final HttpCallBack back) {
		CityLoadStateEnum stateEnum = new SharedPreferenceUtil().getCityLoadStatue(context);
		if (stateEnum != CityLoadStateEnum.FINISH) {
			String url = util.getUrl(R.string.url_findCity);
			Map<String, Object> params = new HashMap<String, Object>();
			XUtil.Post(url, params, new MyCallBack<BaseEntity<LinkedTreeMap<String, Object>>>() {
				@Override
				public void onError(Throwable arg0, boolean arg1) {
					back.onError(arg0, arg1);
				}

				@Override
				public void onSuccess(BaseEntity<LinkedTreeMap<String, Object>> arg0) {
					if (arg0.getResult() == 1) {
						City city = new City();
						LinkMapToObjectUtil.getObject(arg0.getData(), city);
						back.onSuccess(city);
					} else {
						back.onFail(arg0.getResult(), arg0.getMessage(), true);
					}
				}
			});
		} else {
			DbManager dbManager = MyDBUtil.getManager();
			try {
				City city = dbManager.selector(City.class).where("cid", "=", cid).orderBy("id", true).findFirst();
				back.onSuccess(city);
				dbManager.close();
			} catch (DbException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} finally {
				if (dbManager != null) {
					try {
						dbManager.close();
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
					dbManager = null;
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void getAllCityLevel(Map<String, Object> params, final HttpCallBack back) {
		String url = util.getUrl(R.string.url_findByProvince);
		XUtil.Post(url, params, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				back.onError(arg0, arg1);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
					List<City> citys = new ArrayList<City>();
					if (list != null) {
						for (LinkedTreeMap<String, Object> map : list) {
							City city = new City();
							LinkMapToObjectUtil.getObject(map, city);
							citys.add(city);
						}
					}
					back.onSuccess(citys);
				} else {
					back.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}

	@SuppressWarnings("rawtypes")
	public void loadByPager(int currentPage, final HttpCallBack back) {
		String url = util.getUrl(R.string.url_downCity);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("currentPage", currentPage);
		params.put("pageSize", PAGER_SIZE);
		XUtil.Post(url, params, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				back.onError(arg0, arg1);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if (arg0.getResult() == 1) {
					ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
					List<City> citys = new ArrayList<City>();
					if (list != null) {
						for (LinkedTreeMap<String, Object> map : list) {
							City city = new City();
							LinkMapToObjectUtil.getObject(map, city);
							citys.add(city);
						}
					}
					int pageCount = Integer.parseInt(arg0.getMessage());
					back.onSuccess(citys, pageCount);
				} else {
					back.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}

	public CityLoadStateEnum getLoadStatue() {
		return new SharedPreferenceUtil().getCityLoadStatue(context);
	}

	public void setLoadStatue(CityLoadStateEnum stateEnum) {
		new SharedPreferenceUtil().saveCityLoadInfo(context, stateEnum);
	}

	private static final String FIND_BY_IDS = "SELECT * FROM city c where c.cid in (?)";

	public void findCityByIds(String cids, final HttpCallBack httpCallBack) {
		if (StringUtil.isNullOrEmpty(cids)) {
			httpCallBack.onSuccess();
			return;
		}
		StringBuffer buffer = new StringBuffer(cids);
		for (int i = buffer.length() - 1; i >= 0; i--) {
			char ch = buffer.charAt(i);
			if (ch != ',' && (ch < '0' || ch > '9')) {
				buffer.deleteCharAt(i);
			}
		}
		if (buffer.length() > 0 && buffer.charAt(0) == ',') {
			buffer.deleteCharAt(0);
		}
		if (buffer.length() > 0 && buffer.charAt(buffer.length() - 1) == ',') {
			buffer.deleteCharAt(buffer.length() - 1);
		}
		if (buffer.length() > 0) {
			CityLoadStateEnum stateEnum = new SharedPreferenceUtil().getCityLoadStatue(context);
			if (stateEnum == CityLoadStateEnum.FINISH) {
				DbManager dbManager = MyDBUtil.getManager();
				Cursor cursor = null;
				try {
					String sql = FIND_BY_IDS.replace("?", cids);
					cursor = dbManager.execQuery(sql);
					List<City> list = getFromCursor(cursor);
					httpCallBack.onSuccess(list);
					return;
				} catch (DbException e) {
					System.out.println(e.getMessage());
				} finally {
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
			}
		}
		httpCallBack.onSuccess();
	}
	
	public void downLoadCity(final HttpCallBack callBack) {
		String url = util.getUrl(R.string.url_downLoadBaseCity);
		XUtil.DownLoadFile(url, ".", new MyCallBack<File>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				callBack.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(File arg0) {
				callBack.onSuccess(arg0);
			}
		});
	}
	
	private List<City> getFromCursor(Cursor cursor) {
		List<City> cities = new ArrayList<City>();
		int idColumn = cursor.getColumnIndex("id");
		int cidColumn = cursor.getColumnIndex("cid");
		int cityColumn = cursor.getColumnIndex("city");
		int pColumn = cursor.getColumnIndex("provice");
		int pidColumn = cursor.getColumnIndex("provice_id");
		int levelColumn = cursor.getColumnIndex("level");
		int codeColumn = cursor.getColumnIndex("code");
		int areaNameColumn = cursor.getColumnIndex("areaName");
		int parentIdColumn = cursor.getColumnIndex("parentId");
		int shortNameColumn = cursor.getColumnIndex("shortName");
		int newLevelColumn = cursor.getColumnIndex("newLevel");
		int sortColumn = cursor.getColumnIndex("sort");
		
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			City city = new City();
			city.setId(cursor.getInt(idColumn));
			city.setCid(cursor.getInt(cidColumn));
			city.setCity(cursor.getString(cityColumn));
			city.setLevel(cursor.getInt(levelColumn));
			city.setProvice(cursor.getString(pColumn));
			city.setProvice_id(cursor.getInt(pidColumn));
			city.setCode(cursor.getInt(codeColumn));
			city.setAreaName(cursor.getString(areaNameColumn));
			city.setParentId(cursor.getInt(parentIdColumn));
			city.setShortName(cursor.getString(shortNameColumn));
			city.setNewLevel(cursor.getInt(newLevelColumn));
			city.setSort(cursor.getInt(sortColumn));
			cities.add(city);
		}
		return cities;
	}
	
	private static final String FINDBYCID = "select * from city where cid = ";
	public City getByCid(int cid) throws DbException {
		DbManager manager = MyDBUtil.getManager();
		Cursor cursor = manager.execQuery(FINDBYCID + cid);
		int idColumn = cursor.getColumnIndex("id");
		int cidColumn = cursor.getColumnIndex("cid");
		int cityColumn = cursor.getColumnIndex("city");
		int pColumn = cursor.getColumnIndex("provice");
		int pidColumn = cursor.getColumnIndex("provice_id");
		int levelColumn = cursor.getColumnIndex("level");
		int codeColumn = cursor.getColumnIndex("code");
		int areaNameColumn = cursor.getColumnIndex("areaName");
		int parentIdColumn = cursor.getColumnIndex("parentId");
		int shortNameColumn = cursor.getColumnIndex("shortName");
		int newLevelColumn = cursor.getColumnIndex("newLevel");
		int sortColumn = cursor.getColumnIndex("sort");
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			City city = new City();
			city.setId(cursor.getInt(idColumn));
			city.setCid(cursor.getInt(cidColumn));
			city.setCity(cursor.getString(cityColumn));
			city.setLevel(cursor.getInt(levelColumn));
			city.setProvice(cursor.getString(pColumn));
			city.setProvice_id(cursor.getInt(pidColumn));
			city.setCode(cursor.getInt(codeColumn));
			city.setAreaName(cursor.getString(areaNameColumn));
			city.setParentId(cursor.getInt(parentIdColumn));
			city.setShortName(cursor.getString(shortNameColumn));
			city.setNewLevel(cursor.getInt(newLevelColumn));
			city.setSort(cursor.getInt(sortColumn));
			return city;
		} else {
			return null;
		}
	}
}

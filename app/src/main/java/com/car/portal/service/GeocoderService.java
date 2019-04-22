package com.car.portal.service;

import android.content.Context;

import com.car.portal.R;
import com.car.portal.entity.ApplicationConfig;
import com.car.portal.entity.BaiduAddress;
import com.car.portal.entity.BaiduEntity;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.XUtil;
import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;

public class GeocoderService extends BaseService{
	
	private static String baidu_ak;
	private static String baidu_url;
	private static final String DATA = "?ak=%1$s&location=%2$s,%3$s&output=json";
	private static final String TAG = "GeocoderService";
	
	public GeocoderService(Context context) {
		super(context);
		if(StringUtil.isNullOrEmpty(baidu_ak)) {
			baidu_ak = ApplicationConfig.getIntance().getBaidu_ak();
		}
		if(StringUtil.isNullOrEmpty(baidu_url)) {
			baidu_url = context.getResources().getString(R.string.baidu_url);
		}
	}
	
	public void execute(String lat, String lng, final HttpCallBack callBack) {
		String data = String.format(DATA, baidu_ak, lat, lng);
		String url = baidu_url + data;
		XUtil.Get(url, null, new MyCallBack<BaiduEntity>() {
			@Override
			public void onSuccess(BaiduEntity result) {
				BaiduAddress address = result.getResult();
				if(address != null) {
					callBack.onSuccess(address.getFormatted_address());
				} else {
					callBack.onFail(0, "位置信息获取失败", false);
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				LogUtils.e(TAG, ex == null ? "error baidu" : ex.getLocalizedMessage());
			}
		});
	}
}

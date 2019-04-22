package com.car.portal.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import android.content.Context;

import com.car.portal.R;
import com.car.portal.application.MyApplication;
import com.car.portal.entity.City;
import com.car.portal.entity.CityLoadStateEnum;
import com.car.portal.http.HttpCallBack;
import com.car.portal.service.CitysService;

public class InitApplicationUtil {
	
	private static InitApplicationUtil util;
	private static SharedPreferenceUtil spUtil;
	private static final String INIT_APPLICATION_SIGN = "initAppSign";
	
	private InitApplicationUtil(){
		spUtil = SharedPreferenceUtil.getIntence();
		context = MyApplication.getContext();
	}
	
	private static class InnerClass {
		static InitApplicationUtil util = new InitApplicationUtil();
	}
	
	public static InitApplicationUtil getInitance() {
		if(util == null) {
			util = InnerClass.util;
		}
		return util;
	}
	
	private Context context;
	private CitysService service;
	
	public void initCity() {
		service = new CitysService(context);
		if(!hasInit()) {
			try {
				InputStream in = context.getResources().openRawResource(R.raw.basecity);
				if(in != null) {
					initCityByFile(in);
				} 
			} catch (Exception e) {
				System.out.println(e.getMessage());
				service.downLoadCity(new HttpCallBack(context) {
					@Override
					public void onFail(int result, String message, boolean show) {
						LogUtils.e("InitApplication", message);
					}
					
					@Override
					public void onSuccess(Object... objects) {
						File file = (File) objects[0];
						try {
							initCityByFile(new FileInputStream(file));
						} catch (FileNotFoundException e) {
							System.out.println(e.getMessage());
						}
					}
				});
			}
		}
	}
	
	private void initCityByFile(final InputStream in){
		new Thread() {
			public void run() {
				DbManager manager = MyDBUtil.getManager();
				InputStreamReader inr = new InputStreamReader(in);
				BufferedReader reader = new BufferedReader(inr);
				try {
					manager.dropTable(City.class);
					City city = new City();
					city.setCid(0);
					city.setCity("全国");
					city.setCid(0);
					city.setLevel(0);
					city.setProvice("全国");
					city.setProvice_id(0);
					manager.save(city);
					String line = null;
					while ((line = reader.readLine()) != null) {
						manager.executeUpdateDelete(line);
					}
					service.setLoadStatue(CityLoadStateEnum.FINISH);
				} catch (DbException e) {
					System.out.println(e.getMessage());
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				try {
					manager.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}.start();
	}
	
	public boolean hasInit() {
		String hasInit = spUtil.getNomalValue(INIT_APPLICATION_SIGN, context);
		return "true".equals(hasInit);
	}
	
}

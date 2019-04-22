package com.car.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import android.content.Context;
import android.database.Cursor;

import com.car.portal.R;
import com.car.portal.entity.AppMessage;
import com.car.portal.entity.BaseEntity;
import com.car.portal.entity.MessageCount;
import com.car.portal.entity.Types;
import com.car.portal.http.HttpCallBack;
import com.car.portal.http.MyCallBack;
import com.car.portal.http.XUtil;
import com.car.portal.util.LinkMapToObjectUtil;
import com.car.portal.util.MyDBUtil;
import com.google.gson.internal.LinkedTreeMap;

public class MessageService extends BaseService{
	
	public MessageService(Context context) {
		super(context);
	}
	
	@SuppressWarnings("rawtypes")
	public void getMessage(int currentId, final HttpCallBack back) {
		final String url = util.getUrl(R.string.url_getMessage);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("currentId", currentId);
		XUtil.Post(url, null, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				back.onError(arg0, arg1);
			}
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if(arg0.getResult() == 1) {
					ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
					List<AppMessage> gds = new ArrayList<AppMessage>();
					if(list != null) {
						for (LinkedTreeMap<String, Object> map:list) {
							AppMessage msg = new AppMessage();
							LinkMapToObjectUtil.getObject(map, msg);
							gds.add(msg);
						}
					}
					back.onSuccess(gds);
				} else {
					back.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	public void getTypeMessage(int type, final HttpCallBack back) {
		String url = util.getUrl(R.string.url_getMessage);
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("type", type);
		XUtil.Post(url, params, new MyCallBack<BaseEntity<ArrayList>>() {
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				back.onError(arg0, arg1);
			}
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(BaseEntity<ArrayList> arg0) {
				if(arg0.getResult() == 1) {
					ArrayList<LinkedTreeMap<String, Object>> list = arg0.getData();
					List<AppMessage> gds = new ArrayList<AppMessage>();
					if(list != null) {
						for (LinkedTreeMap<String, Object> map:list) {
							AppMessage msg = new AppMessage();
							LinkMapToObjectUtil.getObject(map, msg);
							gds.add(msg);
						}
					}
					back.onSuccess(gds);
				} else {
					back.onFail(arg0.getResult(), arg0.getMessage(), true);
				}
			}
		});
	}
	
	private static final String FIND_NUM_SQL = "select type,COUNT(1) as number from appmessage where isRead = 0 GROUP BY type";
	public List<AppMessage> getLocalMessage() {
		DbManager manager = MyDBUtil.getManager();
		List<AppMessage> msgs = null;
		try {
			Cursor cursor = manager.execQuery(FIND_NUM_SQL);
			List<MessageCount> list = new ArrayList<MessageCount>();
			int typeColumn = cursor.getColumnIndex("type");
		    int numColumn = cursor.getColumnIndex("number");
			for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			    MessageCount cnt = new MessageCount();
			    int typeId = cursor.getShort(typeColumn);
			    cnt.setTypeId(typeId);
			    cnt.setTypeName(Types.getMsgType(typeId));
			    cnt.setCount(cursor.getInt(numColumn));
			    list.add(cnt);
			}
			msgs = manager.findAll(AppMessage.class);
			cursor.close();
		} catch (DbException e) {
			System.out.println(e.getMessage());
		}
		return msgs;
		
	}

}

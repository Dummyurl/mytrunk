package com.car.portal.http;

import com.car.portal.util.LogUtils;
import com.car.portal.util.StringUtil;
import com.google.gson.Gson;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class MyResponseParser implements ResponseParser {
    //检查服务器返回的响应头信息
    @Override
    public void checkResponse(UriRequest request) throws Throwable {
    	Map<String, List<String>> maps = request.getResponseHeaders();
    	if(maps != null) {
			List<String> list = maps.get("Set-Cookie");
			if(list != null && list.size() > 0) {
				String str = list.get(0);
				if(!StringUtil.isNullOrEmpty(str) && str.contains(";")) {
					String sessId = str.substring(0, str.indexOf(";"));
					LogUtils.e("sessionId>>>>>>>>>>>>>>",sessId);
					SessionStore.setSessionId(sessId);
				}
			}
    	}
    }

    /**
     * 转换result为resultType类型的对象
     *
     * @param resultType  返回值类型(可能带有泛型信息)
     * @param resultClass 返回值类型
     * @param result      字符串数据
     * @return
     * @throws Throwable
     */
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
    	LogUtils.e("ResponseParser", resultClass.getSimpleName() + ":" + result);
        return new Gson().fromJson(result, resultClass);
    }
}

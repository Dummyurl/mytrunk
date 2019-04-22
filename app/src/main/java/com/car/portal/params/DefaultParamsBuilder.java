package com.car.portal.params;

import java.util.HashMap;

import javax.net.ssl.SSLSocketFactory;

import org.xutils.x;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.ParamsBuilder;

import com.car.portal.http.SessionStore;
import com.car.portal.util.StringUtil;

public class DefaultParamsBuilder implements ParamsBuilder {
	
	public static final String SEEVER_A = "a";
    public static final String SEEVER_B = "b";

    private static final HashMap<String, String> SERVER_MAP = new HashMap<String, String>();

    private static final HashMap<String, String> DEBUG_SERVER_MAP = new HashMap<String, String>();

    static {
        SERVER_MAP.put(SEEVER_A, "http://192.168.0.125/portalWeb/app/");
        DEBUG_SERVER_MAP.put(SEEVER_A, "http://debug.a.xxx.xxx");
    }

	@Override
	public String buildCacheKey(RequestParams arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildParams(RequestParams params) {
		params.setCharset("utf-8");
		String sessionId = SessionStore.getIntence().getSessionId();
		if(sessionId != null) {
			params.setHeader("Cookie", sessionId);
		}
		params.addParameter("time", System.currentTimeMillis() + "");
		params.setAsJsonContent(true);
	}

	@Override
	public void buildSign(RequestParams arg0, String[] arg1) {}

	@Override
	public String buildUri(RequestParams params, HttpRequest httpRequest) {
		String url = getHost(httpRequest.host());
        url += "/" + httpRequest.path();
        return url;
	}

	private String getHost(String host) {
		String result = null;
        if (x.isDebug()) {
            result = DEBUG_SERVER_MAP.get(host);
        } else {
            result = SERVER_MAP.get(host);
        }
        return StringUtil.isNullOrEmpty(result) ? host : result;
	}

	@Override
	public SSLSocketFactory getSSLSocketFactory() {
		return null;
	}

}

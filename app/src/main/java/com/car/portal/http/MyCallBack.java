package com.car.portal.http;

import org.xutils.common.Callback;

public abstract class MyCallBack<ResultType> implements Callback.CommonCallback<ResultType>{

	@Override
	public void onCancelled(CancelledException cex) {
		
	}

	@Override
	public void onFinished() {
//		XUtil.removeCall(this);
	}
}

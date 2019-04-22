package com.car.portal.entity;

import java.io.Serializable;

import org.xutils.http.annotation.HttpResponse;

import com.car.portal.http.BaiduResponseParser;

@HttpResponse(parser = BaiduResponseParser.class)
public class BaiduEntity implements Serializable {
	private static final long serialVersionUID = 1022483153279139267L;
	private int status;
	private BaiduAddress result;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public BaiduAddress getResult() {
		return result;
	}

	public void setResult(BaiduAddress result) {
		this.result = result;
	}

}

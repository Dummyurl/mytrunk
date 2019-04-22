package com.car.portal.entity;

import java.io.Serializable;

import org.xutils.http.annotation.HttpResponse;

import com.car.portal.http.MyResponseParser;


@HttpResponse(parser = MyResponseParser.class)
public class ParseReturn implements Serializable {
	private static final long serialVersionUID = 1L;
	private String result;
	private Message message;
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}

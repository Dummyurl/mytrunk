package com.car.portal.entity;

import java.io.Serializable;

import org.xutils.http.annotation.HttpResponse;

import com.car.portal.http.MyResponseParser;

@HttpResponse(parser=MyResponseParser.class)
public class BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer result;
	private String message;
	private Integer counts;
	private String token; //返回变更的token
	private T data;
	
	public BaseEntity(Integer result, String message, T data,Integer counts,String token) {
		super();
		this.result = result;
		this.message = message;
		this.data = data;
		this.counts = counts;
		this.token = token;
	}
	
	public BaseEntity() {
		super();
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}

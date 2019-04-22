package com.car.portal.entity;

public class NotNetWorkException extends Exception {

	private static final long serialVersionUID = 1523170415481359900L;
	private static final String MSG = "网络未连接，请打开数据连接重试";

	public NotNetWorkException() {
		super(MSG);
	}
	
	public NotNetWorkException(String msg) {
		super(msg);
	}

	public NotNetWorkException(Throwable throwable) {
		super(throwable);
	}
	
	@Override
	public String getLocalizedMessage() {
		return MSG;
	}
}

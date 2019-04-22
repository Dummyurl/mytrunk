package com.car.portal.entity;

import java.io.Serializable;

import org.xutils.http.annotation.HttpResponse;

import com.car.portal.http.MyResponseParser;

@SuppressWarnings("serial")
@HttpResponse(parser = MyResponseParser.class)
public class ContactEntity<T> implements Serializable {
	
	public ContactEntity(int bossId, T contacts) {
		this.bossId = bossId;
		this.contacts = contacts;
	}

	private int bossId;
	private int flag;
	private T contacts;

	public T getContacts() {
		return contacts;
	}

	public void setContacts(T contacts) {
		this.contacts = contacts;
	}

	public int getBossId() {
		return bossId;
	}

	public void setBossId(int bossId) {
		this.bossId = bossId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}

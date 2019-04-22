package com.car.portal.entity;

import com.car.portal.http.MyResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@HttpResponse(parser = MyResponseParser.class)
public class MyJsonReturn<T> implements Serializable {

	private Boolean overtime;
	private Integer pageRows;
	private Integer count;
	private List<T> list;
	private Integer result;
	private List<T> contractList;
	private String contactsNames[];
	private String tels[];
	private int uids[];
	private String token;

	public Boolean getOvertime() {
		return overtime;
	}

	public void setOvertime(Boolean overtime) {
		this.overtime = overtime;
	}

	public Integer getPageRows() {
		return pageRows;
	}

	public void setPageRows(Integer pageRows) {
		this.pageRows = pageRows;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public List<T> getContractList() {
		return contractList;
	}

	public void setContractList(List<T> contractList) {
		this.contractList = contractList;
	}

	public String[] getContactsNames() {
		return contactsNames;
	}

	public void setContactsNames(String[] contactsNames) {
		this.contactsNames = contactsNames;
	}

	public String[] getTels() {
		return tels;
	}

	public void setTels(String[] tels) {
		this.tels = tels;
	}

	public int[] getUids() {
		return uids;
	}

	public void setUids(int[] uids) {
		this.uids = uids;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}

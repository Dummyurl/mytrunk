package com.car.portal.entity;

import android.os.Parcel;
import android.os.Parcelable;
//../myjson/jsonTodayEntructingAction!doGoodsFoeower_.action?hasHelloFlog=true&userId=104&pathValue=0&t=
public class Goods implements Parcelable {
	private Integer id;
	private Integer company_id;
	private String createtime;
	private String detail;
	private Integer flag;
	private String tel;
	private String contact;
	private String start_address;
	private String end_address;
	private String valid_date;
	private String loading_date;
	private String issue_date;
	private String memo;
	private Integer goodsId;
	private Integer body_type;
	private String goods_name;
	private String car_long;
	private String car_width;
	private String company_name;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}

	public String getCreatetime() {
		return createtime == null ? "" : createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getDetail() {
		return detail == null ? "" : detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getTel() {
		return tel == null ? "" : tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getContact() {
		return contact == null ? "" : contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getStart_address() {
		return start_address == null ? "" : start_address;
	}

	public void setStart_address(String start_address) {
		this.start_address = start_address;
	}

	public String getEnd_address() {
		return end_address == null ? "" : end_address;
	}

	public void setEnd_address(String end_address) {
		this.end_address = end_address;
	}

	public String getValid_date() {
		return valid_date == null ? "" : valid_date;
	}

	public void setValid_date(String valid_date) {
		this.valid_date = valid_date;
	}

	public String getLoading_date() {
		return loading_date == null ? "" : loading_date.substring(0, 10);
	}

	public void setLoading_date(String loading_date) {
		this.loading_date = loading_date;
	}

	public String getIssue_date() {
		return issue_date == null ? "" : issue_date.substring(0, 10);
	}

	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}

	public String getMemo() {
		return memo == null ? "" : memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getBody_type() {
		return body_type;
	}

	public void setBody_type(Integer body_type) {
		this.body_type = body_type;
	}

	public String getGoods_name() {
		return goods_name == null ? "" : goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getCar_long() {
		return car_long == null ? "" : car_long;
	}

	public void setCar_long(String car_long) {
		this.car_long = car_long;
	}

	public String getCar_width() {
		return car_width == null ? "" : car_width;
	}

	public void setCar_width(String car_width) {
		this.car_width = car_width;
	}

	public String getCompany_name() {
		return company_name == null ? "" : company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeInt(this.body_type);
		dest.writeInt(this.company_id);
		dest.writeInt(this.flag);
		dest.writeInt(this.goodsId);
		dest.writeString(this.car_long);
		dest.writeString(this.car_width);
		dest.writeString(this.company_name);
		dest.writeString(this.contact);
		dest.writeString(this.createtime);
		dest.writeString(this.detail);
		dest.writeString(this.end_address);
		dest.writeString(this.goods_name);
		dest.writeString(this.issue_date);
		dest.writeString(this.loading_date);
		dest.writeString(this.memo);
		dest.writeString(this.start_address);
		dest.writeString(this.tel);
		dest.writeString(this.valid_date);
	}
	
	public Goods() {}
	
	public Goods(Parcel in) {
		id = in.readInt();
		body_type = in.readInt();
		company_id = in.readInt();
		flag = in.readInt();
		goodsId = in.readInt();
		car_long = in.readString();
		car_width = in.readString();
		company_name = in.readString();
		contact = in.readString();
		createtime = in.readString();
		detail = in.readString();
		end_address = in.readString();
		goods_name = in.readString();
		issue_date = in.readString();
		loading_date = in.readString();
		memo = in.readString();
		start_address = in.readString();
		tel = in.readString();
		valid_date = in.readString();
	}
	
	public static final Parcelable.Creator<Goods> CREATOR = new Parcelable.Creator<Goods>(){

		@Override
		public Goods createFromParcel(Parcel source) {
			return new Goods(source);
		}

		@Override
		public Goods[] newArray(int size) {
			return new Goods[size];
		}
		
	};

}

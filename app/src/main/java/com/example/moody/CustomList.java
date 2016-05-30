package com.example.moody;

public class CustomList {
	private int IconID;
	private String locationdatetime;
	private String msg;
	
	public CustomList(int IconID,String locationdatetime,String msg) {
		super();
		this.IconID=IconID;
		this.locationdatetime=locationdatetime;
		this.msg=msg;
	}

	public int getIconID() {
		return IconID;
	}

	public String getLocationdatetime() {
		return locationdatetime;
	}

	public String getMsg() {
		return msg;
	}
}

package com.furgo.assesment.bean;

public class LocationBean {

	private int id;
	private String locationName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public LocationBean(int id, String locationName) {
		super();
		this.id = id;
		this.locationName = locationName;
	}
}

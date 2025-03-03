package com.furgo.assesment.bean;

import java.time.LocalDate;

import com.furgo.assesment.entity.LocationEntity;

public class OgsBean {

	private LocationEntity location;
	private LocalDate dateCollected;
	private double unitWeight;
	private double waterContent;
	private double shearStrength;

	public LocationEntity getLocation() {
		return location;
	}

	public void setLocation(LocationEntity location) {
		this.location = location;
	}

	public LocalDate getDateCollected() {
		return dateCollected;
	}

	public void setDateCollected(LocalDate dateCollected) {
		this.dateCollected = dateCollected;
	}

	public double getUnitWeight() {
		return unitWeight;
	}

	public void setUnitWeight(double unitWeight) {
		this.unitWeight = unitWeight;
	}

	public double getWaterContent() {
		return waterContent;
	}

	public void setWaterConent(double waterContent) {
		this.waterContent = waterContent;
	}

	public double getShearStrength() {
		return shearStrength;
	}

	public void setShearStrength(double shearStrength) {
		this.shearStrength = shearStrength;
	}

}

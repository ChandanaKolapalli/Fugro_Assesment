package com.furgo.frontend.rcp.model;

import java.time.LocalDate;

public class Sample {

	private int id;
	private Location location;
	private LocalDate dateCollected;
	private Double unitWeight;
	private Double waterContent;
	private Double shearStrength;
	
	
	public int getId() {
		return id;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public LocalDate getDateCollected() {
		return dateCollected;
	}
	public void setDateCollected(LocalDate dateCollected) {
		this.dateCollected = dateCollected;
	}
	public Double getUnitWeight() {
		return unitWeight;
	}
	public void setUnitWeight(Double unitWeight) {
		this.unitWeight = unitWeight;
	}
	public Double getWaterContent() {
		return waterContent;
	}
	public void setWaterContent(Double waterContent) {
		this.waterContent = waterContent;
	}
	public Double getShearStrength() {
		return shearStrength;
	}
	public void setShearStrength(Double shearStrength) {
		this.shearStrength = shearStrength;
	}
	public Sample( Location location, LocalDate dateCollected, Double unitWeight, Double waterContent,
			Double shearStrength) {
		super();
		//this.id = id;
		this.location = location;
		this.dateCollected = dateCollected;
		this.unitWeight = unitWeight;
		this.waterContent = waterContent;
		this.shearStrength = shearStrength;
	}
	@Override
	public String toString() {
		return "Sample [id=" + id + ", location=" + location + ", dateCollected=" + dateCollected + ", unitWeight="
				+ unitWeight + ", waterContent=" + waterContent + ", shearStrength=" + shearStrength + "]";
	}
	
	
	
}

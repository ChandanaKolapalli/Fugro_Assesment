package com.furgo.assesment.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name="Sample")
@Table(name="Sample")
public class OgsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private LocalDate dateCollected;
	private double unitWeight;
	
	@Column(name="water_content")
	private double waterContent;
	private double shearStrength;
	
	@ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public void setWaterContent(double waterContent) {
		this.waterContent = waterContent;
	}

	public double getShearStrength() {
		return shearStrength;
	}

	public void setShearStrength(double shearStrength) {
		this.shearStrength = shearStrength;
	}

	public LocationEntity getLocation() {
		return location;
	}

	public void setLocation(LocationEntity location) {
		this.location = location;
	}

	public OgsEntity(int id, LocalDate dateCollected, double unitWeight, double waterContent, double shearStrength,
			LocationEntity location) {
		super();
		this.id = id;
		this.dateCollected = dateCollected;
		this.unitWeight = unitWeight;
		this.waterContent = waterContent;
		this.shearStrength = shearStrength;
		this.location = location;
	}
	
	public OgsEntity() {
    }
	
	
	
}

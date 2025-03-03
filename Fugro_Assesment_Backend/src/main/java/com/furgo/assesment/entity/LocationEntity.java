package com.furgo.assesment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Locations")
public class LocationEntity {

	@Id
	@Column(name="Location_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name="Location_Name")
	private String name;
	
	//No args Constructor
	public LocationEntity() {}

    public LocationEntity(String name) {
        this.name = name;
    }
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public LocationEntity(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
}

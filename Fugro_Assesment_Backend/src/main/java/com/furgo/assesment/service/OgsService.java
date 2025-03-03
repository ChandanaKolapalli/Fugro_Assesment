package com.furgo.assesment.service;

import java.util.List;


import com.furgo.assesment.entity.LocationEntity;
import com.furgo.assesment.entity.OgsEntity;


public interface OgsService {

	public OgsEntity createData(OgsEntity ogsentity);
	public OgsEntity updateData(int id,OgsEntity bean);
	public void deleteData(int id);
	public Double avgOfWaterContent();
	public List<OgsEntity> getThresholdData(Double weightThreshold,Double waterContentThreshold,Double shearStrengthThreshold);
	public List<OgsEntity>getAllData();
	public List<LocationEntity>getAllLocations();
}

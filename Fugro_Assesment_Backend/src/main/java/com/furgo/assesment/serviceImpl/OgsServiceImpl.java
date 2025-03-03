package com.furgo.assesment.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furgo.assesment.bean.OgsBean;
import com.furgo.assesment.entity.LocationEntity;
import com.furgo.assesment.entity.OgsEntity;
import com.furgo.assesment.respository.LocationRepository;
import com.furgo.assesment.respository.OgsRepository;
import com.furgo.assesment.service.OgsService;

@Service
public class OgsServiceImpl implements OgsService {
	
	@Autowired
	public OgsRepository repo;
	@Autowired
	public LocationRepository LocRepo;
	
	@Override
	public List<LocationEntity> getAllLocations() {		
		return LocRepo.findAll();
	}
	
	@Override
	public List<OgsEntity> getAllData() {
		List<OgsEntity> all = repo.findAll();
		return all;
	}
	
	@Override
	public OgsEntity createData(OgsEntity ogsentity) {		
		return repo.save(ogsentity);
	}

	@Override
	public OgsEntity updateData(int id,OgsEntity bean) {
		Optional<OgsEntity> dataById = repo.findById(id);
		if(dataById.isPresent()) {
			return repo.save(bean);
		}else {
			throw new RuntimeException("Data not found");
		}		
	}

	@Override
	public void deleteData(int id) {
		System.out.println("delete id " + id);
		repo.deleteById(id);		
	}
	
	@Override
	public Double avgOfWaterContent() {
		return repo.findAvgWaterContent();
	}

	@Override
	public List<OgsEntity> getThresholdData(Double weightThreshold, Double waterContentThreshold,
			Double shearStrengthThreshold) {		
		return repo.findByUnitWeightGreaterThanOrWaterContentGreaterThanOrShearStrengthGreaterThan(weightThreshold,waterContentThreshold,
				shearStrengthThreshold);
	}

	

}

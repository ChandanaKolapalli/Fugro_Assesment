package com.furgo.assesment.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.furgo.assesment.entity.OgsEntity;

@Repository
public interface OgsRepository extends JpaRepository<OgsEntity, Integer> {
	@Query("select avg(s.waterContent) from Sample s")
	public Double findAvgWaterContent();
	
	public List<OgsEntity> findByUnitWeightGreaterThanOrWaterContentGreaterThanOrShearStrengthGreaterThan(
	        double unitWeight, double waterContent, double shearStrength);
	
}

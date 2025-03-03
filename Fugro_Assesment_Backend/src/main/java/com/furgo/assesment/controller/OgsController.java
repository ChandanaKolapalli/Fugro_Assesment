package com.furgo.assesment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.furgo.assesment.bean.OgsBean;
import com.furgo.assesment.entity.LocationEntity;
import com.furgo.assesment.entity.OgsEntity;
import com.furgo.assesment.service.OgsService;

@RestController
@RequestMapping(value="/samples")
public class OgsController {
	
	@Value("${spring.furgo.ogs.WeightThreshold}")
	Double weightThreshold;
	
	@Value("${spring.furgo.ogs.WaterContentThreshold}")
	Double waterContentThreshold;
	
	@Value("${spring.furgo.ogs.ShearStrengthThreshold}")
	Double shearStrengthThreshold;
	
	@Autowired
	private OgsService service;
	
	//To List All the Data
	@GetMapping(value="/list")
	public List<OgsEntity> getAllData(){
		System.out.println("test");
		return service.getAllData();
	}
		
	//To Add New Sample
	@PostMapping(value = "/add")
	public OgsEntity createData(@RequestBody OgsEntity ogsentity) {
		return service.createData(ogsentity);		
	}
	
	//To edit the existing Sample
	@PutMapping("/update/{id}")
	public ResponseEntity<OgsEntity> editData(@PathVariable int id, @RequestBody OgsEntity bean) {
		OgsEntity updateData = service.updateData(id,bean);
		return ResponseEntity.ok(updateData);
	}
	
	//To delete Sample
	@DeleteMapping("/delete/{id}")
	public void deleteData(@PathVariable int id) {
		 service.deleteData(id);
	}
	
	//To get All locations
	@GetMapping(value="/allLocations")
	public List<LocationEntity>getAllLocations(){
		return service.getAllLocations();
	}
	
	//To get Average Water Content
	@GetMapping(value ="/avgwatercontent")
	public Double getAvgWaterContent() {
		return service.avgOfWaterContent();
	}
	
	//To get Samples Surpassing Threshold Values
	@GetMapping(value="/thresholdvalues")
	public List<OgsEntity> getThresholdValues(){		
		return service.getThresholdData(weightThreshold, waterContentThreshold, shearStrengthThreshold);
	}
	
}

package com.furgo.assesment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.furgo.assesment.entity.LocationEntity;
import com.furgo.assesment.entity.OgsEntity;
import com.furgo.assesment.respository.OgsRepository;
import com.furgo.assesment.serviceImpl.OgsServiceImpl;

@SpringBootTest
public class FugroAssesmentApplicationTests {
	
	@Mock
    private OgsRepository repo;

    @InjectMocks
    private OgsServiceImpl serviceImpl;

    @Test
    public void testSaveOgsSample() {
    	OgsEntity entity =new OgsEntity(1, LocalDate.now(), 20.5, 10.0, 500.0,new LocationEntity(1,"Amsterdam"));
        when(repo.save(any(OgsEntity.class))).thenReturn(entity);
        OgsEntity savedSample = serviceImpl.createData(entity);
        assertNotNull(savedSample);
        assertEquals("Amsterdam", savedSample.getLocation().getName());
    }
    
    @Test
    public void testUpdateOgsSample() {
    	OgsEntity oldEntity =new OgsEntity(1, LocalDate.now(), 20.5, 10.0, 500.0,new LocationEntity(1,"Amsterdam"));
    	OgsEntity newEntity =new OgsEntity(1, LocalDate.now(), 20.5, 10.0, 500.0,new LocationEntity(1,"Eindhoven"));

        when(repo.findById(1)).thenReturn(Optional.of(oldEntity));
        when(repo.save(any(OgsEntity.class))).thenReturn(newEntity);

        OgsEntity result = serviceImpl.updateData(1, newEntity);
        assertNotNull(result);
        assertEquals("Eindhoven", result.getLocation().getName());
    }
    
    @Test
    public void testDeleteOgsSample() {
        int sampleId = 1;
        doNothing().when(repo).deleteById(sampleId);
        serviceImpl.deleteData(sampleId);
        verify(repo, times(1)).deleteById(sampleId);
    }
    
    @Test
    public void testAvgOfWaterContent() {
       /* List<OgsEntity> samples = Arrays.asList(
            new OgsEntity(1, LocalDate.now(), 20.0, 15.0, 500.0,new LocationEntity(1,"Amsterdam")),
            new OgsEntity(2, LocalDate.now(), 40.0, 45.0, 500.0,new LocationEntity(2,"Eindhoven"))
        );
*/
        when(repo.findAvgWaterContent()).thenReturn(30.0);
        double avg = serviceImpl.avgOfWaterContent();
        assertEquals(30.0, avg);
    }
    
    @Test
    public void testFindOgsSamplesAboveThreshold() {
    	OgsEntity sample1 = new OgsEntity(1, LocalDate.now(), 20.0, 15.0, 500.0,new LocationEntity(1,"Amsterdam"));
    	OgsEntity sample2 = new OgsEntity(2, LocalDate.now(), 40.0, 45.0, 500.0,new LocationEntity(2,"Eindhoven"));
        repo.save(sample1);
        repo.save(sample2);

        List<OgsEntity> highWaterContentSamples = repo.findByUnitWeightGreaterThanOrWaterContentGreaterThanOrShearStrengthGreaterThan(10.0,10.0,10.0);
        assertEquals(2, highWaterContentSamples.size());
    }

    @Test
    public void testGetAllOgsSamples() {
    	List<OgsEntity> samples = Arrays.asList(
                new OgsEntity(1, LocalDate.now(), 20.0, 15.0, 500.0,new LocationEntity(1,"Amsterdam")),
                new OgsEntity(2, LocalDate.now(), 40.0, 45.0, 500.0,new LocationEntity(2,"Eindhoven"))
            );

        when(repo.findAll()).thenReturn(samples);

        List<OgsEntity> result = serviceImpl.getAllData();
        assertEquals(2, result.size());
    }
	

}

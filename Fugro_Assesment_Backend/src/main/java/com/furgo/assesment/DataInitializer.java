package com.furgo.assesment;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.furgo.assesment.entity.LocationEntity;
import com.furgo.assesment.respository.LocationRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public void run(String... args) {
        List<String> defaultLocations = Arrays.asList("Amsterdam", "Eindhoven", "Utrecht", "Rotterdam", "Maastricht", "Den Haag", "Delft");

        for (String loc : defaultLocations) {
            locationRepository.findByName(loc)
                .orElseGet(() -> locationRepository.save(new LocationEntity(loc)));
        }

        System.out.println("Default locations initialized.");
    }
}

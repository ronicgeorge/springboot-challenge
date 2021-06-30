package com.de.location.service;

import com.de.location.dao.Location;
import com.de.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Ronic George
 */

@Service
public class LocationService {

    private LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getLocations() {
        return locationRepository.findAll();
    }

    public Location create(Location location) {
        return locationRepository.save(location);
    }

    public void deleteLocation(String locationID) {
        Long locationIDValue = Long.valueOf(locationID);
        locationRepository.findById(locationIDValue);
        locationRepository.deleteById(Long.valueOf(locationID));
    }

    public Optional<Location> update(Location location) {
        Optional<Location> locationById = locationRepository.findById(location.getLocationId());
        if (locationById.isPresent()) {
            location.setCreatedAt(locationById.get().getCreatedAt());
            locationRepository.save(location);
        }
        return locationById;
    }

}

package com.de.location.controller;

import com.de.location.dao.Location;
import com.de.location.resource.LocationResource;
import com.de.location.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ronic George
 */

@RestController
@RequestMapping(path = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class LocationController {

    private LocationService locationService;

    private final MapperFacade locationMapper;

    @Autowired
    public LocationController(LocationService locationService, MapperFacade locationMapper) {
        this.locationService = locationService;
        this.locationMapper = locationMapper;
    }

    @GetMapping(path = "/list", headers = "Accept=application/json")
    @ResponseBody
    public List<LocationResource> getLocations() {
        List<Location> locations = locationService.getLocations();
        return locations.stream().map(item -> locationMapper.map(item, LocationResource.class)).collect(Collectors.toList());
    }

    @PostMapping(headers = "Accept=application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Void> createLocation(@Valid @RequestBody LocationResource locationResource) {
        locationService.create(locationMapper.map(locationResource, Location.class));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{locationId}", headers = "Accept=application/json")
    public ResponseEntity<Void> deleteLocation(@NotEmpty @PathVariable String locationId) {
        try {
            locationService.deleteLocation(locationId);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Requested location not found for locationId: " + locationId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(headers = "Accept=application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateLocation(@Valid @RequestBody LocationResource locationResource) {
        if (locationResource.getLocationName() == null || locationResource.getLocationName().isEmpty()) {
            log.debug("Cannot update location. Location id must be provided");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Location> updatedLocation = locationService.update(locationMapper.map(locationResource, Location.class));
        return updatedLocation.isPresent() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

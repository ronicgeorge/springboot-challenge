package com.de.location.service;

import com.de.location.dao.Address;
import com.de.location.dao.Location;
import com.de.location.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
class LocationServiceTest {

    @Mock
    LocationRepository locationRepository;

    LocationService sut;

    @Test
    public void shouldGetLocations() {
        //Given
        sut = new LocationService(locationRepository);

        Location location1 = new Location().setAddress(
                new Address().setCity("city1").setStreet("street1").setZipCode("zipCode1"));
        Location location2 = new Location().setAddress(
                new Address().setCity("city2").setStreet("street2").setZipCode("zipCode2"));
        List<Location> locationList = Arrays.asList(location1, location2);
        when(locationRepository.findAll()).thenReturn(locationList);
        //When
        List<Location> fetchedLocationList = sut.getLocations();
        //Then
        verify(locationRepository, times(1)).findAll();
        assertThat(fetchedLocationList.size(),is(2));
        assertThat(fetchedLocationList.get(0),is(location1));
    }

    @Test
    public void shouldCreateLocation() {
        //Given
        sut = new LocationService(locationRepository);
        Location location = new Location().setAddress(
                new Address().setCity("city1").setStreet("street1").setZipCode("zipCode1"));
        //When
        sut.create(location);
        //Then
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    public void shouldDeleteLocation() {
        //Given

        sut = new LocationService(locationRepository);
        Location location = new Location().setLocationId(1L).setAddress(
                new Address().setCity("city1").setStreet("street1").setZipCode("zipCode1"));
        when(locationRepository.findById(location.getLocationId())).thenReturn(Optional.of(location));
        //When
        sut.deleteLocation("1");
        //Then
        verify(locationRepository, times(1)).deleteById(location.getLocationId());
    }

    @Test
    public void shouldThrowExceptionOnNoResourceForDelete() {
        //Given
        Location location = new Location().setLocationId(1L).setAddress(
                new Address().setCity("city1").setStreet("street1").setZipCode("zipCode1"));
        sut = new LocationService(locationRepository);
        when(locationRepository.findById(location.getLocationId())).thenThrow(EmptyResultDataAccessException.class);
        //When
        //Then
        assertThrows(EmptyResultDataAccessException.class,
                () -> sut.deleteLocation("1"));
    }

    @Test
    public void shouldUpdateLocation() {
        //Given
        sut = new LocationService(locationRepository);
        Location location = new Location().setLocationId(1L).setAddress(
                new Address().setCity("city1").setStreet("street1").setZipCode("zipCode1"));
        Location updatedLocation = location;
        updatedLocation.getAddress().setStreet("street2");
        when(locationRepository.findById(location.getLocationId())).thenReturn(Optional.of(location));
        when(locationRepository.save(location)).thenReturn(updatedLocation);
        //When
        Optional<Location> resultLocation = sut.update(location);
        //Then
        verify(locationRepository, times(1)).save(location);
        assertThat(resultLocation.get(),is(updatedLocation));
    }

}

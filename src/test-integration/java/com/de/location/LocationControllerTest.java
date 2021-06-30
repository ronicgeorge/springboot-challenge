package com.de.location;

import com.de.location.dao.Address;
import com.de.location.dao.GeoLocation;
import com.de.location.dao.Location;
import com.de.location.repository.LocationRepository;
import com.de.location.resource.LocationResource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.glasnost.orika.MapperFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Ronic George
 */

@RunWith(SpringRunner.class)
@ActiveProfiles("integration")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationControllerTest {

    @Autowired
    protected WebApplicationContext applicationContext;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    MapperFacade locationMapper;

    protected MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(applicationContext).build();
        locationRepository.deleteAll();
    }

    @Test
    public void shouldCreateAndGetLocation() throws Exception {
        //CREATE
        assertThat(locationRepository.findAll().size(), is(0));
        mockMvc.perform(
                post("/location")
                        .content(objectMapper.writeValueAsString(new LocationResource().setCity("city1").setStreet("street1")
                                .setZipCode("zipcode1").setX(BigDecimal.valueOf(1.12345678)).setY(BigDecimal.valueOf(2.12345678))))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertThat(locationRepository.findAll().size(), is(1));
        //GET
        List<LocationResource> savedlocationList = objectMapper.readValue(mockMvc.perform(
                get("/location/list")
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<LocationResource>>() {
        });
        assertThat(savedlocationList.size(), is(1));
        assertThat(savedlocationList.get(0).getCity(), is("city1"));
        assertThat(savedlocationList.get(0).getX(), is(BigDecimal.valueOf(1.12345678)));
    }

    @Test
    public void shouldUpdateLocation() throws Exception {
        Location location = saveLocation();
        LocationResource locationResourceToUpdate = locationMapper.map(location, LocationResource.class);
        locationResourceToUpdate.setCity("city2");
        mockMvc.perform(
                put("/location").content(objectMapper.writeValueAsString(locationResourceToUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        List<LocationResource> updatedLocationResourceList = objectMapper.readValue(mockMvc.perform(
                get("/location/list")
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<LocationResource>>() {
        });
        assertThat(updatedLocationResourceList.size(), is(1));
        assertThat(updatedLocationResourceList.get(0).getLocationName(), is(locationResourceToUpdate.getLocationName()));
        assertThat(updatedLocationResourceList.get(0).getCity(), is(locationResourceToUpdate.getCity()));
    }

    @Test
    public void shouldDeleteLocation() throws Exception {
        Location savedLocation = saveLocation();
        assertThat(locationRepository.findAll().size(), is(1));
        mockMvc.perform(
                delete("/location/" + savedLocation.getLocationId()))
                .andExpect(status().isNoContent());
        assertThat(locationRepository.findAll().size(), is(0));
    }

    @Test
    public void shouldShowValidationError() throws Exception {
        String errorResponse = mockMvc.perform(
                post("/location")
                        .content(objectMapper.writeValueAsString(new LocationResource()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.length()", is(5))).andReturn().getResponse().getContentAsString();
        assertThat(errorResponse.contains("ZipCode must not be empty"), is(true));
        assertThat(errorResponse.contains("City must not be empty"), is(true));
        assertThat(errorResponse.contains("Street must not be empty"), is(true));
        assertThat(errorResponse.contains("X value must not be empty"), is(true));
        assertThat(errorResponse.contains("Y value must not be empty"), is(true));
    }

    private Location saveLocation() {
        return locationRepository.save(new Location()
                .setAddress(new Address().setCity("city1").setStreet("street1").setZipCode("zipCode1"))
                .setGeoLocation(new GeoLocation().setX(BigDecimal.valueOf(1.12345678)).setY(BigDecimal.valueOf(2.12345678))));
    }

}

package com.de.location.resource;

import com.de.location.dao.Location;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * @author Ronic George
 */

@Component
public class ResourceMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Location.class, LocationResource.class)
                .field("address.street", "street")
                .field("address.city", "city")
                .field("address.zipCode", "zipCode")
                .field("geoLocation.x", "x")
                .field("geoLocation.y", "y")
                .field("locationId", "locationName")
                .byDefault().register();
        factory.classMap(LocationResource.class, Location.class)
                .field("street", "address.street")
                .field("city", "address.city")
                .field("zipCode", "address.zipCode")
                .field("x", "geoLocation.x")
                .field("y", "geoLocation.y")
                .field("locationName", "locationId")
                .byDefault().register();
    }

}

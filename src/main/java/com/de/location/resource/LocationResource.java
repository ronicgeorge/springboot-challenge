package com.de.location.resource;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Ronic George
 */

@Data
@Accessors(chain = true)
public class LocationResource {

    private String locationName;

    @NotEmpty(message = "Street must not be empty")
    private String street;

    @NotEmpty(message = "City must not be empty")
    private String city;

    @NotEmpty(message = "ZipCode must not be empty")
    private String zipCode;

    @NotNull(message = "X value must not be empty")
    private BigDecimal x;

    @NotNull(message = "Y value must not be empty")
    private BigDecimal y;

}

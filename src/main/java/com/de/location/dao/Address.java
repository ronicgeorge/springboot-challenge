package com.de.location.dao;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;

/**
 * @author Ronic George
 */

@Embeddable
@EntityListeners(AuditingEntityListener.class)
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class Address {

    @Column
    private String street;

    @Column
    private String city;

    @Column
    private String zipCode;

}

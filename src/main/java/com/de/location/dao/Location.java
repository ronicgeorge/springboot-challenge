package com.de.location.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @author Ronic George
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
public class Location {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long locationId;

    @Embedded
    private Address address;

    @Embedded
    private GeoLocation geoLocation;

    @Column
    @CreatedDate
    @JsonIgnore
    private long createdAt;

    @Column
    @LastModifiedDate
    @JsonIgnore
    private long modifiedAt;

}

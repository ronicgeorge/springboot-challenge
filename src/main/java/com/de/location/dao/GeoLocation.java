package com.de.location.dao;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;
import java.math.BigDecimal;

/**
 * @author Ronic George
 */

@Embeddable
@EntityListeners(AuditingEntityListener.class)
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class GeoLocation {

    @Column
    private BigDecimal x;

    @Column
    private BigDecimal y;

}

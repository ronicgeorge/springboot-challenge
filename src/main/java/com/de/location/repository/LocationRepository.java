package com.de.location.repository;

import com.de.location.dao.Location;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Ronic George
 */

public interface LocationRepository extends JpaRepository<Location, Long> {
}

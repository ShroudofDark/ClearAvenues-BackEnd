package edu.odu.clearavenues.prototype.accident;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AccidentRepository extends CrudRepository<Accident, Integer> {

    Accident findByAccidentId(int accidentId);

    Iterable<Accident> findByLocationId(int locationId);


    @Query(value = "SELECT count(*) FROM accidents WHERE accident_time >= date_sub(curdate(), interval 6 day) AND location_id = :locationId", nativeQuery = true)
     int getLast7DayAccidentCount(@Param("locationId") int locationId);
}

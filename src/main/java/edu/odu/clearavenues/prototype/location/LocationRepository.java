package edu.odu.clearavenues.prototype.location;

import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Integer> {

    Location findByLocationId(int id);
}
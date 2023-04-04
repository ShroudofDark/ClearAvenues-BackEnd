package edu.odu.clearavenues.prototype.locations;

import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Integer> {

    Location findByLocationId(int id);
}
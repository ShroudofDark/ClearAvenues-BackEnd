package edu.odu.clearavenues.prototype.accident;

import org.springframework.data.repository.CrudRepository;

public interface AccidentRepository extends CrudRepository<Accident, Integer> {

    Accident findByAccidentId(int accidentId);

    Accident findByLocationId(int locationId);
}

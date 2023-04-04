package edu.odu.clearavenues.prototype.organization;

import org.springframework.data.repository.CrudRepository;

public interface OrganizationRepository extends CrudRepository<Organization, Integer> {

    Organization findByOrgName(String name);

}
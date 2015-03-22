package ksbysample.webapp.basic.service;

import ksbysample.webapp.basic.domain.Country;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, String>, JpaSpecificationExecutor<Country> {
}

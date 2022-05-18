package com.codegym.final_examcity.repo;

import com.codegym.final_examcity.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICountryRepo extends CrudRepository<Country, Long> {

}

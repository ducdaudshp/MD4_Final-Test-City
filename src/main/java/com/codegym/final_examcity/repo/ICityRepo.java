package com.codegym.final_examcity.repo;

import com.codegym.final_examcity.model.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICityRepo extends CrudRepository<City, Long> {
}

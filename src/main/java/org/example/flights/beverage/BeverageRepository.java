package org.example.flights.beverage;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BeverageRepository extends CrudRepository<Beverage, Long> {
    @Query("SELECT * FROM beverage WHERE type = :beverageType LIMIT 1")
    Beverage findByType(String beverageType);
}

package org.example.flights.beverage;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BeverageTypeRepository extends CrudRepository<Beverage.Type, String> {

}

package org.example.flights.flight;

import org.example.flights.catering.UnusedItem;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface FlightRepository extends CrudRepository<Flight, Long> {

    @Query("""
            SELECT mtsc.*, b.qty_in_unit AS servings_quantity
            FROM (SELECT pm.beverage_type AS beverage_type, COUNT(beverage_type) AS quantity
                  FROM passenger p
                           INNER JOIN passenger_beverage_preorder pm ON p.ticket_no = pm.ticket_no
                  WHERE p.flight_id = :flightId
                  GROUP BY pm.beverage_type
            ) AS mtsc LEFT JOIN beverage b ON mtsc.beverage_type = b.type;
            """)
    Set<Flight.LoadedBeverage> findBeveragesToLoadByFlightId(@Param("flightId") Long flightId);

    @Query("""
            SELECT pm.meal_type AS meal_type, COUNT(meal_type) AS quantity
            FROM passenger p
                     INNER JOIN passenger_meal_preorder pm ON p.ticket_no = pm.ticket_no
            WHERE p.flight_id = :flightId
            GROUP BY pm.meal_type;
            """)
    Set<Flight.LoadedMeal> findMealsToLoadByFlightId(@Param("flightId") Long flightId);

    @Query("""
            SELECT mtsc.*, b.qty_in_unit AS quantity_in_unit, lb.quantity AS loaded_quantity
            FROM (SELECT pm.beverage_type AS name, COUNT(beverage_type) AS served_quantity
                  FROM passenger p
                           INNER JOIN passenger_beverage_preorder pm ON p.ticket_no = pm.ticket_no
                  WHERE p.flight_id = :flightId
                  GROUP BY pm.beverage_type) AS mtsc
                    LEFT JOIN beverage b ON mtsc.name = b.type
                    LEFT JOIN loaded_beverages lb ON lb.beverage_type = mtsc.name
            """)
    Set<UnusedItem> mapPreorderedAndLoadedBeverages(@Param("flightId") Long flightId);

    @Query("""
            SELECT mtsc.*, lm.quantity AS loaded_quantity
            FROM (SELECT pm.meal_type AS name, COUNT(pm.meal_type) AS served_quantity
                  FROM passenger p
                           INNER JOIN passenger_meal_preorder pm ON p.ticket_no = pm.ticket_no
                  WHERE p.flight_id = :flightId
                  GROUP BY pm.meal_type)
            AS mtsc
                     LEFT JOIN meal m ON mtsc.name = m.type
                     LEFT JOIN loaded_meals lm ON lm.meal_type = mtsc.name
            """)
    Set<UnusedItem> mapPreorderedAndLoadedMeals(@Param("flightId") Long flightId);
}

package org.example.flights.passenger;

import org.example.flights.flight.Flight;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepository extends CrudRepository<Passenger, String> {

    @Query("""
    SELECT * FROM passenger p
    WHERE flight_id = :flightId
    """)
    List<Passenger> findAllByFlightId(Long flightId);
}

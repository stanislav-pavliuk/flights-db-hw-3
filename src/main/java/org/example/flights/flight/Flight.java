package org.example.flights.flight;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("flight")
public class Flight {

    @Id
    private long      id;
    @Column("no")
    private String    no;
    @Column("departure_date")
    private LocalDate departureDate;
    @Column("departure_time")
    private LocalTime departureTime;
    @Column("check_in_time")
    private LocalTime checkInTime;
    @Column("arrival_date")
    private LocalDate arrivalDate;
    @Column("arrival_time")
    private LocalTime arrivalTime;
    @Column("from_airport_code")
    private String    fromAirportCode;
    @Column("to_airport_code")
    private String    toAirportCode;
    @Column("airline_code")
    private String    airlineCode;
    @Column("aircraft_number")
    private String    aircraftNumber;
    @MappedCollection(idColumn = "flight_id")
    private Set<LoadedMeal> loadedMeals;
    @MappedCollection(idColumn = "flight_id")
    private Set<LoadedBeverage> loadedBeverages;

    @Data
    @Table("loaded_meals")
    public static class LoadedMeal {
        private String mealType;
        private Integer quantity;
    }

    @Data
    @Table("loaded_beverages")
    public static class LoadedBeverage {
        private String beverageType;
        private Integer quantity;
        private Integer servingsQuantity;
    }

    public Duration getFlightDuration() {
        return Duration.between(departureDate.atTime(departureTime), arrivalDate.atTime(arrivalTime));
    }
}

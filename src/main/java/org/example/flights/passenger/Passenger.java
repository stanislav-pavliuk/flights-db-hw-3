package org.example.flights.passenger;

import lombok.Data;
import org.example.flights.passenger.preorder.PassengerBeverage;
import org.example.flights.passenger.preorder.PassengerMealType;
import org.example.flights.passenger.tclass.TravelClass;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Table("passenger")
public class Passenger {

    @Id
    @Column("ticket_no")
    private String ticketNo;

    @Column("flight_id")
    private Long flightId;

    @Column("full_name")
    private String fullName;

    @Column("seat_row")
    private Integer seatRow;

    @Column("seat_letter")
    private String seatLetter;

    /**
     * Never store credit card information in plain text. !!!!!!!!!!!!!
     * This is just an example.
     */
    @Column("credit_card_no")
    private String creditCardNo;

    @MappedCollection(idColumn = "ticket_no")
    private Set<PassengerMealType.Preordered> preorderedMeals;

    @MappedCollection(idColumn = "ticket_no")
    private Set<PassengerBeverage.Preordered> preorderedBeverages;

    @MappedCollection(idColumn = "ticket_no")
    private Set<PassengerMealType.ToBeServed> mealsToBeServed;

    @MappedCollection(idColumn = "ticket_no")
    private Set<PassengerBeverage.ToBeServed> beveragesToBeServed;


    public String formatPreorderedMeals() {
        return formatMeals(List.copyOf(preorderedMeals));
    }

    public String formatMealsToBeServed() {
        return formatMeals(List.copyOf(mealsToBeServed));
    }

    public String formatBeveragesToBeServed() {
        return formatBeverages(List.copyOf(beveragesToBeServed));
    }

    public String formatPreorderedBeverages() {
        return formatBeverages(List.copyOf(preorderedBeverages));
    }

    private String formatMeals(List<PassengerMealType> meals) {
        return meals.stream()
                .map(PassengerMealType::getMealType)
                .collect(Collectors.joining(", "));
    }

    private String formatBeverages(List<PassengerBeverage> beverages) {
        return beverages.stream()
                .map(PassengerBeverage::getBeverageType)
                .collect(Collectors.joining(", "));
    }

    public String getCreditCardNo() {
        return creditCardNo.replaceAll("\\d(?=\\d{4})", "*");
    }

    public TravelClass getFlightClass() {
        return TravelClass.fromSeatRow(this.seatRow);
    }
}

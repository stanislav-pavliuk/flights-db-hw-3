package org.example.flights.passenger.preorder;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
public sealed class PassengerBeverage {

    private String beverageType;

    @Table("passenger_beverage_preorder")
    public static final class Preordered extends PassengerBeverage {
    }

    @Table("passenger_beverage_served")
    public static final class ToBeServed extends PassengerBeverage {
    }
}

package org.example.flights.passenger.preorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
public sealed class PassengerBeverage {

    protected String beverageType;

    @Table("passenger_beverage_preorder")
    public static final class Preordered extends PassengerBeverage {
        public Preordered(final String beverageType) {
            super(beverageType);
        }
    }

    @Table("passenger_beverage_served")
    public static final class ToBeServed extends PassengerBeverage {
        public ToBeServed(final String beverageType) {
            super(beverageType);
        }
    }
}

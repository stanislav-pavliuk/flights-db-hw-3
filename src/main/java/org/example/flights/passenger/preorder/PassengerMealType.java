package org.example.flights.passenger.preorder;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
public class PassengerMealType {

    @Column("meal_type")
    private String mealType;

    public PassengerMealType(final String mealType) {
        this.mealType = mealType;
    }

    @Table("passenger_meal_preorder")
    public static class Preordered extends PassengerMealType {
        public Preordered(final String mealType) {
            super(mealType);
        }
    }

    @Table("passenger_meal_served")
    public static class ToBeServed extends PassengerMealType {
        public ToBeServed(final String mealType) {
            super(mealType);
        }
    }
}

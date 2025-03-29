package org.example.flights.meal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("meal")
public record Meal(
        @Id @Column("id") Long id,
        @Column("name") String name,
        @Column("type") String type
) {
    @Table("meal_type")
    public record MealType(@Id String type) {
    }
}

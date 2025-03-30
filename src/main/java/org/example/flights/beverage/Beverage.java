package org.example.flights.beverage;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("beverage")
public record Beverage(@Id Long id, String name, String type, String unit, Integer qtyInUnit) {
    @Table("beverage_type")
    public record Type(@Id String type, @Column("h_o_c") HoC hoc) {
    }

    public enum HoC {
        H, C
    }
}

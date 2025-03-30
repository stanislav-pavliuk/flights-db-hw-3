package org.example.flights.catering;

import lombok.extern.java.Log;

import static java.lang.Math.floorDiv;

@Log
public record UnusedItem(String name, Long servedQuantity, Long loadedQuantity, Long quantityInUnit) {
    public UnusedItem {
        if (loadedQuantity == null) {
            loadedQuantity = 0L;
        }
        if (quantityInUnit == null) {
            quantityInUnit = 1L;
        }
    }

    public Long unusedUnits() {
        log.info("Calculating unused units for item: " + this);
        return floorDiv((long) loadedQuantity - servedQuantity,
                quantityInUnit);
    }
}

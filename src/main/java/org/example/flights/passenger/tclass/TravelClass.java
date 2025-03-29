package org.example.flights.passenger.tclass;

public enum TravelClass {
    F, B, E;

    public static TravelClass fromSeatRow(Integer seatRow) {
        return switch (seatRow) {
            case Integer r when r >= 11 -> E;
            case Integer r when r >= 6 -> B;
            case Integer r when r >= 1 -> F;
            default -> throw new IllegalArgumentException("Invalid seat row: " + seatRow);
        };
    }
}

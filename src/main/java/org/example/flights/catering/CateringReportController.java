package org.example.flights.catering;

import org.example.flights.flight.FlightRepository;
import org.example.flights.passenger.Passenger;
import org.example.flights.passenger.PassengerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/flight/{flightId}/catering/report")
public class CateringReportController {

    private final FlightRepository     flightRepository;
    private final PassengerRepository  passengerRepository;
    private final UnusedItemsProcessor unusedItemsProcessor;

    public CateringReportController(final FlightRepository flightRepository,
                                    final PassengerRepository passengerRepository,
                                    final UnusedItemsProcessor unusedItemsProcessor) {
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;
        this.unusedItemsProcessor = unusedItemsProcessor;
    }

    @GetMapping
    public String cateringReport(@PathVariable final Long flightId, Model model) {
        var flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found"));

        var passengers = passengerRepository
                .findAllByFlightId(flightId);

        model.addAttribute("flight", flight);
        model.addAttribute("passengersByClass", passengers.stream()
                .collect(Collectors.groupingBy(Passenger::getFlightClass)));

        model.addAttribute("unusedMeals", unusedItemsProcessor.findUnusedMeals(flight, passengers));
        model.addAttribute("unusedBeverages", unusedItemsProcessor.findUnusedBeverages(flight, passengers));

        return "catering/report";
    }
}

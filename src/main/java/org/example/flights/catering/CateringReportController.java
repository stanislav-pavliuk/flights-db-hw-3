package org.example.flights.catering;

import org.example.flights.flight.FlightRepository;
import org.example.flights.passenger.Passenger;
import org.example.flights.passenger.PassengerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/flight/{flightId}/catering/report")
public class CateringReportController {

    private final FlightRepository     flightRepository;
    private final PassengerRepository  passengerRepository;

    public CateringReportController(final FlightRepository flightRepository,
                                    final PassengerRepository passengerRepository) {
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;
    }

    @GetMapping
    public String cateringReport(@PathVariable final Long flightId, Model model) {
        var flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found"));

        var passengers = passengerRepository
                .findAllByFlightId(flightId);

        model.addAttribute("flight", flight);
        model.addAttribute("passengersByClass", passengers.stream()
                .collect(Collectors.groupingBy(Passenger::getFlightClass)));

        model.addAttribute("unusedMeals", flightRepository.mapPreorderedAndLoadedMeals(flightId));
        model.addAttribute("unusedBeverages", flightRepository.mapPreorderedAndLoadedBeverages(flightId));

        return "catering/report";
    }
}

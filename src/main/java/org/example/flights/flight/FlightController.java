package org.example.flights.flight;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/flight")
public class FlightController {

    private final FlightRepository flightRepository;

    public FlightController(final FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @GetMapping
    public String listAll(Model model) {
        var flights = flightRepository.findAll();

        model.addAttribute("flights", flights);

        return "flight/list";
    }

    @GetMapping("/{id}/meals/load")
    public String loadFlightPreview(@PathVariable Long id, Model model) {
        var flight = flightRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found."));

        var mealsToLoad = flightRepository.findMealsToLoadByFlightId(id);
        var beveragesToLoad = flightRepository.findBeveragesToLoadByFlightId(id);

        model.addAttribute("flight", flight);
        model.addAttribute("mealsToLoad", mealsToLoad);
        model.addAttribute("beveragesToLoad", beveragesToLoad);

        return "flight/load";
    }

    @GetMapping("/{id}/meals/load/confirm")
    public String loadFlight(@PathVariable Long id) {
        var flight = flightRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found."));

        var loadedMeals = flightRepository.findMealsToLoadByFlightId(id);
        var loadedBeverages = flightRepository.findBeveragesToLoadByFlightId(id);

        flight.setLoadedMeals(loadedMeals);
        flight.setLoadedBeverages(loadedBeverages);

        flightRepository.save(flight);

        return "redirect:/flight";
    }
}

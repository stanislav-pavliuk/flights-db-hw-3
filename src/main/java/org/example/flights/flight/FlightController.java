package org.example.flights.flight;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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


}

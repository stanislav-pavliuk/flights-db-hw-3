package org.example.flights.passenger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/flight/{flightId}/passenger")
public class PassengerController {

    private final PassengerRepository passengerRepository;

    public PassengerController(final PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @GetMapping
    public String listAll(@PathVariable Long flightId, Model model) {
        var passengers = passengerRepository.findAllByFlightId(flightId);

        model.addAttribute("passengers", passengers);

        return "passenger/list";
    }
}

package org.example.flights.passenger;

import org.example.flights.beverage.Beverage;
import org.example.flights.beverage.BeverageTypeRepository;
import org.example.flights.flight.FlightRepository;
import org.example.flights.meal.MealRepository;
import org.example.flights.meal.MealTypeRepository;
import org.example.flights.passenger.preorder.PassengerBeverage;
import org.example.flights.passenger.preorder.PassengerMealType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.BindParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class PassengerController {

    private final PassengerRepository    passengerRepository;
    private final MealTypeRepository     mealTypeRepository;
    private final BeverageTypeRepository beverageTypeRepository;
    private final FlightRepository flightRepository;

    public PassengerController(final PassengerRepository passengerRepository, final MealRepository mealRepository,
                               final MealTypeRepository mealTypeRepository,
                               final BeverageTypeRepository beverageTypeRepository, final FlightRepository flightRepository) {
        this.passengerRepository = passengerRepository;
        this.mealTypeRepository = mealTypeRepository;
        this.beverageTypeRepository = beverageTypeRepository;
        this.flightRepository = flightRepository;
    }

    @GetMapping("/flight/{flightId}/passenger")
    public String listAll(@PathVariable Long flightId, Model model) {
        var passengers = passengerRepository.findAllByFlightId(flightId);

        model.addAttribute("passengers", passengers);
        model.addAttribute("flightId", flightId);

        return "passenger/list";
    }

    @GetMapping("/passenger/{ticketNo}/edit")
    public String editPassengerMeal(@PathVariable String ticketNo,
                                    Model model) {
        var passenger = passengerRepository.findById(ticketNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ticket number:" + ticketNo));
        var mealTypes = mealTypeRepository.findAll();
        var beverageTypes = StreamSupport.stream(beverageTypeRepository.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Beverage.Type::hoc));


        model.addAttribute("passenger", passenger);
        model.addAttribute("mealTypes", mealTypes);
        model.addAttribute("beverageTypesH", beverageTypes.get(Beverage.HoC.H));
        model.addAttribute("beverageTypesC", beverageTypes.get(Beverage.HoC.C));

        return "passenger/edit";
    }

    @PostMapping("/passenger/{ticketNo}/update")
    public String updatePassengerPreorder(
            @PathVariable String ticketNo,
            @BindParam("preorderedMealType") String preorderedMealType,
            @BindParam("preorderedBeverageType") String[] preorderedBeverageType
    ) {
        var passenger = passengerRepository.findById(ticketNo).orElseThrow(
                () -> new IllegalArgumentException("Invalid ticket number:" + ticketNo));

        var mealType = mealTypeRepository.findById(preorderedMealType)
                .map(it -> Set.of(new PassengerMealType.Preordered(it.type())))
                .orElseGet(Set::of);

        var beverages = Arrays.stream(preorderedBeverageType)
                .map(beverageTypeRepository::findById)
                .flatMap(Optional::stream)
                .map(it -> new PassengerBeverage.Preordered(it.type()))
                .collect(Collectors.toSet());

        passenger.setPreorderedBeverages(beverages);
        passenger.setPreorderedMeals(mealType);
        passengerRepository.save(passenger);


        return "redirect:/flight/" + passenger.getFlightId() + "/passenger";
    }

    @GetMapping("/flight/{flightId}/passenger/create")
    public String createPassengerPage(@PathVariable Long flightId, Model model) {
        var flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found."));

        model.addAttribute("flight", flight);
        model.addAttribute("seatLetters", List.of("A", "B", "C", "D", "E", "F"));

        return "passenger/create";
    }
}

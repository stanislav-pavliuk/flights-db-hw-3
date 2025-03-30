package org.example.flights.flight;

import org.example.flights.beverage.BeverageRepository;
import org.example.flights.passenger.Passenger;
import org.example.flights.passenger.PassengerRepository;
import org.example.flights.passenger.preorder.PassengerBeverage;
import org.example.flights.passenger.preorder.PassengerMealType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/flight")
public class FlightController {

    private final FlightRepository    flightRepository;
    private final PassengerRepository passengerRepository;
    private final BeverageRepository  beverageRepository;

    public FlightController(final FlightRepository flightRepository, final PassengerRepository passengerRepository,
                            final BeverageRepository beverageRepository) {
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;
        this.beverageRepository = beverageRepository;
    }

    @GetMapping
    public String listAll(Model model) {
        var flights = flightRepository.findAll();

        model.addAttribute("flights", flights);

        return "flight/list";
    }

    @GetMapping("/{id}/meals/load")
    public String loadFlightPreview(@PathVariable Long id, Model model) {
        if (!flightRepository.existsById(id)) {
            return "redirect:/flight";
        }

        var flight = flightRepository.findById(id).orElseThrow();

        var passengers = passengerRepository.findAllByFlightId(id);

        var mealsToLoad = getMealsToLoad(passengers);
        var beveragesToLoad = getBeveragesToLoad(passengers);

        model.addAttribute("flight", flight);
        model.addAttribute("mealsToLoad", mealsToLoad);
        model.addAttribute("beveragesToLoad", beveragesToLoad);

        return "flight/load";
    }

    @GetMapping("/{id}/meals/load/confirm")
    public String loadFlight(@PathVariable Long id, Model model) {
        if (!flightRepository.existsById(id)) {
            return "redirect:/flight";
        }

        var flight = flightRepository.findById(id).orElseThrow();

        var passengers = passengerRepository.findAllByFlightId(id);

        var mealsToLoad = getMealsToLoad(passengers);
        var beveragesToLoad = getBeveragesToLoad(passengers);

        var loadedMeals = mealsToLoad.stream().map(it -> new Flight.LoadedMeal(
                it.item(),
                it.quantity()
        )).collect(Collectors.toSet());

        var loadedBeverages = beveragesToLoad.stream().map(it -> new Flight.LoadedBeverage(
                it.item(),
                it.quantity(),
                it.servingsQuantity()
        )).collect(Collectors.toSet());

        flight.setLoadedMeals(loadedMeals);
        flight.setLoadedBeverages(loadedBeverages);

        flightRepository.save(flight);

        return "redirect:/flight";
    }

    private List<LoadedItem> getBeveragesToLoad(final List<Passenger> passengers) {
        var beveragesPreordered = passengers.stream()
                .map(Passenger::getPreorderedBeverages)
                .flatMap(Set::stream)
                .map(PassengerBeverage.Preordered::getBeverageType)
                .collect(Collectors.groupingBy(
                        beverageType -> beverageType,
                        Collectors.counting()
                ));

        return beveragesPreordered
                .entrySet()
                .stream()
                .map(entry -> {
                    var beverageType = entry.getKey();
                    long servingsNeeded = entry.getValue();
                    var beverage = beverageRepository.findByType(beverageType);

                    var itemsCount = Math.ceilDiv(servingsNeeded, beverage.qtyInUnit());

                    return new LoadedItem(beverageType, itemsCount, beverage.qtyInUnit());
                })
                .toList();
    }

    private List<LoadedItem> getMealsToLoad(final List<Passenger> passengers) {
        var preorderedMeals = passengers.stream()
                .map(Passenger::getPreorderedMeals)
                .flatMap(Set::stream)
                .map(PassengerMealType.Preordered::getMealType)
                .collect(Collectors.groupingBy(
                        mealType -> mealType,
                        Collectors.counting()
                ));

        return preorderedMeals
                .entrySet()
                .stream()
                .map(entry -> {
                    var mealType = entry.getKey();
                    var servingsCount = entry.getValue();

                    return new LoadedItem(mealType, servingsCount, 1);
                })
                .toList();
    }
}

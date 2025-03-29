package org.example.flights.catering;

import org.example.flights.flight.Flight;
import org.example.flights.passenger.Passenger;
import org.example.flights.passenger.preorder.PassengerBeverage;
import org.example.flights.passenger.preorder.PassengerMealType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.floorDiv;

@Service
public class UnusedItemsProcessor {

    public List<UnusedItem> findUnusedMeals(final Flight flight, final List<Passenger> passenger) {
        var loadedMeals = flight.getLoadedMeals();

        var servedMealsCounts = passenger
                .stream()
                .flatMap(p -> p.getMealsToBeServed().stream())
                .map(PassengerMealType::getMealType)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        return loadedMeals
                .stream()
                .filter(loadedMeal -> {
                    var mealType = loadedMeal.getMealType();
                    var loadedQuantity = loadedMeal.getQuantity();
                    var servedQuantity = servedMealsCounts.getOrDefault(mealType, 0L);

                    return loadedQuantity > servedQuantity;
                })
                .map(loadedMeal -> {
                    var mealType = loadedMeal.getMealType();
                    var loadedQuantity = loadedMeal.getQuantity();
                    var servedQuantity = servedMealsCounts.getOrDefault(mealType, 0L);

                    return new UnusedItem(mealType, (long) loadedQuantity - servedQuantity);
                })
                .collect(Collectors.toList());

    }

    public List<UnusedItem> findUnusedBeverages(final Flight flight, final List<Passenger> passenger) {
        var loadedBeverages = flight.getLoadedBeverages();

        var servedBeveragesCounts = passenger
                .stream()
                .flatMap(p -> p.getBeveragesToBeServed().stream())
                .map(PassengerBeverage::getBeverageType)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        return loadedBeverages
                .stream()
                .map(loadedBeverage -> {
                    var beverageType = loadedBeverage.getBeverageType();
                    var loadedQuantity = loadedBeverage.getQuantity() * loadedBeverage.getServingsQuantity();
                    var servedQuantity = servedBeveragesCounts.getOrDefault(beverageType, 0L);

                    return new UnusedItem(beverageType, floorDiv((long) loadedQuantity - servedQuantity,
                            loadedBeverage.getServingsQuantity()));

                })
                .collect(Collectors.toList());

    }
}

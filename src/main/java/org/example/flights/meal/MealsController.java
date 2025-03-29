package org.example.flights.meal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MealsController {

    private final MealRepository mealRepository;

    public MealsController(final MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @GetMapping("/meal")
    public Iterable<Meal> listMeals() {
        return mealRepository.findAll();
    }
}

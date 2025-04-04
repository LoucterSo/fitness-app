package io.github.LoucterSo.fitness_app.api.controller.meal;

import io.github.LoucterSo.fitness_app.form.meal.MealDto;
import io.github.LoucterSo.fitness_app.service.meal.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealRestController {
    private final MealService mealService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MealDto addMeal(@Valid @RequestBody MealDto mealDto) {

        return mealService.createMeal(mealDto);
    }
}

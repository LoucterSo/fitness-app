package io.github.LoucterSo.fitness_app.api.controller.dish;

import io.github.LoucterSo.fitness_app.form.dish.DishDto;
import io.github.LoucterSo.fitness_app.service.dish.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
public class DishRestController {
    private final DishService dishService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public DishDto createDish(@Valid @RequestBody DishDto dish) {

        return dishService.createDish(dish);
    }
}

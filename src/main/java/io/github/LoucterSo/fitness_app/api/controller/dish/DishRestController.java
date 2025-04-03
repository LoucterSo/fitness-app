package io.github.LoucterSo.fitness_app.api.controller.dish;

import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import io.github.LoucterSo.fitness_app.form.dish.DishDto;
import io.github.LoucterSo.fitness_app.repository.dish.DishRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
public class DishRestController {
    private final DishRepository dishRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public DishDto createDish(@Valid @RequestBody DishDto dish) {
        Dish newDish = new Dish();
        newDish.setName(dish.name());
        newDish.setProteins(dish.proteins());
        newDish.setFats(dish.fats());
        newDish.setCarbs(dish.carbs());
        return DishDto.fromEntity(dishRepository.save(newDish));
    }
}

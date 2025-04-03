package io.github.LoucterSo.fitness_app.service.dish;

import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import io.github.LoucterSo.fitness_app.form.dish.DishDto;

import java.util.List;

public interface DishService {
    DishDto saveDish(Dish dish);
    List<Dish> findAllById(List<Long> ids);
    DishDto createDish(DishDto dishDto);
}

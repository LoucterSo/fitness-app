package io.github.LoucterSo.fitness_app.form.dish;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.LoucterSo.fitness_app.entity.dish.Dish;

public record DishDto(
        Long id,
        String name,
        @JsonProperty("calories_per_serving") Integer caloriesPerServing,
        Integer proteins,
        Integer fats,
        Integer carbs) {

    public static DishDto fromEntity(Dish dish) {
        return new DishDto(
                dish.getId(),
                dish.getName(),
                dish.getCaloriesPerServing(),
                dish.getProteins(),
                dish.getFats(),
                dish.getCarbs()
        );
    }
}

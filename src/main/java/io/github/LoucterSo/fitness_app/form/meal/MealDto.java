package io.github.LoucterSo.fitness_app.form.meal;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import io.github.LoucterSo.fitness_app.form.dish.DishDto;

import java.sql.Timestamp;
import java.util.List;

public record MealDto(
        Long id,
        @JsonProperty("user_id") Long userId,
        String name,
        List<DishDto> dishes,
        Timestamp created
) {

    public static MealDto fromEntity(Meal meal) {
        return new MealDto(
                meal.getId(),
                meal.getUser().getId(),
                meal.getName(),
                meal.getDishes().stream()
                        .map(DishDto::fromEntity)
                        .toList(),
                meal.getCreated()
        );
    }
}

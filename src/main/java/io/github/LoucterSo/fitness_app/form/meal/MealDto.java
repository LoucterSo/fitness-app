package io.github.LoucterSo.fitness_app.form.meal;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import io.github.LoucterSo.fitness_app.form.dish.DishDto;
import io.github.LoucterSo.fitness_app.form.group.FirstGroup;
import io.github.LoucterSo.fitness_app.form.group.SecondGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;

import java.sql.Timestamp;
import java.util.List;

@GroupSequence({FirstGroup.class, SecondGroup.class, MealDto.class})
public record MealDto(
        Long id,
        @JsonProperty("user_id")
        @NotNull(message = "User_id cannot be null", groups = FirstGroup.class)
        @PositiveOrZero(message = "User_id cannot be negative", groups = SecondGroup.class)
        Long userId,
        @NotBlank(message = "Name cannot be empty", groups = FirstGroup.class)
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters", groups = SecondGroup.class)
        String name,
        @NotEmpty(message = "Dishes cannot be empty", groups = FirstGroup.class)
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

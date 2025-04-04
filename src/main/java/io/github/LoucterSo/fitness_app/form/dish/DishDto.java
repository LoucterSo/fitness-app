package io.github.LoucterSo.fitness_app.form.dish;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import io.github.LoucterSo.fitness_app.form.group.FirstGroup;
import io.github.LoucterSo.fitness_app.form.group.SecondGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;


@GroupSequence({FirstGroup.class, SecondGroup.class, DishDto.class})
public record DishDto(
        Long id,
        @NotBlank(message = "Name cannot be empty", groups = FirstGroup.class)
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters", groups = SecondGroup.class)
        String name,
        @JsonProperty("calories_per_serving")
        Integer caloriesPerServing,
        @NotNull(message = "Proteins cannot be null", groups = FirstGroup.class)
        @PositiveOrZero(message = "Proteins cannot be negative", groups = SecondGroup.class)
        Integer proteins,
        @NotNull(message = "Fats cannot be null", groups = FirstGroup.class)
        @PositiveOrZero(message = "Fats cannot be negative", groups = SecondGroup.class)
        Integer fats,
        @NotNull(message = "Carbs cannot be null", groups = FirstGroup.class)
        @PositiveOrZero(message = "Carbs cannot be negative", groups = SecondGroup.class)
        Integer carbs
) {

    public DishDto(Long id, String name, Integer proteins, Integer fats, Integer carbs) {
        this(id, name, 0, proteins, fats, carbs);
    }

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

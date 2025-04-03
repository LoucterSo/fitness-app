package io.github.LoucterSo.fitness_app.form.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.entity.user.User.Goal;
import io.github.LoucterSo.fitness_app.entity.user.User.Sex;

public record UserDto(
        Long id,
        String name,
        String email,
        Integer age,
        Double weight,
        Double height,
        Sex sex,
        Goal goal,
        @JsonProperty("daily_calorie_norm") Integer dailyCalorieNorm
) {

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getWeightInKg(),
                user.getHeightInCm(),
                user.getSex(),
                user.getGoal(),
                user.getDailyCalorieNorm()
        );
    }
}


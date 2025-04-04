package io.github.LoucterSo.fitness_app.form.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.form.group.FirstGroup;
import io.github.LoucterSo.fitness_app.form.group.SecondGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;

@GroupSequence({FirstGroup.class, SecondGroup.class, UserDto.class})
public record UserDto(
        Long id,
        @NotBlank(message = "Name cannot be empty", groups = FirstGroup.class)
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters", groups = SecondGroup.class)
        String name,
        @NotBlank(message = "Email cannot be empty", groups = FirstGroup.class)
        @Email(message = "Invalid email", groups = SecondGroup.class)
        String email,
        @Min(value = 10, message = "Age cannot be less than 10 years", groups = FirstGroup.class)
        Integer age,
        @Min(value = 30, message = "Weight cannot be less than 30 kg", groups = FirstGroup.class)
        Double weight,
        @Min(value = 100, message = "Height cannot be less than 100 cm", groups = FirstGroup.class)
        Double height,
        @NotNull(message = "Sex cannot be empty", groups = FirstGroup.class)
        User.Sex sex,
        @NotNull(message = "Goal cannot be empty", groups = FirstGroup.class)
        User.Goal goal,
        @JsonProperty("daily_calorie_norm")
        Integer dailyCalorieNorm
) {

    public UserDto(Long id, String name, String email, Integer age, Double weight, Double height, User.Sex sex, User.Goal goal) {
        this(id, name, email, age, weight, height, sex, goal, 0);
    }

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


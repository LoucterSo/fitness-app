package io.github.LoucterSo.fitness_app.api.controller;

import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.exception.user.UserNotFoundException;
import io.github.LoucterSo.fitness_app.form.meal.MealDto;
import io.github.LoucterSo.fitness_app.form.response.DailyReportResponse;
import io.github.LoucterSo.fitness_app.form.response.DailyReportResponse2;
import io.github.LoucterSo.fitness_app.repository.meal.MealRepository;
import io.github.LoucterSo.fitness_app.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class FitnessRestController {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    @GetMapping("/daily-report/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DailyReportResponse getDailyReport(@PathVariable Long userId) {
        ZonedDateTime nowInZone = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        ZonedDateTime startOfDayInZone = nowInZone.toLocalDate().atStartOfDay(ZoneId.of("Europe/Moscow"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found".formatted(userId)));

        List<Meal> dailyUserMeals =
                mealRepository.findAllByUserIdAndCreatedTimeAfterOrEqualTo(userId, Timestamp.valueOf(startOfDayInZone.toLocalDateTime()));

        Integer calories = dailyUserMeals.stream()
                .flatMap(meal -> meal.getDishes().stream())
                .mapToInt(Dish::getCaloriesPerServing)
                .sum();

        Integer mealCount = dailyUserMeals.size();

        return new DailyReportResponse(calories, mealCount);
    }

    @GetMapping("/daily-report2/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DailyReportResponse2 getDailyReport2(@PathVariable Long userId) {
        ZonedDateTime nowInZone = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        ZonedDateTime startOfDayInZone = nowInZone.toLocalDate().atStartOfDay(ZoneId.of("Europe/Moscow"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found".formatted(userId)));

        List<Meal> dailyUserMeals =
                mealRepository.findAllByUserIdAndCreatedTimeAfterOrEqualTo(userId, Timestamp.valueOf(startOfDayInZone.toLocalDateTime()));

        Integer calories = dailyUserMeals.stream()
                .flatMap(meal -> meal.getDishes().stream())
                .mapToInt(Dish::getCaloriesPerServing)
                .sum();

        return new DailyReportResponse2(calories, user.getDailyCalorieNorm(), calories > user.getDailyCalorieNorm());
    }

    @GetMapping("/meal-history/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<LocalDate, List<MealDto>> getMealHistory(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found".formatted(userId)));

        List<MealDto> allUserMeals = user.getMeals().stream()
                .map(MealDto::fromEntity)
                .toList();

        return allUserMeals.stream()
                .collect(Collectors.groupingBy(m -> {
                    Timestamp timestamp = m.created();
                    LocalDateTime l = timestamp.toLocalDateTime();
                    return LocalDate.of(l.getYear(), l.getMonth(), l.getDayOfMonth());
                }));
    }

}

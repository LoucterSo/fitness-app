package io.github.LoucterSo.fitness_app.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.LoucterSo.fitness_app.form.meal.MealDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record MealHistoryResponse(
        @JsonProperty("meal_history")
        Map<LocalDate, List<MealDto>> mealHistory,
        @JsonProperty("total_days")
        int totalDays,
        @JsonProperty("time_range")
        String timeRange
) { }

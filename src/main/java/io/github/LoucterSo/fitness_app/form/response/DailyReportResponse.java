package io.github.LoucterSo.fitness_app.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DailyReportResponse(
        @JsonProperty("daily_calorie_sum") Integer dailyCaloriesSum,
        @JsonProperty("daily_meal_sum")Integer dailyMealsSum
) { }

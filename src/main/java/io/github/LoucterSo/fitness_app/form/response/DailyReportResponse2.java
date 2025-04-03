package io.github.LoucterSo.fitness_app.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DailyReportResponse2(
        @JsonProperty("daily_calorie_sum") Integer dailyCalorieSum,
        @JsonProperty("daily_calorie_norm") Integer dailyСalorieNorm,
        @JsonProperty("exceeded") Boolean isExceeded
)
{ }

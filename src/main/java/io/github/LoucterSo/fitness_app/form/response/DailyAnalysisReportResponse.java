package io.github.LoucterSo.fitness_app.form.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DailyAnalysisReportResponse(
        @JsonProperty("daily_calorie_sum") Integer dailyCalorieSum,
        @JsonProperty("daily_calorie_norm") Integer dailyСalorieNorm,
        @JsonProperty("exceeded") boolean exceeded
)
{ }

package io.github.LoucterSo.fitness_app.form.response;

import io.github.LoucterSo.fitness_app.form.meal.MealDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record MealHistoryResponse(Map<LocalDate, List<MealDto>> dayWithMeals) { }

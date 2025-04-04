package io.github.LoucterSo.fitness_app.form.meal;

import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import lombok.Getter;

import java.util.List;

@Getter
public class DailyMealData {

    private List<Meal> dailyMeals;
    private Integer dailyCalorieIntake;
    private Integer dailyCalorieNorm;
    private boolean exceeded;
    private Integer mealCount;

    private DailyMealData() {}

    public DailyMealData(List<Meal> dailyMeals, Integer dailyCalorieNorm, Integer dailyCalorieIntake) {
        this.dailyMeals = dailyMeals;
        this.dailyCalorieNorm = dailyCalorieNorm;
        this.dailyCalorieIntake = dailyCalorieIntake;
        this.exceeded = dailyCalorieIntake > dailyCalorieNorm;
        this.mealCount = dailyMeals.size();
    }
}

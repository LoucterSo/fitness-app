package io.github.LoucterSo.fitness_app;

import io.github.LoucterSo.fitness_app.entity.user.User;

public class Util {

    public static Integer calculateDailyCalorieIntake(User user) {
        int calorieNorm;
        if (user.isMan()) {
            calorieNorm = (int) (Math.round(66.5 + (13.75 * user.getWeightInCm()) + (5.003 * user.getHeightInCm()) - (6.755 * user.getAge())));
        } else {
            calorieNorm = (int) (Math.round(655 + (9.563 * user.getWeightInCm()) + (1.850 * user.getHeightInCm()) - (4.676 * user.getAge())));
        }

        if (user.getGoal().equals(User.Goal.WEIGHT_LOSS)) {
            calorieNorm -= 300;
        } else if (user.getGoal().equals(User.Goal.BULKING)) {
            calorieNorm += 300;
        }

        return calorieNorm;
    }
}

package io.github.LoucterSo.fitness_app.service.report;

import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.exception.user.UserNotFoundException;
import io.github.LoucterSo.fitness_app.form.meal.DailyMealData;
import io.github.LoucterSo.fitness_app.form.meal.MealDto;
import io.github.LoucterSo.fitness_app.form.response.DailyAnalysisReportResponse;
import io.github.LoucterSo.fitness_app.form.response.DailySummaryReportResponse;
import io.github.LoucterSo.fitness_app.repository.meal.MealRepository;
import io.github.LoucterSo.fitness_app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final UserService userService;
    private final MealRepository mealRepository;

    @Override
    @Transactional(readOnly = true)
    public DailySummaryReportResponse createDailySummaryReport(Long userId) {
        DailyMealData data = getDailyMealData(userId);
        return new DailySummaryReportResponse(data.getDailyCalorieIntake(), data.getMealCount());
    }

    @Override
    @Transactional(readOnly = true)
    public DailyAnalysisReportResponse createDailyAnalysisReport(Long userId) {
        DailyMealData data = getDailyMealData(userId);
        return new DailyAnalysisReportResponse(data.getDailyCalorieIntake(), data.getDailyCalorieNorm(), data.isExceeded());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<LocalDate, List<MealDto>> createMealHistoryReport(Long userId) {
        if (!userService.existsById(userId)) {
            throw new UserNotFoundException("User with id %s not found".formatted(userId));
        }
        List<MealDto> allUserMeals = mealRepository.findAllByUserId(userId).stream()
                .map(MealDto::fromEntity)
                .toList();
        return allUserMeals.stream()
                .collect(Collectors.groupingBy(m -> m.created().toLocalDateTime().toLocalDate()));
    }

    private DailyMealData getDailyMealData(Long userId) {
        User foundUser = userService.findById(userId);

        ZonedDateTime nowInZone = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime startOfDayInZone = nowInZone.toLocalDate().atStartOfDay(ZoneId.systemDefault());
        List<Meal> dailyUserMeals =
                mealRepository.findAllByUserIdAndCreatedGreaterThanEqual(userId, Timestamp.valueOf(startOfDayInZone.toLocalDateTime()));
        Integer dailyNorm = foundUser.getDailyCalorieNorm();
        Integer dailyCalories = dailyUserMeals.stream()
                .flatMap(meal -> meal.getDishes().stream())
                .mapToInt(Dish::getCaloriesPerServing)
                .sum();
        return new DailyMealData(dailyUserMeals, dailyNorm, dailyCalories);
    }
}

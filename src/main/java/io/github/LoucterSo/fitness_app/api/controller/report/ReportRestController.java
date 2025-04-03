package io.github.LoucterSo.fitness_app.api.controller.report;

import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.exception.user.UserNotFoundException;
import io.github.LoucterSo.fitness_app.form.meal.MealDto;
import io.github.LoucterSo.fitness_app.form.response.DailySummaryReportResponse;
import io.github.LoucterSo.fitness_app.form.response.DailyAnalysisReportResponse;
import io.github.LoucterSo.fitness_app.repository.meal.MealRepository;
import io.github.LoucterSo.fitness_app.repository.user.UserRepository;
import io.github.LoucterSo.fitness_app.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportRestController {
    private final ReportService reportService;

    @GetMapping("/daily/summary/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DailySummaryReportResponse getDailySummary(@PathVariable Long userId) {

        return reportService.createDailySummaryReport(userId);
    }

    @GetMapping("/daily/analysis/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DailyAnalysisReportResponse getDailyAnalysis(@PathVariable Long userId) {

        return reportService.createDailyAnalysisReport(userId);
    }

    @GetMapping("/history/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<LocalDate, List<MealDto>> getMealHistory(@PathVariable Long userId) {

        return reportService.createMealHistoryReport(userId);
    }

}

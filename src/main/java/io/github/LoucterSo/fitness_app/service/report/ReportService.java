package io.github.LoucterSo.fitness_app.service.report;

import io.github.LoucterSo.fitness_app.form.meal.MealDto;
import io.github.LoucterSo.fitness_app.form.response.DailyAnalysisReportResponse;
import io.github.LoucterSo.fitness_app.form.response.DailySummaryReportResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService {
    DailySummaryReportResponse createDailySummaryReport(Long userId);
    DailyAnalysisReportResponse createDailyAnalysisReport(Long userId);
    Map<LocalDate, List<MealDto>> createMealHistoryReport(Long userId);
}

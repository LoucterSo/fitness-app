package io.github.LoucterSo.fitness_app.service.report;

import io.github.LoucterSo.fitness_app.form.response.DailyAnalysisReportResponse;
import io.github.LoucterSo.fitness_app.form.response.DailySummaryReportResponse;
import io.github.LoucterSo.fitness_app.form.response.MealHistoryResponse;

public interface ReportService {
    DailySummaryReportResponse createDailySummaryReport(Long userId);
    DailyAnalysisReportResponse createDailyAnalysisReport(Long userId);
    MealHistoryResponse createMealHistoryReport(Long userId);
}

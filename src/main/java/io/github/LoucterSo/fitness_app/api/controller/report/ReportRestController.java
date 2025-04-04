package io.github.LoucterSo.fitness_app.api.controller.report;

import io.github.LoucterSo.fitness_app.form.response.DailySummaryReportResponse;
import io.github.LoucterSo.fitness_app.form.response.DailyAnalysisReportResponse;
import io.github.LoucterSo.fitness_app.form.response.MealHistoryResponse;
import io.github.LoucterSo.fitness_app.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public MealHistoryResponse getMealHistory(@PathVariable Long userId) {

        return reportService.createMealHistoryReport(userId);
    }

}

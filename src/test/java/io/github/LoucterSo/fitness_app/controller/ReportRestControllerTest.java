package io.github.LoucterSo.fitness_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.LoucterSo.fitness_app.api.controller.report.ReportRestController;
import io.github.LoucterSo.fitness_app.api.controller.user.UserRestController;
import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.form.meal.MealDto;
import io.github.LoucterSo.fitness_app.form.response.DailyAnalysisReportResponse;
import io.github.LoucterSo.fitness_app.form.response.DailySummaryReportResponse;
import io.github.LoucterSo.fitness_app.form.response.MealHistoryResponse;
import io.github.LoucterSo.fitness_app.form.user.UserDto;
import io.github.LoucterSo.fitness_app.service.report.ReportService;
import io.github.LoucterSo.fitness_app.service.report.ReportServiceImpl;
import io.github.LoucterSo.fitness_app.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@WebMvcTest(ReportRestController.class)
public class ReportRestControllerTest {
    private static final Integer TEST_DAILY_CALORIE_NORM = 1300;
    private static final Integer TEST_DAILY_CALORIE_SUM = 1590;
    private static final Integer TEST_DAILY_MEAL_SUM = 4;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private ReportServiceImpl reportService;

    private User mockUser;

    @Nested
    class GetDailySummary {
        @Test
        void getDailySummary_shouldResponseWithDailySummaryReportAndOkStatusCode() throws Exception {
            // given
            var requestBuilder = get("/report/daily/summary/{userId}", 1);
            var expectedResponse = new DailySummaryReportResponse(TEST_DAILY_CALORIE_SUM, TEST_DAILY_MEAL_SUM);

            // when
            when(reportService.createDailySummaryReport(any(Long.class)))
                    .thenReturn(expectedResponse);
            mockMvc.perform(requestBuilder)
                    // then
                    .andExpectAll(
                            jsonPath("$.length()").value(2),
                            jsonPath("$.daily_calorie_sum").value(expectedResponse.dailyCalorieSum()),
                            jsonPath("$.daily_meal_sum").value(expectedResponse.dailyMealSum()),
                            status().isOk());

            verify(reportService).createDailySummaryReport(any(Long.class));
        }
    }

    @Nested
    class GetDailyAnalysis {
        @Test
        void getDailyAnalysis_shouldResponseWithDailyAnalysisReportAndOkStatusCode() throws Exception {
            // given
            var requestBuilder = get("/report/daily/analysis/{userId}", 1);
            var expectedResponse = new DailyAnalysisReportResponse(TEST_DAILY_CALORIE_SUM, TEST_DAILY_CALORIE_NORM, true);

            // when
            when(reportService.createDailyAnalysisReport(any(Long.class)))
                    .thenReturn(expectedResponse);
            mockMvc.perform(requestBuilder)
                    // then
                    .andExpectAll(
                            jsonPath("$.length()").value(3),
                            jsonPath("$.daily_calorie_sum").value(expectedResponse.dailyCalorieSum()),
                            jsonPath("$.daily_calorie_norm").value(expectedResponse.dailyСalorieNorm()),
                            jsonPath("$.exceeded").value(expectedResponse.exceeded()),
                            status().isOk());

            verify(reportService).createDailyAnalysisReport(any(Long.class));
        }
    }

    @Nested
    class GetMealHistory {
        @Test
        void getMealHistory_shouldResponseWithMealHistoryReportAndOkStatusCode() throws Exception {
            // given
            Long userId = 1L;

            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
            Timestamp time = Timestamp.from(zonedDateTime.toInstant());
            MealDto meal1 = new MealDto(1L, 1L, "Breakfast", List.of(), time);
            MealDto meal2 = new MealDto(2L, 1L, "Lunch", List.of(), time);

            Map<LocalDate, List<MealDto>> mealHistory = Map.of(
                    zonedDateTime.toLocalDate(), List.of(meal1, meal2)
            );
            var expectedResponse = new MealHistoryResponse(mealHistory, mealHistory.size(), "Full history");
            var requestBuilder = get("/report/history/{userId}", 1);

            String expectedTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSxxx").format(zonedDateTime);
            // when
            when(reportService.createMealHistoryReport(any(Long.class)))
                    .thenReturn(expectedResponse);
            mockMvc.perform(requestBuilder)
                    // then
                    .andExpectAll(
                            jsonPath("$.length()").value(3),
                            jsonPath("$.meal_history").exists(),

                            jsonPath("$.meal_history['" + zonedDateTime.toLocalDate() + "'].length()").value(2),
                            jsonPath("$.meal_history['" + zonedDateTime.toLocalDate() + "'][0].id").value(1),
                            jsonPath("$.meal_history['" + zonedDateTime.toLocalDate() + "'][0].user_id").value(1),
                            jsonPath("$.meal_history['" + zonedDateTime.toLocalDate() + "'][0].name").value("Breakfast"),
                            jsonPath("$.meal_history['" + zonedDateTime.toLocalDate() + "'][0].dishes").isEmpty(),
                            jsonPath("$.meal_history['" + zonedDateTime.toLocalDate() + "'][1].id").value(2),
                            jsonPath("$.meal_history['" + zonedDateTime.toLocalDate() + "'][1].user_id").value(1),
                            jsonPath("$.meal_history['" + zonedDateTime.toLocalDate() + "'][1].name").value("Lunch"),
                            jsonPath("$.meal_history['" + zonedDateTime.toLocalDate() + "'][1].dishes").isEmpty(),
                            jsonPath("$.total_days").value(expectedResponse.totalDays()),
                            jsonPath("$.time_range").value(expectedResponse.timeRange()),
                            status().isOk());

            verify(reportService).createMealHistoryReport(any(Long.class));
        }
    }

}

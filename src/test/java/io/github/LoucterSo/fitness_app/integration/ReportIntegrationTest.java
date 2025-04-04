package io.github.LoucterSo.fitness_app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class ReportIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Long USER_ID = 1L;
    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate YESTERDAY = TODAY.minusDays(1);

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("truncate table users, meal, meal_dish, dish restart identity cascade");

        jdbcTemplate.update(
                "INSERT INTO users (user_id, name, email, age, height_in_cm, weight_in_kg, goal, sex) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                USER_ID, "Test User", "test@email.com", 30, 175.0, 70.0, "WEIGHT_LOSS", "MAN"
        );

        jdbcTemplate.update(
                "INSERT INTO dish (dish_id, name, proteins, fats, carbs, calories_per_serving) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                1L, "Гречка", 5, 2, 25, 138
        );
        jdbcTemplate.update(
                "INSERT INTO dish (dish_id, name, proteins, fats, carbs, calories_per_serving) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                2L, "Куриная грудка", 30, 4, 0, 165
        );

        jdbcTemplate.update(
                "INSERT INTO meal (meal_id, user_id, name, created_time) " +
                        "VALUES (?, ?, ?, ?)",
                1L, USER_ID, "Завтрак", Timestamp.valueOf(TODAY.atTime(8, 0))
        );
        jdbcTemplate.update(
                "INSERT INTO meal (meal_id, user_id, name, created_time) " +
                        "VALUES (?, ?, ?, ?)",
                2L, USER_ID, "Обед", Timestamp.valueOf(YESTERDAY.atTime(13, 0))
        );

        jdbcTemplate.update(
                "INSERT INTO meal_dish (meal_id, dish_id) VALUES (?, ?)",
                1L, 1L
        );
        jdbcTemplate.update(
                "INSERT INTO meal_dish (meal_id, dish_id) VALUES (?, ?)",
                2L, 2L
        );
    }

    @Test
    void getDailySummary() throws Exception {
        // given
        var requestBuilder =
                get("/report/daily/summary/{userId}", USER_ID);

        // when
        mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.daily_calorie_sum").value(138),
                        jsonPath("$.daily_meal_sum").value(1)
                );
    }

    @Test
    void getDailyAnalysis() throws Exception {
        // given
        var requestBuilder =
                get("/report/daily/analysis/{userId}", USER_ID);

        // when
        mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.daily_calorie_sum").value(138),
                        jsonPath("$.daily_calorie_norm").value(1402),
                        jsonPath("$.exceeded").value(false)
                );
    }

    @Test
    void getMealHistory() throws Exception {
        // given
        var requestBuilder =
                get("/report/history/{userId}", USER_ID);

        // when
        mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.meal_history").isMap(),
                        jsonPath("$.meal_history['" + TODAY + "'].length()").value(1),
                        jsonPath("$.meal_history['" + YESTERDAY + "'].length()").value(1),
                        jsonPath("$.meal_history['" + TODAY + "'][0].name").value("Завтрак"),
                        jsonPath("$.meal_history['" + TODAY + "'][0].dishes[0].name").value("Гречка"),
                        jsonPath("$.meal_history['" + YESTERDAY + "'][0].name").value("Обед"),
                        jsonPath("$.meal_history['" + YESTERDAY + "'][0].dishes[0].name").value("Куриная грудка"),
                        jsonPath("$.total_days").value(2),
                        jsonPath("$.time_range").value("Full history")
                );
    }
}

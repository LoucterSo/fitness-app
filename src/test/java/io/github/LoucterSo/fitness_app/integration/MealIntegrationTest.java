package io.github.LoucterSo.fitness_app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.LoucterSo.fitness_app.form.dish.DishDto;
import io.github.LoucterSo.fitness_app.form.meal.MealDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class MealIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Long TEST_ID = 1L;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("truncate table users, meal, meal_dish, dish restart identity cascade");

        jdbcTemplate.execute(
                "insert into users (name, email, age, height_in_cm, weight_in_kg, goal, sex) "
                        + "values ('name', 'test@gmail.com', 21, 175.3, 65.3, 'WEIGHT_LOSS', 'MAN')");

        jdbcTemplate.execute(
                "insert into dish (name, proteins, fats, carbs, calories_per_serving) "
                        + "values ('dishName1', 21, 20, 50, 464), ('dishName2', 21, 20, 50, 464)");
    }

    @Test
    void shouldCreateMeal() throws Exception {
        // given
        MealDto mealDto = new MealDto(TEST_ID, 1L, "Breakfast",
                List.of(
                        new DishDto(1L, null, null, null, null),
                        new DishDto(2L, null, null, null, null),
                        new DishDto(null, "dishName3", 21, 21, 50, 434)
                ), null);
        var requestBuilder =
                post("/meal")
                        .content(mapper.writeValueAsString(mealDto))
                        .contentType(MediaType.APPLICATION_JSON);

        // when
        mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(status().isCreated());

        Integer actual = jdbcTemplate.queryForObject(
                        "select count(*) from meal where meal_id = 1", Integer.class);
        Assertions.assertEquals(1, actual);

        actual = jdbcTemplate.queryForObject(
                "select count(*) from dish", Integer.class);
        Assertions.assertEquals(3, actual);
    }
}

package io.github.LoucterSo.fitness_app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.LoucterSo.fitness_app.form.dish.DishDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class DishIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "Dish Name";
    private static final Integer TEST_PROTEINS = 50;
    private static final Integer TEST_FATS = 20;
    private static final Integer TEST_CARBS = 100;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("truncate table users, meal, meal_dish, dish restart identity cascade");
    }

    @Test
    void createDish() throws Exception {
        // given
        DishDto dishDto = new DishDto(TEST_ID, TEST_NAME, TEST_PROTEINS, TEST_FATS, TEST_CARBS);
        var requestBuilder =
                post("/dish")
                        .content(mapper.writeValueAsString(dishDto))
                        .contentType(MediaType.APPLICATION_JSON);

        // when
        mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(status().isCreated());

        Integer actual =
                jdbcTemplate.queryForObject(
                        "select count(*) from dish where dish_id = 1", Integer.class);
        Assertions.assertEquals(1, actual);

        Integer actualCaloriesPerServing = TEST_PROTEINS * 4 + TEST_FATS * 9 + TEST_CARBS * 4;
        Integer caloriesPerServing =
                jdbcTemplate.queryForObject("select calories_per_serving from dish where dish_id = 1" , Integer.class);
        Assertions.assertEquals(actualCaloriesPerServing, caloriesPerServing);
    }
}

package io.github.LoucterSo.fitness_app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.form.user.UserDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Long TEST_ID = 1L;
    private static final String TEST_EMAIL = "test@email.com";
    private static final String TEST_NAME = "Name";
    private static final Integer TEST_AGE = 20;
    private static final Double TEST_WEIGHT = 65d;
    private static final Double TEST_HEIGHT = 176d;
    private static final User.Sex TEST_SEX = User.Sex.MAN;
    private static final User.Goal TEST_GOAL = User.Goal.WEIGHT_LOSS;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("truncate table users, meal, meal_dish, dish restart identity cascade");
    }

    @Test
    void createUser() throws Exception {
        // given
        UserDto userDto = new UserDto(TEST_ID, TEST_NAME, TEST_EMAIL, TEST_AGE, TEST_WEIGHT, TEST_HEIGHT, TEST_SEX, TEST_GOAL);
        var requestBuilder =
                post("/user")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON);

        // when
        mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(status().isCreated());

        Integer actual =
                jdbcTemplate.queryForObject(
                        "select count(*) from users where user_id = 1", Integer.class);
        Assertions.assertEquals(1, actual);
    }
}

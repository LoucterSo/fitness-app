package io.github.LoucterSo.fitness_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.LoucterSo.fitness_app.api.controller.meal.MealRestController;
import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.form.meal.MealDto;
import io.github.LoucterSo.fitness_app.service.meal.MealServiceImpl;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@WebMvcTest(MealRestController.class)
public class MealRestControllerTest {
    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "Meal Name";
    private static final String DISH_NAME = "Test Dish";
    private static final Integer PROTEINS = 20;
    private static final Integer FATS = 10;
    private static final Integer CARBS = 30;

    private static final User TEST_USER;
    private static final List<Dish> TEST_DISHES;

    static {
        TEST_USER = new User();
        TEST_USER.setId(TEST_ID);

        // Создаем тестовые блюда
        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName(DISH_NAME + " 1");
        dish1.setProteins(PROTEINS);
        dish1.setFats(FATS);
        dish1.setCarbs(CARBS);

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName(DISH_NAME + " 2");
        dish2.setProteins(PROTEINS + 5);
        dish2.setFats(FATS + 2);
        dish2.setCarbs(CARBS + 10);

        TEST_DISHES = List.of(dish1, dish2);
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private MealServiceImpl mealService;

    private Meal mockMeal;

    @BeforeEach
    void setUpMockDish() {
        mockMeal = new Meal();
        mockMeal.setId(TEST_ID);
        mockMeal.setName(TEST_NAME);
        mockMeal.setDishes(new ArrayList<>(TEST_DISHES));
        mockMeal.setUser(TEST_USER);
    }

    @Nested
    class CreateMealTest {
        @Test
        void createMeal_shouldResponseWithMealDtoAndCreatedStatusCode() throws Exception {
            // given
            MealDto mealToCreate = MealDto.fromEntity(mockMeal);
            String time = "2025-04-04T05:38:53.288+00:00";
            String userAsJson = mapper.writeValueAsString(mealToCreate);
            String modifiedJson = userAsJson.replace("created\":null", "created\":\"" + time + "\"");
            var requestBuilder =
                    post("/meal")
                            .content(modifiedJson)
                            .contentType(MediaType.APPLICATION_JSON);

            // when
            when(mealService.createMeal(any(MealDto.class)))
                    .thenReturn(mapper.readValue(modifiedJson, MealDto.class));
            mockMvc.perform(requestBuilder)
                    // then
                    .andExpectAll(
                            jsonPath("$.length()").value(5),
                            jsonPath("$.id").value(mealToCreate.id()),
                            jsonPath("$.name").value(mealToCreate.name()),
                            jsonPath("$.user_id").value(mealToCreate.userId()),
                            jsonPath("$.created").value(time),
                            jsonPath("$.dishes.length()").value(TEST_DISHES.size()),
                            jsonPath("$.dishes[0].name").value(DISH_NAME + " 1"),
                            jsonPath("$.dishes[0].proteins").value(TEST_DISHES.get(0).getProteins()),
                            jsonPath("$.dishes[0].fats").value(TEST_DISHES.get(0).getFats()),
                            jsonPath("$.dishes[0].carbs").value(TEST_DISHES.get(0).getCarbs()),
                            jsonPath("$.dishes[0].calories_per_serving").value(TEST_DISHES.get(0).getCaloriesPerServing()),
                            jsonPath("$.dishes[1].name").value(DISH_NAME + " 2"),
                            jsonPath("$.dishes[1].proteins").value(TEST_DISHES.get(1).getProteins()),
                            jsonPath("$.dishes[1].fats").value(TEST_DISHES.get(1).getFats()),
                            jsonPath("$.dishes[1].carbs").value(TEST_DISHES.get(1).getCarbs()),
                            jsonPath("$.dishes[1].calories_per_serving").value(TEST_DISHES.get(1).getCaloriesPerServing()),
                            status().isCreated());

            verify(mealService).createMeal(any(MealDto.class));
        }

        @Nested
        class ValidationTest {

            @Test
            void createUer_shouldReturnBadRequestStatusCodeWhenUserIdIsNegative() throws Exception {
                // given
                mockMeal.getUser().setId(-2L);
                MealDto dishToCreate = MealDto.fromEntity(mockMeal);

                mockMvc.perform(post("/meal")
                                .content(mapper.writeValueAsString(dishToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.userId").value("User_id cannot be negative"));
            }

            @Test
            void createDish_shouldReturnBadRequestStatusCodeWhenUserIdIsNull() throws Exception {
                // given
                MealDto dishToCreate = MealDto.fromEntity(mockMeal);
                String userAsJson = mapper.writeValueAsString(dishToCreate);
                String modifiedJson = userAsJson.replace("user_id\":" + TEST_USER.getId(), "user_id\":null");

                mockMvc.perform(post("/meal")
                                .content(modifiedJson)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.userId").value("User_id cannot be null"));
            }
            
            @Test
            void createMeal_shouldReturnBadRequestStatusCodeWhenNameIsEmpty() throws Exception {
                // given
                mockMeal.setName("");
                MealDto mealToCreate = MealDto.fromEntity(mockMeal);

                mockMvc.perform(post("/meal")
                                .content(mapper.writeValueAsString(mealToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.name").value("Name cannot be empty"));
            }

            @Test
            void createMeal_shouldReturnBadRequestStatusCodeWhenNameSizeIsGreaterThan100() throws Exception {
                // given
                mockMeal.setName("N".repeat(101));
                MealDto mealToCreate = MealDto.fromEntity(mockMeal);

                mockMvc.perform(post("/meal")
                                .content(mapper.writeValueAsString(mealToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.name").value("Name must be between 2 and 100 characters"));
            }

            @Test
            void createMeal_shouldReturnBadRequestStatusCodeWhenNameSizeIsLessThan2() throws Exception {
                // given
                mockMeal.setName("N");
                MealDto mealToCreate = MealDto.fromEntity(mockMeal);

                mockMvc.perform(post("/meal")
                                .content(mapper.writeValueAsString(mealToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.name").value("Name must be between 2 and 100 characters"));
            }

            @Test
            void createMeal_shouldReturnBadRequestStatusCodeWhenDishesArrayIsEmpty() throws Exception {
                // given
                mockMeal.setDishes(new ArrayList<>());
                MealDto mealToCreate = MealDto.fromEntity(mockMeal);

                mockMvc.perform(post("/meal")
                                .content(mapper.writeValueAsString(mealToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.dishes").value("Dishes cannot be empty"));
            }
        }
    }
}

package io.github.LoucterSo.fitness_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.LoucterSo.fitness_app.api.controller.dish.DishRestController;
import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import io.github.LoucterSo.fitness_app.form.dish.DishDto;
import io.github.LoucterSo.fitness_app.service.dish.DishServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@WebMvcTest(DishRestController.class)
public class DishRestControllerTest {
    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "Dish Name";
    private static final Integer TEST_PROTEINS = 50;
    private static final Integer TEST_FATS = 20;
    private static final Integer TEST_CARBS = 100;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private DishServiceImpl dishService;

    private Dish mockDish;

    @BeforeEach
    void setUpMockDish() {
        mockDish = new Dish();
        mockDish.setId(TEST_ID);
        mockDish.setName(TEST_NAME);
        mockDish.setProteins(TEST_PROTEINS);
        mockDish.setFats(TEST_FATS);
        mockDish.setCarbs(TEST_CARBS);
    }
    
    @Nested
    class CreateDishTest {
        @Test
        void createDish_shouldResponseWithDishDtoAndCreatedStatusCode() throws Exception {
            // given
            DishDto dishToCreate = DishDto.fromEntity(mockDish);
            var requestBuilder =
                    post("/dish")
                            .content(mapper.writeValueAsString(dishToCreate))
                            .contentType(MediaType.APPLICATION_JSON);

            // when
            when(dishService.createDish(any(DishDto.class)))
                    .thenReturn(dishToCreate);
            mockMvc.perform(requestBuilder)
                    // then
                    .andExpectAll(
                            jsonPath("$.length()").value(6),
                            jsonPath("$.id").value(dishToCreate.id()),
                            jsonPath("$.name").value(dishToCreate.name()),
                            jsonPath("$.proteins").value(dishToCreate.proteins()),
                            jsonPath("$.fats").value(dishToCreate.fats()),
                            jsonPath("$.carbs").value(dishToCreate.carbs()),
                            status().isCreated());

            verify(dishService).createDish(any(DishDto.class));
        }

        @Nested
        class ValidationTest {
            @Test
            void createDish_shouldReturnBadRequestStatusCodeWhenNameIsEmpty() throws Exception {
                // given
                mockDish.setName("");
                DishDto dishToCreate = DishDto.fromEntity(mockDish);

                mockMvc.perform(post("/dish")
                                .content(mapper.writeValueAsString(dishToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.name").value("Name cannot be empty"));
            }

            @Test
            void createDish_shouldReturnBadRequestStatusCodeWhenNameIsNull() throws Exception {
                // given
                mockDish.setName(null);
                DishDto dishToCreate = DishDto.fromEntity(mockDish);

                mockMvc.perform(post("/dish")
                                .content(mapper.writeValueAsString(dishToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.name").value("Name cannot be empty"));
            }
            
            @Test
            void createUer_shouldReturnBadRequestStatusCodeWhenProteinsAreNegative() throws Exception {
                // given
                mockDish.setProteins(-20);
                DishDto dishToCreate = DishDto.fromEntity(mockDish);

                mockMvc.perform(post("/dish")
                                .content(mapper.writeValueAsString(dishToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.proteins").value("Proteins cannot be negative"));
            }

            @Test
            void createDish_shouldReturnBadRequestStatusCodeWhenProteinsAreNull() throws Exception {
                // given
                DishDto dishToCreate = DishDto.fromEntity(mockDish);
                String userAsJson = mapper.writeValueAsString(dishToCreate);
                String modifiedJson = userAsJson.replace("proteins\":" + TEST_PROTEINS, "proteins\":null");

                mockMvc.perform(post("/dish")
                                .content(modifiedJson)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.proteins").value("Proteins cannot be null"));
            }
            
            @Test
            void createUer_shouldReturnBadRequestStatusCodeWhenFatsAreNegative() throws Exception {
                // given
                mockDish.setFats(-20);
                DishDto dishToCreate = DishDto.fromEntity(mockDish);

                mockMvc.perform(post("/dish")
                                .content(mapper.writeValueAsString(dishToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.fats").value("Fats cannot be negative"));
            }

            @Test
            void createDish_shouldReturnBadRequestStatusCodeWhenFatsAreNull() throws Exception {
                // given
                DishDto dishToCreate = DishDto.fromEntity(mockDish);
                String userAsJson = mapper.writeValueAsString(dishToCreate);
                String modifiedJson = userAsJson.replace("fats\":" + TEST_FATS, "fats\":null");

                mockMvc.perform(post("/dish")
                                .content(modifiedJson)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.fats").value("Fats cannot be null"));
            }

            @Test
            void createUer_shouldReturnBadRequestStatusCodeWhenCarbsAreNegative() throws Exception {
                // given
                mockDish.setCarbs(-20);
                DishDto dishToCreate = DishDto.fromEntity(mockDish);

                mockMvc.perform(post("/dish")
                                .content(mapper.writeValueAsString(dishToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.carbs").value("Carbs cannot be negative"));
            }

            @Test
            void createDish_shouldReturnBadRequestStatusCodeWhenCarbsAreNull() throws Exception {
                // given
                DishDto dishToCreate = DishDto.fromEntity(mockDish);
                String userAsJson = mapper.writeValueAsString(dishToCreate);
                String modifiedJson = userAsJson.replace("carbs\":" + TEST_CARBS, "carbs\":null");

                mockMvc.perform(post("/dish")
                                .content(modifiedJson)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.carbs").value("Carbs cannot be null"));
            }
        }
    }

}

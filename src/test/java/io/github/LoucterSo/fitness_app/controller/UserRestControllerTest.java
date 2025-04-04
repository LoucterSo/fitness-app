package io.github.LoucterSo.fitness_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.LoucterSo.fitness_app.api.controller.user.UserRestController;
import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.form.user.UserDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@WebMvcTest(UserRestController.class)
public class UserRestControllerTest {
    private static final Long TEST_ID = 1L;
    private static final String TEST_EMAIL = "test@email.com";
    private static final String TEST_NAME = "First Name";
    private static final Integer TEST_AGE = 20;
    private static final Double TEST_WEIGHT = 65d;
    private static final Double TEST_HEIGHT = 176d;
    private static final User.Sex TEST_SEX = User.Sex.MAN;
    private static final User.Goal TEST_GOAL = User.Goal.WEIGHT_LOSS;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private UserServiceImpl userService;

    private User mockUser;

    @BeforeEach
    void setUpMockUser() {
        mockUser = new User();
        mockUser.setId(TEST_ID);
        mockUser.setName(TEST_NAME);
        mockUser.setEmail(TEST_EMAIL);
        mockUser.setAge(TEST_AGE);
        mockUser.setWeightInKg(TEST_WEIGHT);
        mockUser.setHeightInCm(TEST_HEIGHT);
        mockUser.setSex(TEST_SEX);
        mockUser.setGoal(TEST_GOAL);
    }

    @Nested
    class CreateUserTest {
        @Test
        void createUser_shouldResponseWithUserDtoAndCreatedStatusCode() throws Exception {
            // given
            UserDto userToCreate = UserDto.fromEntity(mockUser);
            var requestBuilder =
                    post("/user")
                            .content(mapper.writeValueAsString(userToCreate))
                            .contentType(MediaType.APPLICATION_JSON);

            // when
            when(userService.createUser(any(UserDto.class)))
                    .thenReturn(userToCreate);
            mockMvc.perform(requestBuilder)
                    // then
                    .andExpectAll(
                            jsonPath("$.length()").value(9),
                            jsonPath("$.id").value(userToCreate.id()),
                            jsonPath("$.name").value(userToCreate.name()),
                            jsonPath("$.email").value(userToCreate.email()),
                            jsonPath("$.age").value(userToCreate.age()),
                            jsonPath("$.weight").value(userToCreate.weight()),
                            jsonPath("$.height").value(userToCreate.height()),
                            jsonPath("$.sex").value(userToCreate.sex().name()),
                            jsonPath("$.goal").value(userToCreate.goal().name()),
                            jsonPath("$.daily_calorie_norm").value(userToCreate.dailyCalorieNorm()),
                            status().isCreated());

            verify(userService).createUser(any(UserDto.class));
        }

        @Nested
        class ValidationTest {
            @Test
            void createUser_shouldReturnBadRequestStatusCodeWhenNameIsEmpty() throws Exception {
                // given
                mockUser.setName("");
                UserDto userToCreate = UserDto.fromEntity(mockUser);

                mockMvc.perform(post("/user")
                                .content(mapper.writeValueAsString(userToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.name").value("Name cannot be empty"));
            }

            @Test
            void createUser_shouldReturnBadRequestStatusCodeWhenNameIsNull() throws Exception {
                // given
                mockUser.setName(null);
                UserDto userToCreate = UserDto.fromEntity(mockUser);

                mockMvc.perform(post("/user")
                                .content(mapper.writeValueAsString(userToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.name").value("Name cannot be empty"));
            }

            @Test
            void createUser_shouldReturnBadRequestStatusCodeWhenNameSizeIsLessThan2() throws Exception {
                // given
                mockUser.setName("N");
                UserDto userToCreate = UserDto.fromEntity(mockUser);

                mockMvc.perform(post("/user")
                                .content(mapper.writeValueAsString(userToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.name").value("Name must be between 2 and 100 characters"));
            }

            @Test
            void createUser_shouldReturnBadRequestStatusCodeWhenNameSizeIsGreaterThan100() throws Exception {
                // given
                mockUser.setName("N".repeat(101));
                UserDto userToCreate = UserDto.fromEntity(mockUser);

                mockMvc.perform(post("/user")
                                .content(mapper.writeValueAsString(userToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.name").value("Name must be between 2 and 100 characters"));
            }


            @Test
            void createUser_shouldReturnBadRequestStatusCodeWhenEmailIsEmpty() throws Exception {
                // given
                mockUser.setEmail("");
                UserDto userToCreate = UserDto.fromEntity(mockUser);

                mockMvc.perform(post("/user")
                                .content(mapper.writeValueAsString(userToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.email").value("Email cannot be empty"));
            }

            @Test
            void createUser_shouldReturnBadRequestStatusCodeWhenEmailIsInvalid() throws Exception {
                // given
                mockUser.setEmail("invalidemail.com");
                UserDto userToCreate = UserDto.fromEntity(mockUser);

                mockMvc.perform(post("/user")
                                .content(mapper.writeValueAsString(userToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.email").value("Invalid email"));
            }

            @Test
            void createUser_shouldReturnBadRequestStatusCodeWhenAgeIsLessThan10() throws Exception {
                // given
                mockUser.setAge(9);
                UserDto userToCreate = UserDto.fromEntity(mockUser);

                mockMvc.perform(post("/user")
                                .content(mapper.writeValueAsString(userToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.age").value("Age cannot be less than 10 years"));
            }

            @Test
            void createUser_shouldReturnBadRequestStatusCodeWhenHeightIsLessThan30() throws Exception {
                // given
                mockUser.setHeightInCm(99.9);
                UserDto userToCreate = UserDto.fromEntity(mockUser);

                mockMvc.perform(post("/user")
                                .content(mapper.writeValueAsString(userToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.height").value("Height cannot be less than 100 cm"));
            }

            @Test
            void createUser_shouldReturnBadRequestStatusCodeWhenWeightIsLessThan100() throws Exception {
                // given
                mockUser.setWeightInKg(29.9);
                UserDto userToCreate = UserDto.fromEntity(mockUser);

                mockMvc.perform(post("/user")
                                .content(mapper.writeValueAsString(userToCreate))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.weight").value("Weight cannot be less than 30 kg"));
            }

            @Test
            void createUser_shouldReturnBadRequestStatusCodeWhenGoalIsNull() throws Exception {
                // given
                UserDto userToCreate = UserDto.fromEntity(mockUser);
                String userAsJson = mapper.writeValueAsString(userToCreate);
                String modifiedJson = userAsJson.replace("\"" + TEST_GOAL + "\"", "null");

                mockMvc.perform(post("/user")
                                .content(modifiedJson)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.goal").value("Goal cannot be empty"));
            }

            @Test
            void createUser_shouldReturnBadRequestStatusCodeWhenSexIsNull() throws Exception {
                // given
                UserDto userToCreate = UserDto.fromEntity(mockUser);
                String userAsJson = mapper.writeValueAsString(userToCreate);
                String modifiedJson = userAsJson.replace("\"" + TEST_SEX + "\"", "null");

                mockMvc.perform(post("/user")
                                .content(modifiedJson)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorFields.sex").value("Sex cannot be empty"));
            }
        }
    }

}

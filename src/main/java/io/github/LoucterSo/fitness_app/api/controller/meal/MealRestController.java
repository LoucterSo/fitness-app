package io.github.LoucterSo.fitness_app.api.controller.meal;

import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.exception.user.UserNotFoundException;
import io.github.LoucterSo.fitness_app.form.meal.MealDto;
import io.github.LoucterSo.fitness_app.repository.dish.DishRepository;
import io.github.LoucterSo.fitness_app.repository.meal.MealRepository;
import io.github.LoucterSo.fitness_app.repository.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealRestController {
    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final DishRepository dishRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @Transactional
    public MealDto addMeal(@Valid @RequestBody MealDto mealDto) {
        User user = userRepository.findById(mealDto.userId())
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found".formatted(mealDto.userId())));

        Meal newMeal = new Meal();
        newMeal.setName(mealDto.name());
        newMeal.setUser(user);
        newMeal.setDishes(
                mealDto.dishes().stream()
                        .map(dishForm -> {
                            Optional<Dish> foundDish;
                            if (dishForm.id() == null || (foundDish= dishRepository.findById(dishForm.id())).isEmpty()) {
                                Dish dish = new Dish();
                                dish.setName(dishForm.name());
                                dish.setProteins(dishForm.proteins());
                                dish.setFats(dishForm.fats());
                                dish.setCarbs(dishForm.carbs());
                                dish.setMeal(newMeal);
                                return dish;
                            }

                            Dish selectedDish = foundDish.get();
                            selectedDish.setMeal(newMeal);
                            return selectedDish;
                        }).toList());

        Meal savedMeal = mealRepository.save(newMeal);
        return MealDto.fromEntity(savedMeal);
    }
}

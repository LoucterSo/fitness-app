package io.github.LoucterSo.fitness_app.service.meal;

import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.exception.dish.DishNotFoundException;
import io.github.LoucterSo.fitness_app.form.dish.DishDto;
import io.github.LoucterSo.fitness_app.form.meal.MealDto;
import io.github.LoucterSo.fitness_app.repository.meal.MealRepository;
import io.github.LoucterSo.fitness_app.service.dish.DishService;
import io.github.LoucterSo.fitness_app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository;
    private final UserService userService;
    private final DishService dishService;

    public MealDto saveMeal(Meal meal) {
        return MealDto.fromEntity(mealRepository.save(meal));
    }

    @Transactional
    public MealDto createMeal(MealDto mealDto) {
        User foundUser = userService.findById(mealDto.userId());

        Meal newMeal = new Meal();
        newMeal.setName(mealDto.name());
        newMeal.setUser(foundUser);
        List<Dish> dishes = prepareDishesForMeal(newMeal, mealDto.dishes());
        dishes.forEach(dishService::saveDish);
        newMeal.setDishes(dishes);
        return MealDto.fromEntity(mealRepository.save(newMeal));
    }

    private List<Dish> prepareDishesForMeal(Meal meal, List<DishDto> dishDtos) {

        List<Long> existingDishIds = dishDtos.stream()
                .map(DishDto::id)
                .filter(Objects::nonNull)
                .toList();

        Map<Long, Dish> existingDishes = existingDishIds.isEmpty() ?
                Map.of() : dishService.findAllById(existingDishIds).stream()
                .collect(Collectors.toMap(Dish::getId, Function.identity()));

        return dishDtos.stream()
                .map(dishDto -> mapDishDtoToEntity(meal, dishDto, existingDishes))
                .toList();
    }

    private Dish mapDishDtoToEntity(Meal meal, DishDto dishDto,  Map<Long, Dish> existingDishes) {
        Long dishDtoId = dishDto.id();
        if (dishDtoId != null) {
            Dish foundDish = existingDishes.get(dishDtoId);
            if (foundDish == null) {
                throw new DishNotFoundException("Dish with id %d not found".formatted(dishDtoId));
            }
            foundDish.addMeal(meal);
            return foundDish;
        } else {
            Dish dish = new Dish();
            dish.setName(dishDto.name());
            dish.setProteins(dishDto.proteins());
            dish.setFats(dishDto.fats());
            dish.setCarbs(dishDto.carbs());
            dish.addMeal(meal);
            return dish;
        }
    }
}

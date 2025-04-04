package io.github.LoucterSo.fitness_app.service.dish;

import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import io.github.LoucterSo.fitness_app.form.dish.DishDto;
import io.github.LoucterSo.fitness_app.repository.dish.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;

    @Override
    public DishDto saveDish(Dish dish) {
        return DishDto.fromEntity(dishRepository.save(dish));
    }

    @Override
    public List<Dish> findAllById(List<Long> ids) {
        return dishRepository.findAllById(ids);
    }

    @Override
    public DishDto createDish(DishDto dishDto) {
        Dish newDish = new Dish();
        newDish.setName(dishDto.name());
        newDish.setProteins(dishDto.proteins());
        newDish.setFats(dishDto.fats());
        newDish.setCarbs(dishDto.carbs());

        return saveDish(newDish);
    }
}

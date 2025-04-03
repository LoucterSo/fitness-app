package io.github.LoucterSo.fitness_app.repository.dish;

import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
}

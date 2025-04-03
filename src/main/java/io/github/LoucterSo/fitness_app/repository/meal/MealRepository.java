package io.github.LoucterSo.fitness_app.repository.meal;

import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    @EntityGraph(attributePaths = "dishes")
    List<Meal> findAllByUserIdAndCreatedGreaterThanEqual(Long userId, Timestamp created);

}

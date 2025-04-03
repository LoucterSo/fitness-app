package io.github.LoucterSo.fitness_app.repository.meal;

import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    @Query("SELECT m FROM Meal m WHERE m.user.id = :userId AND m.created >= :givenTime")
    List<Meal> findAllByUserIdAndCreatedTimeAfterOrEqualTo(
            @Param("userId") Long userId,
            @Param("givenTime") Timestamp givenTime);

}

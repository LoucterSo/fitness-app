package io.github.LoucterSo.fitness_app.entity.dish;

import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dish")
@Getter @Setter
@NoArgsConstructor
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_id")
    private Long id;

    @ManyToMany(mappedBy = "dishes")
    private List<Meal> meals = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Integer caloriesPerServing;

    @Column(nullable = false)
    private Integer proteins;

    @Column(nullable = false)
    private Integer fats;

    @Column(nullable = false)
    private Integer carbs;

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public Integer getCaloriesPerServing() {
        return proteins * 4 + fats * 9 + carbs * 4;
    }

    @PrePersist
    @PreUpdate
    private void calculateCalories() {
        this.caloriesPerServing = getCaloriesPerServing();
    }
}

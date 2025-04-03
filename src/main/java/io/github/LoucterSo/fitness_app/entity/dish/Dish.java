package io.github.LoucterSo.fitness_app.entity.dish;

import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dish")
@Getter @Setter
@NoArgsConstructor
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_id")
    private Long id;

    @JoinColumn(name = "meal_id")
    @ManyToOne
    private Meal meal;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer caloriesPerServing;

    @Column(nullable = false)
    private Integer proteins;

    @Column(nullable = false)
    private Integer fats;

    @Column(nullable = false)
    private Integer carbs;

}

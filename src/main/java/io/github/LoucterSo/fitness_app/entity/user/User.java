package io.github.LoucterSo.fitness_app.entity.user;

import io.github.LoucterSo.fitness_app.Util;
import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import io.github.LoucterSo.fitness_app.entity.meal.Meal;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "weight_in_cm", nullable = false)
    private Double weightInKg;

    @Column(name = "height_in_cm", nullable = false)
    private Double heightInCm;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Meal> meals = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Sex sex;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Goal goal;

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Integer dailyCalorieNorm;

    public Integer getDailyCalorieNorm() {
        if (dailyCalorieNorm == null) {
            dailyCalorieNorm = Util.calculateDailyCalorieIntake(this);
        }
        return dailyCalorieNorm;
    }

    public boolean isMan() {
        return sex.equals(Sex.MAN);
    }

    public enum Goal {
        WEIGHT_MAINTENANCE,
        WEIGHT_LOSS,
        BULKING
    }

    public enum Sex {
        MAN,
        WOMAN
    }
}

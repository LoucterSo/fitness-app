package io.github.LoucterSo.fitness_app.entity;

import io.github.LoucterSo.fitness_app.Util;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Double weightInCm;

    @Column(name = "height_in_cm", nullable = false)
    private Double heightInCm;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Sex sex;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Goal goal;

    @Transient
    private Integer dailyCalorieNorm = Util.calculateDailyCalorieIntake(this);

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

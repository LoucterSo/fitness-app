package io.github.LoucterSo.fitness_app.entity.meal;


import io.github.LoucterSo.fitness_app.entity.dish.Dish;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal")
@Getter @Setter
@NoArgsConstructor
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Long id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Dish> dishes = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    @Column(name = "created_time", nullable = false)
    private Timestamp created;
}

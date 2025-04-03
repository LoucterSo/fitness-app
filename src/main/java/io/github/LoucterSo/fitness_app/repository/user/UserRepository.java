package io.github.LoucterSo.fitness_app.repository.user;

import io.github.LoucterSo.fitness_app.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}

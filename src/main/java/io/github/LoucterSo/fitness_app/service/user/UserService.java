package io.github.LoucterSo.fitness_app.service.user;

import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.form.user.UserDto;

public interface UserService {
    UserDto createUser(UserDto user);
    User findById(Long userId);
}

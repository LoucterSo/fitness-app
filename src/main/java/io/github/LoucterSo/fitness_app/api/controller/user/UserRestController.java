package io.github.LoucterSo.fitness_app.api.controller.user;

import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.form.user.UserDto;
import io.github.LoucterSo.fitness_app.repository.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserDto createUser(@Valid @RequestBody UserDto user) {
        User newUser = new User();
        newUser.setName(user.name());
        newUser.setEmail(user.email());
        newUser.setAge(user.age());
        newUser.setWeightInKg(user.weight());
        newUser.setHeightInCm(user.height());
        newUser.setSex(user.sex());
        newUser.setGoal(user.goal());
        return UserDto.fromEntity(userRepository.save(newUser));
    }
}

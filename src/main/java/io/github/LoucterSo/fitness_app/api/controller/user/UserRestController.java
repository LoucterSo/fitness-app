package io.github.LoucterSo.fitness_app.api.controller.user;

import io.github.LoucterSo.fitness_app.form.user.UserDto;
import io.github.LoucterSo.fitness_app.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserDto createUser(@Valid @RequestBody UserDto user) {

        return userService.createUser(user);
    }
}

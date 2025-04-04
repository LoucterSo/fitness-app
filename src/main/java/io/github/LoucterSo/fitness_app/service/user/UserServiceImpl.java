package io.github.LoucterSo.fitness_app.service.user;

import io.github.LoucterSo.fitness_app.entity.user.User;
import io.github.LoucterSo.fitness_app.exception.user.UserNotFoundException;
import io.github.LoucterSo.fitness_app.form.user.UserDto;
import io.github.LoucterSo.fitness_app.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto user) {
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

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found".formatted(userId)));
    }

    @Override
    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }


}

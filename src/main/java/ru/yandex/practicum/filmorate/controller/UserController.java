package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        log.info("Получен запрос GET к эндпоинту: /users");
        return new ArrayList<>(users);
    }

    @PostMapping("/user")
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос POST. Данные тела запроса: {}", user);
        user.generatedIdUser();
        users.add(user);
        return user;
    }

    @PutMapping("/user/{id}")
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT. Данные тела запроса: {}", user);
        if (!getAllUsers().contains(user)) {
            throw new ValidationException("Пользователя с идентификатором " + user.getId() + " не существует!");
        }
        user.generatedIdUser();
        users.add(user);
        return user;
    }
}

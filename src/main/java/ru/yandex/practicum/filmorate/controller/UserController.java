package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final List<User> users = new LinkedList<>();
    private int count;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получен запрос GET к эндпоинту: /users");
        return users;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос POST. Данные тела запроса: {}", user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("Поле name не задано. Установлено значение {} из поля login", user.getLogin());
        }
        if (user.getId() == 0) {
            user.setId(++count);
        }
        users.add(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT. Данные тела запроса: {}", user);
        for (User userList : users) {
            if (userList.getId() == user.getId()) {
                userList.setEmail(user.getEmail());
                userList.setLogin(user.getLogin());
                userList.setName(user.getName());
                userList.setBirthday(user.getBirthday());
            } else {
                throw new ValidationException("Пользователя с идентификатором " + user.getId() + " не существует!");

            }
        }
        return user;
    }
}

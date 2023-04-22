package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
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
        return new ArrayList<>(users);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос POST. Данные тела запроса: {}", user);
        if (validate(user)) {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
                log.info("Поле name не задано. Установлено значение {} из поля login", user.getLogin());
            }
            if (user.getId() == 0) {
                user.setId(++count);
            }
            users.add(user);
        } else {
            throw new ValidationException("Пользователь не прошел валидацию");
        }
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT. Данные тела запроса: {}", user);
        if (validate(user)) {
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
        } else {
            throw new ValidationException("Пользователь не прошел валидацию");
        }
        return user;
    }

    private boolean validate(User user) {
        boolean isValid = true;
        if (!user.getEmail().contains("@")) {
            log.info("Некорректный e-mail пользователя: {}", user.getEmail());
            return false;
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.info("Некорректный логин пользователя: {}", user.getLogin());
            return false;
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Некорректная дата рождения пользователя: {}", user.getBirthday());
            return false;
        }
        return isValid;
    }
}

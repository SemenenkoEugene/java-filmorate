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
        log.debug("Получен запрос GET к эндпоинту: /users");
        return new ArrayList<>(users);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validate(user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.info("Поле name не задано. Установлено значение {} из поля login пользователя c ID={}", user.getLogin(), user.getId());
        }
        user.setId(++count);
        users.add(user);
        log.debug("Обработан запрос POST к эндпоинту: '/users' на добавление пользователя с ID={}", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT к эндпоинту: '/users' на обновление пользователя с ID={}", user.getId());
        validate(user);
        for (User userList : users) {
            if (userList.getId() == user.getId()) {
                userList.setEmail(user.getEmail());
                userList.setLogin(user.getLogin());
                userList.setName(user.getName());
                userList.setBirthday(user.getBirthday());
            } else {
                throw new ValidationException("Пользователя с идентификатором ID=" + user.getId() + " не существует!");
            }
        }
        log.debug("Обработан запрос PUT к эндпоинту: '/users' на обновление пользователя с ID={}", user.getId());
        return user;
    }

    private void validate(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта должна содержать символ '@'.");
        }
        if (user.getEmail().isEmpty()){
            throw new ValidationException("Электронная почта не может быть пустой.");
        }
        if (user.getLogin().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым.");
        }
        if (user.getLogin().contains(" ")){
            throw new ValidationException("Логин не должен содержать пробелы.");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}

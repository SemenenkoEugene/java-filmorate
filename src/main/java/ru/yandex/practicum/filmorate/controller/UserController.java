package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.debug("Получен запрос GET к эндпоинту: '/users' на получение списка всех пользователей");
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Запрос POST к эндпоинту: '/users' на добавление пользователя с ID={}", user.getId());
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        log.debug("Запрос PUT к эндпоинту: '/users' на обновление пользователя с ID={}", user.getId());
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        log.debug("Запрос GET к эндпоинту: '/users/{id}' на получение на пользователя с ID={}",
                userService.getUserById(id));
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") Integer id) {
        log.debug("Запрос GET к эндпоинту: '/users/{id}/friends' на получение списка друзей пользователя с ID={}",
                userService.getUserById(id));
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") Integer id,
                                             @PathVariable("otherId") Integer otherId) {
        log.debug("Запрос GET к эндпоинту: '/users/{id}/friends/common/{otherId}' на получение списка общих друзей" +
                  " для пользователя с ID={} и пользователя с ID={}",
                userService.getUserById(id), userService.getUserById(otherId));
        return userService.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id,
                          @PathVariable("friendId") Integer friendId) {
        log.debug("Запрос PUT к эндпоинту: '/users/{id}/friends/{friendId}' на добавление пользователя с ID={}" +
                  " в друзья к пользователю с ID={}",
                userService.getUserById(friendId), userService.getUserById(id));
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer id,
                             @PathVariable("friendId") Integer friendId) {
        log.debug("Запрос DELETE к эндпоинту: '/users/{id}/friends/{friendId}' на удаление у пользователя с ID={}" +
                  " из друзей пользователя с ID={}",
                userService.getUserById(id), userService.getUserById(friendId));
        userService.deleteFriend(id, friendId);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable("id") Integer id){
        return userService.deleteUser(id);
    }
}

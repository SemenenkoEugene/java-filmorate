package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private Integer counterId = 1;

    @Override
    public User getUserById(Integer id) {
        if (!users.containsKey(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Пользователь с Id=" + id + " не найден!");
        }
        return users.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(@Valid User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(counterId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(@Valid User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ResponseStatusException(NOT_FOUND, "Пользователь с Id=" + user.getId() + " не найден!");
        }
    }

    @Override
    public User deleteUser(Integer id) {
        if (id == null) {
            throw new ValidationException("Пустой id!");
        }
        if (!users.containsKey(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Пользователь с ID=" + id + " не найден!");
        }

        for (User user : users.values()) {
            user.getFriends().remove(id);
        }
        return users.remove(id);
    }
}

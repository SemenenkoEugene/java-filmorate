package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private Integer counterId = 1;

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(@Valid User user) {
        user.setId(counterId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(@Valid User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(Integer id) {
        for (User user : users.values()) {
            user.getFriends().remove(id);
        }
        return users.remove(id);
    }
}

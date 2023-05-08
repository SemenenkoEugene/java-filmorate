package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;

public interface UserStorage {

    User getUserById(final Integer id);

    Collection<User> getAllUsers();

    User createUser(@Valid User user);

    User updateUser(@Valid User user);

    User deleteUser(final Integer id);
}

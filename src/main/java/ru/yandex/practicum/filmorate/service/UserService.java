package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserById(final Integer id) {
        User userById = userStorage.getUserById(id);
        if (userById == null) {
            throw new NotFoundUserException("Пользователь с Id=" + id + " не найден!");
        }
        return userById;
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (userStorage.getUserById(user.getId()) == null) {
            throw new NotFoundUserException("Пользователь с Id=" + user.getId() + " не найден!");
        }
        return userStorage.updateUser(user);
    }

    public User deleteUser(final Integer id) {
        if (id == null) {
            throw new ValidationException("Пустой id!");
        }
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundUserException("Пользователь с ID=" + id + " не найден!");
        }
        return userStorage.deleteUser(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    /**
     * Добавляет друга для пользователя
     *
     * @param userId   идентификатор пользователя
     * @param friendId идентификатор друга
     */
    public void addFriend(final Integer userId, final Integer friendId) {
        User userById = userStorage.getUserById(userId);
        User friendById = userStorage.getUserById(friendId);
        userById.getFriends().add(friendId);
        friendById.getFriends().add(userId);
    }

    /**
     * Удаляет друга у пользователя
     *
     * @param userId   идентификатор пользователя
     * @param friendId идентификатор друга
     */
    public void deleteFriend(final Integer userId, final Integer friendId) {
        User userById = getUserById(userId);
        User friendById = getUserById(friendId);
        userById.getFriends().remove(friendId);
        friendById.getFriends().remove(userId);
    }

    /**
     * Возвращает список друзей для пользователя
     *
     * @param id идентификатор пользователя
     * @return список друзей
     */
    public List<User> getFriends(final Integer id) {
        Set<Integer> friends = getUserById(id).getFriends();
        return friends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список общих друзей для двух пользователей
     *
     * @param firstUserId  идентификатор первого пользователя
     * @param secondUserId идентификатор второго пользователя
     * @return список общих друзей
     */
    public Collection<User> getCommonFriends(final Integer firstUserId, final Integer secondUserId) {
        User firstUser = getUserById(firstUserId);
        User secondUser = getUserById(secondUserId);
        Set<Integer> commonFriends = new HashSet<>(firstUser.getFriends());
        commonFriends.retainAll(secondUser.getFriends());
        return commonFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}

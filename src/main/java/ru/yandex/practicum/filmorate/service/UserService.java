package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User deleteUser(final Integer id) {
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
        User userById = userStorage.getUserById(userId);
        User friendById = userStorage.getUserById(friendId);
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
        Set<Integer> friends = userStorage.getUserById(id).getFriends();
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
        User firstUser = userStorage.getUserById(firstUserId);
        User secondUser = userStorage.getUserById(secondUserId);
        if (firstUser == null || secondUser == null) {
            return new ArrayList<>();
        }
        Set<Integer> commonFriends = new HashSet<>(firstUser.getFriends());
        commonFriends.retainAll(secondUser.getFriends());
        return commonFriends.stream()
                .map(userStorage::getUserById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}

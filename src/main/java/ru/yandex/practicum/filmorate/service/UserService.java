package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

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
        friendStorage.addFriend(userId, friendId);
    }

    /**
     * Удаляет друга у пользователя
     *
     * @param userId   идентификатор пользователя
     * @param friendId идентификатор друга
     */
    public void deleteFriend(final Integer userId, final Integer friendId) {
        friendStorage.deleteFriend(userId, friendId);
    }

    /**
     * Возвращает список друзей для пользователя
     *
     * @param userId идентификатор пользователя
     * @return список друзей
     */
    public Collection<User> getFriends(final Integer userId) {
        return friendStorage.getFriends(userId);
    }

    /**
     * Возвращает список общих друзей для двух пользователей
     *
     * @param firstUserId  идентификатор первого пользователя
     * @param secondUserId идентификатор второго пользователя
     * @return список общих друзей
     */
    public Collection<User> getCommonFriends(final Integer firstUserId, final Integer secondUserId) {
        return friendStorage.getCommonFriends(firstUserId, secondUserId);
    }
}

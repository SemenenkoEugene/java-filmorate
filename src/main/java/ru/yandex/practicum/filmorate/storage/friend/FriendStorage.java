package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendStorage {
    void addFriend(final Integer userId, final Integer friendId);
    void deleteFriend(final Integer userId, final Integer friendId);
    Collection<User> getFriends(final Integer userId);
    Collection<User> getCommonFriends(final Integer firstUserId, final Integer secondUserId);
}

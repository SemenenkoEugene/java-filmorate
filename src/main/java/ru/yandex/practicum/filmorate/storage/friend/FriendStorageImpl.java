package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Primary
@Component
@RequiredArgsConstructor
public class FriendStorageImpl implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sql = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID, STATUS) VALUES (?,?,?)";
        jdbcTemplate.update(sql, userId, friendId, true);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getFriends(Integer userId) {
        String sql = "SELECT U.ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY FROM USERS U " +
                     "JOIN FRIENDS F ON F.FRIEND_ID = U.ID WHERE F.USER_ID = ?";
        UserMapper userMapper = new UserMapper();
        return jdbcTemplate.query(sql, ps -> ps.setInt(1, userId), userMapper);
    }

    @Override
    public List<User> getCommonFriends(Integer firstUserId, Integer secondUserId) {
        String sql = "SELECT U.ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY FROM FRIENDS F1 " +
                     "JOIN FRIENDS F2 ON F1.FRIEND_ID = F2.FRIEND_ID " +
                     "JOIN USERS U on F1.FRIEND_ID = U.ID WHERE F1.USER_ID = ? AND F2.USER_ID = ?";
        UserMapper userMapper = new UserMapper();
        return jdbcTemplate.query(sql, ps -> {
            ps.setInt(1, firstUserId);
            ps.setInt(2, secondUserId);
        }, userMapper);
    }
}

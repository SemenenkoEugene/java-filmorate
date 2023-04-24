package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    private User user;

    @Autowired
    private UserController userController;


    @Test
    void shouldCreateNewUserAndReturnListUser() {
        user = User.builder()
                .email("gks-08@mail.ru")
                .login("Eugene")
                .name("Eugene")
                .birthday(LocalDate.of(1982, 10, 8))
                .id(1)
                .build();
        User newUser = userController.createUser(user);
        assertEquals(user, newUser);
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void shouldUpdateUser() {
        user = User.builder()
                .email("gks-08@mail.ru")
                .login("Eugene")
                .name("Eugene")
                .birthday(LocalDate.of(1982, 10, 8))
                .id(1)
                .build();
        user.setBirthday(LocalDate.of(1982, 10, 10));
        User updateUser = userController.updateUser(user);
        assertEquals(user, updateUser);
    }
}
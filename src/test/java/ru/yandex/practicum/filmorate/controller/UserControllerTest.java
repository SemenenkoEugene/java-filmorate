package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    private User user;

    @Autowired
    private UserController userController;

    @BeforeEach
    void before() {
        user = User.builder()
                .email("gks-08@mail.ru")
                .login("Eugene")
                .name("Eugene")
                .birthday(LocalDate.of(1982, 10, 8))
                .id(1)
                .build();
    }

    @Test
    void shouldCreateNewUser() {
        User newUser = userController.createUser(user);
        assertEquals(user, newUser);
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void shouldNotCreateNewUserWithEmptyEmail() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldNotCreateNewUserWithIncorrectlyEmail() {
        user.setEmail("ya.ru");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldNotCreateNewUserWithEmptyLogin() {
        user.setLogin("");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldNotCreateNewUserWithIncorrectlyLogin() {
        user.setLogin("Eugene Semenenko");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldCreateNewUserWithEmptyName() {
        user.setName("");
        User newUser = userController.createUser(user);
        assertEquals(newUser.getName(), user.getLogin());
    }

    @Test
    void shouldNotCreateNewUserWithIncorrectlyBirthday() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }
}
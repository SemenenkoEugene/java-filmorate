package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FilmorateApplicationTests {

    @Autowired
    private FilmController filmController;

    @Autowired
    private UserController userController;

    private final Film film = new Film(1,
            "Transcendence",
            "Dr. Will Custer, an outstanding researcher in the field of artificial intelligence studies," +
            " is working on the creation of a computer",
            LocalDate.of(2014, 4, 18),
            119);

    private final User user = new User(1,
            "gks-08@mail.ru",
            "Eugene",
            "Eugene",
            LocalDate.of(1982, 10, 8));

    @BeforeEach
    public void init() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    void shouldCreateNewFilmAndReturnListFilm() {
        Film newFilm = filmController.createFilm(film);
        assertEquals(film, newFilm);
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    void shouldUpdateFilm() {
        Film createFilm = filmController.createFilm(film);
        createFilm.setDuration(120);
        Film updateFilm = filmController.updateFilm(film);
        assertEquals(film, updateFilm);
    }

    @Test
    void shouldCreateNewUserAndReturnListUser() {
        User newUser = userController.createUser(user);
        assertEquals(user, newUser);
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void shouldUpdateUser() {
        User createUser = userController.createUser(user);
        createUser.setBirthday(LocalDate.of(1982, 10, 10));
        User updateUser = userController.updateUser(user);
        assertEquals(user, updateUser);
    }

}

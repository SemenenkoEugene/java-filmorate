package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FilmorateApplicationTests {

    private User user;
    private Film film;
    @Autowired
    private FilmController filmController;

    @Autowired
    private UserController userController;

    @Test
    void shouldCreateNewFilmAndReturnListFilm() {
        film = Film.builder()
                .name("Transcendence")
                .description("Dr. Will Custer, an outstanding researcher in the field of artificial intelligence studies," +
                             " is working on the creation of a computer")
                .releaseDate(LocalDate.of(2014, 4, 18))
                .duration(119)
                .id(1)
                .build();
        Film newFilm = filmController.createFilm(film);
        assertEquals(film, newFilm);
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    void shouldUpdateFilm() {
        film = Film.builder()
                .name("Transcendence")
                .description("Dr. Will Custer, an outstanding researcher in the field of artificial intelligence studies," +
                             " is working on the creation of a computer")
                .releaseDate(LocalDate.of(2014, 4, 18))
                .duration(119)
                .id(1)
                .build();
        film.setDuration(120);
        Film updateFilm = filmController.updateFilm(film);
        assertEquals(film, updateFilm);

    }

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

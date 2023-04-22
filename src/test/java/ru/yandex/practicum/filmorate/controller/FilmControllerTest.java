package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {


    private Film film;
    @Autowired
    private FilmController filmController;

    @BeforeEach
    public void before() {
        film = Film.builder()
                .name("Transcendence")
                .description("Dr. Will Custer, an outstanding researcher in the field of artificial intelligence studies," +
                             " is working on the creation of a computer")
                .releaseDate(LocalDate.of(2014, 4, 18))
                .duration(119)
                .id(1)
                .build();
    }

    @Test
    void shouldCreateNewFilm() {
        Film newFilm = filmController.createFilm(film);
        assertEquals(film, newFilm);
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    void shouldNotCreateNewFilmWithEmptyName() {
        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldNotCreateNewFilmWithLongDescription() {
        film.setDescription(film.getDescription()+film.getDescription());
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldNotCreateNewFilmWithoutDescription() {
        film.setDescription("");
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldNotCreateNewFilmIncorrectDateRelease() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldNotCreateNewFilmWithDurationZero() {
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldNotCreateNewFilmWithDurationIsNegative() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }
}
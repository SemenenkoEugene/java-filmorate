package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {


    private Film film;
    @Autowired
    private FilmController filmController;

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
}
package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private int count;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос GET к эндпоинту: /films");
        return films;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST. Данные тела запроса: {}", film);
        if (film.getId() == 0) {
            film.setId(++count);
        }
        films.add(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT. Данные тела запроса: {}", film);
        for (Film listFilm : films) {
            if (listFilm.getId() == film.getId()) {
                listFilm.setName(film.getName());
                listFilm.setDescription(film.getDescription());
                listFilm.setDuration(film.getDuration());
                listFilm.setReleaseDate(film.getReleaseDate());
            } else {
                throw new ValidationException("Фильма с идентификатором " + film.getId() + " не существует!");
            }
        }
        return film;
    }
}

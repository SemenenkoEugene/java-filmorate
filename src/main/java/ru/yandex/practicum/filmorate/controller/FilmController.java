package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос GET к эндпоинту: /films");
        Collection<Film> allFilms = films.values();
        if (allFilms.isEmpty()) {
            allFilms.addAll(films.values());
        }
        return allFilms;
    }

    @PostMapping("/film")
    public Film createFilm(@RequestBody Film film) {
        log.info("Получен запрос POST. Данные тела запроса: {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос PUT. Данные тела запроса: {}", film);
        if (!getAllFilms().contains(film)) {
            throw new ValidationException("Фильма с идентификатором " + film.getId() + " не существует!");
        }
        films.put(film.getId(), film);
        return film;
    }
}

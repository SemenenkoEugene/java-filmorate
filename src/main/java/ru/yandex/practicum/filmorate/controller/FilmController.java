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

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос GET к эндпоинту: /films");
        return new ArrayList<>(films);
    }

    @PostMapping("/film")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST. Данные тела запроса: {}", film);
        film.generatedIdFilm();
        films.add(film);
        return film;
    }

    @PutMapping("/film/{id}")
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT. Данные тела запроса: {}", film);
        if (!getAllFilms().contains(film)) {
            throw new ValidationException("Фильма с идентификатором " + film.getId() + " не существует!");
        }
        film.generatedIdFilm();
        films.add(film);
        return film;
    }
}

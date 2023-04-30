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
        log.debug("Получен запрос GET к эндпоинту: '/films'");
        return new ArrayList<>(films);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            film.setId(++count);
        }
        films.add(film);
        log.debug("Обработан запрос POST к эндпоинту: '/films' на добавление фильма с ID={}", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.debug("Получен запрос PUT к эндпоинту: '/films' на обновление фильма с ID={}", film.getId());
        for (Film listFilm : films) {
            if (Objects.equals(listFilm.getId(), film.getId())) {
                listFilm.setName(film.getName());
                listFilm.setDescription(film.getDescription());
                listFilm.setDuration(film.getDuration());
                listFilm.setReleaseDate(film.getReleaseDate());
            } else {
                throw new ValidationException("Фильма с идентификатором " + film.getId() + " не существует!");
            }
        }
        log.debug("Обработан запрос PUT к эндпоинту: '/films' на обновление фильма с ID={}", film.getId());
        return film;
    }
}
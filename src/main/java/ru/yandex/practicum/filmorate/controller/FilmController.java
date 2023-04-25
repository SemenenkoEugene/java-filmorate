package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
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
        validate(film);
        film.setId(++count);
        films.add(film);
        log.debug("Обработан запрос POST к эндпоинту: '/films' на добавление фильма с ID={}", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT к эндпоинту: '/films' на обновление фильма с ID={}", film.getId());
        validate(film);
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
        log.debug("Обработан запрос PUT к эндпоинту: '/films' на обновление фильма с ID={}", film.getId());
        return film;
    }

    private void validate(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Фильм не может быть без названия.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не должно быть длиннее 200 знаков.");
        }
        if (film.getDescription().isEmpty()) {
            throw new ValidationException("Описание фильма не должно быть пустым.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

}
package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.debug("Получен запрос GET к эндпоинту: '/films'");
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Запрос POST к эндпоинту: '/films' на добавление фильма с ID={}", film.getId());
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Запрос PUT к эндпоинту: '/films' на обновление фильма с ID={}", film.getId());
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer id) {
        log.debug("Запрос GET к эндпоинту: '/films/{id}' на получение фильма с ID={}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        log.debug("Запрос GET к эндпоинту: '/films/popular' на получение {} популярных фильмов", count);
        return filmService.getPopular(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer id,
                        @PathVariable("userId") Integer userId) {
        log.debug("Запрос PUT к эндпоинту: '/films/{id}/like/{userId}' на добавление like для фильма с ID={} " +
                  "от пользователя с userID={}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer id,
                           @PathVariable("userId") Integer userId) {
        log.debug("Запрос DELETE к эндпоинту: '/films/{id}/like/{userId}' на удаление like для фильма с ID={} " +
                  "от пользователя с userID={}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public Film deleteFilm(@PathVariable("id") Integer id) {
        log.debug("Запрос DELETE к эндпоинту: '/films' на удаление фильма с ID={}", id);
        return filmService.deleteFilm(id);
    }
}
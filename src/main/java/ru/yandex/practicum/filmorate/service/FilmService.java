package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film deleteFilm(Integer id) {
        return filmStorage.deleteFilm(id);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    /**
     * Добавляет like пользователя к фильму
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    public void addLike(Integer filmId, Integer userId) {
        Film filmById = filmStorage.getFilmById(filmId);
        if (userStorage.getUserById(userId) != null) {
            filmById.getLikes().add(userId);
        } else {
            throw new ResponseStatusException(NOT_FOUND, "Пользователь c ID=" + userId + " не найден!");
        }
    }

    /**
     * Удаляет like пользователя к фильму
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    public void deleteLike(Integer filmId, Integer userId) {
        Film filmById = filmStorage.getFilmById(filmId);
        if (!filmById.getLikes().remove(userId)) {
            throw new ResponseStatusException(NOT_FOUND, "Лайк от пользователя c ID=" + userId + " не найден!");
        }
    }

    /**
     * Возвращает список популярных фильмов по количеству like
     *
     * @param count число фильмов в списке
     * @return список фильмов по популярности
     */
    public Collection<Film> getPopular(Integer count) {
        if (count < 1) {
            throw new ValidationException("Количество фильмов для вывода не должно быть меньше 1");
        }
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}

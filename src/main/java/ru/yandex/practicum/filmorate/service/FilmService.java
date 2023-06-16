package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

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
        likeStorage.addLike(filmId, userId);
    }

    /**
     * Удаляет like пользователя к фильму
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     */
    public void deleteLike(Integer filmId, Integer userId) {
        likeStorage.deleteLike(filmId, userId);
    }

    /**
     * Возвращает список популярных фильмов по количеству like
     *
     * @param count число фильмов в списке
     * @return список фильмов по популярности
     */
    public Collection<Film> getPopular(Integer count) {
        return likeStorage.getPopular(count);
    }
}

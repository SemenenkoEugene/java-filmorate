package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;

public interface FilmStorage {

    Film createFilm(@Valid Film film);

    Film updateFilm(@Valid Film film);

    Film deleteFilm(final Integer id);

    Collection<Film> getAllFilms();

    Film getFilmById(final Integer id);
}

package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.validation.Valid;
import java.util.Collection;

public interface GenreStorage {
    Collection<Genre> getGenres();

    Genre getGenreById(final Integer genreId);

    void deleteFilmGenre(@Valid Film film);

    void addFilmGenre(@Valid Film film);

    Collection<Genre> getFilmGenre(final Integer filmId);

    void updateGenres(Film film);
}

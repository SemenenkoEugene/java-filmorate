package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private Integer counterId = 1;

    @Override
    public Film createFilm(@Valid Film film) {
        film.setId(counterId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(@Valid Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteFilm(Integer id) {
        return films.remove(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer id) {
        return films.get(id);
    }
}

package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ResponseStatusException(NOT_FOUND, "Фильм с ID=" + film.getId() + " не найден!");
        }
    }

    @Override
    public Film deleteFilm(Integer id) {
        if (id == null) {
            throw new ValidationException("Передан пустой ID!");
        }
        if (!films.containsKey(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Фильм с ID=" + id + " не найден!");
        }
        return films.remove(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer id) {
        if (!films.containsKey(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Фильм с ID=" + id + " не найден!");

        }
        return films.get(id);
    }
}

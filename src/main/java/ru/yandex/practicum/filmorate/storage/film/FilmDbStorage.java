package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmColumn;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.*;

@Primary
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;


    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> map = new HashMap<>();
        map.put("name", film.getName());
        map.put("description", film.getDescription());
        map.put("duration", film.getDuration());
        map.put("release_date", film.getReleaseDate());
        map.put("rating_id", film.getMpa().getId());
        int value = simpleJdbcInsert.executeAndReturnKey(map).intValue();
        film.setId(value);
        film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
        genreStorage.addFilmGenre(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE FILMS SET NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, RATING_ID=? WHERE ID=?";
        int updateCount = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (updateCount != 0) {
            film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
            genreStorage.updateGenres(film);
            return film;
        } else {
            throw new NotFoundFilmException("Фильм с Id=" + film.getId() + " не найден!");
        }
    }

    @Override
    public Film deleteFilm(Integer filmId) {
        Film film = getFilmById(filmId);
        String sql = "DELETE FROM FILMS WHERE ID=?";
        if (jdbcTemplate.update(sql, filmId) == 0) {
            throw new NotFoundFilmException("Фильм с Id=" + filmId + " не найден!");
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT F.*, G.*, M.*" +
                     "FROM FILMS F LEFT JOIN FILM_GENRES GF ON GF.FILM_ID = F.ID " +
                     "LEFT JOIN GENRES G ON G.ID = GF.GENRE_ID " +
                     "LEFT JOIN RATINGS_MPA M ON F.RATING_ID = M.ID";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> new Film(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new Mpa(rs.getInt("rating_id"), rs.getString("name")),
                new HashSet<>(genreStorage.getFilmGenre(rs.getInt("id"))),
                new HashSet<>(likeStorage.getLikes(rs.getInt("id")))
        )));
    }

    @Override
    public Film getFilmById(Integer filmId) {
        String sqlQuery = "SELECT * FROM FILMS WHERE ID = ?";
        try {
            FilmColumn filmColumn = jdbcTemplate.queryForObject(sqlQuery, new FilmMapper(), filmId);
            return fromColumnsToDao(Objects.requireNonNull(filmColumn));
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundFilmException("Фильм с Id=" + filmId + " не найден!");
        }
    }

    private Film fromColumnsToDao(FilmColumn filmColumn) {
        Film film = new Film();
        film.setId(filmColumn.getId());
        film.setName(filmColumn.getName());
        film.setDescription(filmColumn.getDescription());
        film.setDuration(filmColumn.getDuration());
        film.setReleaseDate(filmColumn.getReleaseDate());
        film.setMpa(mpaStorage.getMpaById(filmColumn.getMpaId()));
        film.setGenres(new HashSet<>(genreStorage.getFilmGenre(film.getId())));
        film.setLikes(new HashSet<>(likeStorage.getLikes(filmColumn.getId())));
        return film;
    }
}

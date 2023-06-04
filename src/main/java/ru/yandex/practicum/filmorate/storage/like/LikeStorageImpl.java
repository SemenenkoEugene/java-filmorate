package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundLikeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Primary
@Component
@RequiredArgsConstructor
public class LikeStorageImpl implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final GenreService genreService;

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO FILM_LIKES (film_id, user_id) VALUES (?,?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        String sql = "SELECT COUNT(*) FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, filmId, userId);
        if (count != null && count > 0) {
            String deleteSql = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
            jdbcTemplate.update(deleteSql, filmId, userId);
        } else {
            throw new NotFoundLikeException("Лайк для фильма " + filmId + " пользователя " + userId + " не найден!");
        }
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        String sql = "SELECT FILMS.*, RATING_ID " +
                     "FROM FILMS LEFT " +
                     "JOIN FILM_LIKES ON FILMS.ID = FILM_LIKES.FILM_ID " +
                     "GROUP BY FILMS.ID " +
                     "ORDER BY COUNT(FILM_LIKES.USER_ID) DESC " +
                     "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Integer filmId = rs.getInt("id");
            String filmName = rs.getString("name");
            String filmDescription = rs.getString("description");
            LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
            int duration = rs.getInt("duration");
            Mpa mpa = mpaService.getMpaById(rs.getInt("rating_id"));
            Set<Genre> genres = genreService.getFilmGenre(filmId);
            Set<Integer> likes = new HashSet<>(getLikes(filmId));

            Film film = new Film();
            film.setId(filmId);
            film.setName(filmName);
            film.setDescription(filmDescription);
            film.setReleaseDate(releaseDate);
            film.setDuration(duration);
            film.setMpa(mpa);
            film.setGenres(genres);
            film.setLikes(likes);

            return film;
        }, count);
    }

    @Override
    public Collection<Integer> getLikes(Integer filmId) {
        String sql = "SELECT USER_ID FROM FILM_LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, new LikeMapper(), filmId);
    }
}

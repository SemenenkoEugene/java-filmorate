package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Primary
@Component
@RequiredArgsConstructor
public class GenreStorageImpl implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    GenreMapper genreMapper = new GenreMapper();

    public List<Genre> getGenres() {
        String sql = "SELECT * FROM GENRES ORDER BY ID";
        return jdbcTemplate.query(sql, genreMapper);
    }

    public Genre getGenreById(Integer genreId) {
        if (genreId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        Genre genre;
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE ID=?", genreId);
        if (genreRows.first()) {
            genre = new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name")
            );
        } else {
            throw new GenreNotFoundException("Жанр с Id=" + genreId + " не найден!");
        }
        return genre;
    }

    public void deleteFilmGenre(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE FILM_ID = ?", film.getId());
    }

    public void addFilmGenre(Film film) {
        if (film.getGenres() != null) {
            List<Genre> genres = new ArrayList<>(film.getGenres());
            jdbcTemplate.batchUpdate("INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?,?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, film.getId());
                            ps.setInt(2, genres.get(i).getId());
                        }

                        @Override
                        public int getBatchSize() {
                            return genres.size();
                        }
                    });
        }
    }

    public List<Genre> getFilmGenre(Integer filmId) {
        String sql = "SELECT * FROM FILM_GENRES INNER JOIN GENRES ON GENRE_ID = ID WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, genreMapper, filmId);
    }

    public void updateGenres(Film film) {
        String deleteGenresQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(deleteGenresQuery, film.getId());
        String insertGenresQuery = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(insertGenresQuery, film.getId(), genre.getId());
        }
    }
}

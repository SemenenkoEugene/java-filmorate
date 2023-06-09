package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FilmColumn;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmColumnMapper implements RowMapper<FilmColumn> {
    @Override
    public FilmColumn mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmColumn film = new FilmColumn();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getInt("duration"));
        film.setMpaId(rs.getInt("rating_id"));
        film.setReleaseDate(rs.getDate("release_Date").toLocalDate());
        return film;
    }
}

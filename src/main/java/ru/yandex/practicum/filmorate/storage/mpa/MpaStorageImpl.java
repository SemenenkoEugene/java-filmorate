package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
@Primary
@Component
@RequiredArgsConstructor
public class MpaStorageImpl implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM RATINGS_MPA ORDER BY ID";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> new Mpa(
                rs.getInt("id"),
                rs.getString("name")
        )));
    }

    public Mpa getMpaById(Integer mpaId) {
        if (mpaId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        Mpa mpa;
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM RATINGS_MPA WHERE ID = ?", mpaId);
        if (mpaRows.first()) {
            mpa = new Mpa(
                    mpaRows.getInt("id"),
                    mpaRows.getString("name")
            );
        } else {
            throw new MpaNotFoundException("Рейтинг с Id=" + mpaId + " не найден!");
        }
        return mpa;
    }
}

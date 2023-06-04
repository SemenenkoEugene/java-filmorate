package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FilmColumn {

    private Integer id;
    private String name;
    private String description;
    private int duration;
    private Integer mpaId;
    private LocalDate releaseDate;
}

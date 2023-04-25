package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateValidation;

import java.time.LocalDate;

@Data
@Builder
public class Film {

    private int id;

    private String name;

    private String description;

    @ReleaseDateValidation
    private LocalDate releaseDate;

    private long duration;
}

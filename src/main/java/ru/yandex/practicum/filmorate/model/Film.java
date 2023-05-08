package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Integer id;

    @NotNull(message = "Поле название фильма пустое.")
    @NotBlank(message = "Название фильма не должно быть пустым.")
    private String name;

    @NotNull(message = "Поле описание фильма пустое.")
    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов.")
    private String description;

    @NotNull(message = "Поле даты релиза пустое.")
    @ReleaseDateValidation(message = "Дата релиза раньше 28-12-1895.")
    private LocalDate releaseDate;

    @NotNull(message = "Поле продолжительности фильма пустое.")
    @Positive(message = "Продолжительность фильма не может быть отрицательной.")
    private long duration;

    private Set<Integer> likes;

    public Film(Integer id, String name, String description, LocalDate releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
    }
}

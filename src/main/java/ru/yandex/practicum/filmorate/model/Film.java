package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    @NotNull(message = "Поле рейтинг фильма пустое.")
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();

    private Set<Integer> likes = new HashSet<>();

    public void setGenres(Set<Genre> genres) {
        this.genres = genres.stream()
                .sorted(Comparator.comparingInt(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {

    private int id;

    @NotBlank(message = "Название фильма не должно быть пустым.")
    private String name;

    @Size(max = 200,message = "Описание фильма не должно превышать 200 символов.")
    private String description;

    @PastOrPresent(message = "Дата релиза не должна быть в будущем.")
    @ReleaseDateValidation
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма не может быть отрицательной.")
    private long duration;
}

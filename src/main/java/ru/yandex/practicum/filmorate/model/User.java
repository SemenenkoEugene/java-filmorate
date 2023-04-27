package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class User {

    private Integer id;

    @NotNull
    @Email(message = "Введенное значение не является адресом электронной почты.")
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы.")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;


}

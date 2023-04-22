package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {


    private Film film;
    @Autowired
    private FilmController filmController;

    @BeforeEach
    public void before() {
        film = Film.builder()
                .name("Превосходство")
                .description("Выдающийся исследователь в области изучения искусственного разума доктор" +
                             "Уилл Кастер работает над созданием компьютера")
                .releaseDate(LocalDate.of(2014, 4, 18))
                .duration(119)
                .id(1)
                .build();
    }

    @Test
    void shouldCreateNewFilm() {
        Film newFilm = filmController.createFilm(film);
        assertEquals(film, newFilm);
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    void shouldNotCreateNewFilmWithEmptyName() {
        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldNotCreateNewFilmWithLongDescription() {
        film.setDescription("Выдающийся исследователь в области изучения искусственного разума доктор Уилл Кастер " +
                            "работает над созданием компьютера, который, по его словам, " +
                            "сможет достичь состояния «превосходства» (то есть, технологической сингулярности), " +
                            "превзойдя разум всего человечества и собрав в себе все знания и опыт, накопленные людьми." +
                            " Однако радикальная антитехнологическая группировка RIFT" +
                            " (букв. «Революционная независимость от технологии») совершает серию террористических актов," +
                            " уничтожая основные лаборатории по разработке искусственного интеллекта, " +
                            "убивая многих исследователей и смертельно раня самого Уилла пулей, отравленной полонием." +
                            " Уиллу остаётся жить не более пяти недель.\n" +
                            "\n" +
                            "Жена Уилла Эвелин и лучший друг Макс Уотерс узнают," +
                            " что один из убитых учёных успел создать алгоритм записи мозговой активности," +
                            " то есть создания компьютерной копии разума. Используя алгоритм, " +
                            "Эвелин копирует сознание Уилла на жёсткие диски суперкомпьютера PINN, " +
                            "созданного Кастером ранее, и хотя тело Уилла умирает, его разум возрождается в машине," +
                            " таким образом обретая цифровое бессмертие. RIFT, следившие за процессом," +
                            " не успевают уничтожить Уилла, и тот загружает себя в Интернет, становясь недосягаемым." +
                            " В этой форме он обладает безграничными умственными способностями (как и предполагал при жизни)," +
                            " которые использует, чтобы раскрыть правительству личности большинства участников RIFT," +
                            " добившись тем самым их ареста. Лидер RIFT Бри," +
                            " остатки её организации и захваченный ими Макс скрываются в глухом лесу," +
                            " где Уилл не может их найти из-за отсутствия инфраструктуры. " +
                            "Постепенно Бри убеждает Макса, что Уилл опасен, но тот не видит способа как-либо остановить разум," +
                            " превосходящий любого человека.");
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldNotCreateNewFilmWithoutDescription() {
        film.setDescription("");
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldNotCreateNewFilmIncorrectDateRelease() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldNotCreateNewFilmWithDurationZero() {
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldNotCreateNewFilmWithDurationIsNegative() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }
}
package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmorateApplicationTests {

    private final UserStorage userStorage;
    private final UserService userService;
    private final FilmStorage filmStorage;
    private final FilmService filmService;
    private User user1;
    private User user2;
    private User user3;
    private Film film1;
    private Film film2;
    private Film film3;

    @BeforeEach
    public void init() {
        user1 = new User();
        user1.setEmail("first@test.ru");
        user1.setLogin("first");
        user1.setName("User1");
        user1.setBirthday(LocalDate.of(1982, 10, 8));

        user2 = new User();
        user2.setEmail("second@test.ru");
        user2.setLogin("second");
        user2.setName("User2");
        user2.setBirthday(LocalDate.of(1983, 3, 24));

        user3 = new User();
        user3.setEmail("third@test.ru");
        user3.setLogin("third");
        user3.setName("User3");
        user3.setBirthday(LocalDate.of(2006, 12, 25));

        film1 = new Film();
        film1.setName("Film1");
        film1.setDescription("description Film1");
        film1.setReleaseDate(LocalDate.of(1989, 4, 14));
        film1.setDuration(120);
        film1.setMpa(new Mpa(1, "G"));
        film1.setLikes(new HashSet<>());
        film1.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Документальный"), new Genre(1, "Комедия"))));

        film2 = new Film();
        film2.setName("Film2");
        film2.setDescription("description Film2");
        film2.setReleaseDate(LocalDate.of(2015, 5, 30));
        film2.setDuration(108);
        film2.setMpa(new Mpa(3, "PG"));
        film2.setLikes(new HashSet<>());
        film2.setGenres(new HashSet<>(Collections.singletonList(new Genre(6, "Боевик"))));

        film3 = new Film();
        film3.setName("Film3");
        film3.setDescription("description Film3");
        film3.setReleaseDate(LocalDate.of(1999, 7, 23));
        film3.setDuration(90);
        film3.setMpa(new Mpa(4, "NC-17"));
        film3.setLikes(new HashSet<>());
        film3.setGenres(new HashSet<>(Collections.singletonList(new Genre(4, "Триллер"))));
    }

    @Test
    void shouldCreateUserAndGetUserById() {
        User createUser = userStorage.createUser(user1);
        User userById = userStorage.getUserById(createUser.getId());
        assertThat(userById)
                .extracting(User::getId, User::getEmail, User::getLogin, User::getName, User::getBirthday)
                .containsExactly(
                        createUser.getId(),
                        createUser.getEmail(),
                        createUser.getLogin(),
                        createUser.getName(),
                        createUser.getBirthday()
                );
    }

    @Test
    void shouldCreateFilmAndGetFilmById() {
        Film createFilm = filmStorage.createFilm(film1);
        Film filmById = filmStorage.getFilmById(createFilm.getId());
        assertThat(filmById)
                .extracting(Film::getId, Film::getName)
                .containsExactly(createFilm.getId(), "Film1");
    }

    @Test
    void shouldUpdateUser() {
        User createUser = userStorage.createUser(user1);

        User updateUser = new User();
        updateUser.setId(createUser.getId());
        updateUser.setEmail("first@test.ru");
        updateUser.setLogin("first");
        updateUser.setName("UpdateUser1");
        updateUser.setBirthday(LocalDate.of(1982, 10, 8));

        User updateUserStorage = userStorage.updateUser(updateUser);
        assertThat(updateUserStorage)
                .extracting(User::getName)
                .isEqualTo("UpdateUser1");
    }

    @Test
    void shouldUpdateFilm() {
        Film createFilm = filmStorage.createFilm(film1);
        Film updateFilm = new Film();
        updateFilm.setId(createFilm.getId());
        updateFilm.setName("Update Name Film1");
        updateFilm.setDescription("Update Description Film1");
        updateFilm.setReleaseDate(LocalDate.of(1989, 4, 14));
        updateFilm.setDuration(125);
        updateFilm.setMpa(new Mpa(1, "G"));
        Film updateFilmStorage = filmStorage.updateFilm(updateFilm);
        assertThat(updateFilmStorage)
                .hasFieldOrPropertyWithValue("name", "Update Name Film1")
                .hasFieldOrPropertyWithValue("description", "Update Description Film1")
                .hasFieldOrPropertyWithValue("duration", 125L);
    }

    @Test
    void shouldDeleteUser() {
        User createUser = userStorage.createUser(user1);
        userStorage.deleteUser(createUser.getId());
        Collection<User> allUsers = userStorage.getAllUsers();
        assertThat(allUsers).isEmpty();
    }

    @Test
    void shouldDeleteFilm() {
        Film createFilm = filmStorage.createFilm(film1);
        filmStorage.deleteFilm(createFilm.getId());
        Collection<Film> allFilms = filmStorage.getAllFilms();
        assertThat(allFilms).isEmpty();
    }

    @Test
    void shouldAddLike() {
        User createUser = userStorage.createUser(user1);
        Film createFilm = filmStorage.createFilm(film1);
        filmService.addLike(createFilm.getId(), createUser.getId());
        Film filmById = filmStorage.getFilmById(createFilm.getId());
        assertThat(filmById.getLikes())
                .hasSize(1)
                .contains(createUser.getId());

    }

    @Test
    void shouldDeleteLike() {
        User createUser1 = userStorage.createUser(user1);
        User createUser2 = userStorage.createUser(user2);
        Film createFilm = filmStorage.createFilm(film1);
        filmService.addLike(createFilm.getId(), createUser1.getId());
        filmService.addLike(createFilm.getId(), createUser2.getId());
        filmService.deleteLike(createFilm.getId(), createUser1.getId());
        Film filmById = filmStorage.getFilmById(createFilm.getId());
        assertThat(filmById.getLikes())
                .hasSize(1)
                .contains(createUser2.getId());
    }

    @Test
    void shouldGetPopularFilms() {
        User createUser1 = userStorage.createUser(user1);
        User createUser2 = userStorage.createUser(user2);
        User createUser3 = userStorage.createUser(user3);

        Film createFilm1 = filmStorage.createFilm(film1);
        Film createFilm2 = filmStorage.createFilm(film2);
        Film createFilm3 = filmStorage.createFilm(film3);

        filmService.addLike(createFilm1.getId(), createUser1.getId());
        filmService.addLike(createFilm1.getId(), createUser2.getId());
        filmService.addLike(createFilm1.getId(), createUser3.getId());

        filmService.addLike(createFilm2.getId(), createUser1.getId());

        filmService.addLike(createFilm3.getId(), createUser2.getId());
        filmService.addLike(createFilm3.getId(), createUser3.getId());

        Collection<Film> filmServicePopular = filmService.getPopular(5);
        assertThat(filmServicePopular)
                .hasSize(3)
                .extracting(Film::getName)
                .containsExactly("Film1", "Film3", "Film2");

    }

    @Test
    void shouldAddFriend() {
        User createUser1 = userStorage.createUser(user1);
        User createUser2 = userStorage.createUser(user2);
        userService.addFriend(createUser1.getId(), createUser2.getId());
        Collection<User> friends = userService.getFriends(createUser1.getId());
        assertThat(friends)
                .hasSize(1)
                .anyMatch(user -> user.getId().equals(createUser2.getId()));
    }

    @Test
    void shouldDeleteFriend() {
        User createUser1 = userStorage.createUser(user1);
        User createUser2 = userStorage.createUser(user2);
        User createUser3 = userStorage.createUser(user3);

        userService.addFriend(createUser1.getId(), createUser2.getId());
        userService.addFriend(createUser1.getId(), createUser3.getId());
        userService.deleteFriend(createUser1.getId(), createUser3.getId());

        Collection<User> friends = userService.getFriends(createUser1.getId());
        assertThat(friends)
                .hasSize(1)
                .anyMatch(user -> user.getId().equals(createUser2.getId()));
    }

    @Test
    void shouldGetFriends() {
        User createUser1 = userStorage.createUser(user1);
        User createUser2 = userStorage.createUser(user2);
        User createUser3 = userStorage.createUser(user3);

        userService.addFriend(createUser1.getId(), createUser2.getId());
        userService.addFriend(createUser1.getId(), createUser3.getId());

        Collection<User> friends = userService.getFriends(createUser1.getId());
        assertThat(friends)
                .hasSize(2)
                .anyMatch(user -> user.getId().equals(createUser2.getId()))
                .anyMatch(user -> user.getId().equals(createUser3.getId()));

    }

    @Test
    void shouldGetCommonFriends() {
        User createUser1 = userStorage.createUser(user1);
        User createUser2 = userStorage.createUser(user2);
        User createUser3 = userStorage.createUser(user3);

        userService.addFriend(createUser1.getId(), createUser2.getId());
        userService.addFriend(createUser1.getId(), createUser3.getId());
        userService.addFriend(createUser2.getId(), createUser1.getId());
        userService.addFriend(createUser2.getId(), createUser3.getId());

        List<User> commonFriends = (List<User>) userService.getCommonFriends(createUser1.getId(), createUser2.getId());
        assertThat(commonFriends)
                .hasSize(1)
                .satisfies(users -> assertThat(users.get(0).getId()).isEqualTo(createUser3.getId()));
    }

}

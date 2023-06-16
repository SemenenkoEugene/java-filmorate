package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface LikeStorage {
    void addLike(final Integer filmId, final Integer userId);

    void deleteLike(final Integer filmId, final Integer userId);

    Collection<Film> getPopular(final Integer count);

    Collection<Integer> getLikes(final Integer filmId);
}

package ru.yandex.practicum.filmorate.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError validationHandle(ValidationException e) {
        log.error(e.getMessage());
        return new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError notFoundFilmHandle(NotFoundFilmException e) {
        log.error(e.getMessage());
        return new ResponseError(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError notFoundUserHandle(NotFoundUserException e) {
        log.error(e.getMessage());
        return new ResponseError(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @Getter
    @RequiredArgsConstructor
    private static class ResponseError {
        private final String message;
        private final HttpStatus status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private final LocalDateTime localDateTime = LocalDateTime.now();
    }
}
package io.github.LoucterSo.fitness_app.api.handle;

import io.github.LoucterSo.fitness_app.exception.user.UserNotFoundException;
import io.github.LoucterSo.fitness_app.form.error.ErrorResponse;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(exception = {
            UserNotFoundException.class,
            UserNotFoundException.class
    })
    public ErrorResponse handleUserNotFoundException(Exception ex) {

        return new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
    }
}

package io.github.LoucterSo.fitness_app.api.handle;


import io.github.LoucterSo.fitness_app.exception.user.UserNotFoundException;
import io.github.LoucterSo.fitness_app.form.error.ErrorResponse;
import io.github.LoucterSo.fitness_app.form.error.ValidationErrorResponse;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

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

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ValidationErrorResponse handleValidationHasErrorsException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ValidationErrorResponse(errors, System.currentTimeMillis());
    }
}

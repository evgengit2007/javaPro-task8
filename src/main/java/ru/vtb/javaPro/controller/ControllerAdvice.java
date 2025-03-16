package ru.vtb.javaPro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vtb.javaPro.exception.ExceptionRequest;
import ru.vtb.javaPro.response.ResponseError;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ExceptionRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handlerRequestException(ExceptionRequest exceptionRequest) {
        return new ResponseError(exceptionRequest.getHttpStatus().name(), exceptionRequest.getMessage());
    }
}

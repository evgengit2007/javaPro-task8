package ru.vtb.javaPro.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionRequest extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String message;

    public ExceptionRequest(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.message = message;
    }

    public ExceptionRequest(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

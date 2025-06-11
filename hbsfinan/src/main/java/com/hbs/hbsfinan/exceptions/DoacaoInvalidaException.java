package com.hbs.hbsfinan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Retorna 400 Bad Request
public class DoacaoInvalidaException extends RuntimeException {

    public DoacaoInvalidaException(String message) {
        super(message);
    }

    public DoacaoInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
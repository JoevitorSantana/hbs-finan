package com.hbs.hbsfinan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Retorna 404 Not Found
public class FuncionarioNotFoundException extends RuntimeException {

    public FuncionarioNotFoundException(String message) {
        super(message);
    }

    public FuncionarioNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
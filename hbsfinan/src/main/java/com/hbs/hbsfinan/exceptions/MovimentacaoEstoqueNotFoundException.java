package com.hbs.hbsfinan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovimentacaoEstoqueNotFoundException extends RuntimeException {
    public MovimentacaoEstoqueNotFoundException(String message) {
        super(message);
    }
    public MovimentacaoEstoqueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
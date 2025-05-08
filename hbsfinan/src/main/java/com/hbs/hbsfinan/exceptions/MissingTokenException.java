package com.hbs.hbsfinan.exceptions;

public class MissingTokenException extends RuntimeException {

    public MissingTokenException(String message) {
        super(message);
    }

    public MissingTokenException() {
        super("Token inválido ou expirado!");
    }
}

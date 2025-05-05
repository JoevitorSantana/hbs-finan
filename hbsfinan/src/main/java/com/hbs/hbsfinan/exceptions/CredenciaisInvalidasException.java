package com.hbs.hbsfinan.exceptions;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() {
        super("Email/Senha Invalida!");
    }
    public CredenciaisInvalidasException(String message) {
        super(message);
    }
}

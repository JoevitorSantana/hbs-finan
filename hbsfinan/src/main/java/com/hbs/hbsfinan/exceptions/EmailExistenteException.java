package com.hbs.hbsfinan.exceptions;

public class EmailExistenteException extends RuntimeException {
    public EmailExistenteException() { super("Este Email já está em uso!"); }
    public EmailExistenteException(String message) {
        super(message);
    }
}

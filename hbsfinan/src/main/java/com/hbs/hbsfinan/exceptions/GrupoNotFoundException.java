package com.hbs.hbsfinan.exceptions;
//
public class GrupoNotFoundException extends RuntimeException {
    public GrupoNotFoundException() {
        super("Grupo não encontrado!");
    }

    public GrupoNotFoundException(String message) {
        super(message);
    }
}

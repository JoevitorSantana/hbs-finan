package com.hbs.hbsfinan.exceptions;

public class DoacaoMonetariaNotFoundException extends RuntimeException {
    public DoacaoMonetariaNotFoundException() {
        super("Doacao não encontrada!");
    }

    public DoacaoMonetariaNotFoundException(String message) {
        super(message);
    }
}

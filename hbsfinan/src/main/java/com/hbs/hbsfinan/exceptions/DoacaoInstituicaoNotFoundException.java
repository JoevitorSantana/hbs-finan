package com.hbs.hbsfinan.exceptions;

public class DoacaoInstituicaoNotFoundException extends RuntimeException {

    public DoacaoInstituicaoNotFoundException() {
        super("Doacao não encontrada!");
    }

    public DoacaoInstituicaoNotFoundException(String message) {
        super(message);
    }


}

package com.hbs.hbsfinan.exceptions;

public class AnotacoesNotFoundException extends RuntimeException {
    public AnotacoesNotFoundException(){
      super("Anotação nao encontrada!");
    }

    public AnotacoesNotFoundException(String message) {
        super(message);
    }
}

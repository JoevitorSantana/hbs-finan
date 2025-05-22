package com.hbs.hbsfinan.exceptions;

public class EventoNotFoundException extends RuntimeException {
  public EventoNotFoundException() {
    super("Evento não encontrado!");
  }

  public EventoNotFoundException(String message) {
    super(message);
  }
}

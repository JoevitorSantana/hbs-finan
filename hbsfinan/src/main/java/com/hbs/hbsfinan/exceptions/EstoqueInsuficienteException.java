package com.hbs.hbsfinan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Retorna 400 Bad Request
public class EstoqueInsuficienteException extends RuntimeException {

  public EstoqueInsuficienteException(String message) {
    super(message);
  }

  public EstoqueInsuficienteException(String message, Throwable cause) {
    super(message, cause);
  }
}
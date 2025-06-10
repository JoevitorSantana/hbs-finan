package com.hbs.hbsfinan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Opcional, mas útil: Anotação para que o Spring lide automaticamente com o status HTTP
@ResponseStatus(HttpStatus.NOT_FOUND) // Define que, se esta exceção for lançada, o status HTTP será 404 Not Found
public class ApoiadorNotFoundException extends RuntimeException {

    public ApoiadorNotFoundException(String message) {
        super(message);
    }

    public ApoiadorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

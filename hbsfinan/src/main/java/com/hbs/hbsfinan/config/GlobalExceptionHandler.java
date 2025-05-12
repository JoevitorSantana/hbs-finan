package com.hbs.hbsfinan.config;

import com.hbs.hbsfinan.exceptions.ParametrizacaoNaoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ParametrizacaoNaoEncontradaException.class)
    public ResponseEntity<ErrorDTO> handleParamNotFound(ParametrizacaoNaoEncontradaException ex) {
        ErrorDTO erro = new ErrorDTO("PARAM_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // DTO interno de erro
    public static class ErrorDTO {
        private String code;
        private String message;

        public ErrorDTO(String code, String message) {
            this.code = code;
            this.message = message;
        }
        public String getCode() { return code; }
        public String getMessage() { return message; }
    }
}

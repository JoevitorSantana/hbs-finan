package com.hbs.hbsfinan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UsuarioNotFoundException.class})
    private ResponseEntity<RestErrorMessage> usuarioNotFoundHandler(UsuarioNotFoundException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restErrorMessage);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestErrorMessage> badCredentialsHandler(BadCredentialsException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.UNAUTHORIZED, exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(restErrorMessage);
    }

    @ExceptionHandler(EmailExistenteException.class)
    public ResponseEntity<RestErrorMessage> emailExistenteHandler(EmailExistenteException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(MissingTokenException.class)
    public ResponseEntity<RestErrorMessage> missingTokenHandler(MissingTokenException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<RestErrorMessage> invalidTokenHandler(InvalidTokenException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(RoleInvalidaException.class)
    public ResponseEntity<RestErrorMessage> roleInvalidaHandler(RoleInvalidaException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(ErroExclusaoException.class)
    public ResponseEntity<RestErrorMessage> erroExclusaoHanler(ErroExclusaoException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restErrorMessage);
    }

    @ExceptionHandler(ParametrizacaoNaoEncontradaException.class)
    public ResponseEntity<RestErrorMessage> parametrizacaoNaoEncontradaHandler(ParametrizacaoNaoEncontradaException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restErrorMessage);
    }
}

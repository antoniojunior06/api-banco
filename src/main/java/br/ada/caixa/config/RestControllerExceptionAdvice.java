package br.ada.caixa.config;

import br.ada.caixa.exception.ValidacaoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionAdvice {

    @ExceptionHandler( { ValidacaoException.class,  } )
    public ResponseEntity<String> handlerValidacaoException(ValidacaoException validacaoException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validacaoException.getMessage());
    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity<String> handlerException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}

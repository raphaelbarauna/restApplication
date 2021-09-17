package br.com.santander.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException(String message) {

        super(message);
    }
}

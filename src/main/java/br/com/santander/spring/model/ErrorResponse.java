package br.com.santander.spring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class ErrorResponse {

    private final int code;
    private Date timestamp;
    private final String message;
    private final String description;
}

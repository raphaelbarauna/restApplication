package br.com.santander.spring.model;

import lombok.AllArgsConstructor;

import java.util.Date;

@AllArgsConstructor
public class ErrorResponse {

    private final int code;
    private Date timestamp;
    private final String message;
    private final String description;
}

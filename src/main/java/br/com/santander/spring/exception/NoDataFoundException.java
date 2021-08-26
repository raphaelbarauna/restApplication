package br.com.santander.spring.exception;

public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException() {

        super("Nenhum cliente foi encontrado.");
    }
}

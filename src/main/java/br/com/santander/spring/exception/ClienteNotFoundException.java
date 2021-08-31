package br.com.santander.spring.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(String message) {

        super("Nao foi encontrado um cliente com o id informado.");
    }
}

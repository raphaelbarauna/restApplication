package br.com.santander.spring.exception;

public class ServiceException extends RuntimeException{

    public ServiceException(String message){
        super(message);
    }
}

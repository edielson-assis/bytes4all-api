package br.com.edielsonassis.bookstore.services.exceptions;

public class ValidationException extends RuntimeException {
    
    public ValidationException(String msg) {
        super(msg);
    }
}
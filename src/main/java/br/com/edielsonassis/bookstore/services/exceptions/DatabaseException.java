package br.com.edielsonassis.bookstore.services.exceptions;

public class DataBaseException extends RuntimeException {
    
    public DataBaseException(String msg) {
        super(msg);
    }
}
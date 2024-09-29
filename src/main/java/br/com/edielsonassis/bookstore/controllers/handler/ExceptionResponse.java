package br.com.edielsonassis.bookstore.controllers.handler;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse implements Serializable {
    
    private Date timestamp;
    private String message;
    private String details;
}
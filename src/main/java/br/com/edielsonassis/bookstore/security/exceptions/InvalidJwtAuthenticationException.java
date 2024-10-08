package br.com.edielsonassis.bookstore.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {
	
	public InvalidJwtAuthenticationException(String msg) {
		super(msg);
	}
}
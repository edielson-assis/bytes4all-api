package br.com.edielsonassis.bookstore.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.edielsonassis.bookstore.dtos.v1.request.UserRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.TokenJWT;
import br.com.edielsonassis.bookstore.model.User;
import br.com.edielsonassis.bookstore.repositories.UserRepository;
import br.com.edielsonassis.bookstore.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;
	private final UserRepository repository;
	
	public TokenJWT signin(UserRequest data) {
		try {
			var username = data.email();
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.password()));
			var user = findUserByEmail(username);
			return tokenProvider.createAccessToken(user.getUsername(), user.getRoles());
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid username or password");
		}
	}
	
	public TokenJWT refreshToken(String username, String refreshToken) {
		findUserByEmail(username);
		return tokenProvider.refreshToken(refreshToken);
	}

	private User findUserByEmail(String email) {
        log.info("Verifying the user's email: {}", email);
        return repository.findByEmail(email).orElseThrow(() -> {
            log.error("Username not found: {}", email);
            return new UsernameNotFoundException("Username not found: " + email);
        });    
    }
}
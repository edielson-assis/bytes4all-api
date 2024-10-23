package br.com.edielsonassis.bookstore.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edielsonassis.bookstore.controllers.swagger.AuthControllerSwagger;
import br.com.edielsonassis.bookstore.dtos.v1.request.UserSigninRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.UserSignupRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.TokenResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.UserResponse;
import br.com.edielsonassis.bookstore.services.AuthService;
import br.com.edielsonassis.bookstore.utils.constants.MediaType;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController implements AuthControllerSwagger {

	private final AuthService authService;

    @Transactional
    @PostMapping(path = "/signup",
		consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}, 
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
	)
	@Override
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserSignupRequest userRequest) {
        var user = authService.signup(userRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
	
    @Transactional
	@PostMapping(path = "/signin",
		consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}, 
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
	)	
	@Override
    public ResponseEntity<TokenAndRefreshTokenResponse> signin(@Valid @RequestBody UserSigninRequest userRequest) {
		var token = authService.signin(userRequest);
		return ResponseEntity.ok(token);
	}
	
	@GetMapping(path = "/refresh/{username}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
    @Override
	public ResponseEntity<TokenResponse> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        var token = authService.refreshToken(username, refreshToken);
		return ResponseEntity.ok(token);
	}

	@Transactional
	@DeleteMapping(path = "/delete/{email}")
	@Override
	public ResponseEntity<Void> deleteUser(@PathVariable(value = "email") String email) {
		authService.disableUser(email);
		return ResponseEntity.noContent().build();
	}
}
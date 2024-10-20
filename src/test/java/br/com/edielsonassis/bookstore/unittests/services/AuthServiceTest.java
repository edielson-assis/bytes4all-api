package br.com.edielsonassis.bookstore.unittests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.edielsonassis.bookstore.dtos.v1.request.UserSigninRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.UserSignupRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.TokenResponse;
import br.com.edielsonassis.bookstore.model.Permission;
import br.com.edielsonassis.bookstore.model.User;
import br.com.edielsonassis.bookstore.repositories.UserRepository;
import br.com.edielsonassis.bookstore.security.JwtTokenProvider;
import br.com.edielsonassis.bookstore.services.AuthService;
import br.com.edielsonassis.bookstore.services.PermissionService;
import br.com.edielsonassis.bookstore.services.exceptions.ValidationException;
import br.com.edielsonassis.bookstore.unittests.mapper.mocks.MockUser;

@TestInstance(Lifecycle.PER_METHOD)
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    
    @Mock
    private UserRepository repository;

    @Mock
    private PermissionService permisson;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;
	
    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService service;

    private MockUser input;
    private User user;
    private UserSigninRequest userSigninRequest;
    private UserSignupRequest userSignupRequest;

    private static final Long USER_ID = 1L;
    private static final Integer NUMBER_ONE = 1;
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    @BeforeEach
    void setup() {
        input = new MockUser();
        user = input.user();
        userSignupRequest = input.userSignup();
        userSigninRequest = input.userSignin();
    }

    @Test
    @DisplayName("Should signup and return a UserResponse")
	void testShouldSignupAndReturnUserResponse() {
        when(permisson.findbyPermission(anyString())).thenReturn(new Permission());
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(user);

        var savedUser = service.signup(userSignupRequest);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getUserId());
        assertNotNull(savedUser.getFullName());
		assertNotNull(savedUser.getEmail());

        assertEquals(USER_ID, savedUser.getUserId());
        assertEquals("Test auth", savedUser.getFullName());
        assertEquals("teste@email.com", savedUser.getEmail());
        
        verify(repository, times(NUMBER_ONE)).save(any(User.class));
    }

    @Test
    @DisplayName("Should return a ValidationException if the provided email is already registered")
    void testShouldReturnAValidationExceptionIfTheProvidedEmailIsAlreadyRegistered() {
        when(repository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(ValidationException.class, () -> service.signup(userSignupRequest));

        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should perform login and return a JWT token and a refresh token")
    void testShouldPerformLoginAndReturnAJwtTokenAndARefreshToken() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(tokenProvider.createAccessTokenRefreshToken(user.getUsername(), user.getRoles())).thenReturn(new TokenAndRefreshTokenResponse(ACCESS_TOKEN, REFRESH_TOKEN));

        var response = service.signin(userSigninRequest);

        assertNotNull(response);

        assertEquals(ACCESS_TOKEN, response.accessToken());
        assertEquals(REFRESH_TOKEN, response.refreshToken());

        verify(repository, times(NUMBER_ONE)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Should return an BadCredentialsException if the user is not found")
    void testShouldReturnAnBadCredentialsExceptionIfTheUserIsNotFound() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
    
        assertThrows(BadCredentialsException.class, () -> service.signin(userSigninRequest));

        verify(repository, times(NUMBER_ONE)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Should refresh a JWT token")
	void testShouldRefreshAJWTToken() {
        when(tokenProvider.refreshToken(REFRESH_TOKEN, userSigninRequest.getEmail())).thenReturn(new TokenResponse(ACCESS_TOKEN));

        var response = service.refreshToken(userSigninRequest.getEmail(), REFRESH_TOKEN);

        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.accessToken());
    }
}
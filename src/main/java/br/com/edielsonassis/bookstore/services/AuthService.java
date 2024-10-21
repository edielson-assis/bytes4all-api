package br.com.edielsonassis.bookstore.services;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.edielsonassis.bookstore.dtos.v1.request.UserSigninRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.UserSignupRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.TokenResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.UserResponse;
import br.com.edielsonassis.bookstore.mapper.Mapper;
import br.com.edielsonassis.bookstore.model.User;
import br.com.edielsonassis.bookstore.repositories.UserRepository;
import br.com.edielsonassis.bookstore.security.JwtTokenProvider;
import br.com.edielsonassis.bookstore.services.exceptions.ValidationException;
import br.com.edielsonassis.bookstore.util.component.AuthenticatedUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;
	private final UserRepository repository;
	private final PasswordEncoder encoder;
	private final PermissionService permissionService;

	private static final String USER_PERMISSION = "COMMON_USER";

	public UserResponse signup(UserSignupRequest userRequest) {
		User user = Mapper.parseObject(userRequest, User.class);
        validateEmailNotExists(user);
        encryptPassword(user);
		getPermission(user);
        log.info("Registering a new User");
        return Mapper.parseObject(repository.save(user), UserResponse.class);
    }
	
	public TokenAndRefreshTokenResponse signin(UserSigninRequest data) {
		return authenticateUser(data);
	}
	
	public TokenResponse refreshToken(String username, String refreshToken) {
        return tokenProvider.refreshToken(refreshToken, username);
	}

	public void disableUser(String email) {
		var user = AuthenticatedUser.getCurrentUser();
		if (!(user.getEmail().equals(email) || hasPermissionToDeleteUser(user))) {
            throw new AccessDeniedException("You do not have permission to delete users");
        }
		findUserByEmail(email);
		log.info("Disabling user with email: {}", email);
		repository.disableUser(email);
	}

	private User findUserByEmail(String email) {
        log.info("Verifying the user's email: {}", email);
        return repository.findByEmail(email).orElseThrow(() -> {
            log.error("Username not found: {}", email);
            return new UsernameNotFoundException("Username not found: " + email);
        });    
    }

	private synchronized void validateEmailNotExists(User user) {
		log.info("Verifying the user's email: {}", user.getEmail());
        boolean exists = repository.existsByEmail(user.getEmail().toLowerCase());
        if (exists) {
            log.error("Email already exists: {}", user.getEmail());
            throw new ValidationException("Email already exists");
        }
    }

    private void encryptPassword(User user) {
		log.info("Encrypting password");
        user.setPassword(encoder.encode(user.getPassword()));
    }

	private void getPermission(User user) {
		var permission = permissionService.findbyPermission(USER_PERMISSION);
		user.getPermissions().add(permission);
	}

	private TokenAndRefreshTokenResponse authenticateUser(UserSigninRequest data) {
		String username = data.getEmail();
		try {
			log.debug("Authenticating user with email: {}", username);
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
			log.debug("Authentication successful for user: {}", username);
			var user = findUserByEmail(username);
			log.info("Generating access and refresh token for user: {}", username);
			return tokenProvider.createAccessTokenRefreshToken(user.getUsername(), user.getRoles());
		} catch (Exception e) {
			log.error("Invalid username or password for user: {}", username);
			throw new BadCredentialsException("Invalid username or password");
		}
	}

	public boolean hasPermissionToDeleteUser(User user) {
        return user.getPermissions().stream().anyMatch(permission -> permission.getDescription().equals("ADMIN") || permission.getDescription().equals("MANAGER"));
    }
}
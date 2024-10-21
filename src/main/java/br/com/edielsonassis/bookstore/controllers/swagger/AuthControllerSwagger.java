package br.com.edielsonassis.bookstore.controllers.swagger;

import org.springframework.http.ResponseEntity;

import br.com.edielsonassis.bookstore.dtos.v1.request.UserSigninRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.UserSignupRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.TokenResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication", description = "Endpoints for Managing User")
public interface AuthControllerSwagger {

	static final String SECURITY_SCHEME_KEY = "bearer-key";
    
    @Operation(
        summary = "Adds a new User",
		description = "Adds a new User by passing in a JSON, XML or YML representation of the user!",
		tags = {"Authentication"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Created user",
				content = @Content(schema = @Schema(implementation = UserResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad request - Something is wrong with the request", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<UserResponse> signup(UserSignupRequest userRequest);

    @Operation(
        summary = "Authenticates a user",
		description = "Authenticates a user and returns a token",
		tags = {"Authentication"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Authenticated user",
				content = @Content(schema = @Schema(implementation = TokenAndRefreshTokenResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad request - Something is wrong with the request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid email or password", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<TokenAndRefreshTokenResponse> signin(UserSigninRequest userRequest);

    @Operation(
        summary = "Updates a Token",
		description = "Refresh token for authenticated user and returns a token",
		tags = {"Authentication"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Updated token", 
				content = @Content(schema = @Schema(implementation = TokenResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad request - Something is wrong with the request", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - User not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<TokenResponse> refreshToken(String username, String refreshToken);

	@Operation(
		security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)},
        summary = "Deletes a User",
		description = "Deletes a User by their Email",
		tags = {"Authentication"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Deleted user", content = @Content),
			@ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - User not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
	ResponseEntity<Void> deleteUser(String email);
}
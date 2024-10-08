package br.com.edielsonassis.bookstore.dtos.v1.response;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Representation of a JWT Token")
public record TokenJWT(

    @Schema(description = "Username of the authenticated user", example = "robert@example.com", requiredMode = RequiredMode.REQUIRED)
    String email,

    @Schema(description = "Indicates if the user is authenticated", example = "true", requiredMode = RequiredMode.REQUIRED)
    Boolean authenticated,

    @Schema(description = "The date and time when the token was created", example = "2024-10-08T12:00:00", requiredMode = RequiredMode.REQUIRED)
    Instant created,

    @Schema(description = "The date and time when the token will expire", example = "2024-10-08T14:00:00", requiredMode = RequiredMode.REQUIRED)
    Instant expiration,

    @Schema(description = "Access token provided for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", requiredMode = RequiredMode.REQUIRED)
    String accessToken,

    @Schema(description = "Refresh token provided for re-authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", requiredMode = RequiredMode.REQUIRED)
    String refreshToken
) {}
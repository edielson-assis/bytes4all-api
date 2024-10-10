package br.com.edielsonassis.bookstore.dtos.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Representation of a JWT Token and Refresh token")
public record TokenAndRefreshTokenResponse(

    @Schema(description = "Access token provided for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", requiredMode = RequiredMode.REQUIRED)
    String accessToken,

    @Schema(description = "Refresh token provided for re-authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", requiredMode = RequiredMode.REQUIRED)
    String refreshToken
) {}
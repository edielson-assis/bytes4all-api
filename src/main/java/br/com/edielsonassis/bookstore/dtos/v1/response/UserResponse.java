package br.com.edielsonassis.bookstore.dtos.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Represents a User")
public record UserResponse(

    @Schema(description = "Unique identifier of the user", example = "1", required = true)
    Long userId,

    @Schema(description = "Full name of the person", example = "Robert C. Martin", maxLength = 150, required = true)
    String fullName,

    @Schema(description = "Email of the user.", example = "robert@example.com", maxLength = 100, required = true)
    String email
) {}
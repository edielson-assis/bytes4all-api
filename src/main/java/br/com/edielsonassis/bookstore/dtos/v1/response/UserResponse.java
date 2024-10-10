package br.com.edielsonassis.bookstore.dtos.v1.response;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(name = "Represents a User")
public class UserResponse implements Serializable {

    @Schema(description = "Unique identifier of the user", example = "1", required = true)
    private Long userId;

    @Schema(description = "Full name of the person", example = "Robert Martin", maxLength = 150, required = true)
    private String fullName;

    @Schema(description = "Email of the user.", example = "robert@example.com", maxLength = 100, required = true)
    private String email;
}
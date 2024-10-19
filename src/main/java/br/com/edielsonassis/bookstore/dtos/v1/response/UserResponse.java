package br.com.edielsonassis.bookstore.dtos.v1.response;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Represents a User")
public class UserResponse implements Serializable {

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long userId;

    @Schema(description = "Full name of the person", example = "Robert Martin")
    private String fullName;

    @Schema(description = "Email of the user.", example = "robert@example.com")
    private String email;
}
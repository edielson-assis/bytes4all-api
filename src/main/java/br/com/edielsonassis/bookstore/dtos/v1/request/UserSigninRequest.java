package br.com.edielsonassis.bookstore.dtos.v1.request;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(name = "Represents the User's Signin")
public class UserSigninRequest implements Serializable {

    @Schema(description = "Email of the user.", example = "robert@example.com", maxLength = 100, required = true)
    @NotBlank(message = "Email is required") 
    @Email(message = "Invalid email")
    private String email;

    @Schema(description = "Password for the user account.", example = "372@RfI5n&Ml", maxLength = 255, required = true)
    @NotBlank(message = "Password is required")
    private String password;
}
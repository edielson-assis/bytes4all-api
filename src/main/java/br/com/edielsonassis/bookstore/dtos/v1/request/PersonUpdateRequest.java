package br.com.edielsonassis.bookstore.dtos.v1.request;

import java.io.Serializable;

import br.com.edielsonassis.bookstore.model.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Represents a person")
public class PersonUpdateRequest implements Serializable {

    @Schema(description = "Unique identifier of the person", example = "1", required = true)
    @NotNull(message = "PersonId is required")
    private Long personId;

    @Schema(description = "First name of the person", example = "John", maxLength = 50, required = true)
    @NotBlank(message = "FirstName is required")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s']+$", message = "Only letters should be typed")
    @Size(min = 3, message = "FirstName must have at least {min} characters")
    private String firstName;

    @Schema(description = "Last name of the person", example = "Smith", maxLength = 100, required = true)
    @NotBlank(message = "LastName is required")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s']+$", message = "Only letters should be typed")
    @Size(min = 3, message = "LastName must have at least {min} characters")
    private String lastName;
    
    @Schema(description = "Gender of the person", example = "MALE", required = true)
    @NotNull(message = "Gender is required")
    private Gender gender;

    @Schema(description = "Address information of the person")
    @Valid
    private AddressRequest address;
}
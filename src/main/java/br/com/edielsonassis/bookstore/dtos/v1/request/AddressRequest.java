package br.com.edielsonassis.bookstore.dtos.v1.request;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@Schema(description = "Represents an address")
public class AddressRequest implements Serializable {

    @Schema(description = "City where the person lives", example = "New York", maxLength = 100, required = true)
    @NotBlank(message = "City is required")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s']+$", message = "Only letters should be typed")
    private String city;

    @Schema(description = "State where the person lives (2-letter abbreviation)", example = "NY", maxLength = 2, required = true)
    @NotBlank(message = "State is required")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s']+$", message = "Only letters should be typed")
    @Size(max = 2, message = "State must have at most {max} characters")
    private String state;
}
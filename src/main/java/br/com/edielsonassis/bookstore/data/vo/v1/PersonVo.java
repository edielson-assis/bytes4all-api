package br.com.edielsonassis.bookstore.data.vo.v1;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import br.com.edielsonassis.bookstore.model.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonVo extends RepresentationModel<PersonVo> implements Serializable {
    
    private Long personId;

    @NotBlank(message = "FirstName is required")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "Only letters should be typed")
    @Size(min = 3, message = "FirstName must have at least {min} characters")
    private String firstName;

    @NotBlank(message = "LastName is required")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "Only letters should be typed")
    @Size(min = 3, message = "LastName must have at least {min} characters")
    private String lastName;
    
    @NotNull(message = "Gender is required")
    private Gender gender;

    @Valid
    private AddressVo address;
}
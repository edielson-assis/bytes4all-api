package br.com.edielsonassis.bookstore.dtos.v1.response;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import br.com.edielsonassis.bookstore.model.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Represents a person")
public class PersonResponse extends RepresentationModel<PersonResponse> implements Serializable {
    
    @Schema(description = "Unique identifier of the person", example = "1", required = true)
    private Long personId;

    @Schema(description = "First name of the person", example = "John", maxLength = 50, required = true)
    private String firstName;

    @Schema(description = "Last name of the person", example = "Smith", maxLength = 100, required = true)
    private String lastName;

    @Schema(description = "Gender of the person", example = "MALE", required = true)
    private Gender gender;

    @Schema(description = "Address information of the person")
    private AddressResponse address;
}
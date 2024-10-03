package br.com.edielsonassis.bookstore.dtos.v1.response;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Schema(description = "Represents an address")
public class AddressResponse implements Serializable {

    @Schema(description = "City where the person lives", example = "New York", maxLength = 100, required = true)
    private String city;

    @Schema(description = "State where the person lives (2-letter abbreviation)", example = "NY", maxLength = 2, required = true)
    private String state;
}
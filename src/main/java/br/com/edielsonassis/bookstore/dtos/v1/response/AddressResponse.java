package br.com.edielsonassis.bookstore.dtos.v1.response;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Setter
@Getter
@Schema(description = "Represents an address")
public class AddressResponse implements Serializable {

    @Schema(description = "City where the person lives", example = "New York")
    private String city;

    @Schema(description = "State where the person lives (2-letter abbreviation)", example = "NY")
    private String state;
}
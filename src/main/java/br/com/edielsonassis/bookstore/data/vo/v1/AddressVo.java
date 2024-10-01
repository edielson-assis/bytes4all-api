package br.com.edielsonassis.bookstore.data.vo.v1;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class AddressVo implements Serializable {

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "Only letters should be typed")
    @Size(max = 2, message = "State must have at most {max} characters")
    private String state;
}
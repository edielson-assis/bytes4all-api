package br.com.edielsonassis.bookstore.integrationstests.dtos.request;

import java.io.Serializable;

import br.com.edielsonassis.bookstore.model.enums.Gender;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@Setter
@Getter
public class PersonUpdateRequest implements Serializable {

    private Long personId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private AddressRequest address;
}
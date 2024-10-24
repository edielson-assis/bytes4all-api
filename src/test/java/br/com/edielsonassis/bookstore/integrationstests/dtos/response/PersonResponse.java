package br.com.edielsonassis.bookstore.integrationstests.dtos.response;

import java.io.Serializable;

import br.com.edielsonassis.bookstore.models.enums.Gender;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@Setter
@Getter
public class PersonResponse implements Serializable {
    
    private Long personId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private AddressResponse address;
}
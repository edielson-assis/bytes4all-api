package br.com.edielsonassis.bookstore.data.vo.v1;

import java.io.Serializable;

import br.com.edielsonassis.bookstore.model.enums.Gender;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonVo implements Serializable {
    
    private Long personId;
    private String firstName;
    private String lastName;
    private Gender gender;
    @Embedded private AddressVo address;
}
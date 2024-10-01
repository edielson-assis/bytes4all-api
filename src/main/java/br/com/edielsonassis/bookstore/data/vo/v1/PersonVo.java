package br.com.edielsonassis.bookstore.data.vo.v1;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import br.com.edielsonassis.bookstore.model.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonVo extends RepresentationModel<PersonVo> implements Serializable {
    
    private Long personId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private AddressVo address;
}
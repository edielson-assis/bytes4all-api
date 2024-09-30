package br.com.edielsonassis.bookstore.data.vo.v1;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Embeddable
public class AddressVo implements Serializable {
    
    private String street;
    private String neighborhood;
    private String city;
    private String state;
}
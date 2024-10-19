package br.com.edielsonassis.bookstore.integrationstests.dtos.response;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@Setter
@Getter
public class AddressResponse implements Serializable {

    private String city;
    private String state;
}
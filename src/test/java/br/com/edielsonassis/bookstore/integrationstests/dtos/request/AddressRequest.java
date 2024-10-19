package br.com.edielsonassis.bookstore.integrationstests.dtos.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressRequest implements Serializable {

    private String city;
    private String state;
}
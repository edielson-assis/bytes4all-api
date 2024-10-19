package br.com.edielsonassis.bookstore.integrationstests.dtos.request;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@Setter
@Getter
public class UserSigninRequest implements Serializable {

    private String email;
    private String password;
}
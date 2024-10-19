package br.com.edielsonassis.bookstore.integrationstests.dtos.response;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@Setter
@Getter
public class UserResponse implements Serializable {

    private Long userId;
    private String fullName;
    private String email;
}
package br.com.edielsonassis.bookstore.integrationstests.dtos.response;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "TokenResponse")
public class TokenResponse implements Serializable {

    @XmlElement(name = "accessToken")
    private String accessToken;
}
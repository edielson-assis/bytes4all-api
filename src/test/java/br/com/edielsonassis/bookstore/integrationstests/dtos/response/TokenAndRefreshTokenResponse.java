package br.com.edielsonassis.bookstore.integrationstests.dtos.response;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "TokenAndRefreshTokenResponse")
public class TokenAndRefreshTokenResponse implements Serializable {
    
    @XmlElement(name = "accessToken") 
    private String accessToken; 
    
    @XmlElement(name = "refreshToken") 
    private String refreshToken;
}
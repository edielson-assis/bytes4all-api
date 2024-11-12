package br.com.edielsonassis.bookstore.integrationstests.dtos.response;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@Setter
@Getter
public class BookUpdateResponse implements Serializable {
    
    private Long bookId;
    private String author;
    private LocalDate launchDate;
    private String title;
    private String description;
}
package br.com.edielsonassis.bookstore.integrationstests.dtos.request;

import java.io.Serializable;
import java.time.LocalDate;

import br.com.edielsonassis.bookstore.integrationstests.controllers.withxml.adapter.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@Setter
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class BookUpdateRequest implements Serializable {
    
    private Long bookId;
    private String author;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate launchDate;
	private String title;
	private String description;
}
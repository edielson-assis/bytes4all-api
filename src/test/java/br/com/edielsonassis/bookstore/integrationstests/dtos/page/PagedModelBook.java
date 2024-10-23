package br.com.edielsonassis.bookstore.integrationstests.dtos.page;

import java.io.Serializable;
import java.util.List;

import br.com.edielsonassis.bookstore.integrationstests.dtos.response.BookResponse;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@XmlRootElement
@Getter
public class PagedModelBook implements Serializable {
    
    @XmlElement(name = "content")
	private List<BookResponse> content;
}
package br.com.edielsonassis.bookstore.integrationstests.dtos.wrapper;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.edielsonassis.bookstore.integrationstests.dtos.response.BookResponse;
import lombok.Getter;

@Getter
public class BookEmbedded implements Serializable {

    @JsonProperty("bookResponseList")
	private List<BookResponse> books;
}
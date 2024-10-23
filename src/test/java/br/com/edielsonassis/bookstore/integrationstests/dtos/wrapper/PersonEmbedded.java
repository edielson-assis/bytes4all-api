package br.com.edielsonassis.bookstore.integrationstests.dtos.wrapper;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.edielsonassis.bookstore.integrationstests.dtos.response.PersonResponse;
import lombok.Getter;

@Getter
public class PersonEmbedded implements Serializable {

    @JsonProperty("personResponseList")
	private List<PersonResponse> people;
}
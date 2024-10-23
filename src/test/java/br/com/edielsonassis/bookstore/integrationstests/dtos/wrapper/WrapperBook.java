package br.com.edielsonassis.bookstore.integrationstests.dtos.wrapper;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class WrapperBook implements Serializable {
    
    @JsonProperty("_embedded")
	private BookEmbedded embedded;
}
package br.com.edielsonassis.bookstore.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    
    MALE("Male"),
    FEMALE("Female");

    private final String gender;
}
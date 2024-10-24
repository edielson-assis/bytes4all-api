package br.com.edielsonassis.bookstore.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    
    MALE("Male"),
    FEMALE("Female");

    private final String value;

    @JsonCreator
    private static Gender fromValue(String value) {
        return Gender.valueOf(value.toUpperCase());
    }

    @JsonValue
    private String toValue() {
        return getValue();
    }
}
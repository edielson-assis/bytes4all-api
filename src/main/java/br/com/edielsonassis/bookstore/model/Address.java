package br.com.edielsonassis.bookstore.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Embeddable
public class Address implements Serializable {

    @Column(nullable = false, length = 30)
    private String city;

    @Column(nullable = false, length = 2)
    private String state;
}
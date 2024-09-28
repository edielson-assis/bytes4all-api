package br.com.edielsonassis.bookstore.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class Address implements Serializable {
    
    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 50)
    private String neighborhood;

    @Column(nullable = false, length = 30)
    private String city;

    @Column(nullable = false, length = 30)
    private String state;
}
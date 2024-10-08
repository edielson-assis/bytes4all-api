package br.com.edielsonassis.bookstore.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Setter
@Getter
@Embeddable
public class Address implements Serializable {

    @Column(nullable = false, length = 30)
    private String city;

    @Column(nullable = false, length = 2)
    private String state;

    public void setState(String state) {
        this.state = state.toUpperCase();
    }
}
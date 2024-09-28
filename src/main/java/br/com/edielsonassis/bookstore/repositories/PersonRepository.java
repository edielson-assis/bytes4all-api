package br.com.edielsonassis.bookstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edielsonassis.bookstore.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {}
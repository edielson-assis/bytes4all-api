package br.com.edielsonassis.bookstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.edielsonassis.bookstore.model.Book;

public interface BookRespository extends JpaRepository<Book, Long> {}
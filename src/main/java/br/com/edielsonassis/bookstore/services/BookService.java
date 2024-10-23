package br.com.edielsonassis.bookstore.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.edielsonassis.bookstore.dtos.v1.request.BookRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.BookResponse;
import br.com.edielsonassis.bookstore.mapper.Mapper;
import br.com.edielsonassis.bookstore.model.Book;
import br.com.edielsonassis.bookstore.repositories.BookRepository;
import br.com.edielsonassis.bookstore.services.exceptions.DataBaseException;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class BookService {
    
    private final BookRepository repository;

    public BookResponse createBook(BookRequest bookRequest) {
        var book = Mapper.parseObject(bookRequest, Book.class);
        log.info("Creating a new book: {}", bookRequest.getTitle());
        return Mapper.parseObject(repository.save(book), BookResponse.class);

    }

    public BookResponse findBookById(Long id) {
        log.info("Searching for book with ID: {}", id);
        var book = findById(id);
        return Mapper.parseObject(book, BookResponse.class);
    }

    public Page<BookResponse> findBookByName(String name, Integer page, Integer size, String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        log.info("Searching for book with title: {}", name);
        return repository.findBookByName(name, pageable).map(book -> Mapper.parseObject(book, BookResponse.class));
    }

    public Page<BookResponse> findAllBooks(Integer page, Integer size, String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        log.info("Searching all books");
        return repository.findAll(pageable).map(book -> Mapper.parseObject(book, BookResponse.class));
    }

    public BookResponse updateBook(BookUpdateRequest bookRequest) {
        var book = findById(bookRequest.getBookId());
        book = Mapper.parseObject(bookRequest, Book.class);
        log.info("Updating book with name: {}", book.getTitle());
        return Mapper.parseObject(repository.save(book), BookResponse.class);
    }

    public void deleteBook(Long id) {
        var book = findById(id);
        try {
            log.info("Attempting to delete book with ID: {}", book.getBookId());
            repository.delete(book);
            log.info("Book with name: {} successfully deleted", book.getTitle());
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete book with name: {} due to referential integrity violation", book.getTitle(), e.getMessage());
            throw new DataBaseException(e.getMessage());
        }
    }

    private Book findById(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Book ID not found: {}", id);
            return new ObjectNotFoundException("Book not found");
        });
    }
}
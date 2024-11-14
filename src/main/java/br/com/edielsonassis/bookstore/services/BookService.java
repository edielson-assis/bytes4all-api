package br.com.edielsonassis.bookstore.services;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.com.edielsonassis.bookstore.dtos.v1.request.BookRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.BookResponse;
import br.com.edielsonassis.bookstore.mapper.Mapper;
import br.com.edielsonassis.bookstore.models.Book;
import br.com.edielsonassis.bookstore.models.User;
import br.com.edielsonassis.bookstore.repositories.BookRepository;
import br.com.edielsonassis.bookstore.services.exceptions.ObjectNotFoundException;
import br.com.edielsonassis.bookstore.utils.component.AuthenticatedUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class BookService {
    
    private final BookRepository repository;
    private final FileStorageService fileService;

    public BookResponse createBook(BookRequest bookRequest) {
        var book = Mapper.parseObject(bookRequest, Book.class);
        book.setUser(currentUser());
        book.setDownloadUrl(bookRequest.getFile().getOriginalFilename());
        fileService.storeFile(bookRequest.getFile());
        log.info("Creating a new book: {}", bookRequest.getTitle());
        return Mapper.parseObject(repository.save(book), BookResponse.class);
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
        var book = findBookById(bookRequest.getBookId());
        hasPermission(book);
        var existingBook = book;
        book = Mapper.parseObject(bookRequest, Book.class);
        book.setDownloadUrl(existingBook.getDownloadUrl());
        book.setUser(existingBook.getUser());
        log.info("Updating book with name: {}", book.getTitle());
        return Mapper.parseObject(repository.save(book), BookResponse.class);
    }

    public void deleteBook(Long id) {
        var book = findBookById(id);
        hasPermission(book);
        log.info("Deleting book with ID: {}", book.getBookId());
        repository.delete(book);
    }

    public Resource getFile(String filename) {
        return fileService.loadFileAsResource(filename);
    }

    private Book findBookById(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Book ID not found: {}", id);
            return new ObjectNotFoundException("Book not found");
        });
    }

    private boolean hasPermission(Book book) {
        if (!(currentUser().equals(book.getUser()) || isAdmin(currentUser()))) {
            throw new AccessDeniedException("You do not have permission to delete books");
        }
        return true;
    }

    private User currentUser() {
        return AuthenticatedUser.getCurrentUser();
    }

    private boolean isAdmin(User user) {
        return user.getPermissions().stream().anyMatch(permission -> permission.getDescription().equals("ADMIN"));
    }
}
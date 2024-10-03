package br.com.edielsonassis.bookstore.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edielsonassis.bookstore.controllers.swagger.BookControllerSwagger;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.BookResponse;
import br.com.edielsonassis.bookstore.services.BookService;
import br.com.edielsonassis.bookstore.util.MediaType;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/book")
public class BookController implements BookControllerSwagger {
	
	private final BookService service;

	@Transactional
    @PostMapping(path = "/create",
		consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}, 
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
	)
	@Override
	public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
		var book = service.createBook(bookRequest);
		book.add(linkTo(methodOn(BookController.class).findBookById(book.getBookId())).withSelfRel());
        return new ResponseEntity<>(book, HttpStatus.CREATED);
	}

    @GetMapping(path = "/get/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	@Override
	public ResponseEntity<BookResponse> findBookById(@PathVariable(value = "id") Long id) {
		var book = service.findBookById(id);
		book.add(linkTo(methodOn(BookController.class).findBookById(id)).withSelfRel());
		book.add(linkTo(methodOn(BookController.class).findAllBooks()).withRel("Books List"));
        return new ResponseEntity<>(book, HttpStatus.OK);
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	@Override
	public ResponseEntity<List<BookResponse>> findAllBooks() {
		var people = service.findAllBooks();
		people.stream().forEach(book -> book.add(linkTo(methodOn(BookController.class).findBookById(book.getBookId())).withSelfRel()));
        return new ResponseEntity<>(people, HttpStatus.OK);
	}

	@Transactional
	@PutMapping(path = "update",
		consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}, 
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
	)
	@Override
	public ResponseEntity<BookResponse> updateBook(@Valid @RequestBody BookUpdateRequest bookRequest) {
		var book = service.updateBook(bookRequest);
		book.add(linkTo(methodOn(BookController.class).findBookById(book.getBookId())).withSelfRel());
		return new ResponseEntity<>(book, HttpStatus.OK);
	}
	
	@Transactional
	@DeleteMapping(path = "/delete/{id}")
	@Override
	public ResponseEntity<Void> deleteBook(@PathVariable(value = "id") Long id) {
		service.deleteBook(id);
		return ResponseEntity.noContent().build();
	}
}
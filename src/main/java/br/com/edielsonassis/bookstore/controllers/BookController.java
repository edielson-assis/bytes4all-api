package br.com.edielsonassis.bookstore.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.edielsonassis.bookstore.controllers.swagger.BookControllerSwagger;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.BookResponse;
import br.com.edielsonassis.bookstore.services.BookService;
import br.com.edielsonassis.bookstore.utils.constants.DefaultValue;
import br.com.edielsonassis.bookstore.utils.constants.MediaType;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/books")
public class BookController implements BookControllerSwagger {
	
	private final BookService service;
	private final PagedResourcesAssembler<BookResponse> assembler;

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
		book.add(linkTo(methodOn(BookController.class).findAllBooks(DefaultValue.PAGE, DefaultValue.SIZE, DefaultValue.DIRECTION)).withRel("Books List"));
        return new ResponseEntity<>(book, HttpStatus.OK);
	}

	@GetMapping(path = "/get/name/{name}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	@Override
	public ResponseEntity<PagedModel<EntityModel<BookResponse>>> findBookByName(
			@PathVariable(value = "name") String name, 
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "size", defaultValue = "10") Integer size, 
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {

		var books = service.findBookByName(name, page, size, direction);
		var link = linkTo(methodOn(BookController.class).findBookByName(name, page, size, direction)).withSelfRel();
		books.stream().forEach(book -> book.add(linkTo(methodOn(BookController.class).findBookByName(name, page, size, direction)).withSelfRel()));
		return new ResponseEntity<>(assembler.toModel(books, link), HttpStatus.OK);
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
	@Override
	public ResponseEntity<PagedModel<EntityModel<BookResponse>>> findAllBooks(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "size", defaultValue = "10") Integer size, 
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {

		var books = service.findAllBooks(page, size, direction);
		var link = linkTo(methodOn(BookController.class).findAllBooks(page, size, direction)).withSelfRel();
		books.stream().forEach(book -> book.add(linkTo(methodOn(BookController.class).findBookById(book.getBookId())).withSelfRel()));
        return new ResponseEntity<>(assembler.toModel(books, link), HttpStatus.OK);
	}

	@Transactional
	@PutMapping(path = "/update",
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
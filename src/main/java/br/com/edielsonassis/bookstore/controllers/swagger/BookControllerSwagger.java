package br.com.edielsonassis.bookstore.controllers.swagger;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import br.com.edielsonassis.bookstore.dtos.v1.request.BookRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.BookResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.BookUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Books", description = "Endpoints for Managing Books")
public interface BookControllerSwagger {

	static final String SECURITY_SCHEME_KEY = "bearer-key";
    
    @Operation(
		security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)},
        summary = "Adds a new Book",
		description = "Adds a new Book by passing in a JSON, XML or YML representation of the book!",
		tags = {"Books"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Created book",
				content = @Content(schema = @Schema(implementation = BookResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad request - Something is wrong with the request", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<BookResponse> createBook(BookRequest bookRequest);

    @Operation(
        summary = "Finds all Books", description = "Finds all Books",
		tags = {"Books"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Success", 
				content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<PagedModel<EntityModel<BookResponse>>> findAllBooks(Integer page, Integer size, String direction);

	@Operation(
        summary = "Finds a Book", description = "Search for one or more books by name",
		tags = {"Books"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Success", 
				content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<PagedModel<EntityModel<BookResponse>>> findBookByName(String name, Integer page, Integer size, String direction);

    @Operation(
		security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)},
        summary = "Updates a Book",
		description = "Updates a Book by passing in a JSON, XML or YML representation of the book!",
		tags = {"Books"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Updated book", 
				content = @Content(schema = @Schema(implementation = BookUpdateResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad request - Something is wrong with the request", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - Book not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<BookUpdateResponse> updateBook(BookUpdateRequest bookRequest);

    @Operation(
		security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)},
        summary = "Deletes a Book",
		description = "Deletes a Book by their Id",
		tags = {"Books"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Deleted book", content = @Content),
			@ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - Book not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<Void> deleteBook(@Parameter(description = "The Id of the book to find.") Long id);
}
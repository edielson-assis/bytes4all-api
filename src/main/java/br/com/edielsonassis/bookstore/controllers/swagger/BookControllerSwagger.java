package br.com.edielsonassis.bookstore.controllers.swagger;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.com.edielsonassis.bookstore.dtos.v1.request.BookRequest;
import br.com.edielsonassis.bookstore.dtos.v1.request.BookUpdateRequest;
import br.com.edielsonassis.bookstore.dtos.v1.response.BookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Books", description = "Endpoints for Managing Books")
public interface BookControllerSwagger {
    
    @Operation(
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
        summary = "Finds a Book",
		description = "Finds a book by their Id",
		tags = {"Books"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Success", 
				content = @Content(schema = @Schema(implementation = BookResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - Book not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<BookResponse> findBookById(@Parameter(description = "The Id of the book to find.") Long id);

    @Operation(
        summary = "Finds all Books", description = "Finds all Books",
		tags = {"Books"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Success", 
				content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookResponse.class)))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<List<BookResponse>> findAllBooks();

    @Operation(
        summary = "Updates a Book",
		description = "Updates a Book by passing in a JSON, XML or YML representation of the book!",
		tags = {"Books"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Updated book", 
				content = @Content(schema = @Schema(implementation = BookResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad request - Something is wrong with the request", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Authentication problem",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - Book not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error - Server error", content = @Content)
		}
	)
    ResponseEntity<BookResponse> updateBook(BookUpdateRequest bookRequest);

    @Operation(
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